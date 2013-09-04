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

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public enum ItemSize {
    _1X1                                                                   (1,1),
    _1X2                                                                   (1,2),
    _1X3                                                                   (1,3),
    _1X4                                                                   (1,4),
    _2X1                                                                   (2,1),
    _2X2                                                                   (2,2),
    _2X3                                                                   (2,3),
    _2X4                                                                   (2,4),
    _3X1                                                                   (3,1),
    _3X2                                                                   (3,2),
    _3X3                                                                   (3,3),
    _3X4                                                                   (3,4),
    _4X1                                                                   (4,1),
    _4X2                                                                   (4,2),
    _4X3                                                                   (4,3),
    _4X4                                                                   (4,4);    
    
    private ItemSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
       
    private final int width;
    private final int height;

    public int getHeight() {
        return height;
    }
    
    public int getWidth() {
        return width;
    }    
}
