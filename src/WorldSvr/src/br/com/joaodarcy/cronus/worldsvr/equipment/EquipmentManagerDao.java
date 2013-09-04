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

import br.com.joaodarcy.cronus.worldsvr.AbstractDao;
import br.com.joaodarcy.cronus.worldsvr.core.CharacterIdx;
import br.com.joaodarcy.cronus.worldsvr.core.Item;
import java.sql.Connection;
import java.util.List;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class EquipmentManagerDao extends AbstractDao {

    private final static String SQL_ADD_ITEM = getQueryByName("equipment.addItem");
    private final static String SQL_CLEANUP = getQueryByName("equipment.deleteAllFromCharacter");
    private final static String SQL_GET_ALL_EQUIPMENTS_FROM_CHARACTER = getQueryByName("equipment.getAllFromCharacter");
        
    private EquipmentManagerDao() {
        throw new AssertionError();
    }
         
    static boolean addEquipmentItem(Connection connection, CharacterIdx idx, Item item, int slot){
        return getDataAccess().executeDMLInsert(
            connection, 
            SQL_ADD_ITEM, 
            idx.getCharacterId(),
            getServerId(),
            slot,
            item.getRawItemId().longValue(),
            item.getId().longValue(),
            item.getDuration().longValue(),
            item.getOptions().longValue()
        );
    }
    
    static boolean cleanup(Connection connection, CharacterIdx idx){
        return getDataAccess().executeDMLDelete(
            connection, 
            SQL_CLEANUP, 
            idx.getCharacterId(),
            getServerId()
        );
    }
    
    static List<EquipmentItem> getAllCharacterEquipments(Connection connection, CharacterIdx idx){
        return getDataAccess().executeDQLObjectSelectList(
            connection, 
            SQL_GET_ALL_EQUIPMENTS_FROM_CHARACTER, 
            EquipmentItem.class, 
            idx.getCharacterId(),
            getServerId()
        );
    }
    
}
