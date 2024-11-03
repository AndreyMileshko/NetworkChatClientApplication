package ru.netology;

import ru.netology.service.Client;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientMain {
    public static final Logger logger = Logger.getLogger(ClientMain.class.getName());
    private static final Scanner scanner = new Scanner(System.in);
    private static final Client client = new Client();

    public static void main(String[] args) {
        System.out.println(client.getServerMessage());
        String clientName = scanner.nextLine();
        client.sendMessage(clientName);

        String greetingMessage = client.getServerMessage();
        System.out.println(greetingMessage);

        Thread receiveThread = new Thread(client.getMessageReceiver());
        receiveThread.start();

        String message;
        while (true) {
            message = scanner.nextLine();
            client.sendMessage(message);

            if (message.equalsIgnoreCase("/exit")) {
                System.out.println("Вы покинули чат.");
                break;
            }
        }
        client.getMessageReceiver().stopReceiving();
        try {
            receiveThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.log(Level.WARNING, "Поток был прерван");
        } finally {
            client.closeResources();
        }
    }
}