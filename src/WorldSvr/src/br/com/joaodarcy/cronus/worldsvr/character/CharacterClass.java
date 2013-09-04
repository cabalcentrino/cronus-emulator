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

package br.com.joaodarcy.cronus.worldsvr.character;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public enum CharacterClass {
    ALL                                                             ((byte)0x00),
    WARRIOR                                                         ((byte)0x01),
    BLADER                                                          ((byte)0x02),
    WIZARD                                                          ((byte)0x03),
    FORCE_ARCHER                                                    ((byte)0x04),
    FORCE_SHIELDER                                                  ((byte)0x05),
    FORCE_BLADER                                                    ((byte)0x06);
    
    private final byte byteValue;

    private CharacterClass(byte byteValue) {
        this.byteValue = byteValue;
    }

    public byte getByteValue() {
        return byteValue;
    }    
}
