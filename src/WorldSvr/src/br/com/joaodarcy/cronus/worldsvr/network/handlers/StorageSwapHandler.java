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
import br.com.joaodarcy.cronus.cabal.core.types.UInt16;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.cronus.cabal.core.types.UInt8;
import br.com.joaodarcy.cronus.worldsvr.core.ClientState;
import br.com.joaodarcy.cronus.worldsvr.core.WorldSession;
import br.com.joaodarcy.cronus.worldsvr.network.ServerOpcodeHandler;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class StorageSwapHandler extends ServerOpcodeHandler {

    public StorageSwapHandler() {
        super((short)0x0876, ClientState.IN_WORLD);
    }

    /*
0000    E2 B7 36 00 00 00 00 00 76 08 00 00 00 00 00 00 00 00 01         .ﾷ6.....v..........
0013    00 00 00 01 00 00 00 01 00 00 00 01 00 00 00 00 00 00 00         ...................
0026    00 00 00 00 00 00 04 00 EB FF FF FF 00 00 00 00                    ................
     */

/*    
0000    E2 B7 36 00 00 00 00 00 76 08 00 00 00 00 16 00 00 00 01         .ﾷ6.....v..........
0013    00 00 00 01 00 00 00 01 00 00 00 01 00 00 00 00 00 00 00         ...................
0026    16 00 00 00 00 09 00 00 EB FF FF FF 00 00 00 00                    ................
*/  
    
/*
0000    E2 B7 36 00 00 00 00 00 76 08 00 00 00 00 20 00 00 00 01         .ﾷ6.....v..........
0013    00 00 00 02 00 00 00 01 00 00 00 02 00 00 00 00 00 00 00         ...................
0026    20 00 00 00 00 09 03 00 EB FF FF FF 00 00 00 00                    ................    
* */
    
/*
0000    E2 B7 36 00 00 00 00 00 76 08 00 00 00 00 20 00 00 00 01         .ﾷ6.....v..........
0013    00 00 00 02 00 00 00 01 00 00 00 02 00 00 00 00 00 00 00         ...................
0026    20 00 00 00 00 09 03 00 EB FF FF FF 00 00 00 00                    ................
 */    
    
    @Override
    protected Boolean handleValue(Packet packet) {
        int storageSrc0 = packet.getInt();
        int slotSrc0 = packet.getInt();
        int storageDst0 = packet.getInt();
        int slotDst0 = packet.getInt();
        /*UInt32 unk2 = packet.getUInt32();
        UInt32 equipmentSlotDst2 = packet.getUInt32();
        UInt32 unk3 = packet.getUInt32();
        UInt32 bagSlotSrc2 = packet.getUInt32();*/
        
        WorldSession session = getCurrentSession();
                        
        session.sendPacket(
            makeBooleanPacket(
                session.getPlayer().getStorageController().swapItem(
                    storageSrc0, slotSrc0, storageDst0, slotDst0                
                )
            )
        );
        return true;
    }

}
