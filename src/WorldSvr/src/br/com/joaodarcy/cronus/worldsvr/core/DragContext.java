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
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
@Deprecated
public class DragContext {    
    
    private final static Logger logger;
    
    static{
        logger = LoggerFactory.getLogger(DragContext.class);
    }
    
    private Object object;
    private Storage storage;
    private UInt32 id;
    private UInt16 slot;

    public DragContext() {
        reset();
    }        
    
    protected boolean isDragging(){
        return object != null;
    }
    
    public boolean drop(){
        if(isDragging()){
            if(storage.pop(id, slot)){
                reset();
                return true;
            }else{
                logger.error("Cannot remove item from storage !");
            }
        }else{
            logger.error("Not draggin anything !");
        }
        return false;
    }
    
    public boolean dragBegin(UInt32 id, UInt16 slot, Storage storageSrc){
        if(storageSrc == null){
            logger.error("Param storage must not be null !");
            return false;
        }                
        if(isDragging()){
            logger.error("Already dragging a object !");
            return false;
        }
        
        Object dragObject = storageSrc.getReference(id, slot);
        if(dragObject == null){
            logger.error("Object {} not found in slot {}", id, slot);
        }else{
            this.object = dragObject;
            this.storage = storageSrc;
            this.id = id;
            this.slot = slot;            
            return true;
        }
        return false;
    }
    
    public boolean dragEnd(UInt32 idDst, UInt16 slotDst, Storage storageDst){
        if(storageDst == null){
            logger.error("Param storage must not be null !");
            return false;
        }                
        if(isDragging()){
            // Remove from current storage
            if(storage.pop(id, slot)){
                // Put in new storage
                if(storageDst.push(object, slotDst)){
                    reset();
                    logger.debug("Drag ended for item {}, new slot {}", idDst, slotDst);                    
                    return true;
                }else{
                    logger.error("Cannot put object {} at slot {} un new storage, aborting...", idDst, slotDst);                                        
                    // Return back
                    if(!storage.push(object, slot)){
                        logger.error("FATAL: Cannot put object {} back in slot {}", id, slot);
                    }
                }
            }else{
                logger.error("Cannot remove objet {} at slot {} from storage", idDst, slotDst);
            }                     
        }else{
            logger.error("Not dragging a object !");            
        }
        return false;
    }
    
    public boolean dragSwap(UInt32 idDst, UInt16 slotDst, Storage storageDst){
        if(storageDst == null){
            logger.error("Param storage must not be null !");
            return false;
        }                
        if(isDragging()){
            Object newObject = storageDst.getReference(idDst, slotDst);
            if(newObject == null){
                logger.error("Cannot find {} from destination storage slot {}", idDst, slotDst);
            }else{
                // Remove newObject from old storage
                if(storageDst.pop(idDst, slotDst)){ 
                    // End current drag putting object in storageDst
                    if(dragEnd(this.id, slotDst, storageDst)){
                        logger.debug("Drag, swapped item {} with {}", idDst, id);
                        
                        object = newObject;
                        id = idDst;
                        slot = slotDst;
                        storage = storageDst;
                        
                        return true;
                    }else{                    
                        logger.error("Cannot put item {} in slot {}", this.id, slotDst);
                        if(!storageDst.push(newObject, slotDst)){
                            logger.error("FATAL: Cannot put item {} back to slot {}", idDst, slotDst);
                        }
                    }
                }
            }
        }else{
            logger.error("Not dragging a object !");            
        }
        return false;
    }
    
    private void reset(){
        this.object = null;
        this.storage = null;
        this.id = UInt32.ZERO;
        this.slot = UInt16.ZERO;
    }
    
}
