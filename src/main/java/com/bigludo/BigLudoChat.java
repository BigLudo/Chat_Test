package com.bigludo;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BigLudoChat {

    private static final Logger log = LogManager.getLogger(BigLudoChat.class);

    public static void main(String[] args) {
        log.info("BigLudo Chat v1.0.0");

        ServerHandler srvhHandler = new ServerHandler();
        Thread serverThread = new Thread();
        serverThread.start();

        ClientHandler client1 = new ClientHandler("Ludde", srvhHandler);
        ClientHandler client2 = new ClientHandler("Freddan", srvhHandler);
        ClientHandler client3 = new ClientHandler("Oliver", srvhHandler);

        srvhHandler.register(client1);
        srvhHandler.register(client2);
        srvhHandler.register(client3);

        client1.sendMessage("Testar...");
        client1.sendMessage("Ytterligare push test");

        /*
        try {
            ServerSocket ss = new ServerSocket(8000);
            Socket client = ss.accept();
            client.getOutputStream().write("testar".getBytes());
            client.getOutputStream().flush();

            System.out.println("Press any key to quit");
            System.in.read();

        } catch(Exception e) {
            System.out.println("Error : " + e);
            e.printStackTrace();
        }
        */
    }
}

