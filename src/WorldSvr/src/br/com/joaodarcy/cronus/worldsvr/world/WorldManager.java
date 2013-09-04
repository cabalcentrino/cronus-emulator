
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
package br.com.joaodarcy.cronus.worldsvr.world;

import br.com.joaodarcy.npersistence.core.DataAccessException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class WorldManager {        
    private static final Logger logger;    
            
    static{                
        logger = LoggerFactory.getLogger(WorldManager.class);                
    }
    
    public static List<World> getWorlds(){       
        List<World> databaseWorldList;
        
        try{
            databaseWorldList = WorldManagerDao.getAllWorlds();
            if(databaseWorldList == null || databaseWorldList.isEmpty()){
                throw new DataAccessException("Database does not have maps");
            }
            return databaseWorldList;
        }catch(Throwable t){
            logger.error("Cannot load worlds: {}, closing server", t.getMessage(), t);
            System.exit(1);
        }
        return null;
    }            
}
