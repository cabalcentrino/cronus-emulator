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
import br.com.joaodarcy.cronus.worldsvr.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class QuestLog {    
    private final static Logger logger = LoggerFactory.getLogger(QuestLog.class);
    
    private final static int MAX_OPEN_QUEST = Configuration.maxOpenQuests();
    private final static int MAX_TRACKING_QUEST = Configuration.maxTrackingQuests();
    
    private final QuestLogEntry questLogEntries[];
    private UInt8 questCount;
    private int trackingCount;

    QuestLog() {
        this.questLogEntries = new QuestLogEntry[MAX_OPEN_QUEST];
        this.questCount = UInt8.ZERO;
        this.trackingCount = 0;
    }

    public boolean abandonQuest(UInt16 questId) throws IllegalArgumentException {
        if(questId == null){
            throw new IllegalArgumentException("Param questId cannot be null.");
        }
        synchronized(this){
            if(questCount.byteValue() > 0){
                for(int i = 0 ; i < MAX_OPEN_QUEST ; i++){
                    QuestLogEntry entry = questLogEntries[i];
                    if(entry != null){
                        if(entry.getQuestId().equals(questId)){
                            if(entry.getTracking().equals(UInt8.ONE)){
                                --trackingCount;
                            }
                            questCount = UInt8.valueOf((byte)(questCount.byteValue() - 1));
                            questLogEntries[i] = null;
                            logger.debug("Player abandonned quest {}", questId);
                            return true;
                        }
                    }
                }
                logger.error("HACK: Player not has quest {} to abandon !", questId);
                return false;
            }else{
                logger.error("HACK: There is none quest opened to abandon !");
                return false;
            }
        }        
    }
    
    public boolean addQuest(UInt16 questId, UInt8 slot) throws IllegalArgumentException {
        if(questId == null){
            throw new IllegalArgumentException("Param questId cannot be null.");
        }
        if(slot == null){
            throw new IllegalArgumentException("Param slot cannot be null.");
        }
        // TODO: Check slot range
        
        synchronized(this){ 
            if(questCount.byteValue() < MAX_OPEN_QUEST){            
                if(questLogEntries[slot.byteValue()] == null){
                    boolean track = true;                
                    if(trackingCount >= MAX_TRACKING_QUEST){
                        track = false;
                        logger.debug("Already have max tracking quests");
                    }else{
                        ++trackingCount;
                    }
                    questCount = UInt8.valueOf((byte)(questCount.byteValue() + 1));
                    questLogEntries[slot.byteValue()] = new QuestLogEntry(questId, track);
                    return true;
                }else{
                    logger.error("HACK: Already have a quest in slot {}", slot);
                    return false;
                }
            }else{
                logger.error("HACK: Already have max open quests allowed");
                return false;
            }
        }
    }
    
    public UInt8 getQuestCount() {
        return questCount;
    }
    
    public QuestLogEntry[] getQuestLogEntries() {
        return questLogEntries;
    }    
}
