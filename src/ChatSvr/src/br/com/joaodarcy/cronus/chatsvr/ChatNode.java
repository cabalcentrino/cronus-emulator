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

package br.com.joaodarcy.cronus.chatsvr;

import br.com.joaodarcy.cronus.chatsvr.network.ChatAcceptor;
import br.com.joaodarcy.cronus.chatsvr.network.ChatPacketReader;
import br.com.joaodarcy.cronus.chatsvr.network.handler.ChatPacketHandler;
import br.com.joaodarcy.cronus.cabal.core.PacketHandler;
import br.com.joaodarcy.cronus.cabal.core.crypt.Cryptation;
import br.com.joaodarcy.cronus.cabal.core.crypt.EP8CryptationFactory;
import br.com.joaodarcy.cronus.cabal.core.crypt.EP8KeyFactory;
import br.com.joaodarcy.cronus.cabal.core.crypt.KeyFactory;
import br.com.joaodarcy.cronus.cabal.core.network.EP8ClientHeaderReader;
import br.com.joaodarcy.cronus.cabal.core.network.EP8ClientPacketReader;
import br.com.joaodarcy.cronus.cabal.core.network.EP8PacketBuilderFactory;
import br.com.joaodarcy.cronus.cabal.core.network.HeaderReader;
import br.com.joaodarcy.cronus.cabal.core.network.PacketBuilderFactory;
import br.com.joaodarcy.cronus.cabal.core.network.PacketReader;
import br.com.joaodarcy.cronus.cabal.core.patterns.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class ChatNode {
    private final static PacketBuilderFactory PACKET_BUILDER_FACTORY;
    private final static KeyFactory KEY_FACTORY;
    private final static PacketHandler PACKET_HANDLER;
    private final static PacketReader PACKET_READER;
    private final static HeaderReader HEADER_READER;
    private final static Factory<Cryptation> CRYPTATION_FACTORY;
    private final static Cryptation CRYPTATION;
    
    static {                    
        final Logger logger = LoggerFactory.getLogger(ChatNode.class);                        
        
        PACKET_HANDLER = new ChatPacketHandler();
        PACKET_BUILDER_FACTORY = new EP8PacketBuilderFactory();
        KEY_FACTORY = new EP8KeyFactory();
        HEADER_READER = new EP8ClientHeaderReader();
        PACKET_READER = new ChatPacketReader(
            new EP8ClientPacketReader(HEADER_READER)
        );
        CRYPTATION_FACTORY = new EP8CryptationFactory();
        CRYPTATION = CRYPTATION_FACTORY.create();        
    }
    
    private ChatNode() {
        throw new AssertionError();
    }
   
    public static Cryptation getCryptation(){
        return CRYPTATION;
    }
    
    public static PacketReader getPacketReader(){
        return PACKET_READER;
    }
    
    public static PacketHandler getPacketHandler(){
        return PACKET_HANDLER;
    }
    
    public static PacketBuilderFactory getPacketBuilderFactory(){
        return PACKET_BUILDER_FACTORY;
    }
    
    public static KeyFactory getKeyFactory(){
        return KEY_FACTORY;
    }
        
    static void start(){
        final ChatAcceptor chatAcceptor = new ChatAcceptor(38122);
            
        chatAcceptor.start();
        chatAcceptor.stop();
    }    
            
    public static void main(String args[]){                  
        ChatNode.start();             
    }
    
}
