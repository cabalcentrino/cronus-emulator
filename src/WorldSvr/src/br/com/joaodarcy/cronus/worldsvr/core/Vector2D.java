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

package br.com.joaodarcy.cronus.worldsvr.core;

import br.com.joaodarcy.cronus.cabal.core.types.UInt16;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class Vector2D {
    private UInt16 x;
    private UInt16 y;

    public Vector2D(UInt16 x, UInt16 y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2D(short x, short y) {
        this(UInt16.valueOf(x), UInt16.valueOf(y));
    }
    
    public Vector2D(int x, int y) {
        this(UInt16.valueOf(x), UInt16.valueOf(y));
    }
    
    public Vector2D(){
        this(0, 0);
    }
        
    public double distance(Vector2D v){
        return distance(x.intValue(), y.intValue(), v.x.intValue(), v.y.intValue());
    }
    
    public double distance(int x0, int y0, int x1, int y1){
        return Math.sqrt(
            Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2)
        );
    }
    
    public UInt16 getX() {
        return x;
    }

    public UInt16 getY() {
        return y;
    }
   
    public void relocate(int x, int y){
        relocate(UInt16.valueOf(x), UInt16.valueOf(y));
    }
    
    public void relocate(UInt16 x, UInt16 y){
        this.x = x;
        this.y = y;
    }
    
    public void setX(UInt16 x) {
        this.x = x;
    }

    public void setY(UInt16 y) {
        this.y = y;
    }            
}
