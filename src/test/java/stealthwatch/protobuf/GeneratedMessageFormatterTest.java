//--------------------------------------------------------------------------
// Copyright (C) 2017-2018 Cisco and/or its affiliates. All rights reserved.
//
// This source code is distributed under the terms of the MIT license.
//--------------------------------------------------------------------------

package stealthwatch.protobuf;

import com.google.protobuf.ByteString;
import com.lancope.sw.AddressProtos.IPAddress;
import com.lancope.sw.AddressProtos.MacAddress;
import com.lancope.sw.ExternalFlowProtos.ExtFlow;
import com.lancope.sw.ExternalFlowProtos.ExtFlowExporter;
import com.lancope.sw.ExternalFlowProtos.ExtFlowHost;
import java.util.Calendar;
import org.junit.Test;

public class GeneratedMessageFormatterTest {
    private final ExtFlow flow =
            ExtFlow.newBuilder()
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
                   .setLastActiveUsec(epocMicroseconds(2017, 1, 15, 13, 45, 23, 314, 214))
                   .setStartActiveUsec(epocMicroseconds(2016, 11, 14, 3, 0, 54, 998, 71))
                   .build();

    @SuppressWarnings("JUnitTestMethodWithNoAssertions")
    @Test
    public void generatedMessageFormatter() throws Exception {
        System.out.println(new GeneratedMessageFormatter().format(flow));
    }

    @SuppressWarnings("MethodWithTooManyParameters")
    private static long epocMicroseconds(int year, int month, int date, int hours, int minutes,
                                         int seconds, int milliseconds, int useconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date, hours, minutes, seconds);
        return (calendar.getTime().getTime() + milliseconds) * 1000 + useconds;
    }
}
