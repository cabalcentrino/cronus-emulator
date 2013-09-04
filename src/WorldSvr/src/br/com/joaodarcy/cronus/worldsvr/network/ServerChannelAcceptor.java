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

package br.com.joaodarcy.cronus.worldsvr.network;

import br.com.joaodarcy.cronus.cabal.core.network.AbstractAcceptor;
import br.com.joaodarcy.cronus.worldsvr.channel.Channel;
import br.com.joaodarcy.cronus.worldsvr.core.SessionManager;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class ServerChannelAcceptor extends AbstractAcceptor implements Runnable {        
    private final ExecutorService executorService;    
    private final Channel channel;
    
    public ServerChannelAcceptor(Channel channel) {
        super(channel.getPort().intValue());
        this.channel = channel;
        this.executorService = Executors.newFixedThreadPool(channel.getCapacity());
    }    
    
    private void drop(Socket connection){
        if(connection != null){
            try{
                connection.close();
            }catch(Throwable t){
                
            }
        }
    }
    
    @Override
    public void onAccept(Socket connectedSocket) {
        synchronized(this){
            if(channel.isFull()){
                drop(connectedSocket);
                return;
            }

            executorService.submit(
                SessionManager.newSession(            
                    channel,
                    connectedSocket,
                    1l // TODO: Read account id
                )
            );             
        }
    }    

    @Override
    public void stop() {
        super.stop();
        executorService.shutdown();        
    }

    @Override
    public void run() {
        start();
    }
    
}
