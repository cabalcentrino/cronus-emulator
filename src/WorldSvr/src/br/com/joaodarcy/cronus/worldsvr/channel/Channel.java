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

package br.com.joaodarcy.cronus.worldsvr.channel;

import br.com.joaodarcy.cronus.cabal.core.types.UInt16;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.cronus.cabal.core.types.UInt8;
import br.com.joaodarcy.cronus.worldsvr.core.Player;
import br.com.joaodarcy.cronus.worldsvr.core.Updatable;
import br.com.joaodarcy.cronus.worldsvr.core.WorldSession;
import br.com.joaodarcy.cronus.worldsvr.network.ServerChannelAcceptor;
import br.com.joaodarcy.cronus.worldsvr.world.World;
import br.com.joaodarcy.cronus.worldsvr.world.WorldManager;
import java.util.ArrayList;
import java.util.List;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class Channel implements Updatable {
    private final UInt8 id; 
    private final int capacity;
    private final UInt16 port;
    private final UInt32 type;
    
    private final List<Player> onlyPlayers;
    private final List<World> worldList;
    
    private final ServerChannelAcceptor acceptor;

    public Channel(UInt8 id, UInt16 capacity, UInt16 port, UInt32 type) {
        this.id = id;
        this.capacity = capacity.intValue();
        this.port = port;
        this.type = type;
        
        this.acceptor = new ServerChannelAcceptor(this);
        this.onlyPlayers = new ArrayList<>(capacity.intValue());
        this.worldList = WorldManager.getWorlds();                        
    }

    public synchronized boolean isFull(){
        return onlyPlayers.size() >= capacity;
    }
    
    public static World getWorldById(UInt8 worldId){
        for(World currentWorld : WorldSession.getCurrentWorldSession().getChannel().worldList){
            if(currentWorld.getId().equals(worldId)){
                return currentWorld;
            }
        }
        return null;
    }
            
    public ServerChannelAcceptor getAcceptor() {
        return acceptor;
    }
    
    public UInt8 getId() {
        return id;
    }

    public int getCapacity() {
        return capacity;
    }

    public UInt16 getPort() {
        return port;
    }

    public UInt32 getType() {
        return type;
    }
    
    @Override
    public void update(long timeDiff){       
        for(World world : worldList){
            world.update(timeDiff);
        }
    }
    
    static Channel valueOf(ChannelTemplate template){        
        return new Channel(template.getId(), template.getCapacity(), template.getPort(), template.getType());
    }    
}
