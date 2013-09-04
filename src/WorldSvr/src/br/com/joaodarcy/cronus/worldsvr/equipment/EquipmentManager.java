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

import br.com.joaodarcy.cronus.worldsvr.core.CharacterIdx;
import br.com.joaodarcy.cronus.worldsvr.core.Item;
import java.sql.Connection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class EquipmentManager {

    private final static Logger logger = LoggerFactory.getLogger(EquipmentManager.class);
    
    private EquipmentManager() {
        throw new AssertionError();
    }

    private static Equipment buildEquipment(CharacterIdx idx, List<EquipmentItem> itemList){        
        if(itemList != null){        
            Equipment equipment = new Equipment(idx);
            for(EquipmentItem itemAtual : itemList){
                equipment.push(itemAtual.toItem(), itemAtual.getSlot().shortValue());
            }            
            return equipment;
        }                
        return null;            
    }
            
    public static Equipment loadCharacterEquipment(Connection connection, CharacterIdx idx){
        return buildEquipment(
            idx,
            EquipmentManagerDao.getAllCharacterEquipments(connection, idx)                
        );
    }
    
    public static boolean saveCharacterEquipment(Connection connection, CharacterIdx idx, Equipment equipment){        
        if(EquipmentManagerDao.cleanup(connection, idx)){        
            int slot = 0;
            for(Item item : equipment.getArray()){
                if(item != null){
                    if(!EquipmentManagerDao.addEquipmentItem(connection, idx, item, slot)){
                        logger.error("Error saving item {} of player {} in slot " + slot, item, idx);
                        return false;
                    }
                }
                ++slot;
            }        
            return true;
        }else{
            logger.error("Error cleaning up character equipment for save");
        }
        return false;
    }
    
}
