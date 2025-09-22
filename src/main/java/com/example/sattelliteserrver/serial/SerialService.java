package com.example.sattelliteserrver.serial;

import com.example.sattelliteserrver.data.DataEntity;
import com.example.sattelliteserrver.data.DataService;
import com.example.sattelliteserrver.websocket.WebSocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fazecast.jSerialComm.SerialPort;
import jakarta.annotation.PreDestroy;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log
public class SerialService {

    private SerialPort serialPort;
    private volatile boolean running = false;

    @Autowired
    private DataService dataService;
    @Autowired
    private WebSocketService webSocketService;

    public SerialService() {
        String portName = "COM7";
        if (running) {
            System.out.println("---------------------------------------------------------------");
            log.info("Serial service is already running on port: " + portName);
            System.out.println("---------------------------------------------------------------");
            return;
        }

        try {
            // Khởi tạo cổng serial
            serialPort = SerialPort.getCommPort(portName);
            serialPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
            serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);

            // Mở cổng
            if (!serialPort.openPort()) {
                throw new RuntimeException("Không thể mở cổng serial: " + portName);
            }
            running = true;
            log.info("Serial service started on port: " + portName);

            // Tạo luồng đọc dữ liệu
            new Thread(() -> {
                try (var input = serialPort.getInputStream()) {
                    StringBuilder buffer = new StringBuilder();
                    while (running) {
                        if (input.available() > 0) {
                            byte[] data = new byte[input.available()];
                            input.read(data);
                            for (byte b : data) {
                                if (b == '\n') {
                                    String message = buffer.toString().trim();
                                    if (!message.isEmpty()) {
                                        log.info("Received message: " + message);
                                        synchronized (this) {
                                            handleMessage(message);
                                        }
                                    }
                                    buffer.setLength(0);
                                } else {
                                    buffer.append((char) b);
                                }
                            }
                        }
                        Thread.sleep(100); // Tránh chiếm CPU
                    }
                } catch (Exception e) {
                    if (running) {
                        log.severe("Error reading from serial port: " + e.getMessage());
                    }
                } finally {
                    stop();
                }
            }).start();

        } catch (Exception e) {
            log.severe("Failed to start serial service: " + e.getMessage());
            stop();
        }
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            log.info("Serial port closed");
        }
        serialPort = null;
    }

    public synchronized void send(String command) {
        if (!running || serialPort == null || !serialPort.isOpen()) {
            log.warning("Cannot send command: Serial port is not open");
            throw new IllegalStateException("Serial port is not open");
        }
        try {
            serialPort.getOutputStream().write((command + "\n").getBytes());
            serialPort.getOutputStream().flush();
            log.info("Sent command: " + command);
        } catch (Exception e) {
            log.severe("Error sending command: " + e.getMessage());
            throw new RuntimeException("Failed to send command: " + e.getMessage());
        }
    }

    private void handleMessage(String message) {
        if (message.charAt(0) == '{') {
            try {
                DataEntity data = new ObjectMapper().readValue(message, DataEntity.class);
                data.setTimestamp(Instant.now());
                dataService.save(data);
                webSocketService.addNewData(data);
                log.info("Processed and saved data: " + message);
            } catch (JsonProcessingException e) {
                log.severe("JsonProcessingException: " + e.getMessage());
            }
        } else {
            log.warning("Invalid JSON message: " + message);
        }
    }

    public static String[] listPorts() {
        SerialPort[] ports = SerialPort.getCommPorts();
        String[] portNames = new String[ports.length];
        for (int i = 0; i < ports.length; i++) {
            portNames[i] = ports[i].getSystemPortName() + " (" + ports[i].getDescriptivePortName() + ")";
        }
        return portNames;
    }

    @PreDestroy
    public void onDestroy() {
        stop();
    }
}