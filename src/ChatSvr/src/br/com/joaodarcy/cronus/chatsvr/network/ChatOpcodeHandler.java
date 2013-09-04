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

package br.com.joaodarcy.cronus.chatsvr.network;

import br.com.joaodarcy.cronus.chatsvr.ChatNode;
import br.com.joaodarcy.cronus.chatsvr.core.ChatSession;
import br.com.joaodarcy.cronus.chatsvr.core.SessionState;
import br.com.joaodarcy.cronus.cabal.core.AbstractOpcodeHandler;
import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import br.com.joaodarcy.cronus.cabal.core.network.PacketBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public abstract class ChatOpcodeHandler extends AbstractOpcodeHandler {
    protected final SessionState requiredSessionState;
    protected final Logger logger;
    
    public ChatOpcodeHandler(short opcodeHandled, SessionState requiredClientState) {
        super(opcodeHandled);
        this.requiredSessionState = requiredClientState;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    protected void onIncorrectState(){
        logger.error("Cannot handle packet incorret state, required {}, current {}", requiredSessionState, getCurrentSessionState());
    }
    
    @Override
    protected boolean canHandle(Packet value) {
        if(super.canHandle(value)){
            if(requiredSessionState == getCurrentSessionState()){
                return true;
            }else{
                onIncorrectState();
            }
        }
        return false;
    }
    
    protected Packet makeBooleanPacket(boolean success){
        PacketBuilder builder = getPacketBuilder();
        
        builder.putByte((byte)(success ? 0x01 : 00));
        
        return builder.build();
    }
            
    protected PacketBuilder getPacketBuilder(){
        return getPacketBuilder(opcodeHandled);
    }
        
    protected static PacketBuilder getPacketBuilder(int opcode){
        return getPacketBuilder((short)(opcode & 0xFFFF));
    }
    
    protected static PacketBuilder getPacketBuilder(short opcode){
        return ChatNode.getPacketBuilderFactory().create(opcode);
    }
    
    protected static void changeSessionState(SessionState newSessionState){
        getCurrentSession().changeSessionState(newSessionState);
    }
    
    protected static SessionState getCurrentSessionState(){
        return getCurrentSession().getSessionState();
    }
    
    protected static ChatSession getCurrentSession(){
        return ChatSession.getCurrentChatSession();
    }
}
