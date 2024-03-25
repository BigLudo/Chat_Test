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
    private BufferedReader clientReader;
    private PrintWriter clientSender;
    private Thread clientThread;
    private boolean disconnected = false;

    public void setID (String newId) {
        this.ID = newId;
    }

    public ClientHandler(String id, Socket cs, IServer srv) {
        ID = id;
        clientSocket = cs;
        server = srv;
        log.info("ClientHandler created " + id);

        try {
            clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
            clientSender = new PrintWriter(clientSocket.getOutputStream(), true);

            clientThread = new Thread(this);
            clientThread.start();
        } catch (IOException e) {
            log.error("Error initiation : " + e.getMessage());
        }
    }

    @Override
    public void sendMessage(String message) {
        clientSender.println(message);
    }

    @Override
    public String receiveMessage() {
        String message = null;
        try {
            message = clientReader.readLine();
            if (message == null && !disconnected) {
                disconnected = true;
                log.info("Client disconnected: " + ID);
            }
        } catch (IOException e) {
            log.error("Error intercepting message: " + e.getMessage());
        }

        log.debug("Receiving message, ID : " + getId() + " [" + message + "]");
        return message;
    }


    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void run() {
        try {
            while (!disconnected) {
                String message = receiveMessage();
                server.sendMessage(message, this);
            }
        } catch (Exception e) {
            log.error("Error intercepting message: " + e.getMessage());
        }
        log.info("Client stopped listen on incoming messages, ID: " + getId());
    }

}
