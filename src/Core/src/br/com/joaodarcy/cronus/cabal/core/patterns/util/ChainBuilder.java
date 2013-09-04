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

package br.com.joaodarcy.cronus.cabal.core.patterns.util;

import br.com.joaodarcy.cronus.cabal.core.patterns.Chain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class ChainBuilder<T extends Chain> {
    protected final List<T> chainList;
    protected final Logger logger;
    protected boolean built;
    protected T lastElement = null;

    
    
    private ChainBuilder() {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.chainList = new ArrayList<T>();
        this.built = false;
    }
    
    public static ChainBuilder create(){
        return new ChainBuilder();
    }
    
    public ChainBuilder<T> add(T chainElement){
        if(built){
            throw new IllegalStateException("Chain already built !");
        }
        
        if(chainElement != null){
            if(!chainList.contains(chainElement)){
                chainList.add(chainElement);
            }
        }        
        return this;
    }

    public ChainBuilder<T> setLastElement(T lastElement) {
        this.lastElement = lastElement;
        return this;
    }
    
    public Chain build() throws IllegalStateException {
        if(!built){                                    
            if(chainList.isEmpty()){
                throw new IllegalStateException("Chain list is empty !");
            }
            built = true;
            
            logger.debug("Building chain...");
            
            Collections.sort(chainList);
            T oldChainElement = null;
            
            for(T chainElement : chainList){
                if(oldChainElement != null){
                    oldChainElement.setNext(chainElement);
                }
                oldChainElement = chainElement;
                logger.debug("Adding chain element: {}", chainElement.getClass().getSimpleName());
            }
            
            oldChainElement.setNext(lastElement);
            
            logger.debug("Chain build...");
            
            return chainList.get(0);
        }else{
            throw new IllegalStateException("Chain already built !");
        }                
    }
}
