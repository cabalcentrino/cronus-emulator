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
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.cronus.worldsvr.core.ClientState;
import br.com.joaodarcy.cronus.worldsvr.core.WorldSession;
import br.com.joaodarcy.cronus.worldsvr.network.ServerOpcodeHandler;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class WorldServerGetTime extends ServerOpcodeHandler {

    public WorldServerGetTime() {
        super((short)0x0094, ClientState.CHARACTER_SELECTION);
    }    
    
    @Override
    protected Boolean handleValue(Packet value) {
        WorldSession session = getCurrentSession();
        
        session.sendPacket(makeResponse());        
        session.sendPacket(makeWorldEnvPacket());
        
        return true;
    }
    
    // FIXME
    private Packet makeResponse(){
        PacketBuilder builder = getPacketBuilder();
     
        // A7 F7 40 50 00 00 00 00 B4 00         
        
        // To time_t = t / 1000L
        builder.put(UInt32.valueOf((System.currentTimeMillis() / 1000L)));
                
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0xB4);
        builder.putByte((byte)0x00);
        
        return builder.build();
    }        

    // FIXME: Local incorreto =D
    private Packet makeWorldEnvPacket(){ // 0x01D0
        PacketBuilder builder = getPacketBuilder(0x01D0);
        
        builder.putByte((byte)0xBE);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x0A);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x0A);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x02);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x5C);
        builder.putByte((byte)0xB2);
        builder.putByte((byte)0xEC);
        builder.putByte((byte)0x22);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x5C);
        builder.putByte((byte)0xB2);
        builder.putByte((byte)0xEC);
        builder.putByte((byte)0x22);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0xE8);
        builder.putByte((byte)0x76);
        builder.putByte((byte)0x48);
        builder.putByte((byte)0x17);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x10);
        builder.putByte((byte)0x0A);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x0A);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x64);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x07);
        builder.putByte((byte)0x00);
        
        return builder.build();
    }
    
}
