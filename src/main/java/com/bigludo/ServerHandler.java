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
                client.recieveMessage(message);
            }
        }
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);

            while (true) {
                Thread clientThread = new Thread();
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
