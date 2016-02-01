package com.pizzaguy.serialization;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class SerializationReader {
    
    public static int readByte(int pointer, byte[] src) {
        return Byte.toUnsignedInt(src[pointer]);
    }

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
    
    public static String readString(int pointer, byte[] src){
        short length = readShort(pointer, src);
        byte[] data = readBytes(pointer + 2, src, length);
        return new String(data);
    }

}
