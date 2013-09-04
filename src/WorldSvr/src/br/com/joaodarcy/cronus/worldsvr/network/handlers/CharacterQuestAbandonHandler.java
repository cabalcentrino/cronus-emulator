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
import br.com.joaodarcy.cronus.worldsvr.core.ClientState;
import br.com.joaodarcy.cronus.worldsvr.core.WorldSession;
import br.com.joaodarcy.cronus.worldsvr.network.ServerOpcodeHandler;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class CharacterQuestAbandonHandler extends ServerOpcodeHandler {

    public CharacterQuestAbandonHandler() {
        super((short)0x011C, ClientState.IN_WORLD);
    }    
    
    // 0000    E2 B7 10 00 00 00 00 00 1C 01 B9 0B 00 00 00 00                    .ﾷ........ﾹ.....
    
    @Override
    protected Boolean handleValue(Packet value) {
        UInt16 questId = value.getUInt16();        
        WorldSession session = getCurrentSession();
        
        session.sendPacket(
            makeBooleanPacket(
                session.getPlayer().getQuestLog().abandonQuest(questId)
            )
        );
        
        return true;
    }

}
