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

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public interface ServerChannel {
    
    final static byte FLAG_NONE                = 0x00;
    final static byte FLAG_PLAYER_KILL         = 0x01;
    final static byte FLAG_UNK                 = 0x02;
    final static byte FLAG_PREMIUM             = 0x04;
    final static byte FLAG_WAR                 = 0x08;
    
    final static byte NORMAL_GREEN = FLAG_NONE;
    final static byte NORMAL_WHITE = FLAG_PLAYER_KILL;
    
    final static byte PREMIUM_GREEN = FLAG_PREMIUM;
    final static byte PREMIUM_WHITE = FLAG_PREMIUM | FLAG_PLAYER_KILL;
    
    final static byte NORMAL_WAR_GREEN = NORMAL_GREEN | FLAG_WAR;
    final static byte NORMAL_WAR_WHITE = NORMAL_WHITE | FLAG_WAR;
    
    final static byte PREMIUM_WAR_GREEN = PREMIUM_GREEN | FLAG_WAR;
    final static byte PREMIUM_WAR_WHITE = PREMIUM_WHITE | FLAG_WAR;        
            
    byte getId();
    int getFlags();    
    
    short getPort();
    short getLoad();
    short getCapacity();
    
    boolean isPlayerKillChannel();    
    boolean isPremiumChannel();
    boolean isWarChannel();   

}
