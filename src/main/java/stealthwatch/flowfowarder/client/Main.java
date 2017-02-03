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

    public static void main(String... hosts) throws Exception {
        System.out.println("STARTING");
        if(hosts.length > 0){
            FlowForwarderClient flowForwarder = new FlowForwarderClient();
            for(String ip : hosts){
                flowForwarder.forwardFlows(ip, HTTP);
            }
            waitForTerminateSignal(flowForwarder);
        }else {
            System.out.println("NO ARGS");
        }

    }

}
