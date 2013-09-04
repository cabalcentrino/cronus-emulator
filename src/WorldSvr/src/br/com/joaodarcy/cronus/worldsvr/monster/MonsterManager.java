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

package br.com.joaodarcy.cronus.worldsvr.monster;

import br.com.joaodarcy.cronus.worldsvr.ServerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class MonsterManager {

    /**
     * Monster templates
     */
    private static Monster monsterArray[] = null;
    private final static Logger logger = LoggerFactory.getLogger(MonsterManager.class);
    
    private final static ServerListener serverListener = new ServerListener() {
        // TODO: Carregar do banco de dados
        @Override
        public void onServerStartup() {
            logger.info("Loading monster data...");
            
            // TODO: Load monsters templates
            monsterArray = new Monster[2];
            monsterArray[1] = new Monster(); // Ferrão Rastejante
            
            logger.info("{} monster(s) loaded...", monsterArray.length);
        }

        @Override
        public void onServerShutdown() {
            logger.info("Unloading {} monsters data...", monsterArray.length);
            
            monsterArray = null;
        }
    };
    
    private MonsterManager() {
        throw new AssertionError();
    }

    public static Monster getMonsterById(int monsterId){
        return monsterArray[monsterId];
    }
    
    public static ServerListener getServerListener() {
        return serverListener;
    }    
}
