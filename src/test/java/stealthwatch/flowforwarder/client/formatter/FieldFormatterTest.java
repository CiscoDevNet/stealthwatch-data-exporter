package stealthwatch.flowforwarder.client.formatter;

import java.net.UnknownHostException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The Unit Test for FieldFormatter.
 */
public class FieldFormatterTest {

    /**
     * Test format IPv4 address.
     *
     * @throws UnknownHostException the unknown host exception
     */
    @Test
    public void testFormatIpv4() {
        long first = 167782420;
        long second = 0;
        String result = FieldFormatter.formatIPAddress(first, second);
        assertEquals("10.0.40.20", result);
    }

    /**
     * Test format mac address.
     */
    @Test
    public void testFormatMac() {
        long value = 203178702119506L;
        String result = FieldFormatter.formatMacAddress(value);
        assertEquals("B8:CA:3A:5D:A2:52", result);
    }

    /**
     * Test format date in microseconds.
     */
    @Test
    public void testFormatDateInMicroseconds() {
        long microseconds = 1486075226020010L;
        String result = FieldFormatter.formatDateFromMicroseconds(microseconds);
        assertEquals("2017-02-02 17:40:26.020010", result);
    }
}
