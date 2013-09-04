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

package br.com.joaodarcy.cronus.chatsvr.core;

import br.com.joaodarcy.cronus.chatsvr.ChatNode;
import br.com.joaodarcy.cronus.cabal.core.ClientSession;
import java.net.Socket;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class ChatSession extends ClientSession {
    private SessionState sessionState;
        
    public ChatSession(Socket socket) {
        super(ChatNode.getPacketHandler(), ChatNode.getPacketReader(), socket, ChatNode.getCryptation(), ChatNode.getKeyFactory().create());        
        this.sessionState = SessionState.CONNECTED;
    }
    
    public static ChatSession getCurrentChatSession(){
        return (ChatSession) getCurrentSession();
    }
    
    public synchronized void changeSessionState(SessionState newSessionState){
        this.sessionState = newSessionState;        
    }
    
    public SessionState getSessionState(){
        return sessionState;
    }
}
