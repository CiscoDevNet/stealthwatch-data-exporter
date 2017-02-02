package stealthwatch.flowfowarder.client;

import com.lancope.sw.ExternalFlowProtos.ExtFlow;

public class ExtFlowFunctions {
    public static String fromFlowExtToString(ExtFlow extFlow) {
        return "flow-sensor-app-id: " + String.valueOf(extFlow.getFlowSensorAppId());
    }
}
