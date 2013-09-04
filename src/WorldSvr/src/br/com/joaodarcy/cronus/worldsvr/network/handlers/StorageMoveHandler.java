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
import br.com.joaodarcy.cronus.worldsvr.core.WorldSession;
import br.com.joaodarcy.cronus.worldsvr.network.ServerOpcodeHandler;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class StorageMoveHandler extends ServerOpcodeHandler {

    public StorageMoveHandler() {
        super((short)0x0875, ClientState.IN_WORLD);
    }
/*
0000    E2 B7 26 00 00 00 00 00 75 08 00 00 00 00 1E 00 00 00 01         .ﾷ......u..........
0013    00 00 00 06 00 00 00 00 06 DD 03 EB FF FF FF 00 00 00 00           ...................
 */   
    
/* Equip
0000    E2 B7 26 00 00 00 00 00 75 08 00 00 00 00 06 00 00 00 01         .ﾷ......u..........
0013    00 00 00 00 00 00 00 00 00 08 00 EB FF FF FF 00 00 00 00           ...................
 */    
    
/* Unequip
0000    E2 B7 26 00 00 00 00 00 75 08 01 00 00 00 00 00 00 00 00         .ﾷ......u..........
0013    00 00 00 06 00 00 00 00 09 03 00 EB FF FF FF 00 00 00 00           ...................
 */  
    
/*
0000    E2 B7 26 00 00 00 00 00 75 08 01 00 00 00 00 00 00 00 00         .ﾷ......u..........
0013    00 00 00 16 00 00 00 00 09 03 00 EB FF FF FF 00 00 00 00           ...................
 */    
           
    @Override
    protected Boolean handleValue(Packet packet) {
        int stoSrc = packet.getInt();
        int slotSrc = packet.getInt();
        int stoDst = packet.getInt();
        int slotDst = packet.getInt();
        
        WorldSession session = getCurrentSession();
                
        session.sendPacket(
            makeBooleanPacket(
                session.getPlayer().getStorageController().swapItem(stoSrc, slotSrc, stoDst, slotDst)
            )
        );
        
        return true;
    }

}
