package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
    protected static List<String> users = new ArrayList<>();
    protected static ServerSocket server;
    protected static PrintWriter out;
    protected static BufferedReader in;
    protected static String msg;

    public static void main(String[] args) throws IOException {
        connect();
    }

    public static void connect() throws IOException {
        System.out.println("Запуск сервера");
        String url = "/home/acer/IdeaProjects/ServerChat/settings.txt";
        File settings = new File(url);
        Scanner scanner1 = new Scanner(settings);
        String hostPort = scanner1.nextLine();
        String[] hostPortMas = hostPort.split(":");
        int port = Integer.parseInt(hostPortMas[1]);
        server = new ServerSocket(port);
        Socket client = server.accept();
        new Thread(() -> {

            try {
                while (true) {
                    System.out.println(client);
                    out = new PrintWriter(client.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    msg = in.readLine();
                    //new MsgClient(client);

                    setAdd(msg);
                    out.println(users);
                }


            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }).start();
    }

    public static void setAdd(String msg) {
        users.add(msg);
    }

    public void end() throws IOException {
        server.close();
    }
}

