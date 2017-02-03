package stealthwatch.flowfowarder.client;

public enum SocketProtocol {
    HTTPS(443), HTTP(8092);

    private int port;

    SocketProtocol(int port){
        this.port = port;
    }

    public int getPort(){
        return this.port;
    }
}
