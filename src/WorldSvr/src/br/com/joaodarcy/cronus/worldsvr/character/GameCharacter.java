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

import br.com.joaodarcy.cronus.cabal.core.types.UInt16;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.cronus.cabal.core.types.UInt8;
import br.com.joaodarcy.cronus.worldsvr.core.CharacterIdx;
import br.com.joaodarcy.cronus.worldsvr.equipment.Equipment;
import br.com.joaodarcy.npersistence.core.Column;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class GameCharacter {    
    @Column("chr_character_id")
    private CharacterIdx characterIdx;  
    @Column("chr_name")
    private String name;
    @Column("chr_level")
    private UInt32 level;
    @Column("chr_style")
    private UInt32 style;    
    @Column("chr_rka_sword_rank_id")
    private UInt8 swordRank;
    @Column("chr_rka_magic_rank_id")
    private UInt8 magicRank;
    @Column("chr_nat_nation_id")
    private UInt8 nation;
    @Column("chr_alz")
    protected UInt32 alz;
    @Column("chr_wld_world_id")
    protected UInt8 world;
    @Column("chr_world_x")
    protected UInt16 x;
    @Column("chr_world_y")
    protected UInt16 y;
        
    @Column(value="", ignore=true)
    private Equipment equipment;    

    public CharacterIdx getCharacterIdx() {
        return characterIdx;
    }

    public String getName() {
        return name;
    }

    public UInt32 getLevel() {
        return level;
    }

    public UInt32 getStyle() {
        return style;
    }

    public UInt8 getSwordRank() {
        return swordRank;
    }

    public UInt8 getMagicRank() {
        return magicRank;
    }

    public UInt8 getNation() {
        return nation;
    }

    public UInt32 getAlz() {
        return alz;
    }

    public UInt8 getWorld() {
        return world;
    }

    public UInt16 getX() {
        return x;
    }

    public UInt16 getY() {
        return y;
    }
    
    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }
        
    @Override
    public String toString() {
        return String.format("{ Character: %s, Name: %s, Level: %d }", characterIdx, name, level);
    }    
}
