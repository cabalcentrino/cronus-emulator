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

import br.com.joaodarcy.cronus.cabal.core.crypt.Cryptation;
import br.com.joaodarcy.cronus.cabal.core.crypt.Key;
import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import br.com.joaodarcy.cronus.cabal.core.network.PacketReader;
import br.com.joaodarcy.cronus.cabal.core.network.EP8ClientFullPacketReader;
import java.io.InputStream;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class ChatPacketReader implements PacketReader {
    private final PacketReader wrappedPacketReader;

    public ChatPacketReader(PacketReader wrappedPacketReader) {
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
