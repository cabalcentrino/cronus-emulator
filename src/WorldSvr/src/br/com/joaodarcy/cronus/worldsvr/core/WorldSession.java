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

import br.com.joaodarcy.cronus.cabal.core.ClientSession;
import br.com.joaodarcy.cronus.worldsvr.ServerNode;
import br.com.joaodarcy.cronus.worldsvr.account.Account;
import br.com.joaodarcy.cronus.worldsvr.channel.Channel;
import br.com.joaodarcy.cronus.worldsvr.character.GameCharacter;
import java.net.Socket;
import java.util.List;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class WorldSession extends ClientSession {
    private final long accountId;    
    private final Channel channel;
    private final Account account;
    
    private ClientState clientState;    
    private Player player;
    private List<GameCharacter> gameCharacterList = null;
        
    WorldSession(Channel channel, Socket socket, long accountId) {
        super(ServerNode.getPacketHandler(), ServerNode.getPacketReader(), socket, ServerNode.getCryptation(), ServerNode.getKeyFactory().create());
        this.channel = channel;
        this.accountId = accountId;
        this.clientState = ClientState.CONNECTED;
        
        this.account = new Account();
        this.account.setId(accountId);
    }

    public Account getAccount() {
        return account;
    }
    
    public Channel getChannel() {
        return channel;
    }
    
    public static WorldSession getCurrentWorldSession(){
        return (WorldSession) getCurrentSession();
    }
    
    public synchronized void changeState(ClientState newClientState){
        if(clientState == ClientState.IN_WORLD){
            if(player != null){
                player.save();
                player = null;
            }            
        }
        clientState = newClientState;        
    }

    public GameCharacter getGameCharacterFromCache(int int32Idx){
        long idx = (int32Idx & 0xFFFFFFFFl);
        for(GameCharacter gameCharacter : gameCharacterList){
            if(gameCharacter.getCharacterIdx().getCharacterId() == idx){
                return gameCharacter;
            }
        }
        return null;
    }
    
    public boolean isGameCharacterInCache(){
        return gameCharacterList != null;
    }
    
    public void setGameCharacterList(List<GameCharacter> gameCharacterList) {
        this.gameCharacterList = gameCharacterList;
    }
    
    public void clearGameCharacterListCache(){
        this.gameCharacterList = null;
    }
    
    public List<GameCharacter> getGameCharacterList() {
        return gameCharacterList;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public void setPlayer(Player player) {        
        this.player = player;
        if(player != null){
            player.onEnterWorld();
        }
    }
    
    public ClientState getClientState(){
        return clientState;
    }

    public long getAccountId() {
        return accountId;
    }  

    @Override
    protected void onSessionStart() {
        SessionManager.onSessionStart(this);
    }

    @Override
    protected void onSessionStop() {
        if(player != null){
            player.save();
            player = null;
        }
        SessionManager.onSessionStop(this);
    }    
}
