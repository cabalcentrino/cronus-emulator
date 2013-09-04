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
public class ChatLogoutHandler extends ChatOpcodeHandler {

    public ChatLogoutHandler() {
        super((short)0x194, SessionState.LOGGED_IN);
    }
    
    @Override
    protected Boolean handleValue(Packet value) {
        final ChatSession session = getCurrentSession();
        
        session.changeSessionState(SessionState.LOGGED_OUT);
        session.sendPacket(makeResponse());
        
        session.disconnectClient();
        
        return true;
    }
    
    private Packet makeResponse(){
        final PacketBuilder builder = getPacketBuilder();        
        return builder.build();
    }

}
