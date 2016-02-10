/*
 * Copyright (C) SainttX <http://sainttx.com>
 * Copyright (C) contributors
 *
 * This file is part of Auctions.
 *
 * Auctions is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Auctions is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Auctions.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.pizzaguy.serialization;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class SerializationReader {


    public static byte[] readBytes(int pointer, byte[] src, int length) {
        byte[] data = new byte[length];
        for (int i = 0; i < length; i++)
            data[i] = src[pointer + i];
        return data;
    }

    public static char readChar(int pointer, byte[] src) {
        return (char) src[pointer];
    }

    public static short readShort(int pointer, byte[] src) {
        byte[] data = Arrays.copyOfRange(src, pointer, pointer + Short.BYTES);
        return ByteBuffer.wrap(data).getShort();
    }

    public static int readInt(int pointer, byte[] src) {
        byte[] data = Arrays.copyOfRange(src, pointer, pointer + Integer.BYTES);
        return ByteBuffer.wrap(data).getInt();
    }

    public static long readLong(int pointer, byte[] src) {
        byte[] data = Arrays.copyOfRange(src, pointer, pointer + Long.BYTES);
        return ByteBuffer.wrap(data).getLong();
    }

    public static float readFloat(int pointer, byte[] src) {
        byte[] data = Arrays.copyOfRange(src, pointer, pointer + Float.BYTES);
        return ByteBuffer.wrap(data).getFloat();
    }

    public static double readDouble(int pointer, byte[] src) {
        byte[] data = Arrays.copyOfRange(src, pointer, pointer + Double.BYTES);
        return ByteBuffer.wrap(data).getDouble();
    }

    public static String readString(int pointer, byte[] src) {
        short length = readShort(pointer, src);
        byte[] data = readBytes(pointer + 2, src, length);
        return new String(data);
    }

    public static boolean readBoolean(int pointer, byte[] src) {
        return (src[pointer] & 0xff) >= 1;
    }

}
