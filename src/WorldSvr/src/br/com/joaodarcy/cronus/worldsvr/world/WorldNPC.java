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

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class WorldNPC extends WorldObject {
    private final UInt16 id;
    
    public WorldNPC(UInt16 id, int x, int y) {
        super(x, y);
        this.id = id;
    }

    public UInt16 getId() {
        return id;
    }        

    @Override
    public void onGridCellChange() {
        
    }

    @Override
    public void onObjectEnterGrid(GridObject o, int worldX, int worldY) {
        
    }

    @Override
    public void onObjectExitGrid(GridObject o, int worldX, int worldY) {
        
    }

    @Override
    public void onObjectMoved(GridObject o, int worldX, int worldY) {
        
    }

    @Override
    public void onObjectBeginMovement(GridObject o) {
        
    }
}
