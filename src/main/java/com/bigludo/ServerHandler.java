package com.bigludo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerHandler implements IServer, Runnable {
    private static final Logger log = LogManager.getLogger(ServerHandler.class);

    private ArrayList<IClient> clients = new ArrayList<>();

    @Override
    public void sendMessage(String message, IClient sender) {
        if (message.startsWith("/")) {
            // Parse the command and its arguments
            CommandInfo commandInfo = parseCommand(message);

            if (commandInfo != null) {
                // Handle the parsed command
                handleCommand(commandInfo, sender);
            } else {
                sender.sendMessage("Invalid command. Please check the syntax or try again later.");
            }
        } else {
            // Broadcast regular message
            broadcastMessage(sender.getId() + " : " + message);
        }
    }

    private CommandInfo parseCommand(String message) {
        try {
            String[] parts = message.split(" ", 2);

            String command = parts[0].substring(1);
            String argument = parts.length > 1 ? parts[1] : null;

            return new CommandInfo(command, argument);
        } catch (Exception e) {
            log.error("Error parsing command: " + e.getMessage());
            return null;
        }
    }

    private void handleCommand(CommandInfo commandInfo, IClient sender) {
        String command = commandInfo.getCommand();
        String argument = commandInfo.getArgument();

        switch (command) {
            case "name":
                if (argument != null) {
                    ((ClientHandler) sender).setID(argument);
                    broadcastMessage("User " + sender.getId() + " changed their name to " + argument + ".");
                } else {
                    sender.sendMessage("Invalid command usage. Usage: /name <new name>");
                }
                break;

            case "test":
                broadcastMessage("Test command received.");
                break;

            default:
                sender.sendMessage("Unknown command: " + command);
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

            } while (!stopped);

            log.info("Server thread stopped.");

        } catch (IOException e) {
            log.error("Error starting server: " + e.getMessage());
        }
    }

    private String getClientId() {
        return "Client#" + System.currentTimeMillis(); // Just a random unique name for now... Issue #14
    }

    public void broadcastMessage(String message) {
        for (IClient client : clients) {
            client.sendMessage(message);
        }
    }
}