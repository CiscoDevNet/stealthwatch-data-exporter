package stealthwatch.flowfowarder.client;

import com.lancope.sw.ExternalFlowProtos.ExtFlow;

import stealthwatch.flowfowarder.client.FieldFormatter.FieldFormatterType;

public class ExtFlowFunctions {
    public static String fromFlowExtToString(ExtFlow extFlow) {
    	// TODO, not properly tested as when I was running the client late I wouldn't get any message
    	MessageFormatter formatter = new MessageFormatter(extFlow);
    	formatter.addNameFormatter("start_active_usec", FieldFormatterType.DATE);
    	formatter.addNameFormatter("last_active_usec", FieldFormatterType.DATE);
    	formatter.addTypeFormatter(".com.lancope.sw.IPAddress", FieldFormatterType.IP);
    	formatter.addTypeFormatter(".com.lancope.sw.MacAddress", FieldFormatterType.MAC);
    	String tmp = formatter.format();
    	System.out.println(tmp);
    	return tmp;
    }
}
