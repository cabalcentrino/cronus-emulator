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

import br.com.joaodarcy.cronus.worldsvr.AbstractDao;
import br.com.joaodarcy.cronus.worldsvr.core.CharacterIdx;
import br.com.joaodarcy.cronus.worldsvr.core.Style;

/**
 * Objeto que realiza as operações de acesso a dados para o character manager
 * 
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class CharacterManagerDao extends AbstractDao {

    private final static String SQL_CHARACTER_CREATE_NEW = getQueryByName("character.createNew");
    
    private CharacterManagerDao() {
        throw new AssertionError();
    }
           
    static Short createNewCharacter(CharacterIdx characterIdx, Style estilo, String nome){        
        return getDataAccess().executeDQLTypeSelectSingle(
            null,
            SQL_CHARACTER_CREATE_NEW, 
            Short.class,
            characterIdx.getCharacterId(), 
            getServerId(),
            characterIdx.getAccountId(), 
            estilo.getCharacterClass().getByteValue(), 
            estilo.getStyle().longValue(),
            nome
        );
    }
    
}
