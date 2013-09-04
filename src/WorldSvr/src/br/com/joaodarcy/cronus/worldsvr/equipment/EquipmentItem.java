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

package br.com.joaodarcy.cronus.worldsvr.equipment;

import br.com.joaodarcy.cronus.cabal.core.types.UInt16;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.cronus.worldsvr.core.Item;
import br.com.joaodarcy.npersistence.core.Column;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class EquipmentItem {
    @Column("eqp_item_opt1")
    private UInt32 id;
    @Column("eqp_item_opt2")
    private UInt32 modifier1;
    @Column("eqp_item_opt3")
    private UInt32 modifier2;
    @Column("eqp_esl_equipment_slot_id")
    private UInt16 slot;

    public UInt32 getId() {
        return id;
    }

    public UInt32 getModifier1() {
        return modifier1;
    }

    public UInt32 getModifier2() {
        return modifier2;
    }

    public UInt16 getSlot() {
        return slot;
    }    
    
    public Item toItem(){
        return new Item(id, modifier1, modifier2); // FIXME: Make superclass is best
    }
}
