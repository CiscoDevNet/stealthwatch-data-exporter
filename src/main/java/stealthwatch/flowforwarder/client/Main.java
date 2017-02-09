package stealthwatch.flowforwarder.client;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static stealthwatch.flowforwarder.client.SocketProtocol.HTTP;

public class Main {

    private static final Object waitLock = new Object();

    private static void waitForTerminateSignal() {
        synchronized (waitLock) {
            try {
                waitLock.wait();
            } catch (InterruptedException ignored) {
            }
        }
    }

    public static void main(String... hosts) throws Exception {
        if (hosts.length == 0) {
            Loggers.system.error("No Flow Collector hostnames or IP Addresses provided.");
            System.exit(1);
        }

        List<FlowCollectorConnection> connections =
                stream(hosts).map(host -> new FlowCollectorConnection(host, HTTP)).collect(toList());

        connections.forEach(FlowCollectorConnection::run);

        waitForTerminateSignal();

        connections.forEach(FlowCollectorConnection::close);
    }
}
