package com.example.sattelliteserrver.socket;

import com.example.sattelliteserrver.data.DataEntity;
import com.example.sattelliteserrver.data.DataService;
import com.example.sattelliteserrver.websocket.WebSocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;
import java.time.Instant;
import java.util.Arrays;

@Component
@Log
public class SocketServerController {

    private ServerSocket serverSocket;
    private volatile boolean running = false;

    @Autowired
    DataService dataService;
    @Autowired
    WebSocketService webSocketService;

    public void start() {
        try {
            int port = 8081;
            serverSocket = new ServerSocket(port);
            running = true;
            System.out.println("Server đang chạy trên port " + port);

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Kết nối từ: " + clientSocket.getInetAddress());
                    handleClient(clientSocket);
                } catch (IOException e) {
                    if (!running) {
                        break;
                    }
                    System.out.println("Lỗi khi chấp nhận kết nối: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Không thể tạo server socket: " + e.getMessage());
        } finally {
            stop();
        }
    }
    private void handleClient(Socket clientSocket) throws IOException {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String inputLine;
            while (true) {
                inputLine = in.readLine();
                if (inputLine == null) {
                    System.out.println("Client " + clientSocket.getInetAddress() + " đã ngắt kết nối");
                    break;
                }
                System.out.println("message = " + inputLine);
                handleMessage(inputLine);
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + e.getMessage());
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi đóng server: " + e.getMessage());
        }
    }

    private void handleMessage(String message) {
        log.info("Serial message: " + message);
        if (message.charAt(0) == '{') {
            DataEntity data = null;
            try {
                data = new ObjectMapper().readValue(message, DataEntity.class);
                data.setTimestamp(Instant.now());
            } catch (JsonProcessingException e) {
                log.severe("JsonProcessingException: " + e.getMessage());
            }
            dataService.save(data);
            webSocketService.addNewData(data);
        }
    }
}