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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class EP8PacketBuilder extends AbstractPacketBuilder {
    
    final static short PACKET_SIGNATURE = (short)0xB7E2;
    
    private final short opcode;
    
    public EP8PacketBuilder(short opcode) {
        this.opcode = opcode;
        
        putShortLE(PACKET_SIGNATURE);   // Header - Signature
        putShortLE((short)0x0000);      // Header - Packet Size
        putShortLE(opcode);             // Opcode
    }
    
    @Override
    public Packet build() {
        byte raw[] = byteArrayOutputStream.toByteArray();
        
        ByteBuffer buff = ByteBuffer.wrap(raw);
        buff.order(ByteOrder.LITTLE_ENDIAN);
        buff.putShort(2, (short)raw.length);
        
        return new Packet(
            new Header(PACKET_SIGNATURE, (short)raw.length, raw), 
            opcode, 
            buff
        );
    }
    
}
