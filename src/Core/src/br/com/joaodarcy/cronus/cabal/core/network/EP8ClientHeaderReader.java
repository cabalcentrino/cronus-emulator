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
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class EP8ClientHeaderReader implements HeaderReader{
    private final ByteBuffer buffer;

    public EP8ClientHeaderReader() {
        buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
    }
    
    @Override
    public Header read(InputStream inputStream, Cryptation crypt) {       
        try{
            // FIXME: To calculate packet size
            int available = inputStream.available();
            int readCount;
            
            // Read Header
            if((readCount = inputStream.read(buffer.array())) == 0x04){ 
                // Decrypt Header
                final byte decryptedHeader[] = crypt.decryptHeader(available);
                if(decryptedHeader == null){
                    throw new Throwable(
                        String.format("Cannot decrypt header: %02X%02X%02X%02X", 
                            buffer.array()[0],
                            buffer.array()[1],
                            buffer.array()[2],
                            buffer.array()[3]
                        )
                    );
                }
                
                // Wrap buffer
                final ByteBuffer decBuffer = ByteBuffer.wrap(decryptedHeader);
                decBuffer.order(ByteOrder.LITTLE_ENDIAN);
                    
                // Check signature
                short signature = decBuffer.getShort();
                if(signature == EP8PacketBuilder.PACKET_SIGNATURE){
                    // Read packet size
                   short size = decBuffer.getShort();               
                   return new Header(signature, size, Arrays.copyOf(buffer.array(), buffer.limit()));
                }else{
                    throw new Throwable(String.format("Wrong signature: %04X", signature));
                }                   
            }else{
                throw new Throwable(String.format("Only %d bytes are readed from stream when %d bytes was expected.", readCount, buffer.capacity()));
            }
        }catch(Throwable t){
            t.printStackTrace();
        }
        
        return null;
    }
    
}
