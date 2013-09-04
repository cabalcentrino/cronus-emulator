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

import br.com.joaodarcy.cronus.worldsvr.AbstractDao;
import br.com.joaodarcy.cronus.worldsvr.core.CharacterIdx;
import br.com.joaodarcy.cronus.worldsvr.core.Item;
import java.sql.Connection;
import java.util.List;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class InventoryManagerDao extends AbstractDao {

    private final static String SQL_CHARACTER_INVENTORY_ADD_ITEM = getQueryByName("characterInventory.addItem");
    private final static String SQL_CHARACTER_INVENTORY_REMOVE_ALL = getQueryByName("characterInventory.removeAll");
    private final static String SQL_CHARACTER_INVENTORY_GET_ALL_ITEMS = getQueryByName("characterInventory.getAllItems");
    
    private InventoryManagerDao() {
        throw new AssertionError();
    }
                      
    static boolean addInvetoryItem(Connection connection, CharacterIdx characterIdx, Item item, int invetorySlot){
        return getDataAccess().executeDMLInsert(
            connection, 
            SQL_CHARACTER_INVENTORY_ADD_ITEM, 
            characterIdx.getCharacterId(),
            getServerId(),
            invetorySlot,
            item.getRawItemId().longValue(),
            item.getId().longValue(),
            item.getDuration().longValue(),
            item.getOptions().longValue()
        );
    }
    
    static boolean removeAll(Connection connection, CharacterIdx characterIdx){
        return getDataAccess().executeDMLDelete(
            connection, 
            SQL_CHARACTER_INVENTORY_REMOVE_ALL, 
            characterIdx.getCharacterId(),
            getServerId()
        );
    }
    
    static List<InventoryItem> getCharacterInventory(Connection connection, CharacterIdx characterIdx){
        return getDataAccess().executeDQLObjectSelectList(
            connection, 
            SQL_CHARACTER_INVENTORY_GET_ALL_ITEMS, 
            InventoryItem.class, 
            characterIdx.getCharacterId(),
            getServerId()
        );                
    }    
    
}
