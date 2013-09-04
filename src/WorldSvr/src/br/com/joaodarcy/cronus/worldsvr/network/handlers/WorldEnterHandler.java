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
import br.com.joaodarcy.cronus.cabal.core.types.UInt16;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.cronus.worldsvr.Configuration;
import br.com.joaodarcy.cronus.worldsvr.character.GameCharacter;
import br.com.joaodarcy.cronus.worldsvr.core.CharacterIdx;
import br.com.joaodarcy.cronus.worldsvr.core.ClientState;
import br.com.joaodarcy.cronus.worldsvr.core.Item;
import br.com.joaodarcy.cronus.worldsvr.core.NStorage;
import br.com.joaodarcy.cronus.worldsvr.core.Player;
import br.com.joaodarcy.cronus.worldsvr.core.QuestLog;
import br.com.joaodarcy.cronus.worldsvr.core.QuestLogEntry;
import br.com.joaodarcy.cronus.worldsvr.core.WorldSession;
import br.com.joaodarcy.cronus.worldsvr.network.ServerOpcodeHandler;
import br.com.joaodarcy.cronus.worldsvr.skillbook.Skill;
import br.com.joaodarcy.cronus.worldsvr.skillbook.SkillBook;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class WorldEnterHandler extends ServerOpcodeHandler {      
    
    public WorldEnterHandler() {
        super((short)0x008E, ClientState.SUBPASSWORD_CHECKED);
    }
    
    @Override
    protected Boolean handleValue(Packet packet) {
        WorldSession session = getCurrentSession();                
        session.changeState(ClientState.ENTERING_WORLD);
        
        int   playerIdx = packet.getInt();
        
        Player player = null;
        
        if(session.isGameCharacterInCache()){
            GameCharacter gameCharacter = session.getGameCharacterFromCache(playerIdx);
            if(gameCharacter != null){ // New created ???
                player = Player.fromCharacter(gameCharacter);
                if(player == null){
                    logger.error("Cannot load player data from character: {}", gameCharacter);
                }
            }            
        }
        
        // Clear game characters cache list from session
        session.clearGameCharacterListCache();
        
        if(player == null){
            CharacterIdx idx = new CharacterIdx(playerIdx);            
            player = Player.fromDatabase(idx);
            if(player == null){
                logger.error("Cannot load player data from database, idx: {}, closing connection", idx);
                session.disconnectClient();
                return true;
            }
        }
        
        // Set current player
        session.setPlayer(player);        
        
        // Update client state
        session.changeState(ClientState.IN_WORLD);
        session.sendPacket(makeUnk0145());
        session.sendPacket(makeEnterWorldPacket(player));
        
        session.sendPacket(makeUnk01E0());
        session.sendPacket(makeUnk083A());
        
        return true;
    }
            
    private Packet makeUnk0145(){
        PacketBuilder builder = getPacketBuilder(0x0145);
        
        builder.putIntLE(0);
        builder.putIntLE(0);
        builder.putIntLE(0);
        
        return builder.build();
    }
    
    private Packet makeUnk083A(){
        PacketBuilder builder = getPacketBuilder(0x083A);
        
        builder.putByte((byte)0x05);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x30);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x15);
        builder.putByte((byte)0x10);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x16);
        builder.putByte((byte)0x10);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x17);
        builder.putByte((byte)0x10);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x1C);
        builder.putByte((byte)0x10);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);

        return builder.build();
    }
    
    private Packet makeUnk01E0(){
        PacketBuilder builder = getPacketBuilder(0x01E0);
        
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);

        return builder.build();
    }
            
    private Packet makeEnterWorldPacket(Player player){
        UInt32 chatIpAddress = UInt32.fromIpAddress(Configuration.serverChatIp());
        UInt16 chatPort      = UInt16.valueOf(Configuration.serverChatPort());
                
        PacketBuilder builder = getPacketBuilder(0x008E);
                                                                                                                                                 
        // Equipamentos
        NStorage equipment = player.getEquipment();                                                
        NStorage bag = player.getInventory();
        
        SkillBook skillBook = player.getSkillBook();
        
        QuestLog questLog = player.getQuestLog();
                        
        //ActionBar quickBar = player.getQuickBar();                
        
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00); // 4
        
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00); // 8
        
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00); // 12
        
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00); // 16
        
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00); // 20
        
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00); // 24
        
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00); // 28
        
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x08); // 32
        
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00); // 36
        
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00); // 40
        
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00); // 44
        
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00); // 48
        
        builder.putByte((byte)0x00);        
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);        
        builder.putByte((byte)0x00); // 52        
        
        builder.putByte((byte)0x00);                                            // FIXME
        builder.putByte((byte)0x00);                                            // FIXME
        builder.putByte((byte)0x00);                                            // FIXME
        builder.putByte((byte)0x00); // 56                                      // FIXME
        
        builder.putByte((byte)0x00); // 57                                      // FIXME
        builder.putByte((byte)0x00); // 58                                      // FIXME
                
        builder.putShortLE((short)0x0214);
        builder.putShortLE((short)0x0000); // Nada aki
        
        builder.putIntLE(0); // Unk - Nada aki
        builder.putIntLE(0); // Unk - Nada aki
        builder.put(player.getAlz()); // Nada aki
        
        builder.putIntLE(0x00000000);                                           // FIXME
        builder.putIntLE(0x00000000);                                           // FIXME
        builder.putByte((byte)0x00);                                            // FIXME        
        
        builder.put(UInt32.valueOf(0xC800FAFFl));                               // FIXME
        builder.putShortLE((short)0xCFDB);                                      // FIXME
        
        // Fim do 0x004C - Inicio do 0x0057
        builder.putIntLE(0x0094E084);                                           // FIXME
        builder.putIntLE(0x6F000000);                                           // FIXME
        builder.putShortLE((short)0x0052);                                      // FIXME
        builder.putByte((byte)0x01);                                            // FIXME        
        
        builder.put(player.getWorld().getId());                                  // FIXME
        builder.putByte((byte)0x00);  // Faz parte do código do mapa                                             // FIXME
        builder.putIntLE(0x00000000); // Faz parte do código do mapa                                           // FIXME        
        builder.putShortLE((short)0x0000); // Faz parte do código do mapa                                     // FIXME        
        
        // 0x0072
        /*builder.putShortLE((short)0x0020);                                    // FIXME
        builder.putShortLE((short)0x0025);                                      // FIXME*/
        builder.put(player.getX());
        builder.put(player.getY());
                
        // FIXME ( Alz, WExp e Exp )
        builder.putInt64(player.getExp().longValue());
        builder.putInt64(player.getAlz().longValue());
        builder.putInt64(player.getWarExp().longValue());
        
        builder.put(player.getLevel().add(110));
        
        builder.putIntLE(0x00000000); // Nada encontrado                        // FIXME
        
        builder.putIntLE(player.getStr() + 1000);                
        builder.putIntLE(player.getDex() + 1000);
        builder.putIntLE(player.getInt() + 1000);               
        builder.putIntLE(player.getStatPoints() + 10);
        
        builder.put(player.getSwordRank().getId());
        builder.put(player.getMagicRank().getId());
        
        builder.putIntLE(0x00000000); // Mecheu no HP e MP Máximo               // FIXME
        builder.putShortLE((short)0x0000);                                      // FIXME
        
        builder.put(player.getMaxHP());
        builder.put(player.getHP());
        builder.put(player.getMaxMP());
        builder.put(player.getMP());
        builder.put(player.getMaxSP());
        builder.put(player.getSP());        
        //builder.put(player.getExperienciaEspada());
        //builder.put(player.getExperienciaMagia());
        //builder.put(player.getRankExp());
        
        builder.putIntLE(0x00000000); // PC
        builder.putShortLE((short)0x2A30); // Duração PC ?????
        
        // Pontos de Honra
        // Penalidade de morte
        // Mana maima
        // Penalidade PK
        // Grau de honra
        // XP Espada
        // XP Mágica
        // FIXME              
        builder.putByte((byte)0x00); // Verificar                               
        builder.putByte((byte)0x00); // Verificar                               
        builder.putByte((byte)0x00); // Verificar                               
        builder.putByte((byte)0x00); // Verificar                               
                
        builder.putByte((byte)0x00); // Verificar                               
        builder.putByte((byte)0x00); // Verificar                               
        builder.putByte((byte)0x00); // Verificar                               
        builder.putByte((byte)0x00); // Verificar                               
        
        builder.putByte((byte)0x00); // Espada - Free Points
        builder.putByte((byte)0x00); // Espada - Free Points
        builder.putByte((byte)0x00); // Verificar                               
        builder.putByte((byte)0x00); // Verificar                               
        
        builder.putByte((byte)0x00); // Mágica - Free Points
        builder.putByte((byte)0x00); // Mágica - Free Points                               
        builder.putByte((byte)0x00); // Espada - Upada XXXX / 21
        builder.putByte((byte)0x00); // Espada - Upada XXXX / 21
        
        builder.putByte((byte)0x00); // Magica - Updata XXXX / 11
        builder.putByte((byte)0x00); // Magica - Updata XXXX / 11
        builder.putByte((byte)0x00); // Verificar                               
        builder.putByte((byte)0x00); // Verificar                               
        
        builder.putByte((byte)0x00); // Verificar                               
        builder.putByte((byte)0x00); // Verificar                               
        builder.putByte((byte)0x00); // Verificar                               
        builder.putByte((byte)0x00); // Verificar                               
        
        builder.putByte((byte)0x00); // Verificar                               
        builder.putByte((byte)0x00); // Verificar  
        
        builder.put(player.getHonorPoints());
        
        builder.putByte((byte)0x00); // Unk
        builder.putByte((byte)0x00); // Unk
                
        builder.putByte((byte)0x00); // Unk
        builder.putByte((byte)0x00); // Unk
        builder.putByte((byte)0x00); // Unk
        builder.putByte((byte)0x00); // Unk
        
        builder.putByte((byte)0x00); // Unk
        builder.putByte((byte)0x00); // Unk
        builder.putByte((byte)0x00); // Penalidade de Morte
        builder.putByte((byte)0x00); // Penalidade de Morte
        
        builder.putByte((byte)0x00); // Penalidade de Morte
        builder.putByte((byte)0x00); // Penalidade de Morte
        builder.putByte((byte)0x00); // Unk
        builder.putByte((byte)0x00); // Unk
        
        builder.putByte((byte)0x00); // Unk
        builder.putByte((byte)0x00); // Unk
        builder.putByte((byte)0x00); // Punição de Morte Sem Mana
        builder.putByte((byte)0x00); // Punição de Morte Sem Mana
        
        builder.putByte((byte)0x00); // Punição de Morte Sem Mana
        builder.putByte((byte)0x00); // Punição de Morte Com Muita Mana (0x32)
        builder.putByte((byte)0x00); // Unk
        builder.putByte((byte)0x00); // Unk
        
        builder.putByte((byte)0x00); // Unk
        builder.putByte((byte)0x00); // Unk
        builder.putByte((byte)0x00); // Penalidade de PK
        builder.putByte((byte)0x00); // Penalidade de PK
                        
        //builder.putIntLE(0x00000000);                                           // FIXME
    //builder.putIntLE(0x01B866E4);                                             // FIXME
        //builder.putIntLE(0x00000000);                                           // FIXME
        
        //builder.put(player.getHonorPoint());
                
        //builder.putInt64(0x0000000000000000);                                   // FIXME
        //builder.putShortLE((short)0x0000);                                      // FIXME
        
        builder.put(chatIpAddress);
        builder.put(chatPort);
        
        builder.put(chatIpAddress);
        builder.put(chatPort);
                        
        builder.put(player.getNation().getId());
        
        builder.putByte((byte)0x00); // UNK
        builder.putByte((byte)0x00); // UNK
        builder.putByte((byte)0x00); // UNK
        builder.putByte((byte)0x00); // UNK
        
        builder.put(player.getWarpFlag());
        builder.put(player.getMapsMask());
        
        builder.put(player.getStyle().getStyle());
        
        builder.putByte((byte)0x00); // UNK
        builder.putByte((byte)0x00); // UNK
        builder.putByte((byte)0x00); // UNK
        builder.putByte((byte)0x00); // UNK        
        builder.putByte((byte)0x00); // UNK
        builder.putByte((byte)0x00); // UNK
        builder.putByte((byte)0x00); // UNK
        
        builder.putByte((byte)0x00); // Inventário Especial - 0
        builder.putByte((byte)0x00); // Inventário Especial - 0
        builder.putByte((byte)0x00); // Inventário Especial - 0
        builder.putByte((byte)0x00); // Inventário Especial - 0
        
        builder.putByte((byte)0x00); // Inventário Especial - 0
        builder.putByte((byte)0x00); // Inventário Especial - 0
        builder.putByte((byte)0x00); // Inventário Especial - 0
        builder.putByte((byte)0x00); // Inventário Especial - 0
        
        builder.putByte((byte)0x00); // Inventário Especial - 0
        builder.putByte((byte)0x00); // Inventário Especial - 0
        builder.putByte((byte)0x00); // Inventário Especial - 0
        builder.putByte((byte)0x00); // Inventário Especial - 0
        
        builder.putByte((byte)0x00); // Inventário Especial - 0
        builder.putByte((byte)0x00); // Inventário Especial - 0
        builder.putByte((byte)0x00); // Inventário Especial - 0
        builder.putByte((byte)0x00); // Inventário Especial - 0
        
        builder.putByte((byte)0x00); // Inventário Especial - 1
        builder.putByte((byte)0x00); // Inventário Especial - 1
        builder.putByte((byte)0x00); // Inventário Especial - 1
        builder.putByte((byte)0x00); // Inventário Especial - 1
        
        builder.putByte((byte)0x00); // Inventário Especial - 1
        builder.putByte((byte)0x00); // Inventário Especial - 1
        builder.putByte((byte)0x00); // Inventário Especial - 1
        builder.putByte((byte)0x00); // Inventário Especial - 1
        
        builder.putByte((byte)0x00); // Inventário Especial - 1
        builder.putByte((byte)0x00); // Inventário Especial - 1
        builder.putByte((byte)0x00); // Inventário Especial - 1
        builder.putByte((byte)0x00); // Inventário Especial - 1
        
        builder.putByte((byte)0x00); // Inventário Especial - 1
        builder.putByte((byte)0x00); // Inventário Especial - 1
        builder.putByte((byte)0x00); // Inventário Especial - 1
        builder.putByte((byte)0x00); // Inventário Especial - 1
                       
        builder.putShortLE((short)equipment.getStoredCount());         
        builder.putShortLE((short)bag.getStoredCount());                
        builder.putShortLE(skillBook.getStoredCount());
                
        builder.putByte((byte)0x00); // QuickBarEntryCount
        builder.putByte((byte)0x00); // QuickBarEntryCount
                                
        //builder.putByte((byte)quickBar.getStoredCount());                        
                  
        for(int i  = 0 ; i < 15 ; i++){
            builder.putByte((byte)0x00);
        }
        
        //byte questInProgressCount = 0x01;
        builder.put(questLog.getQuestCount());
        
        int zeros = 499 + 782 /* EP 8 */;
        for(int i = 0 ; i < zeros ; i++){
            builder.putByte((byte)0x00);
        }
        
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x01);
        
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        
        for(int i = 0 ; i < 235 ; i++){
            builder.putByte((byte)0x00);
        }
        
        builder.putByte((byte)(player.getName().length() + 1));
        builder.putString(player.getName());
                        
        ////////////////////////////////////////////////////////////////////////
        // EQUIPAMENTO
        ////////////////////////////////////////////////////////////////////////
        
        short currentEquipmentSlot = 0;
        for(Item entry : equipment.getArray()){
            if(entry != null){
                builder.put(entry.getId());                
                builder.put(entry.getDuration());                                
                builder.put(entry.getOptions());
                builder.putShortLE(currentEquipmentSlot);
                
                // Separator
                builder.putIntLE(0);
            }            
            ++currentEquipmentSlot;
        }                
        
        /////////////////////////////////////////////////////////////////////
        // BAG
        ////////////////////////////////////////////////////////////////////
        
        short currentBagSlot = 0;
        for(Item entry : bag.getArray()){
            if(entry != null){
                builder.put(entry.getId());                
                builder.put(entry.getDuration());                
                builder.put(entry.getOptions());
                builder.putShortLE(currentBagSlot);
                
                // Separator
                builder.putIntLE(0);
            }
            ++currentBagSlot;
        }
                        
        /////////////////////////////////////////////////////////////////////
        // SKILL BOOK
        ////////////////////////////////////////////////////////////////////                        
                        
        short currentSkillSlot = 0;
        for(Skill entry : skillBook.getStorage()){
            if(entry != null){
                builder.put(entry.getId());
                builder.put(entry.getLevel());
                builder.putShortLE(currentSkillSlot);
            }
            ++currentSkillSlot;
        }                        
                
        /////////////////////////////////////////////////////////////////////
        // PENDING: QUICK BAR
        ////////////////////////////////////////////////////////////////////
        
        /*byte currentQuickBarSlot = 0;
        for(Action entry : quickBar.getStorage()){
            if(entry != null){
                builder.put(entry.getId());
                builder.putByte(currentQuickBarSlot);
            }
            ++currentQuickBarSlot;
        }*/
                                    
        // Quick Bar                
        
        /*builder.putByte((byte)0x84);    // SkillBookSlot        
        builder.putByte((byte)0x00);    // SkillBookSlot
        builder.putByte((byte)0x00);    // QuickBarSlot
        builder.putByte((byte)0x00);    // QuickBarSlot
        
        builder.putByte((byte)0x00);    // SkillBookSlot
        builder.putByte((byte)0x00);    // SkillBookSlot
        builder.putByte((byte)0x01);    // QuickBarSlot
        builder.putByte((byte)0x00);    // QuickBarSlot
        
        builder.putByte((byte)0x01);    // SkillBookSlot
        builder.putByte((byte)0x00);    // SkillBookSlot
        builder.putByte((byte)0x02);    // QuickBarSlot
        builder.putByte((byte)0x00);    // QuickBarSlot
        
        builder.putByte((byte)0xED);    // SkillBookSlot
        builder.putByte((byte)0x00);    // SkillBookSlot
        builder.putByte((byte)0x09);    // QuickBarSlot
        builder.putByte((byte)0x00);    // QuickBarSlot
        
        builder.putByte((byte)0xEF);    // SkillBookSlot
        builder.putByte((byte)0x00);    // SkillBookSlot
        builder.putByte((byte)0x0A);    // QuickBarSlot
        builder.putByte((byte)0x00);    // QuickBarSlot
        
        builder.putByte((byte)0x82);    // SkillBookSlot
        builder.putByte((byte)0x00);    // SkillBookSlot
        builder.putByte((byte)0x0B);    // QuickBarSlot
        builder.putByte((byte)0x00);    // QuickBarSlot
        
        builder.putByte((byte)0x83);    // SkillBookSlot
        builder.putByte((byte)0x00);    // SkillBookSlot
        builder.putByte((byte)0x08);    // QuickBarSlot
        builder.putByte((byte)0x00);    // QuickBarSlot*/
        
        // Quest In Progress
        byte currentQuestLogSlot = 0x00;
        for(QuestLogEntry questLogEntry : questLog.getQuestLogEntries()){
            if(questLogEntry != null){
                builder.put(questLogEntry.getQuestId());
                builder.put(questLogEntry.getQuestProgress());
                builder.put(questLogEntry.getTracking());
                builder.put(questLogEntry.getUnk());
                builder.putByte(currentQuestLogSlot);
            }
            ++currentQuestLogSlot;
        }
        
        return builder.build();
    }            
}
