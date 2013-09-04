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

import br.com.joaodarcy.cronus.cabal.core.types.ByteSwapper;
import br.com.joaodarcy.cronus.cabal.core.types.UInt16;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.cronus.cabal.core.types.UInt8;
import java.io.ByteArrayOutputStream;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public abstract class AbstractPacketBuilder implements PacketBuilder {
    protected final ByteArrayOutputStream byteArrayOutputStream;

    public AbstractPacketBuilder() {
        this.byteArrayOutputStream = new ByteArrayOutputStream();
    }
    
    public AbstractPacketBuilder(int size){
        this.byteArrayOutputStream = new ByteArrayOutputStream(size);
    }
    
    @Override
    public PacketBuilder putByte(byte value) {
        byteArrayOutputStream.write(value);
        return this;
    }

    @Override
    public PacketBuilder putByteArray(byte[] values) {
        try{
            byteArrayOutputStream.write(values);
        }catch(Throwable t){
            t.printStackTrace();
        }
        return this;
    }
        
    @Override
    public PacketBuilder putShortLE(short value) {
        value = ByteSwapper.swapBytes(value);
        
        return putByteArray(new byte[]{ 
            (byte)((value & 0xFF00) >> 8), 
            (byte)(value &0xFF)}
        );
    }
                
    @Override
    public PacketBuilder putIntLE(int value) {
        value = ByteSwapper.swapBytes(value);
        
        return putByteArray(new byte[]{
            (byte)((value & 0xFF000000) >> 24),
            (byte)((value & 0x00FF0000) >> 16),
            (byte)((value & 0x0000FF00) >> 8 ),
            (byte)((value & 0x000000FF))
        });                
    }
        
    @Override
    public PacketBuilder putUInt32LE(UInt32 value) {
        return putByteArray(value.getBytesLE());
    }

    @Override
    public PacketBuilder putString(String value) {
        return putByteArray(value.getBytes());
    }

    @Override
    public PacketBuilder putInt64(long value) {                
        return putByteArray(new byte[]{            
            (byte)((value & 0x00000000000000FFl)),
            (byte)((value & 0x000000000000FF00l) >> 8 ),
            (byte)((value & 0x0000000000FF0000l) >> 16),
            (byte)((value & 0x00000000FF000000l) >> 24),
            (byte)((value & 0x000000FF00000000l) >> 32),
            (byte)((value & 0x0000FF0000000000l) >> 40),
            (byte)((value & 0x00FF000000000000l) >> 48),
            (byte)((value & 0xFF00000000000000l) >> 56)            
        });
    }
    
    @Override
    public PacketBuilder putInt64LE(long value) {
        return putByteArray(new byte[]{            
            (byte)((value & 0xFF00000000000000l) >> 56),
            (byte)((value & 0x00FF000000000000l) >> 48),
            (byte)((value & 0x0000FF0000000000l) >> 40),
            (byte)((value & 0x000000FF00000000l) >> 32),
            (byte)((value & 0x00000000FF000000l) >> 24),
            (byte)((value & 0x0000000000FF0000l) >> 16),
            (byte)((value & 0x000000000000FF00l) >> 8 ),
            (byte)((value & 0x00000000000000FFl))
        });
    }

    @Override
    public PacketBuilder put(UInt8 value) {
        return putByte(value.byteValue());
    }
    
    @Override
    public PacketBuilder put(UInt16 value) {
        return putByteArray(value.getBytesLE());
    }

    @Override
    public PacketBuilder put(UInt32 value) {
        return putByteArray(value.getBytesLE());
    }    
}
