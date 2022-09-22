package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server extends Thread {
    public static LinkedList<Server> serverList = new LinkedList<>();
    protected final BufferedWriter out;
    public final BufferedReader in;
    private final Socket socket;

    public Server(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // переменая принемает сообщения
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));  // переменая отпровляет сообщения
        start();
    }

    public static void main(String[] args) throws IOException {
        logServer("Запуск сервера");
        System.out.println("Запуск сервера");
        String url = "/home/acer/IdeaProjects/ServerClientChat/settings.txt";    // Запуск сервера через файл
        File settings = new File(url);
        Scanner scanner1 = new Scanner(settings);
        String hostPort = scanner1.nextLine();
        String[] hostPortMas = hostPort.split(":");
        final int PORT = Integer.parseInt(hostPortMas[1]);

        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = server.accept();
                try {
                    serverList.add(new Server(socket));
                    logServer("Подключился клиент : " + socket);
                    System.out.println("Подключился клиент : " + socket);
                } catch (IOException e) {
                    socket.close();
                }
            }
        }
    }

    @Override
    public void run() {
        String msg;
        try {
            msg = in.readLine();
            out.write(msg + "\n");
            out.flush();
            while (true) {
                msg = in.readLine();
                try {
                    if (msg.equals("/exit")) {
                        this.endSocket();
                        break;
                    }
                } catch (NullPointerException e) {
                    this.endSocket();
                }
                if (msg != null) {
                    logServer(msg);
                    System.out.println(msg);
                }
                for (Server vr : serverList) {
                    vr.msgSend(msg);
                }
            }
        } catch (IOException e) {
            this.endSocket();
        }
    }

    private void msgSend(String msg) { // отпровляет сообщения клиенту
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (Exception e) {
        }
    }

    protected void endSocket() { // отключате клиента
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();

                for (Server vr : serverList) {
                    if (vr.equals(this)) {
                        vr.interrupt();
                    }
                    serverList.remove(this);
                }
                logServer("Клиент выключился!");
                System.out.println("Клиент выключился!");
            }
        } catch (IOException e) {
            System.out.println("Hellp");
        }
    }

    public static void logServer(String log) throws IOException {
        FileWriter logs = new FileWriter("log.txt", true);
        logs.append(new SimpleDateFormat("dd.MM.yyyy HH.mm.ss ").format(Calendar.getInstance().getTime()))
                .append(" ")
                .append(log)
                .append("\n")
                .flush();
    }
}
