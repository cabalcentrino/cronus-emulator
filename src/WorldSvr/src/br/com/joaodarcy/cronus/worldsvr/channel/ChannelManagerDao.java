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
import java.util.ArrayList;
import java.util.List;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class ChannelManagerDao {

    private ChannelManagerDao() {
        throw new AssertionError();
    }
    
    // TODO: Load from database, the channel info
    static List<ChannelTemplate> getServerChannelTemplate(byte serverId){
        List<ChannelTemplate> channelTemplateList = new ArrayList<>(1);

        ChannelTemplate channelTemplate = new ChannelTemplate();
        channelTemplate.setId(UInt8.valueOf((byte)1));
        channelTemplate.setPort(UInt16.valueOf(38111));
        channelTemplate.setCapacity(UInt16.valueOf(10));
        channelTemplate.setType(UInt32.ZERO);

        channelTemplateList.add(channelTemplate);
        
        return channelTemplateList;
    }
    
}
