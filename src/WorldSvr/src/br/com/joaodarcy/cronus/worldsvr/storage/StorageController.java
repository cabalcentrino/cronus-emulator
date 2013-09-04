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

package br.com.joaodarcy.cronus.worldsvr.storage;

import br.com.joaodarcy.cronus.worldsvr.core.CharacterIdx;
import br.com.joaodarcy.cronus.worldsvr.core.Item;
import br.com.joaodarcy.cronus.worldsvr.core.NStorage;
import br.com.joaodarcy.cronus.worldsvr.equipment.Equipment;
import br.com.joaodarcy.cronus.worldsvr.equipment.EquipmentManager;
import br.com.joaodarcy.cronus.worldsvr.inventory.Inventory;
import br.com.joaodarcy.cronus.worldsvr.inventory.InventoryManager;
import java.sql.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class StorageController {
    
    private final static Logger logger = LoggerFactory.getLogger(StorageController.class);
            
    private final CharacterIdx characterIdx;
    private final NStorage storageArray[];    

    private final static int INVENTORY_INDEX    = 0;
    private final static int EQUIPMENT_INDEX    = 1;
    
    public StorageController(Connection connection, CharacterIdx characterIdx, Equipment equipment) {
        this.characterIdx = characterIdx;
        storageArray = new NStorage[] {
            loadCharacterBag(connection, characterIdx),
            equipment
        };
    }
    
    public StorageController(Connection connection, CharacterIdx idx) {
        this.characterIdx = idx;
        storageArray = new NStorage[] {
            loadCharacterBag(connection, idx),
            loadCharacterEquipment(connection, idx)
        };
    }               
            
    public synchronized boolean drop(int srcStorage, int fromSlot){
        NStorage storage = storageArray[srcStorage];        
        logger.debug("Dropping {}[{}] in world", storage.getClass().getSimpleName(), fromSlot);
        Item droppedItem = storage.pop(fromSlot);        
        if(droppedItem == null){
            logger.error("HACK: Player {} is dropping an null item in world", characterIdx);            
        }else{
            // TODO: Notify world
            return true;
        }
        return false;
    }
    
    public Inventory getInventory(){
        return (Inventory)storageArray[INVENTORY_INDEX];
    }
    
    public Equipment getEquipment(){
        return (Equipment)storageArray[EQUIPMENT_INDEX];
    }
            
    public static Inventory loadCharacterBag(Connection connection, CharacterIdx idx){
        return InventoryManager.loadCharacterInventory(
            connection, 
            idx
        );
    }    
    
    public static Equipment loadCharacterEquipment(Connection connection, CharacterIdx idx){
        return EquipmentManager.loadCharacterEquipment(
            connection, 
            idx
        );
    }
    
    public synchronized boolean pushCashItem(Item cashItem, int slot){
        return getInventory().push(cashItem, slot);
    }
            
    public synchronized boolean save(Connection connection){                            
        logger.info("Saving player {} inventory...", characterIdx);        
        if(!InventoryManager.saveCharacterInventory(connection, characterIdx, getInventory())){
            logger.error("FATAL: Unknow error saving player {} inventory !", characterIdx);
            return false;
        }
        logger.info("Saving player {} equipment...", characterIdx);
        if(!EquipmentManager.saveCharacterEquipment(connection, characterIdx, getEquipment())){
            logger.error("FATAL: Unknow error saving player {} equipment !", characterIdx);
            return false;
        }
        return true;
    }
    
    public synchronized boolean swapItem(int srcStorage, int srcSlot, int dstStorage, int dstSlot){
        return swapItem(storageArray[srcStorage], srcSlot, storageArray[dstStorage], dstSlot);
    }
    
    private boolean swapItem(NStorage src, int srcSlot, NStorage dst, int dstSlot){
        logger.debug("Swapping itens from {}[{}] with " + dst.getClass().getSimpleName() + "[" + dstSlot + "]", src.getClass().getSimpleName(), srcSlot);
        return src.swap(srcSlot, dst, dstSlot);
    }
    
}
