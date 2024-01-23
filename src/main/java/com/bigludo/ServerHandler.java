package com.bigludo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerHandler implements IServer, Runnable{
    private static final Logger log = LogManager.getLogger(ServerHandler.class);

    private ArrayList <IClient> clients = new ArrayList<>();

    @Override
    public void sendMessage(String message, IClient sender) {
        for (IClient client : clients){
            if (client != sender) {
                client.sendMessage(sender.getId() + " : " + message);
            }
        }
    }

    private String getClientId() {
        return "Client#"+System.currentTimeMillis(); // Just a random unique name for now... Issue #14
    }

    public void broadcastMessage(String message) {
        for (IClient client : clients){
            client.sendMessage(message);
        }
    }

    @Override
    public void run() {
        boolean stopped = false;
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            log.info("Server thread started.");

            do {
                Socket clientSocket = serverSocket.accept();
                IClient client = new ClientHandler(getClientId(), clientSocket, this);
                client.sendMessage("Welcome " + client.getId() + " to the server!");
                clients.add(client);

                // Inform all clients
                broadcastMessage("User " + client.getId() + " joined the chat.");

            } while(!stopped);

            log.info("Server thread stopped.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
