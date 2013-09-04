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

package br.com.joaodarcy.cronus.loginsvr.network.handler;

import br.com.joaodarcy.cronus.loginsvr.AuthNode;
import br.com.joaodarcy.cronus.loginsvr.Configuration;
import br.com.joaodarcy.cronus.loginsvr.core.AuthClientSession;
import br.com.joaodarcy.cronus.loginsvr.core.AuthState;
import br.com.joaodarcy.cronus.loginsvr.network.AuthOpcodeHandler;
import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import br.com.joaodarcy.cronus.cabal.core.network.PacketBuilder;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class CheckVersionHandler extends AuthOpcodeHandler {
    
    private final boolean CHECK_CLIENT_VERSION = Configuration.checkClientVersion();
    private final short EXPECT_CLIENT_VERSION = Configuration.clientVersion();
    
    private final Logger logger = LoggerFactory.getLogger(CheckVersionHandler.class);

    private final static AuthState otherValidState = AuthState.AUTHED;
        
    public CheckVersionHandler() {
        super((short)0x007A, AuthState.HANDSHAKED);
    }

    @Override
    protected boolean canHandle(Packet value) {
        if(value.getOpcode() == opcodeHandled){
            if(getCurrentSession().getState() == requiredState || getCurrentSession().getState() == otherValidState){
                return true;
            }else{
                onInvalidState();
            }
        }
        return false;
    }
       
    @Override
    protected Boolean handleValue(Packet value) {
        short clientVersion = value.getShort();
        AuthClientSession session = getCurrentSession();
                
        logger.debug("{} - Client version: {}", session.getSessionId(), clientVersion);
        
        boolean success = false;
        
        if(CHECK_CLIENT_VERSION){
            if( clientVersion == EXPECT_CLIENT_VERSION){
                success = true;
            }else{
                // Update client state
                if(session.getState() == AuthState.AUTHED){
                    session.setState(AuthState.SERVER_SELECTED);
                }else{
                    session.setState(AuthState.WRONG_CLIENT_VERSION);
                }

                logger.info("Wrong client version {}, closing connection !", clientVersion);
                session.sendPacket(makeIncorrectVersion());
                // TODO: Agendar desconexão pois o jogo fecha sozinho para receber a atualização.
                //ClientSession.disconnect();
            }
        }else{
            success = true;
        }
        
        if(success){
            // Update client state
            if(session.getState() == AuthState.AUTHED){
                session.setState(AuthState.SERVER_SELECTED);
            }else{
                session.setState(AuthState.RIGHT_CLIENT_VERSION);
            }

            logger.info("Client version {} accepted", clientVersion);                                    
            session.sendPacket(makeCorrectVersion(clientVersion));
        }                
                        
        return true;
    }
           
    private Packet makeCorrectVersion(short version){
        PacketBuilder packet = AuthNode.getPacketBuilderFactory().create(opcodeHandled);
        
        packet.putShortLE(version);
        packet.putShortLE((short)0x0000); // unk 1
        packet.putUInt32LE(new UInt32(0x59077Cl)); // unk2
        packet.putInt64LE(0x00000000000000l); // pad
        
        return packet.build();
    }
        
    // TODO
    private Packet makeIncorrectVersion(){
        return makeCorrectVersion(EXPECT_CLIENT_VERSION);
    }
        
}
