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
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.cronus.worldsvr.core.AbstractStorage;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class SkillBook extends AbstractStorage<Skill> {

    protected final static byte UPGRADE_SLOT_COUNT = 6;
    
    protected final static byte UNK_SLOT_COUNT = 20;
    protected final static byte UNK_SLOT_COUNT_2 = 20;
            
    protected final static byte SKILL_SCHOOL_COUNT = 2;    
    protected final static byte SKILL_SLOT_COUNT_PER_SCHOOL = 32;
    
    protected final static byte SKILL_MAX_SLOT_COUNT = (SKILL_SCHOOL_COUNT * SKILL_SLOT_COUNT_PER_SCHOOL) + UPGRADE_SLOT_COUNT + UNK_SLOT_COUNT + UNK_SLOT_COUNT_2;
    
    protected final static byte SKILLBOOK_MIN_SLOT = 0;
    protected final static byte SKILLBOOK_MAX_SLOT = SKILL_MAX_SLOT_COUNT - 1;
    
    SkillBook() {
        super(SKILL_MAX_SLOT_COUNT);
    }
    
    @Override
    protected Skill[] makeArray(int capacity) {
        return new Skill[capacity];
    }
             
    @Override
    protected Skill makeNewEntry(UInt32 id) {
        return new Skill(UInt16.valueOf(id.intValue()));
    }

    @Override
    protected boolean checkEntryCompatibility(Skill skill, UInt32 id) {
        return skill != null && skill.getId().equals(UInt16.valueOf(id.intValue()));
    }    
}
