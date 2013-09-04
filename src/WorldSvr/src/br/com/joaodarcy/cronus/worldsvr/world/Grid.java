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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class Grid {
    
    private final static Logger logger = LoggerFactory.getLogger(Grid.class);
    
    private final int recWidth;
    private final int recHeight;

    private final List<GridObject> objectList;
    
    public Grid() {
        this(64,64);
    }        
    
    public Grid(int recWidth, int recHeight) {
        this.recWidth = recWidth;
        this.recHeight = recHeight;
        this.objectList = new ArrayList<>();
    }

    public void beginMovement(GridObject obj){
        if(obj == null){
            return;
        }
        logger.debug("Object {} begin movement", obj);
        notifyGridObjectBeginMovement(obj);
    }
    
    public void enterWorld(GridObject obj, int worldX, int worldY){
        if(obj == null){
            return;
        }
        synchronized(objectList){
            if(objectList.contains(obj)){
                logger.warn("Object {} already in grid !", obj);
            }else{
                objectList.add(obj);
                obj.entered(this);
                
                obj.setGridPosition(-1, -1);
                move(obj, worldX, worldY, false);
                
                logger.debug("Object {} entered grid", obj);                 
                notifyGridObjectEnteredGrid(obj, worldX, worldY);
            }
        }
    }
    
    public void exitWorld(GridObject o, int worldX, int worldY){
        if(o == null){
            return;
        }
        synchronized(objectList){
            int oldX, oldY;
                        
            oldX = o.getGridX();
            oldY = o.getGridY();
            
            objectList.remove(o);
            
            notifyGridObjectExitedGrid(o, worldX, worldY, oldX, oldY);            
        }
    }
    
    public void move(GridObject o, int worldX, int worldY){
        move(o, worldX, worldY, true);
    }
    
    private void move(GridObject o, int worldX, int worldY, boolean notify){
        if(o == null){
            return;
        }
        
        synchronized(objectList){
            int gridX, gridY, oldGridX, oldGridY;

            oldGridX = o.getGridX();
            oldGridY = o.getGridY();
            
            gridX = worldX / recWidth;
            gridY = worldY / recHeight;
                        
            // Check if is changing grid
            if(oldGridX != gridX || oldGridY != gridY){                                
                o.setGridPosition(gridX, gridY);
                o.onGridCellChange();
                
                logger.debug(String.format("Object %s moved from grid %d, %d to %d, %d", o, oldGridX, oldGridY, gridX, gridY));
                
                if(notify){                    
                    notifyGridObjectExitedGrid(o, worldX, worldY, oldGridX, oldGridY);
                    notifyGridObjectEnteredGrid(o, worldX, worldY);                    
                }                                
            }else{ // Only moved in world
                logger.debug(String.format("Object %s at grid %d, %d moved to world position %d, %d", o, gridX, gridY, worldX, worldY));
                
                if(notify){
                    notifyGridObjectMove(o, worldX, worldY);
                }
            }
        }
    }
          
    public List<GridObject> getObjectsInGrid(GridObject o){
        if(o == null){
            return Collections.emptyList();
        }
        return getObjectsInGrid(o.getGridX(), o.getGridY());
    }
    
    public List<GridObject> getObjectsInGrid(int gridX, int gridY){
        List<GridObject> gridObjectList = new ArrayList<>();
        
        synchronized(this){
            for(GridObject gridObject : objectList){
                if(gridObject.getGridX() == gridX && gridObject.getGridY() == gridY){
                    gridObjectList.add(gridObject);
                }
            }
        }
        
        return gridObjectList;
    }
    
    private void notifyGridObjectBeginMovement(GridObject o){
        if(o == null){
            return;
        }
        for(GridObject currentObject : getObjectsInGrid(o)){                        
            if(currentObject == o){
                continue;
            }
            currentObject.onObjectBeginMovement(o);
        }
    }
    
    private void notifyGridObjectExitedGrid(GridObject o, int worldX, int worldY, int oldGridX, int oldGridY){
        if(o == null){
            return;
        }                
        for(GridObject currentObject : getObjectsInGrid(oldGridX, oldGridY)){                        
            if(currentObject == o){
                continue;
            }
            currentObject.onObjectExitGrid(o, worldX, worldY);            
        }
    }
    
    private void notifyGridObjectEnteredGrid(GridObject o, int worldX, int worldY){
        if(o == null){
            return;
        }        
        for(GridObject currentObject : getObjectsInGrid(o)){
            if(currentObject == o){
                continue;
            }
            currentObject.onObjectEnterGrid(o, worldX, worldY);
        }        
    }    
    
    private void notifyGridObjectMove(GridObject o, int worldX, int worldY){
        if(o == null){
            return;
        }        
        for(GridObject currentObject : getObjectsInGrid(o)){        
            if(currentObject == o){
                continue;
            }
            currentObject.onObjectMoved(o, worldX, worldY);
        }
    }
    
}
