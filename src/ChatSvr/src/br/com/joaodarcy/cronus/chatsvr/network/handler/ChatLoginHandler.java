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
import br.com.joaodarcy.cronus.chatsvr.core.ChatSession;
import br.com.joaodarcy.cronus.chatsvr.core.SessionState;
import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import br.com.joaodarcy.cronus.cabal.core.network.PacketBuilder;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class ChatLoginHandler extends ChatOpcodeHandler {

    public ChatLoginHandler() {
        super((short)0x193, SessionState.ACCOUNT_VERIFIED);
    }
    
    @Override
    protected Boolean handleValue(Packet value) {        
        ChatSession session = getCurrentSession();
                                
        session.changeSessionState(SessionState.LOGGED_IN);
        session.sendPacket(makeResponse());
        session.sendPacket(makeUnk01C5());
        
        return true;
    }
    
    private Packet makeResponse(){
        PacketBuilder builder = getPacketBuilder();
        
        builder.putByte((byte)0x00);
        
        return builder.build();
    }
    
    private Packet makeUnk01C5(){
        PacketBuilder builder = getPacketBuilder(0x01C5);
        
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        
        return builder.build();
    }

}
