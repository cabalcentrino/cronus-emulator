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

import br.com.joaodarcy.cronus.chatsvr.ChatNode;
import br.com.joaodarcy.cronus.chatsvr.network.ChatOpcodeHandler;
import br.com.joaodarcy.cronus.chatsvr.core.ChatSession;
import br.com.joaodarcy.cronus.chatsvr.core.SessionState;
import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import br.com.joaodarcy.cronus.cabal.core.network.PacketBuilder;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class ChatConnectHandler extends ChatOpcodeHandler {

    public ChatConnectHandler() {
        super((short)0x0191, SessionState.CONNECTED);
    }
    
    @Override
    protected Boolean handleValue(Packet value) {        
        ChatSession session = getCurrentSession();
        UInt32 cryptKey = UInt32.valueOf(0x0409F6ADl);               
        
        session.changeSessionState(SessionState.HANDSHAKED);        
        session.sendPacket(makeResponse(cryptKey));
        
        // Change client key
        session.changeClientKey(ChatNode.getKeyFactory().create(cryptKey));
        
        return true;
    }
    
    private Packet makeResponse(UInt32 cryptKey){
        PacketBuilder builder = getPacketBuilder();
        
        builder.put(cryptKey);
        
        builder.putByte((byte)0x9F);
        builder.putByte((byte)0x54);
        builder.putByte((byte)0xF0);
        builder.putByte((byte)0x2B);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x00);        
        builder.putByte((byte)0x31);
        builder.putByte((byte)0x05);
        
        return builder.build();
    }

}
