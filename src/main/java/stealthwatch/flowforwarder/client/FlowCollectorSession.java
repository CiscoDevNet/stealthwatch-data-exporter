package stealthwatch.flowforwarder.client;

import javax.websocket.DeploymentException;
import javax.websocket.Session;

import java.io.IOException;
import java.net.URI;

import static javax.websocket.ContainerProvider.getWebSocketContainer;

public class FlowCollectorSession implements Runnable{

    private String ipAddress;
    private int port;
    private Session session;

    public FlowCollectorSession(String ip, int port){
        this.ipAddress = ip;
        this.port = port;
    }

    @Override
    public void run(){
        this.createSession();
    }

    public void stop(){
        try {
            session.close();
        } catch (IOException e){
            Loggers.system.error("Error Closing Session");
        }
    }

    private void createSession() {
        try {
            this.session = getWebSocketContainer().connectToServer(MessageHandler.class,
                    URI.create("ws://" + ipAddress + ":" + port + "/websocket"));
        } catch (IOException | DeploymentException e){
            Loggers.system.error("Error Starting Session");
        }
    }
}
