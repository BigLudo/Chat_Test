public class ClientHandler implements IClient{

    private String ID;
    private IServer server;
    private ILog log;

    public ClientHandler (String id, IServer srv, ILog lg) {
        ID = id;
        server = srv;
        log = lg;
    }

    public void sendMessage(String message) {
        server.sendMessage(message, this);
        log.info("Sending message");
    }
    @Override
    public void recieveMessage(String message) {
        System.out.println(ID + ", " + message);
        log.debug("Receiving message" + message);
    }
    @Override
    public String getId() {
        return ID;
    }
}