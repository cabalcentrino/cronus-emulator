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

package br.com.joaodarcy.cronus.cabal.core.types;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class ByteSwapper {

    private ByteSwapper() {
        throw new AssertionError();
    }
    
    public static short swapBytes(short value){                
        return (short)(((value & 0xFF) << 8) | ((value & 0xFF00) >> 8));
    }
    
    public static int swapBytes(int value){
        return (int)(
                ((value & 0xFF000000) >> 24) |
                ((value & 0x00FF0000) >>  8) |
                ((value & 0x0000FF00) <<  8) |
                ((value & 0x000000FF) << 24)                 
        );
    }        
}
