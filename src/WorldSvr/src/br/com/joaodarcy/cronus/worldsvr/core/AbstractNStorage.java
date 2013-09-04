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

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public abstract class AbstractNStorage implements NStorage {

    protected final Item[] itemArray;
    protected int storedCount;

    public AbstractNStorage(int size) {
        this.itemArray = new Item[size];
        this.storedCount = 0;
    }
    
    protected Item popFromSlot(int slot){
        Item item = itemArray[slot];
        if(item == null){
            return null;
        }
        itemArray[slot] = null;
        return item;
    }
    
    protected boolean putInSlot(Item item, int slot){
        if(fitInSlot(item, slot)){
            itemArray[slot] = item;
            return true;
        }
        return false;
    }
    
    protected boolean canSwap(int srcSlot, int dstSlot){
        // FIXME
        return true;
    }
    
    protected boolean fitInSlot(Item item, int slot){
        return isEmpty(slot);        
    }
    
    @Override
    public Item[] getArray() {
        return itemArray;
    }
       
    @Override
    public int getStoredCount() {
        return storedCount;
    }

    protected boolean isEmpty(int slot){
        return itemArray[slot] == null;
    }
    
    @Override
    public Item pop(int slot) throws IllegalArgumentException  {
        if(slot < 0){
            throw new IllegalArgumentException("slot must be greater than or equal to 0");
        }
        if(slot >= itemArray.length){
            throw new IllegalArgumentException("slot must be lower than " + itemArray.length);
        }
        synchronized(this){
            Item item = popFromSlot(slot);
            if(item != null){
                --storedCount;
            }
            return item;
        }
    }

    @Override
    public boolean push(Item item, int slot) throws IllegalArgumentException  {
        if(item == null){
            throw new IllegalArgumentException("item must not be null.");
        }
        if(slot < 0){
            throw new IllegalArgumentException("slot must be greater than or equal to 0");
        }
        if(slot >= itemArray.length){
            throw new IllegalArgumentException("slot must be lower than " + itemArray.length);
        }
        synchronized(this){                            
            if(putInSlot(item, slot)){
                ++storedCount;
                return true;
            }
        }        
        return false;
    }
    
    @Override
    public boolean swap(int srcSlot, NStorage dstStorage, int dstSlot) throws IllegalArgumentException {
        if(dstStorage == null){
            throw new IllegalArgumentException("dstStorage must not be null");
        }
        if(dstStorage == this){
            if(!canSwap(srcSlot, dstSlot)){
                throw new IllegalArgumentException("dstStorage cannot swap " + srcSlot + " with " + dstSlot + " on self");
            }
        }
        if(srcSlot < 0){
            throw new IllegalArgumentException("srcSlot must be greater than or equal to 0");
        }
        if(srcSlot >= itemArray.length){
            throw new IllegalArgumentException("srcSlot must be lower than " + itemArray.length);
        }        
        synchronized(this){
            Item srcItem  = this.pop(srcSlot);
            if(srcItem == null){
                return false;
            }else{
                Item dstItem = dstStorage.pop(dstSlot);
                if(dstItem != null){
                    // Take item from destination
                    if(!this.push(dstItem, srcSlot)){                                            
                        // Rollback
                        
                        // Clear slots
                        this.pop(srcSlot);
                        dstStorage.pop(dstSlot);

                        // FIXME: Need check
                        this.push(srcItem, srcSlot);
                        dstStorage.push(dstItem, dstSlot);
                        
                        return false;
                    }
                }  
                // Send item to destination
                if(dstStorage.push(srcItem, dstSlot)){
                    return true;
                }else{
                    // Rollback
                    
                    // Clear slots
                    this.pop(srcSlot);
                    dstStorage.pop(dstSlot);
                    
                    // FIXME: Need check
                    this.push(srcItem, srcSlot);
                    dstStorage.push(dstItem, dstSlot);
                    return false;
                } 
            }
        }
    }

    @Override
    public Item valueAt(int slot) throws IllegalArgumentException  {
        if(slot < 0){
            throw new IllegalArgumentException("Illegal slot, must be greater than or equal to 0");
        }
        if(slot >= itemArray.length){
            throw new IllegalArgumentException("Illegal slot, must be lower than " + itemArray.length);
        }
        return itemArray[slot];
    }
}
