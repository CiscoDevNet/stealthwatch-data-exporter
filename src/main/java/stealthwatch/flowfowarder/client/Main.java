package stealthwatch.flowfowarder.client;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class Main {

    public static void main(String... args) throws Exception{

        String destUri = "ws://10.0.37.30:8092/websocket";
        if (args.length > 0)
        {
            destUri = args[0];
        }

        WebSocketClient flowWriter = new WebSocketClient();
        JettyFlowWriter socket = new JettyFlowWriter();
        try
        {
            flowWriter.start();

            URI echoUri = new URI(destUri);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            flowWriter.connect(socket,echoUri,request);
            System.out.println("Connecting to : " + echoUri);

            // wait for closed socket connection.
            socket.awaitClose(5000000, TimeUnit.SECONDS);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        finally
        {
            try
            {
                flowWriter.stop();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        Thread.sleep(50000000000L);

    }
}

