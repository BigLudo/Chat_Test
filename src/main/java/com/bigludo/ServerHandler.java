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
        if (message.startsWith("/")) {
            // Handle command
            handleCommand(message, sender);
        } else {
            // Broadcast regular message
            for (IClient client : clients){
                if (client != sender) {
                    client.sendMessage(sender.getId() + " : " + message);
                }
            }
        }
    }

    private void handleCommand(String message, IClient sender) {
        try {
            String[] parts = message.split(" ", 2);
            // Split the message into two parts, the command and the argument, the limit should be 2
            // since we only want to split the message into two parts.

            String command = parts[0].substring(1);
            // Should ignore the "/" sign and focus on command

            String argument = parts.length > 1 ? parts[1] : null;
            // Checks for argument

            switch (command) {
                case "name":
                    if (argument != null) {
                        // If there is an argument, it will set the client's ID to the argument.
                        ((ClientHandler)sender).setID(argument); // ID can now be set to private
                        broadcastMessage("User " + getClientId() + " changed their name to " + argument + ".");
                    } else {
                        sender.sendMessage("Invalid command usage. Usage: /name <new name>");
                    }
                    break;

                case "test":
                    broadcastMessage("Test command received.");
                    break;
                //Test command, testing if logic works for other commands

                default:
                    sender.sendMessage("Unknown command: " + command);
            }
        } catch (Exception e) {
            // Handle any exceptions that might occur during command processing
            sender.sendMessage("Invalid command. Please check the syntax or try again later.");
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

                // Inform all clients that a user joined
                broadcastMessage("User " + client.getId() + " joined the chat.");

            } while(!stopped);

            log.info("Server thread stopped.");

        } catch (IOException e) {
            e.printStackTrace();
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
}