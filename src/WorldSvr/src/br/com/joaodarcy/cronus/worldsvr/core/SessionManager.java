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

import br.com.joaodarcy.cronus.worldsvr.channel.Channel;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class SessionManager {    
    private final static List<WorldSession> activeSessionList;
    private final static List<WorldSession> umodifiableActiveSessionList;
    
    static{
        activeSessionList = new ArrayList<>();
        umodifiableActiveSessionList = Collections.unmodifiableList(activeSessionList);
    }

    private SessionManager() {
        throw new AssertionError();
    }
    
    public static List<WorldSession> getActiveSessionList() {        
        return umodifiableActiveSessionList;
    }
    
    static void onSessionStart(WorldSession serverClientSession){
        if(serverClientSession != null){
            synchronized(activeSessionList){
                activeSessionList.add(serverClientSession);
            }
        }
    }
    
    static void onSessionStop(WorldSession serverClientSession){
        if(serverClientSession != null){
            synchronized(activeSessionList){
                activeSessionList.remove(serverClientSession);
            }
        }
    }
    
    public static WorldSession newSession(Channel channel, Socket socket, long accountId){
        return new WorldSession(channel, socket, accountId);
    }            
}
