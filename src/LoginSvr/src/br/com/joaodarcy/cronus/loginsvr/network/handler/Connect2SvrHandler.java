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
import br.com.joaodarcy.cronus.loginsvr.core.AuthClientSession;
import br.com.joaodarcy.cronus.loginsvr.core.AuthState;
import br.com.joaodarcy.cronus.loginsvr.network.AuthOpcodeHandler;
import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import br.com.joaodarcy.cronus.cabal.core.network.PacketBuilder;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;

/**
 * Handshake ?????
 * 
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class Connect2SvrHandler extends AuthOpcodeHandler {
    
    public Connect2SvrHandler() {
        super((short)0x0065, AuthState.CONNECTED);
    }
            
    @Override
    protected Boolean handleValue(Packet value) {
        UInt32 authKey = new UInt32(0x135EFE81l);        
        AuthClientSession session = getCurrentSession();
        
        // Update client state
        session.setState(AuthState.HANDSHAKED);
                        
        // Send response
        session.sendPacket(makeResponse(authKey));
        
        // Change client key
        session.changeClientKey(AuthNode.getKeyFactory().create(authKey));
        
        return true;
    }
            
    private Packet makeResponse(UInt32 authKey){        
        PacketBuilder packet = AuthNode.getPacketBuilderFactory().create(opcodeHandled);
        
        packet.putUInt32LE(authKey);
        
        packet.putByte((byte)0xE5);
        packet.putByte((byte)0x27);
        packet.putByte((byte)0xEB);
        packet.putByte((byte)0x2B);
        packet.putByte((byte)0x67);
        packet.putByte((byte)0x4D);
        packet.putByte((byte)0x42);
        packet.putByte((byte)0x39);
               
        return packet.build();
    }
    
}
