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
final class CharacterStyleChangeHandler extends ServerOpcodeHandler {

    public CharacterStyleChangeHandler() {
        super((short)0x0142, ClientState.IN_WORLD);
    }
    
    // FIXME
    @Override
    protected Boolean handleValue(Packet value) {
        WorldSession session = getCurrentSession();
        
        session.sendPacket(makeResponse());
        
        return true;
    }

    private Packet makeResponse(){
        PacketBuilder builder = getPacketBuilder();        
        return builder.build();
    }
    
/*
0000 E2 B7 26 00 00 00 00 00 42 01 09 80 02 04 00 00         .ﾷ......B..ﾀ.... 
0010 00 00 00 00 00 00 00 00 0F 5A 22 87 47 C1 2F F6         .........Z.ﾇG... 
0020 4D 6E 7F 38 00 00                                         Mn.8..
 */    
    
}
