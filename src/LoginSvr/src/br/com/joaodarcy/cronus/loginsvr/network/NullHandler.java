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

package br.com.joaodarcy.cronus.loginsvr.network;

import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import br.com.joaodarcy.cronus.cabal.core.patterns.Chain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class NullHandler implements Chain<Boolean, Packet> {
    
    private final static NullHandler INSTANCE = new NullHandler();        
    
    private final Logger logger;
    
    private NullHandler() {
        this.logger = LoggerFactory.getLogger(NullHandler.class);
    }
    
    public static NullHandler getInstance(){
        return INSTANCE;
    }
    
    @Override
    public Boolean handle(Packet value) {
        logger.error("Cannot handle packet {} yeat, data: {}", String.format("%04X", value.getOpcode()), value.toByteString());
        return true;
    }

    @Override
    public void setNext(Chain<Boolean, Packet> chainElement) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public int compareTo(Chain<Boolean, Packet> t) {
        throw new UnsupportedOperationException("Not supported.");
    }        
}
