package stealthwatch.flowforwarder.client;

public enum SocketProtocol {
    HTTPS(443), HTTP(8092);

    private final int port;

    SocketProtocol(int port) {
        this.port = port;
    }

    public int port() {
        return this.port;
    }
}
