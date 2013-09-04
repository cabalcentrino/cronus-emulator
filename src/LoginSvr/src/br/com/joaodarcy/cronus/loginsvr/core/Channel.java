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

package br.com.joaodarcy.cronus.loginsvr.core;

import br.com.joaodarcy.cronus.loginsvr.Configuration;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class Channel {
    private final byte id;
    private short latency;
    private int unk1;
    private int unk2;
    private int unk3;
    private int unk4;
    private int unk5;
    private short unk6;
    private short capacity;
    private UInt32 ip;
    private int port;
    private int type;

    public Channel(byte id, short latency, short capacity, int type) {
        this.id = id;
        this.latency = latency;
        this.capacity = capacity;
        this.type = type;
        this.ip = UInt32.fromIpAddress(Configuration.channelIp());
        this.port = Configuration.channelPort();
        this.unk1 = this.unk2 = this.unk3 = this.unk4 = this.unk5;
        this.unk6 = (short)0xFF00;
    }

    public byte getId() {
        return id;
    }

    public short getLatency() {
        return latency;
    }

    public int getUnk1() {
        return unk1;
    }

    public int getUnk2() {
        return unk2;
    }

    public int getUnk3() {
        return unk3;
    }

    public int getUnk4() {
        return unk4;
    }

    public int getUnk5() {
        return unk5;
    }

    public short getUnk6() {
        return unk6;
    }

    public short getCapacity() {
        return capacity;
    }

    public UInt32 getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public int getType() {
        return type;
    }    
}
