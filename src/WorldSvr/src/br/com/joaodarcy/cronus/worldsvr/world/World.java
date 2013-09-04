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

import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.cronus.cabal.core.types.UInt8;
import br.com.joaodarcy.npersistence.core.Column;
import java.util.ArrayList;
import java.util.List;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class World {
    @Column("wld_world_id")
    private UInt8 id;
    @Column("wld_name")
    private String name;
    
    private final List<WorldObject> objectList;
    private final Grid grid;
    
    public World() {
        objectList = new ArrayList<>(2);
        grid = new Grid();
    }        

    public void add(WorldObject o){
        if(o == null){
            return;
        }
        synchronized(this){
            if(objectList.contains(o)){
                return;
            }
            objectList.add(o);
            grid.enterWorld(o, o.getX().intValue(), o.getY().intValue());
        }
    }
    
    public void del(WorldObject o){
        if(o == null){
            return;
        }
        synchronized(this){
            if(objectList.contains(o)){
                objectList.remove(o);
                grid.exitWorld(o, o.getX().intValue(), o.getY().intValue());
            }
        }
    }
        
    public UInt8 getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    
    public List<WorldObject> getObjectList() {
        return objectList;
    }
    
    public void spawnMob(int monsterId, int x, int y){
        add(
            new WorldMob(
                UInt32.valueOf(objectList.size() + 1), 
                monsterId, 
                x, 
                y
            )                
        );
    }
    
    public void update(long timeDiff){
        // Desativado para facilitar o desenvolvimento
        /*synchronized(this){
            for(WorldObject obj : objectList){
                obj.update(timeDiff);
            }
        }*/
    }        
}
