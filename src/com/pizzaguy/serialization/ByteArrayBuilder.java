package com.pizzaguy.serialization;

public class ByteArrayBuilder {

    private byte[] data;

    public ByteArrayBuilder(byte[] data) {
        this.data = data;
    }

    public ByteArrayBuilder add(byte[] data) {
        if (data != null)
            this.data = combine(this.data, data);
        return this;
    }

    public byte[] Build() {
        return data;
    }

    public static byte[] combine(byte[] data1, byte[] data2) {
        int length = data1.length + data2.length;
        byte[] data = new byte[length];
        for (int i = 0; i < data1.length; i++) {
            data[i] = data1[i];
        }
        for (int i = 0; i < data2.length; i++) {
            data[data1.length + i] = data2[i];
        }
        return data;
    }

}
