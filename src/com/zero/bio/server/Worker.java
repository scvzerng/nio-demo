package com.zero.bio.server;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

public class Worker implements Runnable  {
    Logger logger = Logger.getLogger(Worker.class.getSimpleName());
    Socket socket;

    public Worker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {

            byte[] buff = new byte[64];
            InputStream stream = socket.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(stream);

            int count ;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            //阻塞
            while ((count=bufferedInputStream.read(buff))!=-1){
                outputStream.write(buff,0,count);
                logger.info("receiving data:"+new String(buff)+"   count:"+count);
            }
                logger.info("receive data:"+new String(outputStream.toByteArray()));
            logger.info("ip:["+socket.getInetAddress().toString()+"]name:["+Thread.currentThread().getName()+"] close");
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
