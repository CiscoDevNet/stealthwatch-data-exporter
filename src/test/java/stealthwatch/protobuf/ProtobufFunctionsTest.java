//--------------------------------------------------------------------------
// Copyright (C) 2017-2018 Cisco and/or its affiliates. All rights reserved.
//
// This source code is distributed under the terms of the MIT license.
//--------------------------------------------------------------------------

package stealthwatch.protobuf;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.lancope.sw.AddressProtos.IPAddress;
import com.lancope.sw.AddressProtos.MacAddress;
import com.lancope.sw.ExternalFlowProtos.ExtFlow;
import com.lancope.sw.ExternalFlowProtos.ExtFlowHost;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Test;

import static java.util.stream.Collectors.partitioningBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static stealthwatch.protobuf.ProtobufFunctions.FROM_BYTE_STRING_TO_STRING;
import static stealthwatch.protobuf.ProtobufFunctions.FROM_EPOC_MICROSECONDS_TO_STRING;
import static stealthwatch.protobuf.ProtobufFunctions.IS_EPOC_MICROSECONDS;
import static stealthwatch.protobuf.ProtobufFunctions.asString;
import static stealthwatch.protobuf.TestFunctions.bytes;

public class ProtobufFunctionsTest {

    @Test
    public void ipAddressV4() throws Exception {
        IPAddress ipv4 = IPAddress.newBuilder().setA0(0x100420ff).setA1(0L).build();
        assertEquals("16.4.32.255", asString(ipv4));
    }

    @Test
    public void ipAddressV6() throws Exception {
        IPAddress ipv6 = IPAddress.newBuilder().setA0(0x123456789abcdef0L).setA1(0x0fedcba987654321L).build();
        assertEquals("1234:5678:9abc:def0:0fed:cba9:8765:4321", asString(ipv6));
    }

    @Test
    public void macAddress() throws Exception {
        MacAddress macAddress = MacAddress.newBuilder().setA(0x123456789abcdef0L).build();

        assertEquals("56:78:9a:bc:de:f0", asString(macAddress));
    }

    @Test
    public void epocMicroseconds() throws Exception {
        Map<Boolean, List<Entry<FieldDescriptor, Object>>> partitions =
                ExtFlow.newBuilder().setFlowId(1L).setLastActiveUsec(1486075226020010L).build()
                       .getAllFields().entrySet().stream()
                       .collect(partitioningBy(field -> "last_active_usec".equals(field.getKey().getName())));

        Entry<FieldDescriptor, Object> lastActiveUsec = partitions.get(true).get(0);
        Entry<FieldDescriptor, Object> anotherField = partitions.get(false).get(0);

        assertFalse(IS_EPOC_MICROSECONDS.test(anotherField));
        assertTrue(IS_EPOC_MICROSECONDS.test(lastActiveUsec));
        assertEquals("last_active_usec=2017-02-02 17:40:26.020010",
                     FROM_EPOC_MICROSECONDS_TO_STRING.apply(lastActiveUsec));
    }

    @Test
    public void byteString() throws Exception {
        Map<Boolean, List<Entry<FieldDescriptor, Object>>> partitions =
                ExtFlowHost.newBuilder()
                           .setPort(443)
                           .setPayloadEx(ByteString.copyFrom(bytes(0x1, 0x5, 0xa, 0xf, 0xfe, 0xff)))
                           .build().getAllFields().entrySet().stream()
                           .collect(partitioningBy(field -> "payload_ex".equals(field.getKey().getName())));

        Entry<FieldDescriptor, Object> payloadEx = partitions.get(true).get(0);

        assertEquals("payload_ex=0x01050a0ffeff", FROM_BYTE_STRING_TO_STRING.apply(payloadEx));
    }
}
