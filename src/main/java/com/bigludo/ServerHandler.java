package com.bigludo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerHandler implements IServer, Runnable{
    private ArrayList <IClient> clients = new ArrayList<>();
    private ArrayList <ClientHandler> clientHandlers = new ArrayList<>();

    private static final Logger log = LogManager.getLogger(ServerHandler.class);
    
    public void register (IClient client) {
        clients.add(client);
        log.info("Register client " + client.getId());
    }

    @Override
    public void sendMessage(String message, IClient sender) {
        for (IClient client : clients){
            if (client != sender) {
                client.recieveMessage();
            }
        }
    }

    @Override
    public void broadcastMessage(String message) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.sendMessage(message);
        }

    }

    public int getActiveClients() {
        return clientHandlers.size();
    }

    @Override
    public void run() {
        boolean stopped = false;
        try {
            ServerSocket serverSocket = new ServerSocket(8000);

            do {
                Socket clientSocket = serverSocket.accept();
                clientHandlers.add(new ClientHandler("ett-namn", clientSocket, this));
            } while(!stopped);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
