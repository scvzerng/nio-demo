package com.zero.nio.client;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress("127.0.0.1",9999));
            while (channel.finishConnect()){
                Scanner scanner = new Scanner(System.in);
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                String input = scanner.next();
                if(input.equals("exit")) {
                    channel.close();
                    return;
                }
                buffer.put(input.getBytes());
                buffer.flip();
                while (buffer.hasRemaining()){
                    channel.write(buffer);
                }
                buffer.clear();
            }


    }
}
