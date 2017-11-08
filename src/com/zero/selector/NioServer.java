package com.zero.selector;

import com.zero.nio.server.Worker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioServer extends Thread {
    public NioServer() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        new NioServer().start();
    }
    ExecutorService service = Executors.newCachedThreadPool();
    Selector selector = Selector.open();

    @Override
    public void run() {
        try {
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(9999));
            serverSocket.configureBlocking(false);
            serverSocket.register(selector,SelectionKey.OP_ACCEPT);
            while (true){
                 selector.select();
                selector.selectedKeys().forEach(this::handlerConnect);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handlerConnect(SelectionKey selectionKey){
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        if(selectionKey.isAcceptable()){
            ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
            try {
                channel.register(selector,SelectionKey.OP_READ);
                if(selectionKey.isReadable()){
                    channel.accept().read(buffer);
                    System.out.println(new String(buffer.array()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
