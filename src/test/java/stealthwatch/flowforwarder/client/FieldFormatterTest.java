package stealthwatch.flowforwarder.client;

import java.time.Instant;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FieldFormatterTest {

    @Test
    public void testFormatIpv4() {
        long first = 167782420;
        long second = 0;
        String result = FieldFormatter.formatIPAddress(first, second);
        assertEquals("10.0.40.20", result);
    }

    @Test
    public void testFormatMac() {
        long value = 203178702119506L;
        String result = FieldFormatter.formatMacAddress(value);
        assertEquals("B8:CA:3A:5D:A2:52", result);
    }

    @Test
    public void testFormatDateInMicroseconds() {
        long microseconds = 1486075226020010L;
        String result = FieldFormatter.formatDateFromMicroseconds(microseconds);
        assertEquals("2017-02-02T22:40:26.020Z", result);

        assertEquals(microseconds / 1000, Instant.parse(result).toEpochMilli());
    }
}
