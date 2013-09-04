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

package br.com.joaodarcy.cronus.worldsvr.character;

import br.com.joaodarcy.cronus.cabal.core.types.UInt8;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public enum WeaponRank {
    NOVICE                      ((byte)0x01),
    APRENTICE                   ((byte)0x02),
    REGULAR                     ((byte)0x03),
    EXPERT                      ((byte)0x04),
    A_EXPERT                    ((byte)0x05),
    MASTER                      ((byte)0x06),
    A_MASTER                    ((byte)0x07),
    G_MASTER                    ((byte)0x08),
    COMPLETER                   ((byte)0x09),
    TRANSCENDER                 ((byte)0x0A);        
    
    private final UInt8 id;

    private WeaponRank(byte id) {
        this.id = UInt8.valueOf(id);
    }
    
    public UInt8 getId() {
        return id;
    }
    
    public static WeaponRank getWeaponRankById(UInt8 id){
        for(WeaponRank current : values()){
            if(current.id.equals(id)){
                return current;
            }
        }
        return null;
    }   
}
