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

import br.com.joaodarcy.cronus.cabal.core.crypt.Cryptation;
import br.com.joaodarcy.cronus.cabal.core.crypt.Key;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class EP8ClientPacketReader implements PacketReader {
    private final HeaderReader headerReader;

    public EP8ClientPacketReader(HeaderReader headerReader) {
        this.headerReader = headerReader;
    }
    
    @Override
    public Packet read(InputStream inputStream, Cryptation crypt, Key clientKey) {
        Header header = headerReader.read(inputStream, crypt);
        if(header != null){
            try{
                ByteBuffer buffer = ByteBuffer.allocate(header.getSize());
                
                // Put header data into beggining of buffer
                buffer.put(header.getData());
                
                // Calculate packet body without reader
                int packetBodyLength = header.getSize() - header.getData().length;
                if(inputStream.read(buffer.array(), header.getData().length, packetBodyLength) == packetBodyLength){                                        
                    // Decrypt packet
                    crypt.decrypt(buffer.array(), clientKey);
                    
                    // Skip header and not used bytes
                    buffer.position(8); // 4 bytes header, 4 bytes unused bytes                    
                    
                    // Setup endian for read packet data
                    buffer.order(ByteOrder.LITTLE_ENDIAN);                    
                            
                    // Read packet data
                    short opcode = buffer.getShort();                                        
                    return new Packet(header, opcode, buffer);
                }else{
                    throw new Throwable("Cannot read packet body length: " + packetBodyLength);
                }                                
            }catch(Throwable t){
                t.printStackTrace();
            }            
        }
        return null;
    }
    
}
