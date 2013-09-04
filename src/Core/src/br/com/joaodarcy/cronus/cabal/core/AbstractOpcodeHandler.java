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

package br.com.joaodarcy.cronus.cabal.core;

import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import br.com.joaodarcy.cronus.cabal.core.patterns.AbstractChain;
import br.com.joaodarcy.cronus.cabal.core.patterns.Chain;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public abstract class AbstractOpcodeHandler extends AbstractChain<Boolean, Packet> {
    protected short opcodeHandled;

    public AbstractOpcodeHandler(short opcodeHandled) {
        this.opcodeHandled = opcodeHandled;
    }
    
    @Override
    protected boolean canHandle(Packet value) {
        if(value == null){
            return false;
        }
        return value.getOpcode() == opcodeHandled;
    }    

    @Override
    protected Boolean endOfChain() {
        return Boolean.FALSE;
    }

    @Override
    public int compareTo(Chain<Boolean, Packet> t) {
        return Short.valueOf(opcodeHandled).compareTo(((AbstractOpcodeHandler)t).opcodeHandled);
    }
    
}
