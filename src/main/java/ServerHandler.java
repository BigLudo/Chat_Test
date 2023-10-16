import java.util.ArrayList;

public class ServerHandler implements IServer{
    private ArrayList <IClient> clients = new ArrayList<>();
    private ILog log;

    public ServerHandler (ILog lg) {
        log = lg;
    }

    public void register (IClient client) {
        clients.add(client);
        log.info("Register client " + client.getId());
    }

    @Override
    public void sendMessage(String message, IClient sender) {
        for (IClient client : clients){
            if (client != sender) {
                client.recieveMessage(message);
            }
        }
    }
}
