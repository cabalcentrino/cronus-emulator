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

import br.com.joaodarcy.cronus.worldsvr.ServerListener;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class ItemManager {
    private final static Logger logger;
    private final static ServerListener serverListener;
    
    private static ItemData itemDataArray[];
            
    static{
        logger = LoggerFactory.getLogger(ItemManager.class);
        itemDataArray = null;
        
        serverListener = new ServerListener() {
            @Override
            public void onServerStartup() {
                logger.info("Loading item data...");
                
                List<ItemData> itemDataList = ItemManagerDao.getItemDataList(null);
                itemDataArray = new ItemData[itemDataList.size() + 1];
                itemDataArray[0] = null;
                for(int i = 1, j = 0 ; i < itemDataArray.length ; i++, j++){                    
                    itemDataArray[i] = itemDataList.get(j);
                }
                
                logger.info("{} itens loaded sucessfully...", itemDataArray.length);

                
                dataSelfCheck();                                
            }

            @Override
            public void onServerShutdown() {
                logger.info("Unloading {} item data...", itemDataArray == null ? 0 : itemDataArray.length - 1);
                
                itemDataArray = null;
            }
        };        
    }
    
    private ItemManager() {
        throw new AssertionError();
    }    
    
    private static void dataSelfCheck(){
        logger.info("Item data self-checking started....");
        
        for(int i = 1 ; i < itemDataArray.length ; i++){
            if(itemDataArray[i] == null){
                throw new IllegalStateException("Item in index " + i + " is null");
            }
            if(itemDataArray[i].getId() != i){
                throw new IllegalStateException("Item in index " + i + " has id incorrect id: " + itemDataArray[i].getId());
            }
        }
        
        logger.info("Item data self-checking ended, all itens OK...");
    }
    
    public static ItemData getItemDataById(int id){
        if(id <= 0 || id >= itemDataArray.length){
            logger.error("Invalid item id: {}", id, new Throwable());
            return null;
        }
        return itemDataArray[id];
    }
    
    public static ServerListener getServerListener() {
        return serverListener;
    }
}
