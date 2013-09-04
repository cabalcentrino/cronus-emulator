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

package br.com.joaodarcy.cronus.worldsvr.equipment;

import br.com.joaodarcy.cronus.cabal.core.types.UInt8;
import br.com.joaodarcy.cronus.worldsvr.core.AbstractNStorage;
import br.com.joaodarcy.cronus.worldsvr.core.CharacterIdx;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class Equipment extends AbstractNStorage {
                    
    public final static UInt8 SLOT_HEAD                     = UInt8.ZERO;
    public final static UInt8 SLOT_BODY                     = UInt8.valueOf((short)0x01);
    public final static UInt8 SLOT_HAND                     = UInt8.valueOf((short)0x02);
    public final static UInt8 SLOT_FEET                     = UInt8.valueOf((short)0x03);
    public final static UInt8 SLOT_LEFT_HAND                = UInt8.valueOf((short)0x04);
    public final static UInt8 SLOT_RIGHT_HAND               = UInt8.valueOf((short)0x05);
    public final static UInt8 SLOT_EPAULET                  = UInt8.valueOf((short)0x06);    
    public final static UInt8 SLOT_AMULETO                  = UInt8.valueOf((short)0x07);
    public final static UInt8 SLOT_UPPER_LEFT_RING          = UInt8.valueOf((short)0x08);
    public final static UInt8 SLOT_UPPER_RIGHT_RING         = UInt8.valueOf((short)0x09);
    public final static UInt8 SLOT_BIKE_BOARD               = UInt8.valueOf((short)0x0A);
    public final static UInt8 SLOT_PET                      = UInt8.valueOf((short)0x0B);
    public final static UInt8 SLOT_UNK0C                    = UInt8.valueOf((short)0x0C);
    public final static UInt8 SLOT_RIGHT_EARRING            = UInt8.valueOf((short)0x0D);
    public final static UInt8 SLOT_LEFT_EARRING             = UInt8.valueOf((short)0x0E);
    public final static UInt8 SLOT_RIGHT_BRACELET           = UInt8.valueOf((short)0x0F);
    public final static UInt8 SLOT_LEFT_BRACELET            = UInt8.valueOf((short)0x10);
    public final static UInt8 SLOT_LOWER_RIGHT_RING         = UInt8.valueOf((short)0x11);
    public final static UInt8 SLOT_LOWER_LEFT_RING          = UInt8.valueOf((short)0x12);
    public final static UInt8 SLOT_BELT                     = UInt8.valueOf((short)0x13);
    
    public final static UInt8 EQUIPMENT_MIN_SLOT            = UInt8.ZERO;
    public final static UInt8 EQUIPMENT_MAX_SLOT            = UInt8.valueOf((byte)(SLOT_BELT.byteValue() + 22 /* Episode 8 - Including mercenaries*/));
    
    protected final static int EQUIPMENT_SLOT_COUNT = EQUIPMENT_MAX_SLOT.intValue() + 1;
                    
    protected final CharacterIdx idx;
    
    Equipment(CharacterIdx idx) {
        super(EQUIPMENT_SLOT_COUNT);
        this.idx = idx;
    }                      

    CharacterIdx getIdx() {
        return idx;
    }        
}
