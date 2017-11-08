package com.zero.bio.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class BioClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",9999);
        OutputStream stream = socket.getOutputStream();
        Scanner scanner = new Scanner(System.in);
        while (true){
            String message = scanner.nextLine();
            if(message.equals("exit")){
                socket.close();
                return;
            }
            stream.write(message.getBytes());
            stream.flush();
        }

    }
}
