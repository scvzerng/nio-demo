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
        try {
            while (true){
                buffer.clear();
                int count = channel.read(buffer);
                    if(count==0){
                        Thread.sleep(100);
                        continue;
                    }
                    if(count==-1){
                        channel.close();
                        System.out.println("close");

                        return;
                    }
                    StringBuilder string = new StringBuilder();
                    string.append(new String(buffer.array()));
                    buffer.clear();
                    System.out.println(Thread.currentThread().getId()+""+string);

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
