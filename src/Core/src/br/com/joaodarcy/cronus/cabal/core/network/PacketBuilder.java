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

package br.com.joaodarcy.cronus.cabal.core.network;

import br.com.joaodarcy.cronus.cabal.core.types.UInt16;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.cronus.cabal.core.types.UInt8;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public interface PacketBuilder {
 
    Packet build();
    
    PacketBuilder putByte(byte value);
    PacketBuilder putByteArray(byte values[]);
            
    /**
     * LITTLE ENDIAN
     * 
     * @param value
     * @return 
     */
    PacketBuilder put(UInt8 value);
    
    /**
     * LITTLE ENDIAN
     * 
     * @param value
     * @return 
     */
    PacketBuilder put(UInt16 value);
    
    /**
     * LITTLE ENDIAN
     * 
     * @param value
     * @return 
     */
    PacketBuilder put(UInt32 value);
    
    /**
     * LITTLE ENDIAN
     * 
     * @param value
     * @return 
     */
    PacketBuilder putShortLE(short value);
            
    /**
     * LITTLE ENDIAN
     * 
     * @param value
     * @return 
     */
    PacketBuilder putIntLE(int value);
            
    /**
     * LITTLE ENDIAN
     * 
     * @param value
     * @return 
     */
    PacketBuilder putUInt32LE(UInt32 value);

    /**
     * LITTLE ENDIAN
     * 
     * @param value
     * @return 
     */
    PacketBuilder putInt64(long value);
    
    /**
     * ATENÇÃO: BIG ENDIAN
     * 
     * @param value
     * @return 
     */
    @Deprecated
    PacketBuilder putInt64LE(long value);
    
    PacketBuilder putString(String value);
    
}
