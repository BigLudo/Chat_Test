package com.bigludo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BigLudoChat {

    private static final Logger log = LogManager.getLogger(BigLudoChat.class);

    public static void main(String[] args) {
        log.info("BigLudo Chat v1.0.0");


        int startActiveClients = 0;
        ServerHandler srvhHandler = new ServerHandler();
        Thread serverThread = new Thread(srvhHandler);
        serverThread.start();

        try {
            while(serverThread.isAlive()){
                Thread.sleep(1000);
                int currentActiveClients = srvhHandler.getActiveClients();
                log.debug("Number of active clients: " + srvhHandler.getActiveClients());

                if (startActiveClients < currentActiveClients) {
                    srvhHandler.broadcastMessage("Welcome to the server!");
                }

                startActiveClients = currentActiveClients;
            }
        } catch(Exception e){
            log.debug("Exception, thread quitted or error...");
        }
        log.info("Chat terminated...");
    }
}

