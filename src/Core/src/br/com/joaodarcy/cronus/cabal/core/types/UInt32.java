/*
 * Copyright (C) 2013 João Darcy Tinoco Sant´Anna Neto <jdtsncomp@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package br.com.joaodarcy.cronus.cabal.core.types;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class UInt32 extends Number implements Comparable<UInt32> {
    private final long value;
    
    public final static UInt32 ZERO = new UInt32(0);
    
    public final static long MIN_VALUE = 0x00000000l;
    public final static long MAX_VALUE = 0xFFFFFFFFl;
    
    public UInt32(int signedValue){
        this((long)signedValue);
    }
    
    public UInt32(long value) {
        this.value = value & MAX_VALUE;
    }                    
          
    public void print(String msg){
        System.out.printf(">> %s: %08X (%d)\n", msg, value, value);
    }
    
    private void copyValue(byte src[], byte out[], int startOffset){
        //System.out.printf(">> Copying value:");
        //for(int i = 0 ; i < src.length ; i++){
        //    System.out.printf(" %02X", src[i]);
        //}        
        //System.out.println(" (" + value + ")");
        if(startOffset < out.length){
            out[startOffset] = src[0];
            if(startOffset + 1 < out.length){
                out[startOffset + 1] = src[1];
                if(startOffset + 2 < out.length){
                    out[startOffset + 2] = src[2];
                    if(startOffset + 3 < out.length){
                        out[startOffset + 3] = src[3];
                    }
                }
            }
        }
                                       
        //System.arraycopy(src, 0, out, startOffset, startOffset + 4 > out.length ? startOffset + 4 - out.length : 4);
    }
    
    public void copyValueLE(byte out[], int startOffset){
        copyValue(getBytesLE(), out, startOffset);
    }
    
    public static UInt32 fromByteArrayLE(byte data[], int startOffset){
//        if((startOffset + 4) > data.length){
//            throw new IndexOutOfBoundsException("O tamanho do vetor não é suficiente para extrair um valor inteiro, startOffset: " + startOffset +", tamanho do vetor: " + data.length);
//        }
        long value = 0l;
  
        if(startOffset < data.length){
            value = value | (((long)data[startOffset]) & 0xFFl);
            if(startOffset + 1 < data.length){
                value = value | ((((long)data[startOffset + 1]) << 8) & 0xFF00l);
                if(startOffset + 2 < data.length){
                    value = value | ((((long)data[startOffset + 2]) << 16) & 0xFF0000l);
                    if(startOffset + 3 < data.length){
                        value = value | ((((long)data[startOffset + 3]) << 24) & 0xFF000000l);
                    }
                }
            }
        }

        return new UInt32(value);
    }
    
    public byte[] getBytesBE(){
        return new byte[]{
            (byte)((value & 0xFF000000l) >> 24),
            (byte)((value & 0x00FF0000l) >> 16),
            (byte)((value & 0x0000FF00l) >> 8),
            (byte) (value & 0x000000FFl)
        };
    }
    
    public byte[] getBytesLE(){
        return new byte[]{
            (byte) (value & 0x000000FFl),
            (byte)((value & 0x0000FF00l) >> 8),
            (byte)((value & 0x00FF0000l) >> 16),
            (byte)((value & 0xFF000000l) >> 24)
        };
    }
    
    public UInt32 decrement(){
        if(this.value == MIN_VALUE){
            return this;
        }
        return new UInt32(this.value - 1l);
    }
    
    public UInt32 increment(){
        if(this.value == MAX_VALUE){
            return this;
        }
        return new UInt32(this.value + 1l);
    }
    
    public UInt32 leftShift(int times){
        return new UInt32(this.value << times);
    }
    
    public UInt32 rightShift(int times){
        return new UInt32(this.value >> times);
    }
    
    public UInt32 bitwiseXor(UInt32 unsigned){
        return new UInt32(this.value ^ unsigned.value);
    }
    
    public UInt32 bitwiseAnd(UInt32 unsigned){
        return new UInt32(this.value & unsigned.value);
    }        
    
    public UInt32 or(UInt32 unsigned){
        return new UInt32(this.value | unsigned.value);
    }    
    
    public UInt32 add(int signed){
        return add(new UInt32(signed));
    }
    
    public UInt32 add(UInt32 unsigned){
        return new UInt32(this.value + unsigned.value);
    }
    
    public UInt32 mul(UInt32 unsigned){
        return new UInt32(this.value * unsigned.value);
    }    
    
    @Override
    public int intValue() {
        return Long.valueOf(value).intValue();
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return Long.valueOf(value).floatValue();
    }

    @Override
    public double doubleValue() {
        return Long.valueOf(value).doubleValue();
    }
    
    public boolean biggerThan(int value){
        return this.value > value;
    }

    @Override
    public int compareTo(UInt32 t) {
        if(t.value == value){
            return 0;
        }else if(t.value > value){
            return 1;
        }else{
            return -1;
        }
    }
    
    public static UInt32 fromIpAddress(String address){
        String values[] = address.split("\\.");
        
        long shiftCount = 0;
        long ipValue = 0l;
        
        for(int i = 0 ; i < 4 ; i++, shiftCount += 8){
            int cv = Integer.valueOf(values[i]);
            if(shiftCount != 0){
                cv = cv << shiftCount;
            }
            
            ipValue |= cv;
        }
        
        return new UInt32(ipValue);
    }
    
    public String toIpAddress(){
        byte bytes[] = getBytesLE();
        
        return String.format(
            "%d.%d.%d.%d", 
            ((int)bytes[0]) & 0xFF,
            ((int)bytes[1]) & 0xFF,
            ((int)bytes[2]) & 0xFF,
            ((int)bytes[3]) & 0xFF
        );
    }
    
    public String hexString(){
        return String.format("0x%08X", value);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj instanceof UInt32){
            return ((UInt32)obj).value == value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        /*int hash = 7;
        hash = 53 * hash + (int) (this.value ^ (this.value >>> 32));
        return hash;*/
        return Long.valueOf(value).hashCode();
    }
    
    public static UInt32 valueOf(int int32){
        return new UInt32(int32);
    }
    
    public static UInt32 valueOf(long int64){
        return new UInt32(int64);
    }    

    @Override
    public String toString() {
        return String.format("%d", value);
    }    
}
