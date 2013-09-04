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
import br.com.joaodarcy.cronus.cabal.core.types.UInt8;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class QuestLogEntry {
    private final UInt16 questId;
    private UInt16 questProgress;
    private UInt8 tracking;
    private UInt8 unk;

    public QuestLogEntry(UInt16 questId, boolean tracking) {
        this.questId = questId;
        this.tracking = tracking ? UInt8.ONE : UInt8.ZERO;
        this.questProgress = UInt16.ZERO;
        this.unk = UInt8.ONE;
    }
            
    public UInt16 getQuestId() {
        return questId;
    }

    public UInt16 getQuestProgress() {
        return questProgress;
    }

    public UInt8 getTracking() {
        return tracking;
    }

    public UInt8 getUnk() {
        return unk;
    }
    
    public void setTracking(boolean tracking){
        this.tracking = tracking ? UInt8.ONE : UInt8.ZERO;
    }

    public void setQuestProgress(UInt16 questProgress) {
        this.questProgress = questProgress;
    }    
}
