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

package br.com.joaodarcy.cronus.worldsvr.network;

import br.com.joaodarcy.cronus.cabal.core.AbstractOpcodeHandler;
import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import br.com.joaodarcy.cronus.cabal.core.network.PacketBuilder;
import br.com.joaodarcy.cronus.worldsvr.ServerNode;
import br.com.joaodarcy.cronus.worldsvr.inventory.Inventory;
import br.com.joaodarcy.cronus.worldsvr.core.CharacterIdx;
import br.com.joaodarcy.cronus.worldsvr.core.ClientState;
import br.com.joaodarcy.cronus.worldsvr.core.Player;
import br.com.joaodarcy.cronus.worldsvr.core.WorldSession;
import br.com.joaodarcy.cronus.worldsvr.equipment.Equipment;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public abstract class ServerOpcodeHandler extends AbstractOpcodeHandler {

    protected final ClientState requiredClientState;
    protected final Logger logger;
    
    public ServerOpcodeHandler(short opcodeHandled, ClientState requiredClientState) {
        super(opcodeHandled);
        this.requiredClientState = requiredClientState;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    protected void onIncorrectState(){
        logger.error("Cannot handle packet incorret state, required {}, current {}", requiredClientState, WorldSession.getCurrentWorldSession().getClientState());        
    }
    
    @Override
    protected boolean canHandle(Packet value) {
        if(super.canHandle(value)){
            if(requiredClientState == getCurrentClientState()){
                return true;
            }else{
                onIncorrectState();
            }
        }
        return false;
    }
    
    protected Packet makeBooleanPacket(boolean success){
        PacketBuilder builder = getPacketBuilder();
        
        builder.putByte((byte)(success ? 0x01 : 00));
        
        return builder.build();
    }
        
    protected CharacterIdx makeCharacterIdx(WorldSession session, byte slot){
        return new CharacterIdx(session.getAccountId(), slot);
    }
    
    protected Packet from(int opcode, InputStream stream){
        PacketBuilder builder = getPacketBuilder(opcode);
        
        try{            
            ByteBuffer buffer = ByteBuffer.allocate(512);
            int readCount;
            
            while((readCount = stream.read(buffer.array())) > 0){
                builder.putByteArray(Arrays.copyOfRange(buffer.array(), 0, readCount));
            }
            
        }catch(Throwable t){
            logger.error("Erro lendo dados do pacote", t);
            t.printStackTrace();
        }
        
        return builder.build();
    }
    
    protected PacketBuilder getPacketBuilder(){
        return getPacketBuilder(opcodeHandled);
    }
    
    protected static Equipment getEquipment(){
        return getPlayer().getEquipment();
    }
    
    protected static Inventory getBag(){
        return getPlayer().getInventory();
    }
    
    protected static Player getPlayer(){
        return getCurrentSession().getPlayer();
    }
    
    protected static PacketBuilder getPacketBuilder(int opcode){
        return getPacketBuilder((short)(opcode & 0xFFFF));
    }
    
    protected static PacketBuilder getPacketBuilder(short opcode){
        return ServerNode.getPacketBuilderFactory().create(opcode);
    }
    
    protected static void changeClientState(ClientState newClientState){
        getCurrentSession().changeState(newClientState);
    }
    
    protected static ClientState getCurrentClientState(){
        return getCurrentSession().getClientState();
    }
    
    protected static WorldSession getCurrentSession(){
        return WorldSession.getCurrentWorldSession();
    }
    
}
