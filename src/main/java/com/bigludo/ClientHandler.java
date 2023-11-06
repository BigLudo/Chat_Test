package com.bigludo;

import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientHandler implements IClient{

    private static final Logger log = LogManager.getLogger(ClientHandler.class);

    private String ID;
    private Socket clientSocket;
    private IServer server;    

    public ClientHandler (String id, Socket cs, IServer srv) {
        ID = id;
        clientSocket = cs;
        server = srv;
    }

    public void sendMessage(String message) {
        server.sendMessage(message, this);
        log.info("Sending message");
    }
    @Override
    public void recieveMessage(String message) {
        System.out.println(ID + ", " + message);
        log.debug("Receiving message" + message);
    }
    @Override
    public String getId() {
        return ID;
    }
}