package com.example.sattelliteserrver.socket;

import com.example.sattelliteserrver.data.DataEntity;
import com.example.sattelliteserrver.data.DataService;
import com.example.sattelliteserrver.websocket.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;
import java.time.Instant;
import java.util.Arrays;

@Component
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

            // Chỉ chấp nhận một client duy nhất
            Socket clientSocket = serverSocket.accept();
            System.out.println("Kết nối từ: " + clientSocket.getInetAddress());
            handleClient(clientSocket);

        } catch (IOException e) {
            System.out.println("Không thể tạo server socket: " + e.getMessage());
        }
    }

    private void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String inputLine;
            // Liên tục nhận message từ client duy nhất
            while (true) {
                inputLine = in.readLine();
                if (inputLine == null) {
                    System.out.println("Client " + clientSocket.getInetAddress() + " đã ngắt kết nối");
                    break;
                }
//                Xử lý message
                System.out.println("message = " + inputLine);
                handleMessage(inputLine);
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi xử lý client: " + e.getMessage());
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

    public void handleMessage(String message) {
        System.out.println(" Start handle message ...");
        try {
            double[] data = Arrays.stream(message.split("-")).mapToDouble(Double::parseDouble).toArray();
            DataEntity dataEntity = DataEntity.builder()
                    .longitude(data[0])
                    .latitude(data[1])
                    .altitude(data[2])
                    .temperature(data[3])
                    .pressure(data[4])
                    .timestamp(Instant.now())
                    .build();
            dataService.save(
                    dataEntity);
            webSocketService.addNewData(dataEntity);
        } catch (Exception e) {
            System.err.println("Error in TCP listener: " + e.getMessage());
        }
    }
}