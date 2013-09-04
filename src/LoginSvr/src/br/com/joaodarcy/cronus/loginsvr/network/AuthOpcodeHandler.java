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

package br.com.joaodarcy.cronus.loginsvr.network;

import br.com.joaodarcy.cronus.loginsvr.core.AuthClientSession;
import br.com.joaodarcy.cronus.loginsvr.core.AuthState;
import br.com.joaodarcy.cronus.cabal.core.AbstractOpcodeHandler;
import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public abstract class AuthOpcodeHandler extends AbstractOpcodeHandler {
    protected final AuthState requiredState;
    private final Logger logger;

    public AuthOpcodeHandler(short opcodeHandled, AuthState requiredState) {
        super(opcodeHandled);
        this.requiredState = requiredState;
        this.logger = LoggerFactory.getLogger(AuthOpcodeHandler.class);
    }
    
    protected void onInvalidState(){
        logger.warn("Current client state {} required state {}, ignoring request.", getCurrentSession().getState(), requiredState);
    }
    
    @Override
    protected boolean canHandle(Packet value) {
        if(super.canHandle(value)){
            // Check if state is correct
            if(getCurrentSession().getState() == requiredState){
                return true;
            }else{
                onInvalidState();
            }
        }
        return false;
    }        
        
    protected AuthClientSession getCurrentSession(){
        return (AuthClientSession)AuthClientSession.getCurrentSession();
    }
    
}
