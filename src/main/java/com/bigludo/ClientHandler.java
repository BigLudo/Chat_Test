package com.bigludo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientHandler implements IClient{

    private static final Logger log = LogManager.getLogger(ClientHandler.class);

    private String ID;
    private IServer server;    

    public ClientHandler (String id, IServer srv) {
        ID = id;
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