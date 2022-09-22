package org.example;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class Client {
    private BufferedWriter out;
    private BufferedReader in;

    private Socket clientSocket;
    private BufferedReader user;
    private String userNick;


    public Client() throws IOException {
        String url = "/home/acer/IdeaProjects/ServerClientChat/settings.txt";  // Запуск клиента через файл
        File setings = new File(url);
        Scanner scanner1 = new Scanner(setings);
        String hostPort = scanner1.nextLine();
        String[] hostPortMas = hostPort.split(":");
        final String HOST = hostPortMas[0];
        final int PORT = Integer.parseInt(hostPortMas[1]);
        try {
            clientSocket = new Socket(HOST, PORT);            // Подключения к серверу
        } catch (IOException e) {
            System.out.println("Ошибка сокет не создан!");
        }

        try {
            user = new BufferedReader(new InputStreamReader(System.in));                    // Ник клиентов
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));   // Переменая принемает сообщения
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));  // Переменая отпровляет сообщения
            userNick = this.nickUser();
            new ReadChat().start();
            new WriteChat().start();
        } catch (IOException e) {
            endSocket();
        }
    }

    public static void main(String[] args) throws IOException {
        new Client();

    }

    public String nickUser() {                      // Сохроняет и отпровляет клиента на сервер
        System.out.println("Ведите вааш ник !");
        try {
            userNick = user.readLine();
            out.write("Добро пожаловать " + userNick + " ! " + "\n");
            out.flush();
        } catch (IOException ignored) {

        }
        return userNick;
    }

    private void endSocket() {                  // Отключает клента
        try {
            if (!clientSocket.isClosed()) {
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {
        }
        clientSocket.isClosed();
    }

    public class ReadChat extends Thread {  // Клиент принемает сообщения
        @Override
        public void run() {
            String msgRead;
            try {
                while (true) {
                    msgRead = in.readLine();
                    if (msgRead.equals("/exit")) {
                        endSocket();
                        break;
                    }
                    System.out.println(msgRead);
                    System.out.print("->");
                }
            } catch (IOException e) {
                endSocket();
            }
        }
    }

    public class WriteChat extends Thread {  // Клиент отпровляет сообщения
        @Override
        public void run() {
            while (true) {
                String msgWrite;
                try {
                    msgWrite = user.readLine();
                    if (msgWrite.equals("/exit")) {
                        out.write("/exit" + "\n");
                        endSocket();
                        break;
                    } else {
                        out.write(new SimpleDateFormat("dd.MM.yyyy HH.mm.ss ").format(Calendar.getInstance().getTime())
                                + userNick + " : " + msgWrite + "\n");
                    }
                    out.flush();
                } catch (Exception e) {
                    endSocket();
                }

            }
        }
    }
}
