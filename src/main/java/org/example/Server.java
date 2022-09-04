package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends Thread {
    public static LinkedList<Server> serverList = new LinkedList<>();
    protected final PrintWriter out;
    public final BufferedReader in;
    private final Socket socket;

    public Server(Socket socket) throws IOException {
        this.socket = socket;
        out = new PrintWriter(socket.getOutputStream(), true);  // переменая отпровляет сообщения
        in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // переменая принемает сообщения
        start();
    }

    public static void main(String[] args) throws IOException {

        System.out.println("Запуск сервера");
        String url = "/home/acer/IdeaProjects/ServerChat/settings.txt";    // Запуск сервера через файл
        File settings = new File(url);
        Scanner scanner1 = new Scanner(settings);
        String hostPort = scanner1.nextLine();
        String[] hostPortMas = hostPort.split(":");
        int port = Integer.parseInt(hostPortMas[1]);

        try (ServerSocket server = new ServerSocket(port)) {


            while (true) {
                Socket socket = server.accept();
                try {
                    serverList.add(new Server(socket));
                    System.out.println("Подключился клиент : " + socket);
                } catch (IOException e) {
                    socket.close();
                }
            }
        }
    }

    @Override
    public void run() {  // поток приходят сообщения от клиента
        String msg;
        try {
            msg = in.readLine();
            out.write(msg + "\n");
            out.flush();
            while (true) {
                try {
                    msg = in.readLine();
                    if (msg.equals("/exit")) {
                        this.endSocket();
                        break;
                    }
                } catch (NullPointerException ignored) {

                }
                System.out.println(msg);
                for (Server msgs : serverList) {  // переберает и отпровляет сообщения клиенту
                    msgs.msgSend(msg);
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
        } catch (Exception ignored) {
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
                System.out.println("Клиент выключился!");
            }
        } catch (IOException ignored) {
        }
    }
}
