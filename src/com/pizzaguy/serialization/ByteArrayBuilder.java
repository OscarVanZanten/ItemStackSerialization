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

import java.util.ArrayList;
import java.util.List;

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

    public byte[] build() {
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

    public static byte[][] split(byte[] data, byte[] delimiter) {
        byte[][] result = null;
        List<Integer> pointers = new ArrayList<Integer>();
        for (int i = 0; i < data.length - (delimiter.length - 1); i++) {
            byte[] d = SerializationReader.readBytes(i, data, delimiter.length);
            if(equals(d, delimiter)){
                pointers.add(i);
            }
        }
        result = new byte[pointers.size()][];
        for(int i = 0; i < pointers.size(); i++){
            if(i == pointers.size() - 1){
                result[i] = subByteArray(data, pointers.get(i), data.length);
            } else {
                result[i] = subByteArray(data, pointers.get(i) , pointers.get(i+1));
            }
        }
        return result;
    }
    
    public static boolean equals(byte[] data1, byte[] data2){
        if(data1.length != data2.length)
            return false;
        for(int i = 0; i < data1.length ;i++){
            if(data1[i] != data2[i])
                return false;
        }
        return true;
    }
    
    public static byte[] subByteArray(byte[] src, int p1, int p2){
        if(p2 <= p1)
            return null;
        int length = p2-p1;
        byte[] result = new byte[length];
        for(int i = 0; i < length; i++){
            result[i] = src[i + p1];
        }
        return result;
    }
    
    

}
