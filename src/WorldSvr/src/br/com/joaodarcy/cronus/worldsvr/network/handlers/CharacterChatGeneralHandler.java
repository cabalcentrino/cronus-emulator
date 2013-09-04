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
import br.com.joaodarcy.cronus.cabal.core.types.UInt8;
import br.com.joaodarcy.cronus.worldsvr.account.Account;
import br.com.joaodarcy.cronus.worldsvr.core.ClientState;
import br.com.joaodarcy.cronus.worldsvr.core.Item;
import br.com.joaodarcy.cronus.worldsvr.core.Vector2D;
import br.com.joaodarcy.cronus.worldsvr.core.WorldSession;
import br.com.joaodarcy.cronus.worldsvr.item.ItemData;
import br.com.joaodarcy.cronus.worldsvr.network.ServerOpcodeHandler;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class CharacterChatGeneralHandler extends ServerOpcodeHandler {

    public CharacterChatGeneralHandler(){
        super((short)0x00C3, ClientState.IN_WORLD);
    }
    
    private void itemAddCommand(WorldSession session, UInt32 itemId, byte upgradeLevel, byte slotCount, byte craftLevel, byte ampEffect){
        Account account = session.getAccount();
        Item newItem = new Item(itemId, UInt32.ZERO, UInt32.ZERO);        
        if(upgradeLevel > 0){
            newItem.setUpgradeLevel(upgradeLevel);
        }
        if(slotCount > 0){
            newItem.setSlotsCount(slotCount);
        }        
        if(craftLevel > 0 && ampEffect > 0){            
            byte effectOption = 0;
            
                ItemData data = newItem.getItemData();
                switch(data.getType()){
                    case BRACELETE:
                        effectOption = ampEffect == 1 ? (byte)3 : (byte)4;
                        break;
                    case EPAULET:
                    case EPAULET2:
                        effectOption = ampEffect == 1 ? (byte)7 : (byte)8;
                        break;
                    case ONE_HAND_SWORD:
                    case TWO_HANDS_SWORD:
                    case GREVA:
                    case LUVA:
                    case VISOR:
                    case VISOR2:
                    case TRAJE:
                        effectOption = ampEffect == 1 ? (byte)8 : (byte)9;
                        break;                    
                }                
            
            if(effectOption > 0){
                newItem.setCraftEffectLevel(craftLevel);
                newItem.setCraftEffect(effectOption);
            }
        }
        newItem.setBound(true); // Only character binded item
        
        logger.info("Item {} give to player {}", newItem, session.getPlayer().getCharacterIdx());        
        account.addCashItem(newItem);        
        session.sendPacket(WorldQueryCashItemHandler.makeResponse(session));
    }
    
    private void warehouseCommand(WorldSession session){
        PacketBuilder builder = getPacketBuilder(0x0091);
        
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
                
        session.sendPacket(builder.build());
    }      
    
    private void cashSetCommand(WorldSession session, UInt32 amount){
        logger.info("Setting player {} cash to {}", session.getPlayer().getName(), amount);
        session.getAccount().setCash(amount);
        
        PacketBuilder builder = getPacketBuilder(0x0886);        
        builder.put(amount);        
        session.sendPacket(builder.build());
    }
    
    @Override
    protected Boolean handleValue(Packet value) {
        WorldSession session = getCurrentSession();
        
        UInt16 dataSize = value.getUInt16();                
        UInt16 messageSize = value.getUInt16();                
        UInt8 unk0 = value.getUInt8();
        UInt8 unk1 = value.getUInt8();
        UInt8 unk2 = value.getUInt8();
        
        String message = value.getString(messageSize.intValue() - 3);
        
        // TODO: Check security level
        if(message.startsWith(".")){
            logger.info("Player command: \"{}\"", message);
            
            try{
                String cmdSplited[] = message.substring(1).split(" ");
                switch(cmdSplited[0]){
                    case "itemadd":
                        UInt32 itemId = UInt32.valueOf(Long.valueOf(cmdSplited[1]));
                        byte slotCount = 0x00;
                        byte upgradeLevel = 0x00;
                        byte craftLevel = 0x00;
                        byte craftEffect = 0x00;
                        if(cmdSplited.length > 2){
                            upgradeLevel = Byte.valueOf(cmdSplited[2].substring(1));
                        }
                        if(cmdSplited.length > 3){
                            slotCount = Byte.valueOf(cmdSplited[3]);
                        }               
                        if(cmdSplited.length > 4){
                            craftLevel = Byte.valueOf(cmdSplited[4]);
                        }
                        if(cmdSplited.length > 5){
                            craftEffect = Byte.valueOf(cmdSplited[5]);
                        }
                        itemAddCommand(session, itemId, upgradeLevel, slotCount, craftLevel, craftEffect);
                        break;
                    case "warehouse":
                        warehouseCommand(session);
                        break;
                    case "dc":
                        logger.info("Disconnecting current player !");
                        session.disconnectClient();
                        break;
                    case "cashset":
                        UInt32 amount = UInt32.valueOf(Long.valueOf(cmdSplited[1]));
                        cashSetCommand(session, amount);
                        break;
                    case "mobspawn":
                        Integer mobId = Integer.valueOf(cmdSplited[1]);
                        Vector2D position = session.getPlayer().getPosition();
                        session.getPlayer().getWorld().spawnMob(mobId, position.getX().intValue(), position.getY().intValue());
                        break;                    
                    default:
                        logger.info("Unknow command: \"{}\"", message);
                        break;
                }   
            }catch(Throwable t){
                logger.error("Error handling player command: \"{}\"", message, t);
            }
        }else{
            logger.info("Player message: \"{}\"", message);
        }
        
        return true;
    }
    
}
