package com.bigludo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientHandler implements IClient, Runnable{
    private static final Logger log = LogManager.getLogger(ClientHandler.class);
    private String ID;
    private Socket clientSocket;
    private IServer server;
    private BufferedReader br;
    private Thread clientThread;

    public ClientHandler (String id, Socket cs, IServer srv) {
        ID = id;
        clientSocket = cs;
        server = srv;
        log.info("ClientHandler created " + id);

        try {
            clientThread = new Thread(this);
            clientThread.start();
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            log.error("Error init BufferedReader: " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        server.sendMessage(message, this);
        log.info("Sending message");
    }
    @Override
    public void recieveMessage(String message) {
        System.out.println(ID + ", " + message);
        log.debug("Receiving message: " + "[" + message + "]");
    }
    @Override
    public String getId() {
        return ID;
    }
    @Override
    public void run() {
        try {
            while (true) {
                String message = br.readLine();

                if (message == null) {
                    log.info("Client disconnected: " + ID);
                    break;
                }

                recieveMessage(message);
            }
        } catch (IOException e) {
            log.error("Error intercepting message: " + e.getMessage());
        }
    }
}