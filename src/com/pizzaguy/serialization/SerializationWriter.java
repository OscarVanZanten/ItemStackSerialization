package com.pizzaguy.serialization;

import org.bukkit.Material;

public class SerializationWriter {

    public static int writeBytes(int pointer, byte[] data, byte value) {
        data[pointer++] = value;
        return pointer;
    }

    public static int writeBytes(int pointer, byte[]data, char value){
        data[pointer++] = (byte) value;
        return pointer;
    }
    
    public static int writeBytes(int pointer, byte[] data, byte[] values) {
        for (int i = 0; i < values.length; i++){
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
    
    public static int writeBytes(int pointer, byte[] data, String string){
        pointer = writeBytes(pointer, data, (short) string.length());
        return writeBytes(pointer, data, string.getBytes());
    }
    
    public static int writeBytes(int pointer, byte[] data, boolean value){
    	byte bool = (byte) (value ? 1 : 0);
    	pointer = writeBytes(pointer, data, bool);
    	return pointer;
    }
}
