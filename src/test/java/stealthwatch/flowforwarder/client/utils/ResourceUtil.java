//--------------------------------------------------------------------------
// Copyright (C) 2017-2018 Cisco and/or its affiliates. All rights reserved.
//
// This source code is distributed under the terms of the MIT license.
//--------------------------------------------------------------------------

package stealthwatch.flowforwarder.client.utils;

import java.net.URL;

public class ResourceUtil {

    private static final ClassLoader classLoaderClass = ResourceUtil.class.getClassLoader();

    public static String getPath(String name) {
        URL url = classLoaderClass.getResource(name);

        if (url == null) {
            throw new RuntimeException("Unable to identify a resource file " + name);
        }

        return url.getPath();
    }

}
