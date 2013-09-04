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

package br.com.joaodarcy.cronus.loginsvr.core;

import br.com.joaodarcy.cronus.cabal.core.ClientSession;
import br.com.joaodarcy.cronus.cabal.core.PacketHandler;
import br.com.joaodarcy.cronus.cabal.core.crypt.Cryptation;
import br.com.joaodarcy.cronus.cabal.core.crypt.Key;
import br.com.joaodarcy.cronus.cabal.core.network.PacketReader;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class AuthClientSession extends ClientSession {
    
    private final static AtomicInteger ATOMIC_INTEGER;
    
    static{
        ATOMIC_INTEGER = new AtomicInteger(1);
    }
    
    private final int sessionId;
    private AuthState state;
    private final Logger logger;
    
    public AuthClientSession(PacketHandler packetHandler, PacketReader reader, Socket socket, Cryptation cryptation, Key clientKey) {
        super(packetHandler, reader, socket, cryptation, clientKey);
        this.sessionId = ATOMIC_INTEGER.getAndIncrement();
        this.state = AuthState.CONNECTED;        
        this.logger = LoggerFactory.getLogger(AuthClientSession.class);
    }

    public void setState(AuthState state) {
        synchronized(state){
            logger.info("[" + sessionId + "] State transition {} -> {}", this.state, state);
            this.state = state; 
        }
    }

    public AuthState getState() {
        return state;
    }   

    public int getSessionId() {
        return sessionId;
    }    
}
