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

package br.com.joaodarcy.cronus.worldsvr.core;

import br.com.joaodarcy.cronus.cabal.core.network.Packet;
import br.com.joaodarcy.cronus.cabal.core.network.PacketBuilder;
import br.com.joaodarcy.cronus.cabal.core.types.UInt16;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.cronus.cabal.core.types.UInt8;
import br.com.joaodarcy.cronus.worldsvr.ServerNode;
import br.com.joaodarcy.cronus.worldsvr.inventory.Inventory;
import br.com.joaodarcy.cronus.worldsvr.channel.Channel;
import br.com.joaodarcy.cronus.worldsvr.character.GameCharacter;
import br.com.joaodarcy.cronus.worldsvr.character.Nation;
import br.com.joaodarcy.cronus.worldsvr.character.StatBonus;
import br.com.joaodarcy.cronus.worldsvr.character.WeaponRank;
import br.com.joaodarcy.cronus.worldsvr.equipment.Equipment;
import br.com.joaodarcy.cronus.worldsvr.quickbar.ActionBar;
import br.com.joaodarcy.cronus.worldsvr.quickbar.QuickBarManager;
import br.com.joaodarcy.cronus.worldsvr.skillbook.SkillBook;
import br.com.joaodarcy.cronus.worldsvr.skillbook.SkillBookManager;
import br.com.joaodarcy.cronus.worldsvr.storage.StorageController;
import br.com.joaodarcy.cronus.worldsvr.world.GridObject;
import br.com.joaodarcy.cronus.worldsvr.world.World;
import br.com.joaodarcy.cronus.worldsvr.world.WorldMob;
import br.com.joaodarcy.cronus.worldsvr.world.WorldObject;
import br.com.joaodarcy.npersistence.Transaction;
import br.com.joaodarcy.npersistence.core.DataAccessException;
import br.com.joaodarcy.npersistence.util.JDBCUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class Player extends WorldObject {   
    
    // Constanst 
    private final static double radiousOfInterest = 2.0d;
    private final static Logger logger = LoggerFactory.getLogger(Player.class);        
    
    // FIXME: Wrong place, only for quick testing
    private final static String SQL_GET_COMPLEMENTARY_INFO_FOR_ENTERING_WORLD = ServerNode.getPersistenceContext().getQueryManager().getQueryByName("character.getComplementaryInfoForEnteringWorld");
    private final static String SQL_GET_ALL_INFO_FOR_ENTERING_WORLD = ServerNode.getPersistenceContext().getQueryManager().getQueryByName("character.getAllInfoForEnteringWorld");    
    // State
    
    private final CharacterIdx characterIdx; // GameCharacter has this
    private final Style style; // GameCharacter has this
    
    private String name; // GameCharacter has this
    private UInt32 level; // GameCharacter has this
    
    private Nation nation; // GameCharacter has this
        
    private final StatBonus statBonus;
    
    private WeaponRank swordRank; // GameCharacter has this
    private WeaponRank magicRank; // GameCharacter has this
    
    private UInt32 honorPoints;
    
    private World world; // GameCharacter has this

    private UInt32 alz; /* FIXME: Changed to UIn64 in EP 8 */
    
    private UInt32 warpMask;
    private UInt32 mapsMask;
    
    private UInt32 exp; /* FIXME: Changed to UIn64 in EP 8 */
    /** WEXP */
    private UInt32 warExp; /* FIXME: UInt64 value */
    
    private UInt16 hp;
    private UInt16 maxHP;
    
    private UInt16 mp;
    private UInt16 maxMP;
    
    private UInt16 sp;
    private UInt16 maxSP;
    
    private UInt32 magicExp;
    private UInt32 swordExp;
    private UInt32 rankExp;
           
    private final SkillBook skillBook;
    private final ActionBar quickBar;
    
    private final StorageController storageController;

    private final QuestLog questLog;
    
    private final WorldSession session;
    
    public Player(CharacterIdx idx, StorageController storageController, Style style, SkillBook skillBook, ActionBar quickBar, QuestLog questLog, int x, int y) {
        super(x, y);
        this.session = WorldSession.getCurrentWorldSession();
        this.characterIdx = idx;
        this.style = style;
        this.skillBook = skillBook;
        this.quickBar = quickBar;
        this.questLog = questLog;
        this.statBonus = new StatBonus();
        this.storageController  = storageController;
        this.warExp = UInt32.ZERO;  // FIXME: Only for testing     
    }
    
    private Player(long rawCharacterIdx, StorageController storageController, long rawStyle, SkillBook skillBook, ActionBar actionBar, QuestLog questLog, int x, int y) {
        super(x, y);
        this.session = WorldSession.getCurrentWorldSession();
        this.characterIdx = new CharacterIdx(rawCharacterIdx);
        this.style = new Style(rawStyle);        
        this.skillBook = skillBook;
        this.quickBar = actionBar;
        this.questLog = questLog;        
        this.statBonus = new StatBonus();
        this.storageController = storageController;
    }
    
    public static Player fromCharacter(GameCharacter character) throws IllegalArgumentException {
        if(character == null){
            throw new IllegalArgumentException("Param character must not be null.");
        }
        Player player = fromDatabaseLite(character);
        
        if(player != null){
            player.alz = character.getAlz();
            player.world = Channel.getWorldById(character.getWorld());
            player.nation = Nation.getNationById(character.getNation());
            player.level = character.getLevel();
            player.name = character.getName();            
            player.swordRank = WeaponRank.getWeaponRankById(character.getSwordRank());
            player.magicRank = WeaponRank.getWeaponRankById(character.getSwordRank());
            
            return player;
        }        
        return null;
    }
    
    // TODO: Move to a DAO object
    private static Player fromDatabaseLite(GameCharacter character) throws IllegalArgumentException {
        if(character == null){
            throw new IllegalArgumentException("Param character must not be null.");
        }
        
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
                
        try{
            CharacterIdx idx = character.getCharacterIdx();
            connection = ServerNode.getConnectionFactory().openConnection();
                                    
            SkillBook skillBook = SkillBookManager.carregarSkillsDoPersonagem(
                connection, 
                idx
            );
            
            ActionBar quickBar = QuickBarManager.carregarBarraRapidaDoPersonagem(
                connection, 
                idx
            );
                        
            StorageController storageController = new StorageController(connection, idx, character.getEquipment());
            Style estilo = new Style(character.getStyle());
            QuestLog questLog = QuestLogManager.getPlayerQuestLog(connection, idx);
            
            Player player = new Player(
                idx, 
                storageController,
                estilo,                 
                skillBook,
                quickBar,                
                questLog,
                character.getX().intValue(),
                character.getY().intValue()
            );
            
            preparedStatement = connection.prepareStatement(SQL_GET_COMPLEMENTARY_INFO_FOR_ENTERING_WORLD);
            preparedStatement.setLong(1, idx.getCharacterId());
            preparedStatement.setByte(2, ServerNode.getServerId());
            
            resultSet = preparedStatement.executeQuery();
            
            if(resultSet.next()){                                
                player.statBonus.setStr(resultSet.getInt(1));
                player.statBonus.setInt(resultSet.getInt(2));
                player.statBonus.setDestreza(resultSet.getInt(3));
                player.statBonus.setStatPoints(resultSet.getInt(4));
                
                player.honorPoints = UInt32.valueOf(resultSet.getLong(5));
                player.warpMask = UInt32.valueOf(resultSet.getInt(6));
                player.mapsMask = UInt32.valueOf(resultSet.getInt(7));
                player.exp = UInt32.valueOf(resultSet.getLong(8));
                player.hp = UInt16.valueOf(resultSet.getInt(9));
                player.maxHP = UInt16.valueOf(resultSet.getInt(10));
                player.mp = UInt16.valueOf(resultSet.getInt(11));
                player.maxMP = UInt16.valueOf(resultSet.getInt(12));
                player.sp = UInt16.valueOf(resultSet.getInt(13));
                player.maxSP = UInt16.valueOf(resultSet.getInt(14));
                player.magicExp = UInt32.valueOf(resultSet.getInt(15));
                player.swordExp = UInt32.valueOf(resultSet.getInt(16));
                player.rankExp = UInt32.valueOf(resultSet.getInt(17));                                
            }else{
                throw new DataAccessException("Nothing found for character " + character);
            }            
            
            return player;
        }catch(Throwable t){
            logger.error("Error loading complementary information of character {}", character, t);
        }finally{
            JDBCUtil.closeResultSet(resultSet);
            JDBCUtil.closePreparedStatement(preparedStatement);            
            JDBCUtil.closeConnection(connection);
        }                                
        
        return null;
    }
    
    // Move to a DAO object
    public static Player fromDatabase(CharacterIdx idx) throws IllegalArgumentException {
        if(idx == null){
            throw new IllegalArgumentException("idx param must not be null.");
        }            
                       
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try{
            connection = ServerNode.getConnectionFactory().openConnection();
                        
            SkillBook skillBook = SkillBookManager.carregarSkillsDoPersonagem(
                connection, 
                idx
            );
            
            ActionBar quickBar = QuickBarManager.carregarBarraRapidaDoPersonagem(
                connection, 
                idx
            );
                                                            
            StorageController storageController = new StorageController(connection, idx);
            
            QuestLog questLog = QuestLogManager.getPlayerQuestLog(
                connection, 
                idx
            );
            
            Player player = null;
            
            preparedStatement = connection.prepareStatement(SQL_GET_ALL_INFO_FOR_ENTERING_WORLD);
            preparedStatement.setLong(1, idx.getCharacterId());
            preparedStatement.setByte(2, ServerNode.getServerId());
            
            resultSet = preparedStatement.executeQuery();
     
            if(resultSet.next()){
                int x = resultSet.getInt(25), y = resultSet.getInt(26);
                Style styles = new Style(resultSet.getLong(22));
                
                player = new Player(
                    idx, 
                    storageController,
                    styles,                     
                    skillBook,
                    quickBar,
                    questLog,
                    x,
                    y
                );
                
                
                player.statBonus.setStr(resultSet.getInt(1));
                player.statBonus.setInt(resultSet.getInt(2));
                player.statBonus.setDestreza(resultSet.getInt(3));
                player.statBonus.setStatPoints(resultSet.getInt(4));
                
                player.honorPoints = UInt32.valueOf(resultSet.getLong(5));
                player.warpMask = UInt32.valueOf(resultSet.getInt(6));
                player.mapsMask = UInt32.valueOf(resultSet.getInt(7));
                player.exp = UInt32.valueOf(resultSet.getLong(8));
                player.hp = UInt16.valueOf(resultSet.getInt(9));
                player.maxHP = UInt16.valueOf(resultSet.getInt(10));
                player.mp = UInt16.valueOf(resultSet.getInt(11));
                player.maxMP = UInt16.valueOf(resultSet.getInt(12));
                player.sp = UInt16.valueOf(resultSet.getInt(13));
                player.maxSP = UInt16.valueOf(resultSet.getInt(14));
                player.magicExp = UInt32.valueOf(resultSet.getInt(15));
                player.swordExp = UInt32.valueOf(resultSet.getInt(16));
                player.rankExp = UInt32.valueOf(resultSet.getInt(17));  
                
                player.world = Channel.getWorldById(UInt8.valueOf(resultSet.getShort(18)));
                player.nation = Nation.getNationById(UInt8.valueOf(resultSet.getByte(19)));
                player.swordRank = WeaponRank.getWeaponRankById(UInt8.valueOf(resultSet.getByte(20)));
                player.magicRank = WeaponRank.getWeaponRankById(UInt8.valueOf(resultSet.getByte(21)));
                player.name = resultSet.getString(23);
                player.level = UInt32.valueOf(resultSet.getByte(24));
                player.alz = UInt32.valueOf(resultSet.getLong(27));
            }else{
                throw new DataAccessException("Nothing found for character " + idx);
            }            
            
            return player;
        }catch(Throwable t){
            logger.error("Cannot load character information {}", idx, t);
        }finally{
            JDBCUtil.closeResultSet(resultSet);
            JDBCUtil.closePreparedStatement(preparedStatement);            
            JDBCUtil.closeConnection(connection);
        }                                
        
        return null;
    }

    public UInt32 getAlz() {
        return alz;
    }
    
    public Inventory getInventory() {
        return storageController.getInventory();
    }
    
    public int getStatPoints() {
        return statBonus.getStatPoints();
    }
    
    public CharacterIdx getCharacterIdx() {
        return characterIdx;
    }
    
    public int getDex() {
        return statBonus.getDex();
    }
    
    public Equipment getEquipment() {
        return storageController.getEquipment();
    }
        
    public UInt32 getExp() {
        return exp;
    }

    public UInt32 getWarExp() {
        return warExp;
    }
    
    public UInt32 getMagicExp() {
        return magicExp;
    }
    
    public UInt32 getSwordExp() {
        return swordExp;
    }
    
    public Style getStyle() {
        return style;
    }
    
    public int getStr() {
        return statBonus.getStr();
    }

    public UInt32 getHonorPoints() {
        return honorPoints;
    }        

    public UInt16 getHP() {
        return hp;
    }

    public UInt16 getMaxHP() {
        return maxHP;
    }   
    
    public int getInt() {
        return statBonus.getInt();
    }
    
    public ActionBar getQuickBar() {
        return quickBar;
    }
           
    public World getWorld() {
        return world;
    }

    public UInt32 getMapsMask() {
        return mapsMask;
    }

    public UInt16 getMP() {
        return mp;
    }

    public UInt16 getMaxMP() {
        return maxMP;
    }    
    
    public Nation getNation() {
        return nation;
    }
    
    public String getName() {
        return name;
    }

    public UInt32 getLevel() {
        return level;
    }
        
    public QuestLog getQuestLog() {
        return questLog;
    }

    public double getRadiousOfInterest() { // TODO: Future, world grid
        return radiousOfInterest;
    }   
    
    public WeaponRank getSwordRank() {
        return swordRank;
    }

    public UInt32 getRankExp() {
        return rankExp;
    }

    public WeaponRank getMagicRank() {
        return magicRank;
    }
    
    public SkillBook getSkillBook() {
        return skillBook;
    }

    public StorageController getStorageController() {
        return storageController;
    }        

    public UInt16 getSP() {
        return sp;
    }

    public UInt16 getMaxSP() {
        return maxSP;
    }      

    public StatBonus getStatBonus() {
        return statBonus;
    }
           
    public UInt32 getWarpFlag() {
        return warpMask;
    }
    
    public void onEnterWorld(){
        world.add(this);
    }

    @Override
    public void onObjectBeginMovement(GridObject o) { // FIXME: Only for testing
        if(o instanceof WorldMob){
            notifyMobMoveBegin((WorldMob)o);
        }
    }
    
    @Override
    public void onGridCellChange() { // FIXME: Only for testing
        List<GridObject> cellContentList = grid.getObjectsInGrid(this);
        List<WorldMob> mobList = new ArrayList<>();
        
        for(GridObject o : cellContentList){
            if(o == this){
                continue;
            }
            if(o instanceof WorldMob){
                mobList.add((WorldMob)o);
            }
        }
        
        notifyMobs(mobList);
    }
    
    private void notifyMobMoveBegin(WorldMob mob){ // FIXME: Only for testing
        PacketBuilder packetBuilder = ServerNode.getPacketBuilderFactory().create((short)0x00D5);
        
        packetBuilder.put(mob.getWorldId());
        packetBuilder.put(UInt16.valueOf(35176)); // Unk ( Heading maybe )
        packetBuilder.put(UInt16.valueOf(16757)); // Unk ( Heading maybe )
        packetBuilder.put(mob.getX());
        packetBuilder.put(mob.getY());
        packetBuilder.put(UInt16.valueOf(mob.getMovingX()));
        packetBuilder.put(UInt16.valueOf(mob.getMovingY()));
        
        send(packetBuilder.build());        
    }
    
    private void notifyMobMoveEnd(WorldMob mob){ // FIXME: Only for testing      
        PacketBuilder packetBuilder = ServerNode.getPacketBuilderFactory().create((short)0x00D6);                
        packetBuilder.put(mob.getWorldId());
        packetBuilder.put(mob.getX());
        packetBuilder.put(mob.getY());        
        send(packetBuilder.build());        
    }
    
    private void notifyMob(WorldMob mob){ // FIXME: Only for testing         
        PacketBuilder packetBuilder = ServerNode.getPacketBuilderFactory().create((short)0x00CA);        
        packetBuilder.putByte((byte)0x01);
        
        packetBuilder.put(mob.getWorldId());
        packetBuilder.put(mob.getX());
        packetBuilder.put(mob.getY());
        packetBuilder.put(UInt16.valueOf(mob.getMovingX()));
        packetBuilder.put(UInt16.valueOf(mob.getMovingY()));
        packetBuilder.put(mob.getMonsterId());
        packetBuilder.put(mob.getHp());
        packetBuilder.put(mob.getMaxHp());
        packetBuilder.putByte((byte)0x00); // Unk                    
        packetBuilder.put(mob.getLevel());                    
        packetBuilder.putByte((byte)0x00); // Unk
        packetBuilder.putShortLE((short)0x0000); // Unk
        packetBuilder.putIntLE((short)0x00000000); // Unk       
        
        send(packetBuilder.build());        
    }
    
    private void notifyMobs(List<WorldMob> mobList){ // FIXME: Only for testing       
        if(mobList.isEmpty()){
            return;
        }
        PacketBuilder packetBuilder = ServerNode.getPacketBuilderFactory().create((short)0x00CA);        
        packetBuilder.putByte((byte)mobList.size()); // MOB Count
        for(WorldMob mob : mobList){
            packetBuilder.put(mob.getWorldId());
            packetBuilder.put(mob.getX());
            packetBuilder.put(mob.getY());
            packetBuilder.put(UInt16.valueOf(mob.getMovingX()));
            packetBuilder.put(UInt16.valueOf(mob.getMovingY()));
            packetBuilder.put(mob.getMonsterId());
            packetBuilder.put(mob.getHp());
            packetBuilder.put(mob.getMaxHp());
            packetBuilder.putByte((byte)0x00); // Unk                   
            packetBuilder.put(mob.getLevel());                    
            packetBuilder.putByte((byte)0x00); // Unk
            packetBuilder.putShortLE((short)0x0000); // Unk
            packetBuilder.putIntLE((short)0x00000000); // Unk
        }   
        
        send(packetBuilder.build());        
    }    
    
    public void send(Packet packet){
        session.sendPacket(packet);
    }
    
    public synchronized void save(){
        Connection connection;
        Transaction transaction = null;
        
        try{
            connection = ServerNode.getConnectionFactory().openConnection();
            transaction = Transaction.begin(connection);
            
            storageController.save(connection);
            
            transaction.commit();            
        }catch(Throwable t){
            logger.error("Error saving character {}", characterIdx, t);
        }finally{
            if(transaction != null && transaction.isOpen()){
                transaction.rollback();
            }
        }
    }    

    @Override
    public void onObjectEnterGrid(GridObject o, int worldX, int worldY) {        
        if(o instanceof WorldMob){            
            notifyMob((WorldMob)o);
        }
    }

    @Override
    public void onObjectExitGrid(GridObject o, int worldX, int worldY) {
        if(o instanceof WorldMob){            
            notifyMobMoveEnd((WorldMob)o);
        }
    }

    @Override
    public void onObjectMoved(GridObject o, int worldX, int worldY) {        
        if(o instanceof WorldMob){            
            notifyMobMoveEnd((WorldMob)o);
        }
    }
}
