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
import br.com.joaodarcy.cronus.worldsvr.ServerNode;
import br.com.joaodarcy.cronus.worldsvr.core.ClientState;
import br.com.joaodarcy.cronus.worldsvr.network.ServerOpcodeHandler;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class ServerHandshakeHandler extends ServerOpcodeHandler {

    public ServerHandshakeHandler() {
        super((short)0x008C, ClientState.CONNECTED);
    }
            
    @Override
    protected Boolean handleValue(Packet value) {
        UInt32 authKey = new UInt32(0x6FF48F95l); // EP 8 AuthKey
        
        // Update client state
        changeClientState(ClientState.HANDSHAKED);
        
        Packet response = makeConnect2SvrAuthKeyResponse(authKey);                        
                        
        // Change client key
        getCurrentSession().changeClientKey(
            ServerNode.getKeyFactory().create(authKey)
        );
        
        // Send response
        getCurrentSession().send(response);
        
        return true;
    }
        
    private Packet makeConnect2SvrAuthKeyResponse(UInt32 authKey){
        PacketBuilder packet = ServerNode.getPacketBuilderFactory().create((short)0x008C);
        
        packet.putUInt32LE(authKey);
        
        packet.putByte((byte)0xD9);
        packet.putByte((byte)0x4C);
        packet.putByte((byte)0xEA);
        packet.putByte((byte)0x2B);
        packet.putByte((byte)0x5F);
        packet.putByte((byte)0x52);
        packet.putByte((byte)0x28);
        packet.putByte((byte)0x0D);
        
        return packet.build();
    }
    
    /*public static void main(String args[]){
        Cryptation cryptation = ServerNode.getCryptation();
        
        
        PacketBuilder packet = ServerNode.getPacketBuilderFactory().create((short)0x008C);
        
        packet.putUInt32LE(new UInt32(0x34BC821Bl));
        packet.putInt64LE(0xF73B9118D4187036l);
        
        Packet finalP = packet.build();
        Key clientKey = ServerNode.getKeyFactory().create();
        
        System.out.println("DEC: " + finalP.toByteString());
        
        cryptation.encrypt(finalP.getData(), clientKey);
        
        System.out.println("ENC: " + finalP.toByteString());
        
    }*/
        
}
