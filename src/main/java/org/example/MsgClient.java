package org.example;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MsgClient extends Thread {
    protected List<String> chat = new ArrayList<>();
    protected Socket client;

    public MsgClient(Socket client) {
        this.client = client;
        start();
    }

    @Override
    public void run() {

        try {
            while (true) {
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                String msg = in.readLine();
                chat.add(msg);

//                for (String chatMsg : chat) {
//                    out.println(chatMsg);
//                }
                out.println(chat);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
