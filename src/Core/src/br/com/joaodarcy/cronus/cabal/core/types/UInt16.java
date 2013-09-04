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
public class UInt16 extends Number implements Comparable<UInt16> {
    
    public final static UInt16 ZERO = new UInt16(0);
    
    /** 0 */
    public final static int MIN_VALUE = 0x0000;
    /** 65535 = ((2 ^ 16) - 1)*/
    public final static int MAX_VALUE = 0xFFFF;
    
    final int value;

    public UInt16(short int16){
        this((int)int16);
    }
    
    public UInt16(int int32) {
        this.value = (int32 & MAX_VALUE);
    }           
    
    int rawValue(){
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj instanceof UInt16){
            return this.value == ((UInt16)obj).value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        /*int hash = 5;
        hash = 13 * hash + this.value;
        return hash;*/
        return this.value;
    }
    
    public UInt16 bitwiseAnd(UInt16 value){
        return valueOf(this.value & value.value);
    }
    
    public UInt16 shiftRight(int times){
        return valueOf(this.value >> times);
    }
    
    @Override
    public byte byteValue(){
        return Integer.valueOf(value).byteValue();
    }
            
    public boolean greaterThanOrEqual(short int16){
        return greaterThanOrEqual(valueOf(int16));
    }
    
    public boolean greaterThanOrEqual(int int32){
        return greaterThanOrEqual(valueOf(int32));
    }
    
    public boolean greaterThanOrEqual(UInt16 uint16){
        return this.value >= uint16.value;
    }
    
    public boolean lowerThan(short int16){
        return lowerThan(valueOf(int16));
    }
    
    public boolean lowerThan(int int32){
        return lowerThan(valueOf(int32));
    }
    
    public boolean lowerThan(UInt16 uint16){
        return this.value < uint16.value;
    }
    
    public byte[] getBytesLE(){
        return new byte[]{
            (byte) (value & 0x00FF),
            (byte)((value & 0xFF00) >> 8)
        };
    }
    
    @Override
    public short shortValue() {
        return Integer.valueOf(value).shortValue();
    }
    
    @Override
    public int intValue() {
        return value;
    }

    public UInt32 uint32Value(){
        return new UInt32(value);
    }
    
    @Override
    public long longValue() {
        return Integer.valueOf(value).longValue();
    }

    @Override
    public float floatValue() {
        return Integer.valueOf(value).floatValue();
    }

    @Override
    public double doubleValue() {
        return Integer.valueOf(value).doubleValue();
    }
        
    @Override
    public int compareTo(UInt16 t) {
        if(t.value == value){
            return 0;
        }else if(t.value > value){
            return 1;
        }else{
            return -1;
        }
    }
    
    public static UInt16 valueOf(UInt8 uint8){
        return new UInt16(uint8.value);
    }
    
    public static UInt16 valueOf(short int16){
        return new UInt16(int16);
    }
    
    public static UInt16 valueOf(int int32){
        return new UInt16(int32);
    }    

    @Override
    public String toString() {
        return String.format("%d", value);
    }
}
