package stealthwatch.flowfowarder.client;

import stealthwatch.flowfowarder.applance.FlowCollector;
import java.util.*;

public class FlowForwarderClient {
    private List<FlowCollector> flowCollectors = new ArrayList<>();


    public void forwardFlows(String ipAddress, SocketProtocol protocol) throws Exception{
        flowCollectors.add(new FlowCollector(ipAddress, protocol));
    }

    public void stopForwardingFlows() throws Exception{
        for(FlowCollector fc : flowCollectors){
            fc.closeSession();
        }
    }

}
