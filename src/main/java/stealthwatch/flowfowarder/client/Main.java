package stealthwatch.flowfowarder.client;

import static stealthwatch.flowfowarder.client.SocketProtocol.HTTP;

public class Main {

    private static final Object waitLock = new Object();

    private static void waitForTerminateSignal(FlowForwarderClient client) throws Exception{
        synchronized (waitLock) {
            try {
                waitLock.wait();
            } catch (InterruptedException ignored) {
            } finally {
                client.stopForwardingFlows();
            }
        }
    }

    public static void main(String... args) throws Exception {
        String ip = "10.0.37.30";
        FlowForwarderClient flowForwarder = new FlowForwarderClient();
        //flowForwarder.forwardFlows("10.0.37.208", HTTP);
        flowForwarder.forwardFlows(ip, HTTP);
        waitForTerminateSignal(flowForwarder);
    }

}
