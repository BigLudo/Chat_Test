package com.bigludo;

public interface IClient {

    String receiveMessage();
    void sendMessage(String message);

    String getId();
}
