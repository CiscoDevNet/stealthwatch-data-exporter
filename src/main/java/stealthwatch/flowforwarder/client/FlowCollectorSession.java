package stealthwatch.flowforwarder.client;

import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.glassfish.tyrus.client.SslEngineConfigurator;

import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;

import static javax.websocket.ContainerProvider.getWebSocketContainer;

public class FlowCollectorSession extends Thread {

    private String hostAddress;
    private Session session;
    private SslEngineConfigurator sslEngineConfigurator;

    public FlowCollectorSession(String hostAddress, SslEngineConfigurator sslEngineConfigurator){
        this.hostAddress = hostAddress;
        this.sslEngineConfigurator = sslEngineConfigurator;
    }

    /*
     * Creates URI from specified hostAddress and connects to it with websocket client.
     * ClientManager::connectToServer will start each web-socket client in context of current thread
     * so they all will work simultaneously.
     * */
    @Override
    public void run() {
        try {
            ClientManager container = (ClientManager) getWebSocketContainer();
            if (sslEngineConfigurator != null) {
                container.getProperties().put(ClientProperties.SSL_ENGINE_CONFIGURATOR, sslEngineConfigurator);
            }
            this.session = container.connectToServer(MessageHandler.class, URI.create(hostAddress));
        } catch (IOException | IllegalArgumentException e) {
            Loggers.system.error("Malformed URI: ", e);
        } catch (DeploymentException e) {
            Loggers.system.error("Error Starting Session: ", e);
        }
    }

    @Override
    public void interrupt() {
        try {
            session.close();
        } catch (IOException e) {
            Loggers.system.error("Error Closing Session: ", e);
        }
    }
}