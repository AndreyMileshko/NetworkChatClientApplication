package ru.netology.service;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;

import static ru.netology.ClientMain.logger;

public class Client {
    private static final String host = "localhost";
    private static final String SETTINGS_FILE_NAME = "src\\main\\java\\ru\\netology\\service\\settings.txt";
    private static int port;
    private static final int defaultPort = 8080;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private MessageReceiver messageReceiver;

    public Client() {
        loadConfig();
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            messageReceiver = new MessageReceiver(in);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при подключении к серверу");
        }
    }

    private void loadConfig() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SETTINGS_FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("port")) {
                    String[] split = line.split(" ");
                    if (split.length == 2) {
                        port = Integer.parseInt(split[1]);
                    } else {
                        logger.warning("Неверный формат информации в файле " + SETTINGS_FILE_NAME +
                                ", используется порт по умолчанию: " + defaultPort);
                        port = defaultPort;
                    }
                }
            }
        } catch (IOException e) {
            logger.warning("Ошибка чтения файла " + SETTINGS_FILE_NAME +
                    ", используется порт по умолчанию: " + defaultPort);
            port = defaultPort;
        } catch (NumberFormatException e) {
            logger.warning("Ошибка формата порта, используется порт по умолчанию: " + defaultPort);
            port = defaultPort;
        }
    }

    public String getServerMessage() {
        try {
            return in.readLine();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при получении сообщения от сервера");
            return null;
        }
    }

    public void sendMessage(String message) {
        if (socket != null && !socket.isClosed()) {
            out.println(message);
        } else {
            logger.log(Level.WARNING, "Не удалось отправить сообщение, сокет закрыт");
        }
    }

    public void closeResources() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при закрытии ресурсов", e);
        }
    }

    public MessageReceiver getMessageReceiver() {
        return messageReceiver;
    }
}
