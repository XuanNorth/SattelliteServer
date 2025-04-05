package com.example.sattelliteserrver.test;

import com.example.sattelliteserrver.socket.SocketServerController;

public class TestSocket {
    public static void main(String[] args) {
        SocketServerController socketTCPServer = new SocketServerController();
        new Thread(socketTCPServer::start).start();
    }
}
