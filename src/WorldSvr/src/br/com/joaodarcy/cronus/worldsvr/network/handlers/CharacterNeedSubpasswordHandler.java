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

package br.com.joaodarcy.cronus.worldsvr.network.handlers;

import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import br.com.joaodarcy.cronus.cabal.core.network.PacketBuilder;
import br.com.joaodarcy.cronus.worldsvr.core.ClientState;
import br.com.joaodarcy.cronus.worldsvr.core.WorldSession;
import br.com.joaodarcy.cronus.worldsvr.network.ServerOpcodeHandler;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class CharacterNeedSubpasswordHandler extends ServerOpcodeHandler {

    private final boolean needSubpassword = false; // FIXME
    
    public CharacterNeedSubpasswordHandler() {
        super((short)0x0408, ClientState.CHARACTER_SELECTION);
    }
    
    @Override
    protected Boolean handleValue(Packet value) {
        WorldSession session = getCurrentSession();
        session.changeState(ClientState.SUBPASSWORD_CHECK);
        
        if(needSubpassword /* FIXME */){
            session.changeState(ClientState.WAITING_SUBPASSWORD);
            session.sendPacket(makeResponse(true));
        }else{
            session.changeState(ClientState.SUBPASSWORD_CHECKED);
            session.sendPacket(makeResponse(false));
        }
        
        return true;
    }

    public Packet makeResponse(boolean needSubpassword){
        PacketBuilder builder = getPacketBuilder();
        
        builder.putIntLE(needSubpassword ? 1 : 0);
        
        return builder.build();
    }
    
}
