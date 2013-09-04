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

package br.com.joaodarcy.cronus.worldsvr.aura;

import br.com.joaodarcy.cronus.worldsvr.ServerListener;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class AuraManager {
    private final static Logger logger;
    private final static ServerListener serverListener;
    
    private static Aura auraArray[];    
            
    static{
        auraArray = null;        
        logger = LoggerFactory.getLogger(AuraManager.class);        
        
        serverListener = new ServerListener() {
            @Override
            public void onServerStartup() {
                logger.info("Loading auras...");
                auraArray = initialize();
                
                selfCheck();
            }

            @Override
            public void onServerShutdown() {
                logger.info("Unloading {} auras...", auraArray == null ? 0 : auraArray.length);
                auraArray = null;
            }
        };
    }
    
    private AuraManager() {
        throw new AssertionError();
    }
                
    public static ServerListener getServerListener() {
        return serverListener;
    }
    
    public static Aura getAuraById(int id){
        if(id < 0 || id >= auraArray.length){
            logger.error("FATAL: Invalid AuraId {}", id, new Throwable());
            return null;
        }
        return auraArray[id];
    }
    
    private static synchronized Aura[] initialize(){
        List<Aura> auraList = AuraManagerDao.getAllAuras(null);
        return auraList.toArray(new Aura[auraList.size()]);
    }
    
    private static void selfCheck(){
        for(int i = 0 ; i < auraArray.length ; i++){
            if(auraArray[i].getId() == i){
                continue;
            }else{
                throw new IllegalStateException("Aura no indice " + i + " tem id " + auraArray[i].getId());                                
            }
        }
    }    
}
