package stealthwatch.flowfowarder.client;

import stealthwatch.flowfowarder.applance.FlowCollector;

import java.util.ArrayList;
import java.util.List;

import static stealthwatch.flowfowarder.client.SocketProtocol.HTTP;

public class Main {

    private static final Object waitLock = new Object();

    private static void waitForTerminateSignal(List<FlowCollector> flowCollectors) throws Exception {
        synchronized (waitLock) {
            try {
                waitLock.wait();
            } catch (InterruptedException ignored) {
            } finally {
                for (FlowCollector flowCollector : flowCollectors) {
                    flowCollector.closeSession();
                }
            }
        }
    }

    public static void main(String... hosts) throws Exception {
        System.out.println("STARTING");

        if (hosts.length == 0) {
            System.out.println("NO ARGS");
            System.exit(1);
        }

        List<FlowCollector> flowCollectors = new ArrayList<>();

        for (String host : hosts) {
            final FlowCollector flowCollector = new FlowCollector(host, HTTP);
            flowCollectors.add(flowCollector);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        flowCollector.startSession();
                    } catch (Exception e) {
                        Loggers.system.error("Unable to start flow collector", e);
                    }
                }
            }).start();
        }
        waitForTerminateSignal(flowCollectors);
    }

}
