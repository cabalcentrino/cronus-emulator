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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Read entire packet, size of packet is used in decryption.
 * 
 * FIXME: Need a way to decrypt first 4 bytes to know the real packet size and read only needed bytes.
 * 
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class EP8ClientFullPacketReader {
    private final static Logger logger;
    
    static{
        logger = LoggerFactory.getLogger(EP8ClientFullPacketReader.class);
    }
    
    private EP8ClientFullPacketReader() {
        
    }
    
    public static InputStream read(InputStream source){        
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);                        
        
        int readCount = 0;
        
        logger.debug("Waiting client data arrive...");
        
        try{
            readCount = source.read(readBuffer.array());
            logger.debug("Readed {} bytes from client", readCount);                        
        }catch(Throwable t){
            logger.error("Error reading data from client {}", t.getMessage(), t);
        }
                
        if(readCount > 0){
            return new ByteArrayInputStream(Arrays.copyOfRange(readBuffer.array(), 0, readCount));
        }else{        
            return new ByteArrayInputStream(new byte[0]);
        }
    }
    
}
