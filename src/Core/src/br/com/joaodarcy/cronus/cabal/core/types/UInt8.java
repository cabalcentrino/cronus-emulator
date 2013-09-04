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
public class UInt8 extends Number implements Comparable<UInt8> {

    public final static UInt8 ZERO = valueOf((short)0);
    public final static UInt8 ONE = valueOf((short)1);
    
    /** 0 */
    public final static short MIN_VALUE = 0x00;
    /** 255 = ((2^8) -1) */
    public final static short MAX_VALUE = 0xFF;
    
    final short value;

    public UInt8(byte int8){
        this((short)int8);
    }
    
    public UInt8(short uint8) {
        this.value = (short)(uint8 & MAX_VALUE);
    }           
    
    short rawValue(){
        return value;
    }
    
    @Override
    public byte byteValue(){
        return (byte)value;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(obj instanceof UInt8){
            return ((UInt8)obj).value == value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        /*int hash = 3;
        hash = 73 * hash + this.value;
        return hash;*/
        return value;
    }
    
    @Override
    public short shortValue() {
        return value;
    }
    
    @Override
    public int intValue() {
        return Short.valueOf(value).intValue();
    }

    public UInt32 uint32Value(){
        return new UInt32((int)value);
    }
    
    @Override
    public long longValue() {
        return Short.valueOf(value).longValue();
    }

    @Override
    public float floatValue() {
        return Short.valueOf(value).floatValue();
    }

    @Override
    public double doubleValue() {
        return Short.valueOf(value).doubleValue();
    }

    @Override
    public int compareTo(UInt8 t) {
        if(t.value == value){
            return 0;
        }else if(t.value > value){
            return 1;
        }else{
            return -1;
        }
    }
    
    public static UInt8 valueOf(UInt16 uint16){
        return new UInt8(uint16.shortValue());
    }
    
    public static UInt8 valueOf(byte int8){
        return new UInt8(int8);
    }
    
    public static UInt8 valueOf(short int16){
        return new UInt8(int16);
    }

    @Override
    public String toString() {
        return String.format("%d", value);
    }    
}
