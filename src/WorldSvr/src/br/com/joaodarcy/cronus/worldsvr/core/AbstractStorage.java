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

package br.com.joaodarcy.cronus.worldsvr.core;

import br.com.joaodarcy.cronus.cabal.core.types.UInt16;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
@Deprecated
public abstract class AbstractStorage<T extends Object> implements Storage<T> {
    
    protected final T storage[];
    protected final UInt16 capacity;
    
    protected short storedCount;
    
    public AbstractStorage(int capacity) {
        this.capacity = UInt16.valueOf(capacity);
        this.storage = makeArray(capacity);
        this.storedCount = 0;
    }            
    
    protected abstract T[] makeArray(int capacity);
    protected abstract T makeNewEntry(UInt32 id);
    protected abstract boolean checkEntryCompatibility(T entry, UInt32 id);
    
    protected boolean isEmptySlot(UInt16 slot){
        return storage[slot.intValue()] == null;
    }
    
    protected boolean isValidSlot(UInt16 slot){
        return slot.greaterThanOrEqual(UInt16.ZERO) && slot.lowerThan(capacity);
    }

    @Override
    public short getStoredCount() {
        return storedCount;
    }
    
    @Override
    public T[] getStorage() {
        return storage;
    }        
    
    @Override
    public T getReference(UInt32 id, UInt16 slot, DragContext ctx) {
        return getReference(id, slot);
    }
    
    @Override
    public T getReference(UInt32 id, UInt16 slot) {
        if(isValidSlot(slot)){
            if(checkEntryCompatibility((T)storage[slot.intValue()], id)){
                return (T)storage[slot.intValue()];
            }
        }
        return null;
    }

    @Override
    public boolean push(T object, UInt16 slot) {
        if(isValidSlot(slot)){
            if(isEmptySlot(slot)){
                storage[slot.intValue()] = object;
                ++storedCount;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean push(UInt32 id, UInt16 slot) {
        return push(makeNewEntry(id), slot);
    }

    @Override
    public boolean pop(UInt32 id, UInt16 slot) {
        if(isValidSlot(slot)){
            if(!isEmptySlot(slot)){
                storage[slot.intValue()] = null;
                --storedCount;
                return true;
            }
        }
        return false;
    }

}
