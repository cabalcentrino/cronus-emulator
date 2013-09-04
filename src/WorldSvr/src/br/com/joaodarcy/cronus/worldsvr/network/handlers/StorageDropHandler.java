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
final class StorageDropHandler extends ServerOpcodeHandler {

    public StorageDropHandler() {
        super((short)0x0878, ClientState.IN_WORLD);
    }

/*
0000    E2 B7 1E 00 00 00 00 00 78 08 00 00 00 00 02 00 00 00 00         .ﾷ......x..........
0013    00 84 D4 EB FF FF FF 00 00 00 00                                   .ﾄￔ........
 */    
    
    @Override
    protected Boolean handleValue(Packet packet) {
        int storageSrc = packet.getInt();
        int slotSrc = packet.getInt();
        
        WorldSession session = getCurrentSession();                
        session.sendPacket(
            makeBooleanPacket(
                session.getPlayer().getStorageController().drop(storageSrc, slotSrc)
            )
        );
        
        return true;
    }
    
}
