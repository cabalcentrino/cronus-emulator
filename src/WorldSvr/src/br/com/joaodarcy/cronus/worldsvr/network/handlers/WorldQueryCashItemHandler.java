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
import br.com.joaodarcy.cronus.worldsvr.core.ClientState;
import br.com.joaodarcy.cronus.worldsvr.core.WorldSession;
import br.com.joaodarcy.cronus.worldsvr.item.CashItem;
import br.com.joaodarcy.cronus.worldsvr.network.ServerOpcodeHandler;
import java.util.List;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class WorldQueryCashItemHandler extends ServerOpcodeHandler {

    public WorldQueryCashItemHandler() {
        super((short)0x01A2, ClientState.IN_WORLD);
    }
    
    // Client > E2 B7 0A 00 00 00 00 00 A2 01
    
    // Server > E2 B7 0A 00 A2 01 00 00 00 00
    //
    // int unk
    
    // TODO
    @Override
    protected Boolean handleValue(Packet value) {                        
        WorldSession session = getCurrentSession();
        session.sendPacket(makeResponse(session));        
        return true;
    }
    
    static Packet temp(){
        PacketBuilder builder = getPacketBuilder(0x01A2);
        
        builder.putByte((byte)0x08);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x7C);
        builder.putByte((byte)0xCE);
        builder.putByte((byte)0x08);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x25);
        builder.putByte((byte)0xA0);
        builder.putByte((byte)0x08);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0xB4);
        builder.putByte((byte)0x12);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x10);
        builder.putByte((byte)0x0E);
        builder.putByte((byte)0x7D);
        builder.putByte((byte)0xCE);
        builder.putByte((byte)0x08);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x35);
        builder.putByte((byte)0xA5);
        builder.putByte((byte)0x08);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0xB2);
        builder.putByte((byte)0x12);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x10);
        builder.putByte((byte)0x0E);
        builder.putByte((byte)0x7E);
        builder.putByte((byte)0xCE);
        builder.putByte((byte)0x08);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x23);
        builder.putByte((byte)0xA5);
        builder.putByte((byte)0x08);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0xB2);
        builder.putByte((byte)0x11);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x10);
        builder.putByte((byte)0x0E);
        builder.putByte((byte)0x7F);
        builder.putByte((byte)0xCE);
        builder.putByte((byte)0x08);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x29);
        builder.putByte((byte)0xA5);
        builder.putByte((byte)0x08);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0xB1);
        builder.putByte((byte)0x11);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x10);
        builder.putByte((byte)0x0E);
        builder.putByte((byte)0x80);
        builder.putByte((byte)0xCE);
        builder.putByte((byte)0x08);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x2F);
        builder.putByte((byte)0xA5);
        builder.putByte((byte)0x08);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0xB1);
        builder.putByte((byte)0x11);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x10);
        builder.putByte((byte)0x0E);
        builder.putByte((byte)0x81);
        builder.putByte((byte)0xCE);
        builder.putByte((byte)0x08);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x39);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x08);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x0E);
        builder.putByte((byte)0x82);
        builder.putByte((byte)0xCE);
        builder.putByte((byte)0x08);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x36);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x08);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x0E);
        builder.putByte((byte)0x83);
        builder.putByte((byte)0xCE);
        builder.putByte((byte)0x08);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x43);
        builder.putByte((byte)0xA0);
        builder.putByte((byte)0x08);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0xB4);
        builder.putByte((byte)0x11);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x10);
        builder.putByte((byte)0x0E);        
        
        return builder.build();
    }
    
    static Packet makeResponse(WorldSession session){
        PacketBuilder builder = getPacketBuilder(0x01A2);
        List<CashItem> cashItemList = session.getAccount().getCashItemList();
        
        builder.putIntLE(cashItemList.size()); // FIXME
        
        for(CashItem cashItem : cashItemList){
            builder.put(cashItem.getTransactionId());
            builder.put(cashItem.getItem().getId());
            builder.put(cashItem.getItem().getOptions());
            builder.put(cashItem.getDuration().getValue());
        }
        
        return builder.build();
        //return temp();
    }
}
