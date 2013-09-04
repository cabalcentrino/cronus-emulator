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
import br.com.joaodarcy.cronus.worldsvr.character.CharacterManager;
import br.com.joaodarcy.cronus.worldsvr.core.CharacterIdx;
import br.com.joaodarcy.cronus.worldsvr.core.ClientState;
import br.com.joaodarcy.cronus.worldsvr.core.WorldSession;
import br.com.joaodarcy.cronus.worldsvr.core.Style;
import br.com.joaodarcy.cronus.worldsvr.network.ServerOpcodeHandler;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class CharacterCreateHandler extends ServerOpcodeHandler {
    
    // Style ( Little Endian )
    // byte  class;                                                             // 09 GU / 0A DU / 0B MA / 0C AA / 0D GA / 0E EA
    // byte  face;                                                              // Min: 00, Max: 04
    // byte  hairStyle;                                                         // Min: 00, Max: 0C ??????
    // byte  sex;                                                               // 00 = Male , 04 = Female
    
    public CharacterCreateHandler() {
        super((short)0x0086, ClientState.CHARACTER_SELECTION);
    }
                                
    // Struct ( Client )
    // int style
    // bool joinBegginer guild
    // byte slot                                                                // 0x00 - 0x05
    // byte nameLen
    // char name[nameLen]    
    
    @Override
    protected Boolean handleValue(Packet packet) {
        WorldSession session = getCurrentSession();
        
        try{
            Style estilo = new Style(packet.getInt());            
            boolean entrarNaGuildIniciante = packet.getBoolean();
            CharacterIdx idx = makeCharacterIdx(session, packet.getByte());            
            byte tamanhoNome = packet.getByte();
            
            if(tamanhoNome < CharacterManager.CHARACTER_MIN_NAME_LENGHT || tamanhoNome > CharacterManager.CHARACTER_MAX_NAME_LENGHT){
                throw new IllegalArgumentException("O tamanho mínimo e máximo do nome do personagem foi ultrapassado, possível tentativa de HACK, tamanho informado: " + tamanhoNome);
            }
                
            String nome = packet.getString(tamanhoNome);
            short response = CharacterManager.createNewCharacter(
                idx, 
                estilo, 
                nome, 
                entrarNaGuildIniciante
            );

            session.sendPacket(makeCharacterCreationgResponse(idx, response));
                       
        }catch(Throwable t){
            logger.error("Erro lendo pacote de criação de novo personagem, o cliente será desconectado", t);
            session.disconnectClient();
        }        
                
        return true;
    }    
            
    // Struct
    // int characterIdx
    // byte response ??? 0xA1 ~ 0xA6 Success
    
    private Packet makeCharacterCreationgResponse(CharacterIdx idx, short response){
        PacketBuilder builder = getPacketBuilder(0x0086);
                
        logger.info("Caramba do idx: {}", idx);
        
        if(response > (short)0xA0){
            builder.putByteArray(idx.getBytes());
        }else{
            builder.putIntLE(0);
        }        
        
        builder.putByte((byte)response);
        
        return builder.build();
    }
}
