package stealthwatch.flowforwarder.client.utils;

import java.net.URL;

public class ResourceUtil {

    private static ClassLoader classLoaderClass = ResourceUtil.class.getClassLoader();

    public static String getPath(String name) {
        URL url = classLoaderClass.getResource(name);

        if (url == null) {
            throw new RuntimeException("Unable to identify a resource file " + name);
        }

        return url.getPath();
    }

}