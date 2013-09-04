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

package br.com.joaodarcy.cronus.worldsvr.account;

import br.com.joaodarcy.cronus.worldsvr.ServerNode;
import br.com.joaodarcy.cronus.worldsvr.character.CharacterManager;
import br.com.joaodarcy.cronus.worldsvr.character.GameCharacter;
import br.com.joaodarcy.cronus.worldsvr.core.WorldSession;
import br.com.joaodarcy.cronus.worldsvr.equipment.Equipment;
import br.com.joaodarcy.cronus.worldsvr.equipment.EquipmentManager;
import br.com.joaodarcy.npersistence.core.DataAccessException;
import br.com.joaodarcy.npersistence.util.JDBCUtil;
import java.sql.Connection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class AccountManager {

    private final static Logger logger = LoggerFactory.getLogger(CharacterManager.class);
    
    private AccountManager() {
        throw new AssertionError();
    }
    
    public static boolean getCharactersForSelection(){
        Connection connection = null;
        List<GameCharacter> gameCharacterList;
        
        try{
            connection = ServerNode.getConnectionFactory().openConnection();
            
            gameCharacterList = AccountManagerDao.getCharactersForSelection(connection);
            for(GameCharacter gameCharacter : gameCharacterList){
                Equipment currentEquipment = EquipmentManager.loadCharacterEquipment(
                    connection, 
                    gameCharacter.getCharacterIdx()
                );
                
                if(currentEquipment == null){
                    throw new DataAccessException("Não foi possível carregar o equipamento do personagem: " + gameCharacter);
                }                
                gameCharacter.setEquipment(currentEquipment);
            } 
            
            WorldSession.getCurrentWorldSession()
                    .setGameCharacterList(gameCharacterList);
            
            return true;
        }catch(Throwable t){
            logger.error("Erro carregando todos os personagens", t);
        }finally{            
            JDBCUtil.closeConnection(connection);
        }
        
        return false;
    }
    
}
