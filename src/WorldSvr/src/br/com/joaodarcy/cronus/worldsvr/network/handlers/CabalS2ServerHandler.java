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

import br.com.joaodarcy.cronus.cabal.core.PacketHandler;
import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import br.com.joaodarcy.cronus.cabal.core.patterns.Chain;
import br.com.joaodarcy.cronus.cabal.core.patterns.util.ChainBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class CabalS2ServerHandler implements PacketHandler {

    private final Chain<Boolean, Packet> handlers;
    private final Logger logger;
    
    public CabalS2ServerHandler() {
        this.handlers = buildHandlersChain();
        this.logger = LoggerFactory.getLogger(CabalS2ServerHandler.class);
    }
    
    // TODO: Put handlers on a indexed array to speed up
    private Chain<Boolean, Packet> buildHandlersChain(){
        ChainBuilder builder = ChainBuilder.create();
        
        builder
            .add(new AccountChargeInfoHandler())            
            .add(new AccountCheckUserPrivacyDataHandler())
            .add(new CharacterCashItemBuyHandler())
            .add(new CharacterCashItemReceiveHandler())
            .add(new CharacterChatGeneralHandler())
            .add(new CharacterCreateHandler())
            .add(new CharacterDeleteHandler())               
            .add(new CharacterGetMyCharactersHandler())
            .add(new CharacterInventoryActiveCostumeHandler())
            .add(new CharacterNeedSubpasswordHandler())
            .add(new CharacterQuestAbandonHandler())
            .add(new CharacterQuestBeginHandler())
            .add(new CharacterQuickBarSetHandler())
            .add(new CharacterStyleChangeHandler())
            .add(new CharacterTargetPlayerHandler())
            .add(new PlayerSkillUserHandler())
            .add(new PlayerMoveBeginnedHandler())
            .add(new PlayerMoveEndedHandler())
            .add(new SkillUntrainHandler())
            .add(new StorageDropHandler())
            .add(new StorageMoveHandler())
            .add(new StorageSwapHandler())
            .add(new ServerHandshakeHandler())            
            .add(new WorldEnterHandler())
            .add(new WorldExitHandler())
            .add(new WorldExitSelectCharacterHandler())
            .add(new WorldLeavingHandler())
            .add(new WorldQueryCashItemHandler())
            .add(new WorldMoveTilePositionHandler())
            .add(new WorldNPCShopGetDataHandler())
            .add(new WorldNPCShopOpenHandler())
            .add(new WorldServerGetTime())
            .add(new WorldUnk0881())
            .add(new WorldUnk0883Handler())            
            .add(new WorldUnk08BDHandler())
            .add(new WorldWarpHandler());
        
        return builder.build();
    }
        
    @Override
    public void handle(Packet packet) {
        logger.debug("Client packet\nOPCODE: {}\n{}", String.format("%04X", packet.getOpcode()), packet.toByteString());
        
        if(!handlers.handle(packet)){
            logger.debug("Cannot handle packet, opcode: {}, data: {}", String.format("%04X", packet.getOpcode()), packet.toByteString());
        }
    }
                    
}
