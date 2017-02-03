package stealthwatch.flowforwarder.client;

import java.util.ArrayList;
import java.util.List;

import static stealthwatch.flowforwarder.client.SocketProtocol.HTTP;

public class FlowForwarderClient {

    private final List<FlowCollector> flowCollectors = new ArrayList<>();

    public void forwardFlows(String[] hosts){
        for (String host : hosts) {
            final FlowCollector flowCollector = new FlowCollector(host, HTTP);
            flowCollectors.add(flowCollector);
            flowCollector.startSession();
        }
    }

    public void stop(){
        for (FlowCollector flowCollector : flowCollectors) {
            flowCollector.closeSession();
        }
    }

}
