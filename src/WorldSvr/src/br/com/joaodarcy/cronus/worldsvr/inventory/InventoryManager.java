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

import br.com.joaodarcy.cronus.worldsvr.core.CharacterIdx;
import br.com.joaodarcy.cronus.worldsvr.core.Item;
import java.sql.Connection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class InventoryManager {

    private final static Logger logger = LoggerFactory.getLogger(InventoryManager.class);
    
    public InventoryManager() {
        throw new AssertionError();
    }

    public static Inventory loadCharacterInventory(Connection connection, CharacterIdx idx){
        List<InventoryItem> bagItemList = InventoryManagerDao.getCharacterInventory(connection, idx);        
        Inventory characterBag = new Inventory();
        
        for(InventoryItem bagItem : bagItemList){
            characterBag.push(Item.from(bagItem), bagItem.getInventorySlot().intValue());
        }
        
        return characterBag;
    }
    
    public static boolean saveCharacterInventory(Connection connection, CharacterIdx characterIdx, Inventory inventory){
        logger.info("Saving character inventory {}", characterIdx);
        
        if(InventoryManagerDao.removeAll(connection, characterIdx)){
            int inventorySlot = 0;
            for(Item currentItem : inventory.getArray()){
                if(currentItem != null){
                    if(!InventoryManagerDao.addInvetoryItem(connection, characterIdx, currentItem, inventorySlot)){
                        logger.error("Error saving character inventory {} on item {}", characterIdx, currentItem);
                        return false;
                    }
                }
                ++inventorySlot;
            }
            return true;
        }        
        return false;
    }    
}
