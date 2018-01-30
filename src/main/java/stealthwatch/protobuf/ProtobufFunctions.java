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
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;

interface ProtobufFunctions {

    static String format(Entry<FieldDescriptor, Object> field, String value) {
        return String.format("%s=%s", field.getKey().getName(), value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Function<Entry<FieldDescriptor, Object>, String>
            FROM_FIELD_TO_STRING = field -> format(field, field.getValue().toString());

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Predicate<Entry<FieldDescriptor, Object>> IS_EPOC_MICROSECONDS =
            field -> field.getKey().getName().endsWith("_usec");

    Function<Entry<FieldDescriptor, Object>, String> FROM_EPOC_MICROSECONDS_TO_STRING =
            field -> format(field, Instant.ofEpochMilli((Long) field.getValue() / 1000L).toString());

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Function<Entry<FieldDescriptor, Object>, String> FROM_BYTE_STRING_TO_STRING =
            field -> format(field, asString(((ByteString) field.getValue()).toByteArray()));

    static String asString(byte... bytes) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        StringBuilder builder = new StringBuilder(bytes.length * 2 + 2).append("0x");
        for (byte b : bytes) {
            builder.append(hexDigits[b >>> 4 & 0xf]).append(hexDigits[b & 0xf]);
        }
        return builder.toString();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Function<Entry<FieldDescriptor, Object>, String> FROM_IP_ADDRESS_TO_STRING
            = field -> format(field, asString((IPAddress) field.getValue()));

    static String asString(IPAddress ipAddress) {
        long a0 = ipAddress.getA0();
        long a1 = ipAddress.getA1();
        if ((a0 & 0xffffffff00000000L) == 0 && a1 == 0) {
            return String.format("%d.%d.%d.%d", a0 >>> 24, a0 >>> 16 & 0xff, a0 >>> 8 & 0xff, a0 & 0xff);
        }
        return String.format("%04x:%04x:%04x:%04x:%04x:%04x:%04x:%04x",
                             a0 >>> 48 & 0xffff, a0 >>> 32 & 0xffff, a0 >>> 16 & 0xffff, a0 & 0xffff,
                             a1 >>> 48 & 0xffff, a1 >>> 32 & 0xffff, a1 >>> 16 & 0xffff, a1 & 0xffff);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Function<Entry<FieldDescriptor, Object>, String> FROM_MAC_ADDRESS_TO_STRING =
            field -> format(field, asString((MacAddress) field.getValue()));

    static String asString(MacAddress macAddress) {
        long a = macAddress.getA();
        return String.format("%02x:%02x:%02x:%02x:%02x:%02x",
                             a >>> 40 & 0xff, a >>> 32 & 0xff, a >>> 24 & 0xff,
                             a >>> 16 & 0xff, a >>> 8 & 0xff, a & 0xff);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SafeVarargs
    public static <T> List<T> list(T... elements) {
        List<T> list = new ArrayList<>(elements.length);
        Collections.addAll(list, elements);
        return list;
    }
}
