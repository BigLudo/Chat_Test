# Chat server and client

This is a work in progress of a simple chat server and client application 
that allows users to communicate in real-time.


### Features


### Way of testing

Since the program is in early stages of testing, no client has been constructed as of now.
To test the connection, we therefore recommend using another client such as TelNet.
1. Download the source code for the repository
2. Download or activate the TelNet by accessing **Windows control panel** >> **Programs** >> 
**Programs and functions** >> **Activate or deactivate windows functions** and then activating the Telnet attachment. 
3. Run the program to initialize thread.
4. Type in the Windows powershell **"open 127.0.0.1 8000"** to access hardcoded port.

## Commond line

- Maven version: Apache Maven 3.8.3
- Java version: Openjdk version "21.0.1" 2023-10-17

### build

Build the java package with maven.
```sh
mvn package assembly:single
```

### run

1. Start server
```sh
java -cp target/chat-test-1.1-SNAPSHOT-jar-with-dependencies.jar com.bigludo.BigLudoChat
```

2. Connect client(s), start arbitrary number of clients for test
```sh
telnet 127.0.0.1
```