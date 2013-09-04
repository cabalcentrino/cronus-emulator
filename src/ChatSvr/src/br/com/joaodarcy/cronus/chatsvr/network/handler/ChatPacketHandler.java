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

package br.com.joaodarcy.cronus.chatsvr.network.handler;

import br.com.joaodarcy.cronus.chatsvr.network.ChatOpcodeHandler;
import br.com.joaodarcy.cronus.cabal.core.PacketHandler;
import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import br.com.joaodarcy.cronus.cabal.core.patterns.Chain;
import br.com.joaodarcy.cronus.cabal.core.patterns.util.ChainBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class ChatPacketHandler implements PacketHandler {

    private final Chain<Boolean, Packet> handlingChain;
    private final Logger logger;

    public ChatPacketHandler() {
        this.handlingChain = buildHandlingChain();
        this.logger = LoggerFactory.getLogger(ChatPacketHandler.class);
    }
    
    private Chain buildHandlingChain(){
        ChainBuilder<ChatOpcodeHandler> builder = ChainBuilder.create();
        
        builder
            .add(new AccountAuthAccountHandler())
            .add(new CharacterBuddyGetBlackListHandler())
            .add(new CharacterBuddyGetGroupListHandler())
            .add(new CharacterBuddyGetMemberListHandler())
            .add(new ChatConnectHandler())
            .add(new ChatLoginHandler())
            .add(new ChatLogoutHandler());
        
        return builder.build();
    }    
    
    @Override
    public void handle(Packet packet) {
        logger.debug("New packet received: {}", packet.toByteString());
        handlingChain.handle(packet);
    }

}
