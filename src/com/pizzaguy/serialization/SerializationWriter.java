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

public class SerializationWriter {

    public static int writeBytes(int pointer, byte[] data, byte value) {
        data[pointer++] = value;
        return pointer;
    }

    public static int writeBytes(int pointer, byte[] data, char value) {
        data[pointer++] = (byte) value;
        return pointer;
    }

    public static int writeBytes(int pointer, byte[] data, byte[] values, int length) {
        for (int i = 0; i < length; i++) {
            pointer = writeBytes(pointer, data, values[i]);
        }
        return pointer;
    }

    public static int writeBytes(int pointer, byte[] data, short value) {
        data[pointer++] = (byte) ((value >> 8) & 0xff);
        data[pointer++] = (byte) ((value >> 0) & 0xff);
        return pointer;
    }

    public static int writeBytes(int pointer, byte[] data, int value) {
        data[pointer++] = (byte) ((value >> 24) & 0xff);
        data[pointer++] = (byte) ((value >> 16) & 0xff);
        data[pointer++] = (byte) ((value >> 8) & 0xff);
        data[pointer++] = (byte) ((value) & 0xff);
        return pointer;
    }

    public static int writeBytes(int pointer, byte[] data, long value) {
        data[pointer++] = (byte) ((value >> 56) & 0xff);
        data[pointer++] = (byte) ((value >> 48) & 0xff);
        data[pointer++] = (byte) ((value >> 40) & 0xff);
        data[pointer++] = (byte) ((value >> 32) & 0xff);
        data[pointer++] = (byte) ((value >> 24) & 0xff);
        data[pointer++] = (byte) ((value >> 16) & 0xff);
        data[pointer++] = (byte) ((value >> 8) & 0xff);
        data[pointer++] = (byte) ((value) & 0xff);
        return pointer;
    }

    public static int writeFloat(int pointer, byte[] data, float value) {
        int i = Float.floatToIntBits(value);
        return writeBytes(pointer, data, i);
    }

    public static int writeBytes(int pointer, byte[] data, double value) {
        long i = Double.doubleToLongBits(value);
        return writeBytes(pointer, data, i);
    }

    public static int writeBytes(int pointer, byte[] data, String string) {
        pointer = writeBytes(pointer, data, (short) string.length());
        return writeBytes(pointer, data, string.getBytes(), string.length());
    }

    public static int writeBytes(int pointer, byte[] data, boolean value) {
        byte bool = (byte) (value ? 255 : 0);
        pointer = writeBytes(pointer, data, bool);
        return pointer;
    }
}
