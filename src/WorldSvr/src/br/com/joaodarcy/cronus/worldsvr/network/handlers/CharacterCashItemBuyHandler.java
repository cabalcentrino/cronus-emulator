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
import br.com.joaodarcy.cronus.cabal.core.types.UInt16;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.cronus.worldsvr.core.ClientState;
import br.com.joaodarcy.cronus.worldsvr.core.WorldSession;
import br.com.joaodarcy.cronus.worldsvr.network.ServerOpcodeHandler;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class CharacterCashItemBuyHandler extends ServerOpcodeHandler {

    public CharacterCashItemBuyHandler() {
        super((short)0x00A1, ClientState.IN_WORLD);
    }    
    
/* Comprando + de 5
0000    E2 B7 39 00 00 00 00 00 A1 00 33 02 00 40 00 09 10 00 00         .ﾷ9.....ﾡ.3........
0013    00 00 00 00 40 00 00 00 00 F8 00 00 00 00 05 00 00 00 04         ...................
0026    00 00 00 05 00 00 00 06 00 00 00 07 00 00 00 08 00 00 00           ...................
 */        
/* Comparando 1
0000    E2 B7 29 00 00 00 00 00 A1 00 33 02 00 40 00 09 10 00 00         .ﾷ......ﾡ.3........
0013    00 00 00 00 40 00 00 00 00 F8 00 00 00 00 01 00 00 00 04         ...................
0026    00 00 00                   
 */    
/*
0000    E2 B7 29 00 00 00 00 00 A1 00 05 04 00 01 00 20 1C 00 00         .ﾷ......ﾡ..........
0013    E7 03 00 00 01 00 00 00 00 F8 00 00 00 00 01 00 00 00 04         ...................
0026    00 00 00                                                           ...
 */
/*
0000    E2 B7 29 00 00 00 00 00 A1 00 05 04 00 00 00 1F 1C 00 00         .ﾷ......ﾡ..........
0013    E7 03 00 00 00 00 00 00 00 F8 00 00 00 00 01 00 00 00 04         ...................
0026    00 00 00
 */
/*
0000    E2 B7 29 00 00 00 00 00 A1 00 05 04 00 22 00 E6 15 00 00         .ﾷ......ﾡ..........
0013    00 00 00 00 22 00 00 00 00 10 00 00 00 00 01 00 00 00 04         ...................
0026    00 00 00                                                           ...
 */    
    
    @Override
    protected Boolean handleValue(Packet value) {
        WorldSession session = getCurrentSession(); 
        
        UInt32 unk3 = value.getUInt32();
        UInt32 itemId = value.getUInt32();
        UInt32 itemOpt1 = value.getUInt32();        
        UInt16 shopSlot = value.getUInt16();
        UInt32 unk0 = value.getUInt32();
        UInt32 unk1 = value.getUInt32();
        UInt32 buyCount = value.getUInt32();
        UInt32 unk2 = value.getUInt32();                
        
        logger.info("Jogador {} esta comprando um item Premium {}", session.getPlayer().getCharacterIdx(), itemId.bitwiseAnd(UInt32.valueOf(0xFFF)));        
        session.sendPacket(makeResponse(session));
        return true;
    }
    
    protected Packet makeResponse(WorldSession session){
        PacketBuilder builder = getPacketBuilder();        
        builder.put(session.getAccount().getCash());        
        return builder.build();
    }
}
