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
import br.com.joaodarcy.cronus.loginsvr.core.AuthState;
import br.com.joaodarcy.cronus.loginsvr.core.Channel;
import br.com.joaodarcy.cronus.loginsvr.core.Server;
import br.com.joaodarcy.cronus.loginsvr.network.AuthOpcodeHandler;
import br.com.joaodarcy.cronus.cabal.core.ClientSession;
import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import br.com.joaodarcy.cronus.cabal.core.network.PacketBuilder;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class AuthAccountHandler extends AuthOpcodeHandler {
                    
    private final byte  AUTH_RESPONSE_OK = 0x20;
    private final byte  AUTH_RESPONSE_INCORRET_LOGIN_OR_PASSWORD = 0x21;
    private final byte  AUTH_RESPONSE_ALREADY_CONNECTED = 0x22;
    //private final byte  AUTH_RESPONSE_UNK = 0x23;    // Cannot connect at this moment
    private final byte  AUTH_RESPONSE_ACCOUNT_HAS_EXPIRED = 0x24; // The account has expired   
    private final byte  AUTH_RESPONSE_IP_BLOCKED = 0x25; // The ip has been blocked, you cannot login with this ip
    private final byte  AUTH_RESPONSE_ACCOUNT_BLOCKED = 0x26;
    //private final byte  AUTH_RESPONSE_UNK = 0x27; // you cannot use the test server during free trial period
    //private final byte  AUTH_RESPONSE_UNK = 0x28; // Please use a internet cafe to login in this game
    //private final byte  AUTH_RESPONSE_UNK = 0x29; // Your id has not been authorized to use, check your e-mail for authorization
    //private final byte  AUTH_RESPONSE_UNK = 0x2A; // Your account was been withdraw from our member list
    private final byte  AUTH_RESPONSE_UNK = 0x2B; // Current account is locked because incorrect password was entered too many times
//    private final byte  AUTH_RESPONSE_UNK = 0x2A;
    
    private final static Server serverArray[];
    
    static{
        serverArray = new Server[1];
        
        serverArray[0] = new Server((byte)0x01, (byte)0x00, 0);
        //serverArray[1] = new Server((byte)0x01, (byte)0x00, 0);
        //serverArray[2] = new Server((byte)0x01, (byte)0x00, 0);
        
        serverArray[0].add(new Channel((byte)0x01, (short)0x00A2, (short)0x00C8, 1024));
        serverArray[0].add(new Channel((byte)0x02, (short)0x00AD, (short)0x012C,    0));
        serverArray[0].add(new Channel((byte)0x03, (short)0x0099, (short)0x012C,    0));
        serverArray[0].add(new Channel((byte)0x04, (short)0x0035, (short)0x00C8,    4));
        serverArray[0].add(new Channel((byte)0x05, (short)0x0039, (short)0x00C8,    4));
        serverArray[0].add(new Channel((byte)0x06, (short)0x002a, (short)0x0096,    4));
        serverArray[0].add(new Channel((byte)0x07, (short)0x0096, (short)0x012C,    0));
        serverArray[0].add(new Channel((byte)0x08, (short)0x00A1, (short)0x012C,    0));
        serverArray[0].add(new Channel((byte)0x09, (short)0x00A9, (short)0x012C,    0));
        serverArray[0].add(new Channel((byte)0x0A, (short)0x0022, (short)0x0096,    5));
        serverArray[0].add(new Channel((byte)0x0B, (short)0x0027, (short)0x0096,    5));
        serverArray[0].add(new Channel((byte)0x0C, (short)0x0021, (short)0x0096,    5));
        serverArray[0].add(new Channel((byte)0x0D, (short)0x0099, (short)0x012C,    1));
        serverArray[0].add(new Channel((byte)0x0E, (short)0x0088, (short)0x012C,    1));        
        serverArray[0].add(new Channel((byte)0x0F, (short)0x0083, (short)0x012C,    1));
        serverArray[0].add(new Channel((byte)0x10, (short)0x006A, (short)0x00FA,    1));
        serverArray[0].add(new Channel((byte)0x11, (short)0x0070, (short)0x00FA,    1));
        serverArray[0].add(new Channel((byte)0x12, (short)0x006A, (short)0x00FA,    1));
        serverArray[0].add(new Channel((byte)0x13, (short)0x007A, (short)0x012C,    1));
        serverArray[0].add(new Channel((byte)0x14, (short)0x0018, (short)0x0096,    8));             
        
    }
    
    public AuthAccountHandler() {
        super((short)0x0067, AuthState.RIGHT_CLIENT_VERSION);
    }
   
    @Override
    protected Boolean handleValue(Packet packet) {        
        ClientSession session = ClientSession.getCurrentSession();
                        
        // Send Server List
        session.sendPacket(makeServerList());
        session.sendPacket(make0080());        
        
        session.sendPacket(makeEpisode8Response()); 
        session.sendPacket(make0078());
                
        session.sendPacket(makeServerList());
        
        return Boolean.TRUE;
    }
        
    private String fill(String value, int expectedLength, byte endValue){
        StringBuilder sb = new StringBuilder(expectedLength);        
        sb.append(value);
        
        for(int i  = value.length() ; i < expectedLength - 1 ; i++){
            sb.append('a');
        }
        sb.append((char)endValue);
        
        return sb.toString();
    }
    
    private Packet make0080(){
        PacketBuilder builder = AuthNode.getPacketBuilderFactory().create((short)0x0080);
        
        String endereco = "http://127.0.0.1/"; // Shop address and others...
        
        builder.putByte((byte)0x2A);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x28);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x3B);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        
        builder.putString(fill(endereco, 60, (byte)0x44));
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        
        builder.putString(fill(endereco, 69, (byte)0x3B));
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        
        builder.putString(fill(endereco, 60, (byte)0x44));
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        
        builder.putString(fill(endereco, 69, (byte)0x16));
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        
        builder.putString(fill(endereco, 22, (byte)0x72));
                        
        return builder.build();
    }
    
    private Packet makeEpisode8Response(){
        PacketBuilder builder = AuthNode.getPacketBuilderFactory().create(opcodeHandled);

        getCurrentSession().setState(AuthState.AUTHED);
                
        builder.putByte((byte)AUTH_RESPONSE_OK);        
        builder.putByte((byte)0x60);
        builder.putByte((byte)0x0D);
        builder.putByte((byte)0x11);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x23);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x7A); // Data e hora da criação da conta ( time_t )
        builder.putByte((byte)0xEF); // Data e hora da criação da conta ( time_t )
        builder.putByte((byte)0x33); // Data e hora da criação da conta ( time_t )
        builder.putByte((byte)0x50); // Data e hora da criação da conta ( time_t )
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x01);
        
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x01);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x35);
        builder.putByte((byte)0x38);
        builder.putByte((byte)0x41);
        builder.putByte((byte)0x38);
        builder.putByte((byte)0x30);
        
        builder.putByte((byte)0x42);
        builder.putByte((byte)0x42);
        builder.putByte((byte)0x30);
        builder.putByte((byte)0x32);
        builder.putByte((byte)0x46);
        builder.putByte((byte)0x44);
        builder.putByte((byte)0x32);
        builder.putByte((byte)0x34);
        builder.putByte((byte)0x31);
        builder.putByte((byte)0x31);
        builder.putByte((byte)0x43);
        builder.putByte((byte)0x41);
        builder.putByte((byte)0x33);
        builder.putByte((byte)0x31);
        builder.putByte((byte)0x35);
        builder.putByte((byte)0x44);
        
        builder.putByte((byte)0x34);
        builder.putByte((byte)0x41);
        builder.putByte((byte)0x33);
        builder.putByte((byte)0x43);
        builder.putByte((byte)0x46);
        builder.putByte((byte)0x35);
        builder.putByte((byte)0x45);
        builder.putByte((byte)0x34);
        builder.putByte((byte)0x32);
        builder.putByte((byte)0x42);
        builder.putByte((byte)0x33);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x01);    // Server ID
        builder.putByte((byte)0x06);    // Server Player Count
        
        return builder.build();
    }
    
    private Packet make0078(){
        PacketBuilder builder = AuthNode.getPacketBuilderFactory().create((short)0x0078);
        
        builder.putByte((byte)0x09);
        builder.putByte((byte)0x00);
        builder.putByte((byte)0x00);
        
        return builder.build();
    }
    
    private Packet makeServerList(){
        PacketBuilder packet = AuthNode.getPacketBuilderFactory().create((short)0x0079);
                                
        packet.putByte((byte)serverArray.length);
        
        //byte serverFlag = 0x10; // HOT FLAG
        
        for(Server server : serverArray){
                            
            packet.putByte(server.getId());
            packet.putByte(server.getFlag());
            packet.putIntLE(server.getUnk()); // Unk0
            packet.putByte((byte)server.getChannelList().size());

            for(Channel channel : server.getChannelList()){                        
                packet.putByte(channel.getId());
                packet.putShortLE(channel.getLatency());
                
                packet.putIntLE(channel.getUnk1()); // Unk 1
                packet.putIntLE(channel.getUnk2()); // Unk 2
                packet.putIntLE(channel.getUnk3()); // Unk 3
                packet.putIntLE(channel.getUnk4()); // Unk 4
                packet.putIntLE(channel.getUnk5()); // Unk 5                
                packet.putShortLE(channel.getUnk6()); // Unk 6
                
                packet.putShortLE(channel.getCapacity());
                packet.putUInt32LE(channel.getIp());
                packet.putShortLE((short)channel.getPort());
                packet.putIntLE(channel.getType());                
            }               
        }
                
        return packet.build();
    }            
}
