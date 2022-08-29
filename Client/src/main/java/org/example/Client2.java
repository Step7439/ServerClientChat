package org.example;


import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client2 {
    PrintWriter out;
    BufferedReader in;
    String resp;
    Socket clientSocket;

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.clients();

    }

    public void clients() throws IOException {
        String url = "/home/acer/IdeaProjects/ServerChat/settings.txt";
        File setings = new File(url);
        Scanner scanner1 = new Scanner(setings);
        String hostPort = scanner1.nextLine();
        String[] hostPortMas = hostPort.split(":");
        String host = hostPortMas[0];
        int port = Integer.parseInt(hostPortMas[1]);


        clientSocket = new Socket(host, port);

        scanner1.close();
        new Thread(() -> {

            try {
                while (true) {
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("- > ");
                    String name = scanner.nextLine();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out.println(name);
                    resp = in.readLine();


                    if (resp.equals("q")) {
                        out.close();
                        in.close();
                        clientSocket.close();
                    }else {
                        toString();
                    }

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public String toString() {
        System.out.println(resp);
        return resp;
    }
}