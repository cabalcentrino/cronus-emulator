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

import br.com.joaodarcy.cronus.loginsvr.AuthNode;
import br.com.joaodarcy.cronus.loginsvr.core.AuthClientSession;
import br.com.joaodarcy.cronus.loginsvr.network.handler.CabalEP8Handler;
import br.com.joaodarcy.cronus.cabal.core.PacketHandler;
import br.com.joaodarcy.cronus.cabal.core.crypt.Cryptation;
import br.com.joaodarcy.cronus.cabal.core.crypt.EP8CryptationFactory;
import br.com.joaodarcy.cronus.cabal.core.crypt.Key;
import br.com.joaodarcy.cronus.cabal.core.network.*;
import java.io.InputStream;
import java.net.Socket;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class AuthAcceptor extends AbstractAcceptor {
    private final PacketHandler packetHandler;
    private final PacketReader packetReader;
    private final Cryptation cryptation;
    
    private class PacketReaderTemp implements PacketReader {
        private final PacketReader wrappedPacketReader;

        public PacketReaderTemp(PacketReader wrappedPacketReader) {
            this.wrappedPacketReader = wrappedPacketReader;
        }

        @Override
        public Packet read(InputStream inputStream, Cryptation crypt, Key clientKey) {
            return wrappedPacketReader.read(
                EP8ClientFullPacketReader.read(inputStream), 
                crypt, 
                clientKey
            );
        }        
        
    }
    
    public AuthAcceptor(int port) {
        super(port);
        this.packetHandler = new CabalEP8Handler();
        this.cryptation = new EP8CryptationFactory().create();
        this.packetReader = new PacketReaderTemp(
            new EP8ClientPacketReader(
                new EP8ClientHeaderReader()
            )
        );
    }
    
    @Override
    public void onAccept(Socket connecSocket) {
        String threadName = String.format(
            "%s:%d", 
            connecSocket.getInetAddress().getHostAddress(),
            connecSocket.getPort()
        );
        
        new Thread(            
            new AuthClientSession(
                packetHandler, 
                packetReader, 
                connecSocket, 
                cryptation, 
                AuthNode.getKeyFactory().create()
            ),
            threadName
        ).start();
    }

}
