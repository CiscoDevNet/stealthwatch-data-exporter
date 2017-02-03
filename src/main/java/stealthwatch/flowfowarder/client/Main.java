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

    public static void main(String... ips) throws Exception {
        FlowForwarderClient flowForwarder = new FlowForwarderClient();
        for(String ip : ips){
            flowForwarder.forwardFlows(ip, HTTP);
        }
        waitForTerminateSignal(flowForwarder);
    }

}
