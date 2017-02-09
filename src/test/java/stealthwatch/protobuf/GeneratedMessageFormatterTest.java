package stealthwatch.protobuf;

import com.google.protobuf.ByteString;
import com.lancope.sw.AddressProtos.IPAddress;
import com.lancope.sw.AddressProtos.MacAddress;
import com.lancope.sw.ExternalFlowProtos.ExtFlow;
import com.lancope.sw.ExternalFlowProtos.ExtFlowExporter;
import com.lancope.sw.ExternalFlowProtos.ExtFlowHost;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static stealthwatch.protobuf.TestFunctions.epocMicroseconds;

public class GeneratedMessageFormatterTest {

    private final GeneratedMessageFormatter generatedMessageFormatter = new GeneratedMessageFormatter();
    private final ExtFlow                   flow                      = ExtFlow
            .newBuilder()
            .setFlowId(13)
            .setClient(ExtFlowHost.newBuilder()
                                  .setAsn(31)
                                  .setIp(IPAddress.newBuilder().setA0(0x100420ff).setA1(0))
                                  .setMac(MacAddress.newBuilder().setA(0x123456789abcdef0L))
                                  .setNumBytes(20231)
                                  .setNumPackets(10)
                                  .setPayloadEx(ByteString.copyFrom("hello, world!".getBytes()))
                                  .setPort(443)
                                  .setXlateIp(IPAddress.newBuilder().setA0(0x100420ff).setA1(0))
                                  .setXlatePort(3035)
                                  .addGroupList(43))
            .setServer(ExtFlowHost.newBuilder()
                                  .setAsn(92)
                                  .setIp(IPAddress.newBuilder().setA0(0x08040201).setA1(0))
                                  .setMac(MacAddress.newBuilder().setA(0xccccddddeeeeffffL))
                                  .setNumBytes(9032)
                                  .setNumPackets(2)
                                  .setPayloadEx(ByteString.copyFrom(new byte[]{1, 2, 3, 5, 8, 13}))
                                  .setPort(2038)
                                  .setXlateIp(IPAddress.newBuilder().setA0(0xaabbcc).setA1(0))
                                  .setXlatePort(6948)
                                  .addGroupList(1)
                                  .addGroupList(3))
            .addExporters(ExtFlowExporter.newBuilder()
                                         .setInterface(4)
                                         .setIp(IPAddress.newBuilder().setA0(0x11111111).setA1(0)))
            .addExporters(ExtFlowExporter.newBuilder()
                                         .setInterface(4)
                                         .setIp(IPAddress.newBuilder().setA0(0x22222222).setA1(0)))
            .setConnections(27)
            .setFcIp(IPAddress.newBuilder().setA0(0x11111111).setA1(0))
            .setFlowSensorAppId(93)
            .setMplsLabel(902)
            .setNbarAppId(12386)
            .setPaloAltoAppId("arriba")
            .setPaloAltoAppIdBytes(ByteString.copyFrom(new byte[]{3, 1, 4, 9, 2}))
            .setSequenceNum(11000L)
            .setServicePort(55051)
            .setUsername("gpburdell")
            .setUsernameBytes(ByteString.copyFrom("gpburdell".getBytes()))
            .setStartActiveUsec(epocMicroseconds(2016, 11, 14, 3, 0, 54, 898, 71))
            .setLastActiveUsec(epocMicroseconds(2017, 1, 15, 13, 45, 23, 314, 214))
            .build();

    @Test
    public void generatedMessageFormatter() throws Exception {
        String logEntry = generatedMessageFormatter.format(flow);

        System.out.println(logEntry);

        assertTrue(logEntry.contains("flow_id=13|"));
        assertTrue(logEntry.contains("|start_active_usec=2016-12-14 03:00:55"));
        assertTrue(logEntry.contains("|last_active_usec=2017-02-15 13:45:23"));
        assertTrue(logEntry.contains("|service_port=55051|"));
        assertTrue(logEntry.contains("|flow_sensor_app_id=93|"));
        assertTrue(logEntry.contains("|nbar_app_id=12386|"));
        assertTrue(logEntry.contains("|palo_alto_app_id=	|"));
        assertTrue(logEntry.contains("|username=gpburdell|"));
        assertTrue(logEntry.contains("|mpls_label=902|"));
        assertTrue(logEntry.contains("|connections=27|"));
        assertTrue(logEntry.contains("|sequence_num=11000|"));
        assertTrue(logEntry.contains("|fc_ip=17.17.17.17|"));
        assertTrue(logEntry.contains("|client_ip=16.4.32.255|"));
        assertTrue(logEntry.contains("|client_port=443|"));
        assertTrue(logEntry.contains("|client_xlate_ip=16.4.32.255|"));
        assertTrue(logEntry.contains("|client_xlate_port=3035|"));
        assertTrue(logEntry.contains("|client_mac=56:78:9a:bc:de:f0|"));
        assertTrue(logEntry.contains("|client_asn=31|"));
        assertTrue(logEntry.contains("|client_payload_ex=0x68656c6c6f2c20776f726c6421|"));
        assertTrue(logEntry.contains("|client_group_list=[43]|"));
        assertTrue(logEntry.contains("|client_num_bytes=20231|"));
        assertTrue(logEntry.contains("|client_num_packets=10|"));
        assertTrue(logEntry.contains("|server_ip=8.4.2.1|"));
        assertTrue(logEntry.contains("|server_port=2038|"));
        assertTrue(logEntry.contains("|server_xlate_ip=0.170.187.204|"));
        assertTrue(logEntry.contains("|server_xlate_port=6948|"));
        assertTrue(logEntry.contains("|server_mac=dd:dd:ee:ee:ff:ff|"));
        assertTrue(logEntry.contains("|server_asn=92|"));
        assertTrue(logEntry.contains("|server_payload_ex=0x01020305080d|"));
        assertTrue(logEntry.contains("|server_group_list=[1, 3]|"));
        assertTrue(logEntry.contains("|server_num_bytes=9032|"));
        assertTrue(logEntry.contains("|server_num_packets=2|"));
        assertTrue(logEntry.contains("|exporters_0_ip=17.17.17.17|"));
        assertTrue(logEntry.contains("|exporters_0_interface=4|"));
        assertTrue(logEntry.contains("|exporters_1_ip=34.34.34.34|"));
        assertTrue(logEntry.contains("|exporters_1_interface=4"));
    }
}
