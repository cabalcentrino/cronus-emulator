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

package br.com.joaodarcy.cronus.worldsvr.inventory;

import br.com.joaodarcy.cronus.cabal.core.types.UInt16;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.npersistence.core.Column;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class InventoryItem {
    @Column("ItemId")
    private UInt32 itemId;
    @Column("ItemOpt1")
    private UInt32 itemOpt1;
    @Column("ItemOpt2")
    private UInt32 itemOpt2;
    @Column("InventorySlot")
    private UInt16 invSlot;

    public UInt32 getItemId() {
        return itemId;
    }

    public UInt32 getItemOpt1() {
        return itemOpt1;
    }

    public UInt32 getItemOpt2() {
        return itemOpt2;
    }

    public UInt16 getInventorySlot() {
        return invSlot;
    }    
}
