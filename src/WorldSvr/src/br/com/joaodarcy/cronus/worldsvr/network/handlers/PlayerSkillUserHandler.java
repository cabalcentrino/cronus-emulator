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
final class PlayerSkillUserHandler extends ServerOpcodeHandler {

    public PlayerSkillUserHandler() {
        super((byte)0x00AF, ClientState.IN_WORLD);
    }   
    
/* FRENTE
0000    E2 B7 11 00 00 00 00 00 AF 00 D0 00 03 8E 00 1D         .ﾷ......ﾯ....ﾎ..
0010    00*/

/* TRAS
0000    E2 B7 11 00 00 00 00 00 AF 00 D1 00 02 96 00 1E         .ﾷ......ﾯ....ﾖ..
0010    00*/
    
/*
0000    E2 B7 11 00 00 00 00 00 AF 00 D0 00 03 92 00 1E         .ﾷ......ﾯ....ﾒ..
0010    00*/
    
/*
0000    E2 B7 11 00 00 00 00 00 AF 00 D0 00 03 94 00 22         .ﾷ......ﾯ....ﾔ..
0010    00*/

    @Override
    protected Boolean handleValue(Packet value) {
        short skillId = value.getShort();
        byte unk0 = value.getByte();
        short newX = value.getShort();
        short newY = value.getByte();        
        
        //getPlayer().setX(newX);
        //getPlayer().setY(newY);
        
        return false;
    }
    
}
