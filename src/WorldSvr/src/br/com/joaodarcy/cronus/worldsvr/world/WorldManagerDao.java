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

import br.com.joaodarcy.cronus.worldsvr.AbstractDao;
import java.util.List;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class WorldManagerDao extends AbstractDao {

    private final static String SQL_GET_ALL_WORLDS_ORDERED_BY_ID = getQueryByName("world.getAllWorldMapsOrderedById");
    
    private WorldManagerDao() {
        throw new AssertionError();
    }
        
    public static List<World> getAllWorlds(){
        return getDataAccess().executeDQLObjectSelectList(
            null, 
            SQL_GET_ALL_WORLDS_ORDERED_BY_ID, 
            World.class
        );
    }    
    
}
