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
final class WorldWarpHandler extends ServerOpcodeHandler {

    public WorldWarpHandler() {
        super((short)0x00F4, ClientState.IN_WORLD);
    }
    
    @Override
    protected Boolean handleValue(Packet value) {
        WorldSession session = getCurrentSession();        
        session.sendPacket(makeResponse());        
        return true;
    }
    
    protected Packet makeResponse(){
        PacketBuilder builder = getPacketBuilder();
        
        builder.putByte((byte)0x90);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0xF1);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0xF6);
        builder.putByte((byte)0x56);
        builder.putByte((byte)0x15);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0xFF);
        builder.putByte((byte)0x03);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        
        return builder.build();
    }

}
