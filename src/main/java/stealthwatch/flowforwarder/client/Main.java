package stealthwatch.flowforwarder.client;

public class Main {

    private static final Object waitLock = new Object();

    private static void waitForTerminateSignal(FlowForwarderClient client) throws Exception {
        synchronized (waitLock) {
            try {
                waitLock.wait();
            } catch (InterruptedException ignored) {
            } finally {
                client.stop();
            }
        }
    }

    public static void main(String... hosts) throws Exception {
        if (hosts.length == 0) {
            Loggers.system.error("No Flow Collector hostnames or IP Addresses provided.");
            System.exit(1);
        }
        FlowForwarderClient client = new FlowForwarderClient();
        client.forwardFlows(hosts);
        waitForTerminateSignal(client);
    }

}
