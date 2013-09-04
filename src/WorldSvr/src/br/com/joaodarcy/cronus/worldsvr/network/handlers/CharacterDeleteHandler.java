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
import br.com.joaodarcy.cronus.worldsvr.network.ServerOpcodeHandler;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class CharacterDeleteHandler extends ServerOpcodeHandler{

    public CharacterDeleteHandler() {
        super((short)0x0087, ClientState.CHARACTER_SELECTION);
    }
    
    @Override
    protected Boolean handleValue(Packet value) {
        getCurrentSession().sendPacket(makeDelMyCharacterPacketSuccessfully());        
        return true;
    }

    private Packet makeDelMyCharacterPacket(byte response){
        PacketBuilder builder = getPacketBuilder(0x0087);
        
        builder.putByte(response);
        
        return builder.build();
    }
    
    //private byte discoveryResponse = (byte)0x9F;
    
    /*private Packet makeDelMyCharacterPacketDiscovery(){
        return makeDelMyCharacterPacket(discoveryResponse++);
    }*/
    
    private Packet makeDelMyCharacterPacketSuccessfully(){
        return makeDelMyCharacterPacket((byte)0xA1);
    }
    
}
