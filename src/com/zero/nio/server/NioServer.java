package com.zero.nio.server;


import java.io.IOException;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioServer extends Thread {
    public static void main(String[] args) {
        new NioServer().start();
    }
    ExecutorService service = Executors.newCachedThreadPool();
    @Override
    public void run() {
        try {
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(9999));
            serverSocket.configureBlocking(false);
            while (true){
                Thread.sleep(1);
                //非阻塞
                SocketChannel channel = serverSocket.accept();
                if(channel!=null){
                    channel.configureBlocking(false);
                    service.submit(new Worker(channel));
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
