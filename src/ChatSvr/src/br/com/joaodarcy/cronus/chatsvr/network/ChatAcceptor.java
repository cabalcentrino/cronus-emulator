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

import br.com.joaodarcy.cronus.chatsvr.core.ChatSession;
import br.com.joaodarcy.cronus.cabal.core.network.AbstractAcceptor;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class ChatAcceptor extends AbstractAcceptor {    
    private final ExecutorService executorService;    
    //private final Logger logger;
    
    public ChatAcceptor(int port) {
        super(port);
        this.executorService = Executors.newCachedThreadPool();
        //this.logger = LoggerFactory.getLogger(ChatAcceptor.class);
    }    
    
    @Override
    public void onAccept(Socket connectedSocket) {
        executorService.submit(new ChatSession(connectedSocket));             
    }    

    @Override
    public void stop() {
        super.stop();
        executorService.shutdown();        
    }        
}
