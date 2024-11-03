package ru.netology.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;

import static ru.netology.ClientMain.logger;

public class MessageReceiver implements Runnable {
    private volatile boolean isContinue = true;

    private final BufferedReader in;

    public MessageReceiver(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            String response;
            while (isContinue && (response = in.readLine()) != null) {
                System.out.println(response);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при получении сообщения");
        }
    }

    public void stopReceiving() {
        isContinue = false;
    }
}