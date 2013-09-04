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

package br.com.joaodarcy.cronus.loginsvr;

import br.com.joaodarcy.cronus.loginsvr.network.AuthAcceptor;
import br.com.joaodarcy.cronus.cabal.core.crypt.EP8KeyFactory;
import br.com.joaodarcy.cronus.cabal.core.crypt.KeyFactory;
import br.com.joaodarcy.cronus.cabal.core.network.Acceptor;
import br.com.joaodarcy.cronus.cabal.core.network.EP8PacketBuilderFactory;
import br.com.joaodarcy.cronus.cabal.core.network.PacketBuilderFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class AuthNode {
    private final static PacketBuilderFactory PACKET_BUILDER_FACTORY;
    private final static KeyFactory KEY_FACTORY;
    
    static {
        PACKET_BUILDER_FACTORY = new EP8PacketBuilderFactory();
        KEY_FACTORY = new EP8KeyFactory();        
    }

    private AuthNode() {
        throw new AssertionError();
    }
    
    public static PacketBuilderFactory getPacketBuilderFactory(){
        return PACKET_BUILDER_FACTORY;
    }
    
    public static KeyFactory getKeyFactory(){
        return KEY_FACTORY;
    }
    
    static void start(){
        final Acceptor acceptor = new AuthAcceptor(Configuration.listenPort());
                        
        Runtime.getRuntime().addShutdownHook(
            new Thread(new Runnable() {
                @Override
                public void run() {
                    acceptor.stop();
                }
            })
        );
        
        acceptor.start();
    }    
            
    public static void main(String args[]){
        AuthNode.start();                
    }
    
}
