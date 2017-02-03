package stealthwatch.flowforwarder.client;

import com.lancope.sw.ExternalFlowProtos.ExtFlow;

import stealthwatch.flowforwarder.client.FieldFormatter.FieldFormatterType;

public class ExtFlowFunctions {

    public static String fromFlowExtToString(ExtFlow extFlow) {
    	MessageFormatter formatter = new MessageFormatter(extFlow);
    	formatter.addNameFormatter("start_active_usec", FieldFormatterType.DATE);
    	formatter.addNameFormatter("last_active_usec", FieldFormatterType.DATE);
    	formatter.addTypeFormatter(".com.lancope.sw.IPAddress", FieldFormatterType.IP);
    	formatter.addTypeFormatter(".com.lancope.sw.MacAddress", FieldFormatterType.MAC);
    	String tmp = formatter.format();
    	return tmp;
    }

}