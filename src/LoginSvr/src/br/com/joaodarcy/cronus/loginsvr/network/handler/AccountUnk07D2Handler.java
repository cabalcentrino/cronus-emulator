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
import br.com.joaodarcy.cronus.cabal.core.ClientSession;
import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import br.com.joaodarcy.cronus.cabal.core.network.PacketBuilder;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class AccountUnk07D2Handler extends AuthOpcodeHandler {

    public AccountUnk07D2Handler() {
        super((short)0x07D2, AuthState.RIGHT_CLIENT_VERSION);
    }
    
    @Override
    protected Boolean handleValue(Packet value) {
        PacketBuilder builder = AuthNode.getPacketBuilderFactory().create(opcodeHandled);
        
        // Filled With Zeros
        builder.putByteArray(new byte[4109]);
        
        builder.putByte((byte)0x00);
        builder.putByte((byte)0xE1);
        builder.putByte((byte)0x37);
        builder.putByte((byte)0x00);
        
        // Send response
        ClientSession.send(builder.build());        
        
        return true;
    }

}
