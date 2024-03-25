package com.bigludo;

public class CommandInfo {
    private String command;
    private String argument;

    public CommandInfo(String command, String argument) {
        this.command = command;
        this.argument = argument;
    }

    public String getCommand() {
        return command;
    }

    public String getArgument() {
        return argument;
    }
}