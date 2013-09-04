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

package br.com.joaodarcy.cronus.loginsvr.network.handler;

import br.com.joaodarcy.cronus.loginsvr.AuthNode;
import br.com.joaodarcy.cronus.loginsvr.core.AuthState;
import br.com.joaodarcy.cronus.loginsvr.network.AuthOpcodeHandler;
import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import br.com.joaodarcy.cronus.cabal.core.network.PacketBuilder;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class VerifyLinksHandler extends AuthOpcodeHandler {
    
    public VerifyLinksHandler() {
        super((short)0x0066, AuthState.SERVER_SELECTED);
    }
    
    @Override
    protected Boolean handleValue(Packet packet) {
        packet.getInt();
        packet.getShort();
        
        byte channelId = packet.getByte();
        byte serverId = packet.getByte();
        
        getCurrentSession().sendPacket(makeCorrectLink(channelId, serverId));
        
        return true;
    }
    
    private Packet makeCorrectLink(byte channelId, byte serverId){
        PacketBuilder builder = AuthNode.getPacketBuilderFactory().create((short)0x0066);
        
        builder.putByte(channelId);
        builder.putByte(serverId);
        builder.putByte((byte)0x01);
        
        return builder.build();
    }    

}
