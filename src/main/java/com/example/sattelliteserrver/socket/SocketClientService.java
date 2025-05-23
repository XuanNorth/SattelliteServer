package com.example.sattelliteserrver.socket;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

@Service
public class SocketClientService {

    public static boolean sendDataTCP(String ip, Integer port ,String data){
        try {
            Socket clientSocket = new Socket(InetAddress.getByName(ip), port);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(data);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
