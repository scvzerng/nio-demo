package com.zero;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;

public class Worker {
    SocketChannel channel;
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    public Worker(SocketChannel channel) {
        super();
        this.channel = channel;
    }
    public void process(){
        while (channel.isOpen()){
            try {
                    StringBuilder string = new StringBuilder();
                    while (channel.read(buffer)!=-1){
                        string.append(new String(buffer.array()));
                        buffer.clear();
                    }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
