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

import br.com.joaodarcy.cronus.cabal.core.types.UInt16;
import br.com.joaodarcy.cronus.worldsvr.core.Updatable;
import br.com.joaodarcy.cronus.worldsvr.core.Vector2D;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public abstract class WorldObject implements Updatable, GridObject {    
    protected final Vector2D position;
        
    protected int gridX;
    protected int gridY;

    protected Grid grid;
            
    public WorldObject(int x, int y) {
        this.position = new Vector2D(x, y);
        this.grid = null;
    }             

    @Override
    public void entered(Grid grid) {
        synchronized(this){
            if(this.grid != null){
                this.grid.exitWorld(this, position.getX().intValue(), position.getY().intValue());
            }
            this.grid = grid;
        }
    }
    
    @Override
    public int getGridX() {
        return gridX;
    }

    @Override
    public int getGridY() {
        return gridY;
    }
    
    public Vector2D getPosition() {
        return position;
    }
    
    public UInt16 getX(){
        return position.getX();
    }
    
    public UInt16 getY(){
        return position.getY();
    }
    
    public void relocate(int x, int y){
        synchronized(this){
            position.setX(UInt16.valueOf(x));
            position.setY(UInt16.valueOf(y));
            if(grid != null){
                grid.move(this, x, y);
            }            
        }
    }
    
    @Override
    public void setGridPosition(int x, int y) {
        this.gridX = x;
        this.gridY = y;
    }
    
    @Override
    public void update(long timeDiff) {
        
    }
}
