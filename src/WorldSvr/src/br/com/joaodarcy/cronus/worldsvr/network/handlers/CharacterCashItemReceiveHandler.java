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
import br.com.joaodarcy.cronus.worldsvr.item.CashItem;
import br.com.joaodarcy.cronus.worldsvr.network.ServerOpcodeHandler;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class CharacterCashItemReceiveHandler extends ServerOpcodeHandler {

    public CharacterCashItemReceiveHandler() {
        super((short)0x01A3, ClientState.IN_WORLD);
    }
    
    //0000    E2 B7 10 00 00 00 00 00 A3 01 01 00 00 00 04 00
    @Override
    protected Boolean handleValue(Packet value) {
        UInt32 transactionId = value.getUInt32();
        UInt16 bagSlot = value.getUInt16();
        
        WorldSession session = getCurrentSession();                
        CashItem cashItem = session.getAccount().receiveCashItem(transactionId);
        if(cashItem == null){
            session.sendPacket(makeResponseCannotReceiveCashItem());
        }else{        
            if(session.getPlayer().getStorageController().pushCashItem(cashItem.getItem(), bagSlot.intValue())){
                session.sendPacket(makeResponseCashItemReceived(cashItem, bagSlot));
            }else{
                session.sendPacket(makeResponseCannotReceiveCashItem());
            }            
        }
        
        return true;
    }
            
    static Packet makeResponseCannotReceiveCashItem(){
        PacketBuilder builder = getPacketBuilder(0x01A3);
        builder.put(UInt32.ZERO);
        return builder.build();
    }
    
    static Packet makeResponseCashItemReceived(CashItem cashItem, UInt16 bagSlot){
        PacketBuilder builder = getPacketBuilder(0x01A3);
                                
        builder.put(cashItem.getTransactionId());
        builder.put(cashItem.getItem().getId());
        builder.put(cashItem.getItem().getOptions());

        builder.put(bagSlot);
        builder.put(cashItem.getItem().getDuration());

        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);        
        
        return builder.build();        
    }
}
