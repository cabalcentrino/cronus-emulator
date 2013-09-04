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

package br.com.joaodarcy.cronus.worldsvr.skillbook;

import br.com.joaodarcy.cronus.cabal.core.types.UInt16;
import br.com.joaodarcy.cronus.cabal.core.types.UInt8;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class Skill {
    
    protected final static UInt8 MIN_LEVEL = UInt8.valueOf((short)1);
    
    protected final UInt16 id;
    protected UInt8 level;

    public Skill(UInt16 id){
        this(id, MIN_LEVEL);
    }
       
    public Skill(UInt16 id, UInt8 level) {
        this.id = id;
        this.level = level;
    }    
        
    public static Skill from(SkillBookEntry skill){
        return new Skill(skill.getSkillId(), skill.getSkillLevel());
    }
    
    public UInt16 getId() {
        return id;
    }

    public UInt8 getLevel() {
        return level;
    }
    
    @Override
    public String toString() {
        return String.format("{Skill %04X - %4d Level %02d}", id, id, level);
    }
}
