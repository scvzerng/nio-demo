package com.zero.nio.server;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

public class Worker implements Runnable {
    Logger logger = Logger.getLogger(Worker.class.getSimpleName());
    SocketChannel channel;
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    public Worker(SocketChannel channel) {
        this.channel = channel;

    }

    @Override
    public void run() {
        try {
            SocketAddress address = channel.getRemoteAddress();
            String threadName = Thread.currentThread().getName();
            StringBuilder allMessage = new StringBuilder();
            while (true){

                buffer.clear();
                //非阻塞
                int count = channel.read(buffer);
                if(count==0){
//                    logger.info(String.format("[%s] [%s] no data do other",address.toString(),threadName));
                    Thread.sleep(100);
                    continue;
                }
                if(count==-1){
                    channel.close();
                    logger.info(String.format("received data:%s",allMessage));
                    logger.info(String.format("[%s] [%s] close",address.toString(),threadName));
                    return;
                }
                String onceMessage = new String(buffer.array());
                allMessage.append(onceMessage);
                logger.info(String.format("receiving data:%s",onceMessage));
                buffer.clear();

            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
