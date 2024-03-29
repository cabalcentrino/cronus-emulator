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

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class Header {
    private final short signature;
    private final short size;   
    private final byte data[];
    
    public Header(short signature, short size, byte data[]) {
        this.signature = signature;
        this.size = size;
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
    
    public short getSignature() {
        return signature;
    }

    public short getSize() {
        return size;
    }
}
