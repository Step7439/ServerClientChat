package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    protected final static String status = "Привет от сервера !";
    protected static Socket clientSocket;

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.connect();

        startServer(clientSocket);

    }

    public void connect() throws IOException {
        System.out.println("Запуск сервера");
        String url = "/home/acer/IdeaProjects/ServerChat/settings.txt";
        File settings = new File(url);
        Scanner scanner1 = new Scanner(settings);
        String hostPort = scanner1.nextLine();
        String[] hostPortMas = hostPort.split(":");
        int port = Integer.parseInt(hostPortMas[1]);
        ServerSocket serverSocket = new ServerSocket(port);

        clientSocket = serverSocket.accept();
    }

    public static void startServer(Socket clientSocket) throws IOException {
        new Thread(() -> {
            while (true) {
                try {
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    String msg = in.readLine();

                    if (msg.equals("q")) {
                        out.println(status);

                    } else {
                        out.println("Не правельно !");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}

