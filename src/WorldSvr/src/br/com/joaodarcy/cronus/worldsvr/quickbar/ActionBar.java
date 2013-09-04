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

package br.com.joaodarcy.cronus.worldsvr.quickbar;

import br.com.joaodarcy.cronus.cabal.core.types.UInt16;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.cronus.cabal.core.types.UInt8;
import br.com.joaodarcy.cronus.worldsvr.core.AbstractStorage;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class ActionBar extends AbstractStorage<Action> {
    
    protected final static byte QUICK_BAR_COUNT = 4;    
    protected final static byte QUICK_BAR_SLOT_COUNT = 12;
        
    protected final static byte QUICK_MIN_SLOT = 0;
    protected final static byte QUICK_MAX_SLOT = (QUICK_BAR_COUNT * QUICK_BAR_SLOT_COUNT) -1;
    
    protected final static byte QUICK_BAR_MAX_SLOT_COUNT = QUICK_MAX_SLOT + 1;
    
    //0000    E2 B7 0C 00 00 00 00 00 92 00 2F 26

    ActionBar() {
        super(QUICK_BAR_MAX_SLOT_COUNT);
    }
    
    /*public boolean push(Item item, UInt8 slot) {
        return push(item.getId(), slot);
    }
    
    public boolean push(UInt16 id, UInt8 slot) {
        return super.push(id, UInt16.valueOf(slot));
    }*/
    
    @Override
    protected Action[] makeArray(int capacity) {
        return new Action[capacity];
    }

    @Override
    protected Action makeNewEntry(UInt32 id) {
        return new Action(UInt8.valueOf(id.shortValue())/* PENDING */);
    }

    @Override
    protected boolean checkEntryCompatibility(Action entry, UInt32 id) {
        return UInt16.valueOf(entry.getId()).equals(id);
    }
}
