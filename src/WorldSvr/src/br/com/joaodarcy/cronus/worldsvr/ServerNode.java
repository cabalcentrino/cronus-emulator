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

package br.com.joaodarcy.cronus.worldsvr;

import br.com.joaodarcy.cronus.cabal.core.PacketHandler;
import br.com.joaodarcy.cronus.cabal.core.crypt.Cryptation;
import br.com.joaodarcy.cronus.cabal.core.crypt.EP8CryptationFactory;
import br.com.joaodarcy.cronus.cabal.core.crypt.EP8KeyFactory;
import br.com.joaodarcy.cronus.cabal.core.crypt.KeyFactory;
import br.com.joaodarcy.cronus.cabal.core.network.EP8ClientHeaderReader;
import br.com.joaodarcy.cronus.cabal.core.network.EP8ClientPacketReader;
import br.com.joaodarcy.cronus.cabal.core.network.EP8PacketBuilderFactory;
import br.com.joaodarcy.cronus.cabal.core.network.HeaderReader;
import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import br.com.joaodarcy.cronus.cabal.core.network.PacketBuilder;
import br.com.joaodarcy.cronus.cabal.core.network.PacketBuilderFactory;
import br.com.joaodarcy.cronus.cabal.core.network.PacketReader;
import br.com.joaodarcy.cronus.cabal.core.patterns.Factory;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.cronus.cabal.core.types.UInt8;
import br.com.joaodarcy.cronus.worldsvr.aura.AuraManager;
import br.com.joaodarcy.cronus.worldsvr.channel.Channel;
import br.com.joaodarcy.cronus.worldsvr.channel.ChannelManager;
import br.com.joaodarcy.cronus.worldsvr.core.SessionManager;
import br.com.joaodarcy.cronus.worldsvr.core.WorldSession;
import br.com.joaodarcy.cronus.worldsvr.item.ItemManager;
import br.com.joaodarcy.cronus.worldsvr.monster.MonsterManager;
import br.com.joaodarcy.cronus.worldsvr.network.handlers.CabalS2ServerHandler;
import br.com.joaodarcy.cronus.worldsvr.sqltypes.CharacterClassSQLType;
import br.com.joaodarcy.cronus.worldsvr.sqltypes.CharacterIdxType;
import br.com.joaodarcy.cronus.worldsvr.sqltypes.ItemPropertieSQLType;
import br.com.joaodarcy.cronus.worldsvr.sqltypes.ItemSizeSQLType;
import br.com.joaodarcy.cronus.worldsvr.sqltypes.ItemTypeSQLType;
import br.com.joaodarcy.cronus.worldsvr.sqltypes.NationType;
import br.com.joaodarcy.cronus.worldsvr.sqltypes.StyleType;
import br.com.joaodarcy.cronus.worldsvr.sqltypes.UInt16Type;
import br.com.joaodarcy.cronus.worldsvr.sqltypes.UInt32Type;
import br.com.joaodarcy.cronus.worldsvr.sqltypes.UInt8Type;
import br.com.joaodarcy.cronus.worldsvr.sqltypes.WeaponRankType;
import br.com.joaodarcy.cronus.worldsvr.sqltypes.WorldType;
import br.com.joaodarcy.npersistence.AutoOpenConnectionDataAccess;
import br.com.joaodarcy.npersistence.ConnectionFactory;
import br.com.joaodarcy.npersistence.DefaultDataAccess;
import br.com.joaodarcy.npersistence.Persistence;
import br.com.joaodarcy.npersistence.PersistenceContext;
import br.com.joaodarcy.npersistence.TypeManager;
import br.com.joaodarcy.npersistence.core.CallableStatementBuilderFactory;
import br.com.joaodarcy.npersistence.core.DataAccess;
import br.com.joaodarcy.npersistence.util.InputStreamUtil;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class ServerNode {
    
    private final static byte serverId;
    private final static PacketBuilderFactory PACKET_BUILDER_FACTORY;
    private final static KeyFactory KEY_FACTORY;
    private final static PacketHandler PACKET_HANDLER;
    private final static PacketReader PACKET_READER;
    private final static HeaderReader HEADER_READER;
    private final static Factory<Cryptation> CRYPTATION_FACTORY;
    private final static Cryptation CRYPTATION;
    
    // Persistence
    private final static PersistenceContext persistenceContext;
    private final static ConnectionFactory connectionFactory;        
    private final static DataAccess dataAccess;
    
    private static boolean running = false;
    
    private final static List<ServerListener> SERVER_LISTENERS;
    
    static {                
                
        final Logger logger = LoggerFactory.getLogger(ServerNode.class);
        
        SERVER_LISTENERS = new ArrayList<>();
        
        // Persistence
        logger.info("Setupping persistence, API Version: {}", Persistence.getVersion());                        
        
        logger.debug("Creating new persistence context...");
        persistenceContext = PersistenceContext.createPersistenceContext("ServerNode"); 
        
        logger.info("Loading query from file...");
        {
            InputStream queryFileInputStream = null;
            try{
                queryFileInputStream = new FileInputStream("db/cronus_querys.xml");                
                persistenceContext.getQueryManager().loadFromXML(queryFileInputStream);
            }catch(IOException e){
                logger.error("Error loading query file: {}", e.getMessage(), e);
            }finally{
                InputStreamUtil.closeInputStream(queryFileInputStream);
            }
        }
        
        logger.debug("Registering custom types...");
        TypeManager typeManager = persistenceContext.getTypeManager();
        // Basic Types
        typeManager.registerType(new UInt8Type());
        typeManager.registerType(new UInt16Type());
        typeManager.registerType(new UInt32Type());
        // Wrapped Types
        typeManager.registerType(new CharacterIdxType());        
        typeManager.registerType(new NationType());
        typeManager.registerType(new StyleType());
        typeManager.registerType(new WorldType());
        typeManager.registerType(new WeaponRankType());        
        typeManager.registerType(new ItemPropertieSQLType());
        typeManager.registerType(new ItemSizeSQLType());
        typeManager.registerType(new ItemTypeSQLType());
        typeManager.registerType(new CharacterClassSQLType());
        
        logger.debug("Creating connection factory...");
        connectionFactory = new DatabaseConnectionFactory();
        
        logger.debug("Creating DataAccess object...");
        dataAccess = new AutoOpenConnectionDataAccess(
            connectionFactory, 
            new DefaultDataAccess(
                /*new DebugStatementBuilderWrapperFactory<>(*/
                    new CallableStatementBuilderFactory(persistenceContext)
                /*)*/,
                persistenceContext
            )
        );
                
        serverId = Configuration.serverId();                       
        
        PACKET_HANDLER = new CabalS2ServerHandler();
        PACKET_BUILDER_FACTORY = new EP8PacketBuilderFactory();
        KEY_FACTORY = new EP8KeyFactory();
        HEADER_READER = new EP8ClientHeaderReader();
        PACKET_READER = new ServerPacketReader(
            new EP8ClientPacketReader(HEADER_READER)
        );
        CRYPTATION_FACTORY = new EP8CryptationFactory();
        CRYPTATION = CRYPTATION_FACTORY.create();                 
    }

    private ServerNode() {
        throw new AssertionError();
    }

    public static ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }
    
    public static PersistenceContext getPersistenceContext(){
        return persistenceContext;
    }
    
    public static DataAccess getDataAccess() {
        return dataAccess;
    }
    
    public static byte getServerId() {
        return serverId;
    }
    
    public static Cryptation getCryptation(){
        return CRYPTATION;
    }
    
    public static PacketReader getPacketReader(){
        return PACKET_READER;
    }
    
    public static PacketHandler getPacketHandler(){
        return PACKET_HANDLER;
    }
    
    public static PacketBuilderFactory getPacketBuilderFactory(){
        return PACKET_BUILDER_FACTORY;
    }
    
    public static KeyFactory getKeyFactory(){
        return KEY_FACTORY;
    }
    
    public static void send00CA(short creatureId){
        PacketBuilder builder = getPacketBuilderFactory().create((short)0x00CA);                       
                
        UInt8  npcCount = UInt8.valueOf((short)0x01);
        UInt32 npcId = UInt32.valueOf(0x02010011);
                        
        builder.put(npcCount);
        builder.put(npcId);
        
        /*byte unkData0[] = {
            0x6F, 0x5A
        };*/
        byte unkData0[] = {
            0x00, 0x00
        };
        
        builder.putByteArray(unkData0);
        
        short unkShort0 = 0x0007;
        builder.putShortLE(unkShort0);        
        
        short spawnX = 0x0020;
        short spawnY = 0x0040;
        
        builder.putShortLE(spawnX);
        builder.putShortLE(spawnY);                
        
        short x = 0x001C;
        short y = 0x003F;
        byte level = 0x03;
        int hpTotal = 0x68;
        //int hpAtual = 0x68;
        int hpAtual = hpTotal / 2;
        //short creatureId = 0x0028; // ????
                
        builder.putShortLE(x);
        builder.putShortLE(y);
        builder.putShortLE(creatureId);
        builder.putIntLE(hpTotal);
        builder.putIntLE(hpAtual);
        
        byte unkData2[] = {
            0x00, level, 0x00
        };
        
        builder.putByteArray(unkData2);
        
        Packet packet = builder.build();
        
        for(WorldSession session : SessionManager.getActiveSessionList()){
            session.sendPacket(packet);
        }                
    }
    
    public static void sendToAll(byte[] rawData){
        ByteBuffer data = ByteBuffer.wrap(rawData);
        data.order(ByteOrder.LITTLE_ENDIAN);
                
        data.getShort(); // Magic
        data.getShort(); // Size
        
        Packet packet = getPacketBuilderFactory()
                .create(data.getShort())
                .putByteArray(Arrays.copyOfRange(rawData, data.position(), data.capacity()))
                .build();                
        
        System.out.println("Pacote para enviar para todos: " + packet.toByteString());
        
        for(WorldSession session : SessionManager.getActiveSessionList()){
            session.sendPacket(packet);
        }
    }
    
    private static void notifyShutdown(){
        for(ServerListener serverListener : SERVER_LISTENERS){
            serverListener.onServerShutdown();
        }        
    }
    
    private static void notifyStartup(){
        for(ServerListener serverListener : SERVER_LISTENERS){
            serverListener.onServerStartup();
        }
    }
    
    static void start(byte serverId) throws InterruptedException{   
        final Logger logger = LoggerFactory.getLogger(ServerNode.class);
        
        running = true;
                
        Runtime.getRuntime().addShutdownHook(
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ServerNode.running = false;                    
                }
            })
        );
             
        SERVER_LISTENERS.add(AuraManager.getServerListener());
        SERVER_LISTENERS.add(ItemManager.getServerListener());
        SERVER_LISTENERS.add(MonsterManager.getServerListener());
        
        final List<Channel> channelList = ChannelManager.getServerChannels(serverId);
        final ExecutorService channelAcceptor = Executors.newFixedThreadPool(channelList.size());
        
        for(Channel channel : channelList){
            try{
                channelAcceptor.submit(channel.getAcceptor());
            }catch(Throwable t){
                t.printStackTrace();
            }
        }
        
        notifyStartup();
        
        logger.info("Server started and accepting connections...");
        
        long lastTick = System.currentTimeMillis();
        long currentTime, elapsedTime;
        
        while(running){
            currentTime = System.currentTimeMillis();
            elapsedTime = currentTime - lastTick;
            
            for(Channel currentChannel : channelList){
                currentChannel.update(elapsedTime);
            }
            
            lastTick = currentTime;
            Thread.sleep(10);
        }  
        
        notifyShutdown();
        SERVER_LISTENERS.clear();
        
        for(Channel channel : channelList){
            try{
                channel.getAcceptor().stop();                
            }catch(Throwable t){
                t.printStackTrace();
            }
        }
        
        try{
            channelAcceptor.awaitTermination(2, TimeUnit.MINUTES);
        }catch(Throwable t){
            t.printStackTrace();
        }
    }    
           
    static void requestStop(){
        running = false;
    }
    
    public static void main(String args[]){ 
        final Logger logger = LoggerFactory.getLogger(ServerNode.class);                                
        
        if(Configuration.developmentOpenConsole()){
            logger.info("Opening developer console...");
            
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new DeveloperConsole().setVisible(true);
                }
            });
        }
        
        try{
            ServerNode.start((byte)1);             
        }catch(Throwable t){
            t.printStackTrace();
        }
    }
}
