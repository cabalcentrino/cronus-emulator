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

package br.com.joaodarcy.cronus.worldsvr.network.handlers;

import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import br.com.joaodarcy.cronus.cabal.core.network.PacketBuilder;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.cronus.worldsvr.account.AccountManager;
import br.com.joaodarcy.cronus.worldsvr.character.CharacterManager;
import br.com.joaodarcy.cronus.worldsvr.character.GameCharacter;
import br.com.joaodarcy.cronus.worldsvr.core.ClientState;
import br.com.joaodarcy.cronus.worldsvr.core.Item;
import br.com.joaodarcy.cronus.worldsvr.core.WorldSession;
import br.com.joaodarcy.cronus.worldsvr.network.ServerOpcodeHandler;
import java.util.List;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class CharacterGetMyCharactersHandler extends ServerOpcodeHandler {

    public CharacterGetMyCharactersHandler() {
        super((short)0x0085, ClientState.HANDSHAKED);
    }    
    
    @Override
    protected Boolean handleValue(Packet value) {
        WorldSession session = getCurrentSession();
        session.changeState(ClientState.CHARACTER_SELECTION);
        
        if(!session.isGameCharacterInCache()){
            if(!AccountManager.getCharactersForSelection()){                            
                logger.error("Desconectando o cliente pois não foi possível carregar a sua lista de personagens.");
                session.disconnectClient();
                return true;
            }
        }
                        
        session.sendPacket(
            makeGetMyCharactersPacketResponse(session.getGameCharacterList())
        );
        
        return true;
    }

    private Packet makeGetMyCharactersPacketResponse(List<GameCharacter> gameCharacterList){
        PacketBuilder builder = getPacketBuilder();
               
        builder.putByte((byte)0x01);
        for(int i = 0 ; i < 12 ; i++){
            builder.putByte((byte)0x00);
        }
        
        builder.putByte((byte)0x01);
        for(int i = 0 ; i < 8 ; i++){
            builder.putByte((byte)0x00);
        }
                
        //builder.putByte((byte)0x00); // Skip
        for(GameCharacter player : gameCharacterList){
                        
/*
0000 E2 B7 19 00 00 00 00 00 86 00 0B 80 02 04 00 00         .ﾷ......ﾆ..ﾀ.... 
0010 08 42 75 73 68 69 6E 4D 41                                .BushinMA
 */            
            
            if(player == null){
                continue;
            }
            
            builder.putByteArray(player.getCharacterIdx().getBytes());            
            builder.put(UInt32.valueOf(0x5040f83Bl)); // Unk Int Timestamp ????
            
            builder.putIntLE(0);
            
            builder.put(player.getStyle());
            builder.put(player.getLevel());           
            
            builder.put(player.getSwordRank());
            builder.put(player.getMagicRank());
            
            builder.putShortLE((short)0x0000); // FIXME 
            
            builder.put(player.getAlz());
            
            builder.putIntLE(0x00000000); // FIXME  
            
            builder.put(player.getNation());
            builder.put(player.getWorld());
            builder.put(player.getY());
            builder.put(player.getX());

            // Equipamentos
            for(Item entry : player.getEquipment().getArray()){
                if(entry == null){
                    builder.putIntLE(0x00000000);
                }else{
                    builder.put(entry.getId());
                }
            }
                        
            builder.putByte((byte)(player.getName().length() + 1));
            builder.putString(player.getName());
            builder.putByte((byte)0x00); // Finalização da linha
        }
        
        return builder.build();
    }            
}
