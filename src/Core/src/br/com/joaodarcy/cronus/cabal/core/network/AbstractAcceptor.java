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

package br.com.joaodarcy.cronus.cabal.core.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public abstract class AbstractAcceptor implements Acceptor {
    protected final int port;
    private final Logger logger;
    protected boolean running = false;    
    
    public AbstractAcceptor(int port) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.port = port;
    }
    
    protected ServerSocket createServerSocket(){                        
        ServerSocket serverSocket = null;
        
        logger.debug("Creating ServerSocket on port {}", port);
        
        try{
            serverSocket = new ServerSocket(port);            
        }catch(IOException e){
            logger.error("Error trying to listen on port {}", port, e);
        }
                
        return serverSocket;
    }
    
    @Override
    public void start() {
        if(running){
            logger.error("Acceptor allready started");
            return;
        }
        
        logger.info("Starting acceptor");
        final ServerSocket serverSocket;
        
        synchronized(this){            
            serverSocket = createServerSocket();        
            if(serverSocket == null){
                running = false;
            }else{
                running = true;
                logger.info("Accepting connections on port {}", port);
            }
        }
        
        while(running){                        
            try{
                final Socket clientConnection = serverSocket.accept();
                
                if(clientConnection == null){
                    throw new IOException("Null socket ??? WTF ????");
                }
                
                logger.info("New connection accepted {}", clientConnection.getRemoteSocketAddress());                
                onAccept(clientConnection);
                
            }catch(IOException e){
                logger.error("Error accepting connection: {}", e.getMessage(), e);
            }
        }                
    }

    @Override
    public void stop() {
        if(running){
            logger.info("Stopping acceptor");
            running = false;
        }else{
            logger.warn(("Acceptor allready stopped !"));
        }
    }
    
}
