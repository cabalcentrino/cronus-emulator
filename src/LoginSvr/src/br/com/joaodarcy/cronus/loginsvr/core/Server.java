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

import java.util.ArrayList;
import java.util.List;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class Server {
    private final byte id;
    private final byte flag;
    private final int unk;

    private final List<Channel> channelList;
    
    public Server(byte id, byte flag, int unk) {
        this.id = id;
        this.flag = flag;
        this.unk = unk;
        this.channelList = new ArrayList<Channel>(14);
    }

    public void add(Channel channel){
        channelList.add(channel);
    }
    
    public List<Channel> getChannelList() {
        return channelList;
    }
    
    public byte getId() {
        return id;
    }

    public byte getFlag() {
        return flag;
    }

    public int getUnk() {
        return unk;
    }    
}
