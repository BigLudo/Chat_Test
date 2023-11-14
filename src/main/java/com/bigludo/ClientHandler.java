package com.bigludo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientThread = new Thread(this);
            clientThread.start();
        } catch (IOException e) {
            log.error("Error init BufferedReader: " + e.getMessage());
        }
    }

    /*public void sendMessage(String message) {
        server.sendMessage(message, this);
        log.info("Sending message: " + message);
    }*/

    public void sendMessage (String message) {
        try {
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println(message);
        } catch (IOException e) {
            log.error("Error sending message: " + e.getMessage());
        }
    }

    @Override
    public void recieveMessage() {
        try {
            String message = br.readLine();

            if (message == null && !disconnected) {
                disconnected = true;
                log.info("Client disconnected: " + ID);
            } else {
                System.out.println(ID + ": " + message);
                log.debug("Receiving message: " + "[" + message + "]");
            }

           // if (!disconnected) {
             //   System.out.println(ID + ", " + message);

            //}
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public void run() {
        try {
            while (!disconnected) {
                recieveMessage();
            }
        } catch (Exception e) {
            log.error("Error intercepting message: " + e.getMessage());
        }
    }
}