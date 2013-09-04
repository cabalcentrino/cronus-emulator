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
import br.com.joaodarcy.npersistence.core.Column;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class ChannelTemplate {
    @Column("ChannelId")
    private UInt8 id; 
    @Column("ChannelCapacity")
    private UInt16 capacity;
    @Column("ChannelPort")
    private UInt16 port;
    @Column("ChannelType")
    private UInt32 type;

    public UInt8 getId() {
        return id;
    }

    public UInt16 getCapacity() {
        return capacity;
    }

    public UInt16 getPort() {
        return port;
    }

    public UInt32 getType() {
        return type;
    }  

    void setId(UInt8 id) {
        this.id = id;
    }

    void setCapacity(UInt16 capacity) {
        this.capacity = capacity;
    }

    void setPort(UInt16 port) {
        this.port = port;
    }

    void setType(UInt32 type) {
        this.type = type;
    }    
}
