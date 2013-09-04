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
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class CharacterBuddyGetGroupListHandler extends ChatOpcodeHandler {

    public CharacterBuddyGetGroupListHandler(){
        super((short)0x01CD, SessionState.LOGGED_IN);
    }
    
    @Override
    protected Boolean handleValue(Packet packet) {
        ChatSession session = getCurrentSession();
        UInt32 unk = packet.getUInt32();
        
        session.sendPacket(makeResponse(unk));
        
        return true;
    }
      
    private Packet makeResponse(UInt32 unk){
        PacketBuilder builder = getPacketBuilder();
        
        builder.put(unk);
        
        builder.putByte((byte)0x0F);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0xFF);
        builder.putByte((byte)0xFF);
        builder.putByte((byte)0xFF);
        builder.putByte((byte)0x7F);
        builder.putByte((byte)0x05); // Short size of "Buddy" ???
        builder.putByte((byte)0x00); // Short size of "Buddy" ???
        
        builder.putString("Buddy");        
        
        return builder.build();
    }
    
}
