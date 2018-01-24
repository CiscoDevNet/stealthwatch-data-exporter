//--------------------------------------------------------------------------
// Copyright (C) 2017 Cisco and/or its affiliates. All rights reserved.
//
// This source code is distributed under the terms of the MIT license.
//--------------------------------------------------------------------------

package stealthwatch.flowforwarder.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.String.format;

/**
 * The Field Formatter utilities.
 */
class FieldFormatter {

    public enum FieldFormatterType {IP_ADDRESS, MAC_ADDRESS, DATE}

    /**
     * Format IP address.
     *
     * @param first  the first value from the protobuf
     * @param second the second from the protobuf
     *
     * @return the formatted string
     */
    static String formatIPAddress(long first, long second) {
        return ipV4(first, second)
                ? format("%d.%d.%d.%d",
                         (int) (first >>> 24),
                         (int) (first >>> 16) & 0xFF,
                         (int) (first >>> 8) & 0xFF,
                         (int) first & 0xFF)
                : hostAddress(ByteBuffer.allocate(16).putLong(first).putLong(second));
    }

    private static String hostAddress(ByteBuffer buf) {
        try {
            return InetAddress.getByAddress(buf.array()).getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException("Unable to resolve host address", e);
        }
    }

    private static boolean ipV4(long first, long second) {
        return second == 0 && (first & 0xFFFFFFFF00000000L) == 0;
    }

    /**
     * Format mac address.
     *
     * @param value the value
     *
     * @return the string
     */
    static String formatMacAddress(long value) {
        return format("%02X:%02X:%02X:%02X:%02X:%02X",
                      (int) (value >>> 40) & 0xFF,
                      (int) (value >>> 32) & 0xFF,
                      (int) (value >>> 24) & 0xFF,
                      (int) (value >>> 16) & 0xFF,
                      (int) (value >>> 8) & 0xFF,
                      (int) value & 0xFF);
    }

    /**
     * Format date from microseconds value.
     *
     * @param microseconds the microseconds
     *
     * @return the string
     */
    static String formatDateFromMicroseconds(long microseconds) {
        long milliseconds = microseconds / 1000L;
        long remaining = microseconds % 1000L;
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(milliseconds));
        return format("%s%03d", date, remaining);
    }
}
