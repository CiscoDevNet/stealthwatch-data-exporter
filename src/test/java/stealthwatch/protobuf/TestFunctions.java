package stealthwatch.protobuf;

import java.util.Calendar;

interface TestFunctions {

    static byte[] bytes(int... bytes) {
        byte[] result = new byte[bytes.length];
        for (int i = 0; i < bytes.length; ++i) {
            result[i] = (byte) bytes[i];
        }
        return result;
    }

    @SuppressWarnings("MethodWithTooManyParameters")
    static long epocMicroseconds(int year, int month, int date, int hour, int minute,
                                 int second, int milliseconds, int useconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, date, hour, minute, second);
        long time = calendar.getTime().getTime();
        time += milliseconds;
        time *= 1000;
        time += useconds;
        return time;
    }
}
