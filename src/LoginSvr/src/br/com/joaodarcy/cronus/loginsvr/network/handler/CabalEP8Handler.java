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

package br.com.joaodarcy.cronus.loginsvr.network.handler;

import br.com.joaodarcy.cronus.loginsvr.network.NullHandler;
import br.com.joaodarcy.cronus.cabal.core.PacketHandler;
import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import br.com.joaodarcy.cronus.cabal.core.patterns.Chain;
import br.com.joaodarcy.cronus.cabal.core.patterns.util.ChainBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class CabalEP8Handler implements PacketHandler {
    private final Chain<Boolean, Packet> handlers;
    private final Logger logger = LoggerFactory.getLogger(CabalEP8Handler.class);
    
    public CabalEP8Handler() {
        this.handlers = buildHandlersChain();
    }
    
    private Chain<Boolean, Packet> buildHandlersChain(){
        ChainBuilder builder = ChainBuilder.create();
        
        builder
            .setLastElement(NullHandler.getInstance())
            .add(new Connect2SvrHandler())                          // 0x0065
            .add(new VerifyLinksHandler())                          // 0x0066
            .add(new AuthAccountHandler())                          // 0x0067
            .add(new CheckVersionHandler())                         // 0x007A
            .add(new AccountUnk07D1Handler())                       // 0x07D1
            .add(new AccountUnk07D2Handler());                      // 0x07D2                       
        
        return builder.build();
    }
        
    @Override
    public void handle(Packet packet) {
        logger.debug("Handling packet, opcode: {}, data: {}", String.format("%04X", packet.getOpcode()), packet.toByteString());        
        handlers.handle(packet);
    }            
}
