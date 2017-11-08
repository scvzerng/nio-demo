package com.zero.bio.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioServer extends Thread {
    ServerSocket serverSocket;
    ExecutorService service = Executors.newCachedThreadPool();
    public static void main(String[] args) {
         new BioServer().start();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(9999);
            while (true){
                // 阻塞
                Socket socket = serverSocket.accept();
                service.submit(new Worker(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
