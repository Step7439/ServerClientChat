package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.conectServer();

    }

    public void conectServer() throws IOException {
        String url = "/home/acer/IdeaProjects/ServerChat/settings.txt";
        File setings = new File(url);
        Scanner scanner1 = new Scanner(setings);
        String hostPort = scanner1.nextLine();
        String[] hostPortMas = hostPort.split(":");
        String host = hostPortMas[0];
        int port = Integer.parseInt(hostPortMas[1]);
        Socket clientSocket = new Socket(host, port);

        scanner1.close();
        client(clientSocket);
    }
    public void client(Socket clientSocket) {
        new Thread(() -> {
            try {
                while (true) {
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Write your name: ");
                    String name = scanner.nextLine();
                    out.println(name);
                    if (name.equals("exit")) {
                        break;
                    }

                    String resp = in.readLine();
                    System.out.println(resp);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
