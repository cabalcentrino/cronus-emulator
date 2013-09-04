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

package br.com.joaodarcy.cronus.cabal.core;

import br.com.joaodarcy.cronus.cabal.core.crypt.Cryptation;
import br.com.joaodarcy.cronus.cabal.core.crypt.Key;
import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import br.com.joaodarcy.cronus.cabal.core.network.PacketReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class ClientSession implements Runnable {
    protected final PacketHandler packetHandler;
    protected final PacketReader reader;
    protected final Cryptation cryptation;    
    protected final Socket socket;    
    
    protected Key clientKey;
    protected boolean active;                
    
    private final static ThreadLocal<ClientSession> CONTEXT_HOLDER;
    
    private final static Logger logger = LoggerFactory.getLogger(ClientSession.class);
    
    static{
        CONTEXT_HOLDER = new ThreadLocal();
    }

    public ClientSession(PacketHandler packetHandler, PacketReader reader, Socket socket, Cryptation cryptation, Key clientKey) {
        this.packetHandler = packetHandler;
        this.cryptation = cryptation;
        this.clientKey = clientKey;
        this.reader = reader;
        this.socket = socket;        
        
        this.active = false;
    }
    
    public synchronized void changeClientKey(Key newClientKey){
        this.clientKey = newClientKey;
    }
    
    public synchronized void disconnectClient(){
        if(this.active){
            this.active = false;
            try{
                this.socket.close();
            }catch(Throwable t){
                logger.warn("Error closing client connection: {}", t.getMessage(), t);                
            }
        }
    }
    
    public static ClientSession getCurrentSession(){
        return CONTEXT_HOLDER.get();
    }
    
    public static void disconnect(){
        ClientSession session = getCurrentSession();
        if(session == null){
            logger.warn("Cannot disconnect a null session o.O");
        }else{
            session.disconnectClient();
        }
    }
    
    protected static void registerClientSession(ClientSession session){
        synchronized(CONTEXT_HOLDER){
            CONTEXT_HOLDER.set(session);
        }
    }
    
    protected static void unregisterClientSession(ClientSession session){
        synchronized(CONTEXT_HOLDER){
            CONTEXT_HOLDER.remove();
        }
    }
    
    @Override
    public void run() {
        registerClientSession(this);
        
        active = true;
        
        onSessionStart();
        
        while(active){
            try{
                final InputStream socketInputStream = socket.getInputStream();
                final Packet packet = reader.read(socketInputStream, cryptation, clientKey);
                if(packet == null){
                    throw new Throwable("null packet received !");
                }else{
                    packetHandler.handle(packet);
                }                
            }catch(Throwable t){
                logger.error("Error on packet reading loop: {}", t.getMessage(), t);                
                disconnectClient();
            }
        }        
                
        unregisterClientSession(this);        
        onSessionStop();
    }
    
    public boolean sendPacket(Packet packet){
        try{
            logger.debug("Sending packet to client: {}", packet.toByteString());
            
            // Encrypt packet
            cryptation.encrypt(packet.getData(), clientKey);
            
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(packet.getData());
            outputStream.flush();
            return true;
        }catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean send(Packet packet){
        ClientSession currentSession = getCurrentSession();
        if(currentSession == null){
            return false;
        }
        return currentSession.sendPacket(packet);
    }
    
    protected void onSessionStart(){
        
    }
    
    protected void onSessionStop(){
        
    }    
}
