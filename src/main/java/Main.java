import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {

        Logger logger = new Logger(false);

        ServerHandler srvhHandler = new ServerHandler(logger);
        ClientHandler client1 = new ClientHandler("Ludde", srvhHandler, logger);
        ClientHandler client2 = new ClientHandler("Freddan", srvhHandler, logger);
        ClientHandler client3 = new ClientHandler("Oliver", srvhHandler, logger);

        srvhHandler.register(client1);
        srvhHandler.register(client2);
        srvhHandler.register(client3);

        client1.sendMessage("Test");

        try {
            ServerSocket ss = new ServerSocket(8080);
            Socket client = ss.accept();
            client.getOutputStream().write("testar".getBytes());
            client.getOutputStream().flush();

            System.out.println("Press any key to quit");
            System.in.read();

        } catch(Exception e) {
            System.out.println("Error : " + e);
            e.printStackTrace();
        }

    }
}

