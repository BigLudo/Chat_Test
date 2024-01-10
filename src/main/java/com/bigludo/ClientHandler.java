package com.bigludo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientHandler implements IClient, Runnable {
    private static final Logger log = LogManager.getLogger(ClientHandler.class);
    private String ID;
    private Socket clientSocket;
    private IServer server;
    private BufferedReader br;
    private Thread clientThread;
    private boolean disconnected = false;

    public ClientHandler(String id, Socket cs, IServer srv) {
        ID = id;
        clientSocket = cs;
        server = srv;
        log.info("ClientHandler created " + id);

        try {
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            clientThread = new Thread(this);
            clientThread.start();
        } catch (IOException e) {
            log.error("Error init BufferedReader: " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        try {
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println(message);

        } catch (IOException e) {
            log.error("Error sending message: " + e.getMessage());
        }
    }

    @Override
    public void receiveMessage(String message) {

        for (byte b : message.getBytes()) {
            System.out.print(b + " ");
        }
        System.out.println();

        System.out.println(ID + ": " + message);
        log.debug("Receiving message: " + "[" + message + "]");
    }


    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void run() {
        try {
            while (!disconnected) {
                String message = br.readLine();
                if (message == null && !disconnected) {
                    disconnected = true;
                    log.info("Client disconnected: " + ID);

                } else {
                    System.out.println(ID + ": " + message);

                    server.broadcastMessage(ID + ": " + message);

                    if (!ID.equals("ett-namn")) {
                        sendMessage(ID + ": " + message);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error intercepting message: " + e.getMessage());
        }
    }
}