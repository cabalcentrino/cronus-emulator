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
import br.com.joaodarcy.cronus.worldsvr.core.ClientState;
import br.com.joaodarcy.cronus.worldsvr.network.ServerOpcodeHandler;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class PlayerMoveBeginnedHandler extends ServerOpcodeHandler {

    public PlayerMoveBeginnedHandler(){
        super((short)0x00BE, ClientState.IN_WORLD);
    }
    
    @Override
    protected Boolean handleValue(Packet packet) {
        short sourceX, sourceY;
        short nextX0, nextY0;
        short nextX1, nextY1;
        short unk;
        
        sourceX = packet.getShort();
        sourceY = packet.getShort();
        
        nextX0 = packet.getShort();
        nextY0 = packet.getShort();
        
        nextX1 = packet.getShort();
        nextY1 = packet.getShort();
        
        unk = packet.getShort();
        
        return true;
    }
    
    
}
