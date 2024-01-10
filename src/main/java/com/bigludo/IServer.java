package com.bigludo;

public interface IServer {

    void sendMessage (String message, IClient cnt);

    void broadcastMessage (String message);

}
