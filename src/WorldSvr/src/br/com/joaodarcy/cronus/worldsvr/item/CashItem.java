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

package br.com.joaodarcy.cronus.worldsvr.item;

import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.cronus.worldsvr.core.Item;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class CashItem {
    private final UInt32 transactionId;
    private final Item item;
    private final ItemDuration duration;

    public CashItem(UInt32 transactionId, Item item, ItemDuration duration) {
        this.transactionId = transactionId;
        this.item = item;
        this.duration = duration;
    }

    public UInt32 getTransactionId() {
        return transactionId;
    }

    public Item getItem() {
        return item;
    }

    public ItemDuration getDuration() {
        return duration;
    }    
}
