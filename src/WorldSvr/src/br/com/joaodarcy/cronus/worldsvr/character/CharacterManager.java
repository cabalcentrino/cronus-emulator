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

package br.com.joaodarcy.cronus.worldsvr.character;

import br.com.joaodarcy.cronus.worldsvr.core.CharacterIdx;
import br.com.joaodarcy.cronus.worldsvr.core.Style;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class CharacterManager {
        
    private final static Logger logger = LoggerFactory.getLogger(CharacterManager.class);

    public final static byte CHARACTER_MIN_NAME_LENGHT = 1;
    public final static byte CHARACTER_MAX_NAME_LENGHT = 16;
    
    private CharacterManager() {
        throw new AssertionError();
    }                                     
    
    // TODO: Make character join on beginners guild
    // TODO: Check styles is not out of range (Eg. premium hair or invalid values )
    public static short createNewCharacter(CharacterIdx idx, Style style, String name, boolean joinBeginnerGuild){        
        try{                        
            Short response;
            response = CharacterManagerDao.createNewCharacter(idx, style, name);
                        
            if(response == null){
                logger.error("Cannot create a new character, null response");
                return 1;
            }else if(response <= (short)0xA0){
                logger.error("Cannot create a new character, response: {}", response);
            }
            return (short)response;
        }catch(Throwable t){
            logger.error("Unknow error creating character: {}", t.getMessage(), t);
        }        
        return 1;                        
    }        
}
