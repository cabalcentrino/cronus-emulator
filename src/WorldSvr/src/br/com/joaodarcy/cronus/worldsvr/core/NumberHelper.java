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

package br.com.joaodarcy.cronus.worldsvr.core;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class NumberHelper {

    private final static short UNSIGNED_BYTE_MASK = 0xFF;
    private final static int   UNSIGNED_INT16_MASK = 0xFFFF;
    
    private NumberHelper() {
        throw new AssertionError();
    }

    public static short Int16FromUInt16(int value){
        return (short)(value & UNSIGNED_INT16_MASK);
    }
    
    public static short byteToUByte(byte value){
        return (short)(((short)value) & UNSIGNED_BYTE_MASK);
    }
    
    public static byte ubyteToByte(short value){
        return (byte)(value & UNSIGNED_INT16_MASK);
    }
    
    public static int int16ToUInt16(short value){
        return (((int)value) & UNSIGNED_INT16_MASK);
    }
    
}
