//--------------------------------------------------------------------------
// Copyright (C) 2017-2018 Cisco and/or its affiliates. All rights reserved.
//
// This source code is distributed under the terms of the MIT license.
//--------------------------------------------------------------------------

package stealthwatch.protobuf;

interface TestFunctions {

    static byte[] bytes(int... bytes) {
        byte[] result = new byte[bytes.length];
        for (int i = 0; i < bytes.length; ++i) {
            result[i] = (byte) bytes[i];
        }
        return result;
    }
}
