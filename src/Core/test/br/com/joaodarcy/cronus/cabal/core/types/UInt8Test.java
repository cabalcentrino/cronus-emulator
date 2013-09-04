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

import org.junit.Test;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class UInt8Test {

    private final byte int8[] = {
        -3, -2, -1, 0,  1,  2,  3 
    };
    
    private final short uint8[] = {
        253, 254, 255, 0, 1, 2, 3 
    };
    
    private final short int16[] = {
        253, 254, 255, 0, 1,   2,  3
    };
    
    private final int int32[] = {
        253, 254, 255, 0, 1,   2,  3
    };
    
    private final long uint32[] = {
        253l, 254l, 255l, 0l, 1l,   2l,  3l
    };
    
    private final long int64[] = {
        253l, 254l, 255l, 0l, 1l,   2l,  3l
    };            
    
    @Test
    public void int8ToUInt8Test(){
        byte int8Value;
        short uint8Value;
        UInt8 uint;
        
        for(int i = 0 ; i < int8.length ; i++){
            int8Value = int8[i];
            uint8Value = uint8[i];
                        
            uint = new UInt8(int8Value);
            System.out.printf("Int8: %02X (%3d) UInt8: %02X (%3d) Current: %02X (%3d)\n", int8Value, int8Value, uint8Value, uint8Value,  uint.rawValue(), uint.rawValue());
            assert(uint.rawValue() == uint8Value);
        }        
    }
    
    @Test
    public void uInt8ConversionTest(){
        byte int8Value;
        UInt8 uint;
        
        for(int i = 0 ; i < int8.length ; i++){
            int8Value = int8[i];                        
            uint = new UInt8(int8Value);
            
            System.out.printf("UInt8: %02X (%3d) Other: %02X (%3d) - Conv: %02X (%03d)\n", uint.rawValue(), uint.rawValue(), int8[i], int8[i], uint.byteValue(), uint.byteValue());
            
            assert(int8[i] == uint.byteValue());            
            assert(uint8[i] == uint.shortValue());            
            assert(int32[i] == uint.intValue());
            assert(uint32[i] == uint.longValue());
            assert(int64[i] == uint.longValue());                                                
        }
        
        UInt8 first = UInt8.valueOf((short)120);
        UInt8 sec = UInt8.valueOf((short)120);
        
        assert(first.equals(sec));
    }
    
}
