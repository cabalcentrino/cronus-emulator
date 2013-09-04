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

package br.com.joaodarcy.cronus.worldsvr.world;

import br.com.joaodarcy.cronus.cabal.core.types.UInt16;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.cronus.cabal.core.types.UInt8;
import br.com.joaodarcy.cronus.worldsvr.monster.Monster;
import br.com.joaodarcy.cronus.worldsvr.monster.MonsterManager;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class WorldMob extends WorldObject {  
    private final UInt32 worldId;

    private final Monster monster;
    
    private UInt32 hp;    
    private final UInt8 level;
       
    private final int spawnInterval;
    
    private long findTimer;
    //private long moveTimer;
    private long movementTimer;
    //private long chasTimer;
    private long spawnTimer;
    
    private int movingX;
    private int movingY;
    
    private final int spawnX;
    private final int spawnY;
            
    private enum MobState {        
        CHANGING,
        FINDING,
        MOVING,
        DEADED
    }
    
    private MobState state;
    
    public WorldMob(UInt32 worldId, int monsterId, int x, int y) {
        super(x, y);
        
        this.movingX = this.spawnX = x;
        this.movingY = this.spawnY = y;
        
        this.worldId = worldId;
        this.monster = MonsterManager.getMonsterById(monsterId);
        this.hp = UInt32.valueOf(monster.getHp());
        this.level = UInt8.ONE; // FIXME
        
        //this.moveTimer = 0;
        //this.chasTimer = 0; 
        this.spawnTimer = 0;
        this.findTimer = 0;
        
        this.spawnInterval = 12000;
        
        this.state = MobState.FINDING;
    }

    public void endMove(){
        this.state = MobState.CHANGING;
        
        //moveTimer = 0l;        
        relocate(movingX, movingY);
        
        this.state = MobState.FINDING;
    }
    
    public void beginMove(int x, int y){
        this.state = MobState.CHANGING;
        
        int currentX, currentY, moveCount = 0;
                                        
        currentX = position.getX().intValue();
        currentY = position.getY().intValue();
        
        if(currentX > x){
            moveCount += currentX - x;
        }else if(currentX < x){
            moveCount += x - currentX;
        }
        
        if(currentY > y){
            moveCount += currentY - y;
        }else if(currentY < y){
            moveCount += y - currentY;
        }
        
        if(moveCount > 0){        
            this.movingX = x;
            this.movingY = y;

            this.movementTimer = (long)((moveCount / monster.getMoveSpeed()) * 1000l);

            this.state = MobState.MOVING;            
            this.grid.beginMovement(this);
        }
    }
    
    public UInt32 getHp() {
        return hp;
    }

    public UInt32 getMaxHp() {
        return UInt32.valueOf(monster.getHp());
    }

    public UInt8 getLevel() {
        return level;
    }
    
    public UInt32 getWorldId() {
        return worldId;
    }

    public UInt16 getMonsterId() {
        return UInt16.valueOf(monster.getId());
    }    

    public int getMovingX() {
        return movingX;
    }

    public int getMovingY() {
        return movingY;
    }   
            
    @Override
    public void onGridCellChange() {
        
    }

    @Override
    public void onObjectBeginMovement(GridObject o){
        
    }
    
    @Override
    public void onObjectEnterGrid(GridObject o, int worldX, int worldY) {
        
    }

    @Override
    public void onObjectExitGrid(GridObject o, int worldX, int worldY) {
        
    }

    @Override
    public void onObjectMoved(GridObject o, int worldX, int worldY) {
        
    }   
    
    private void resetTimers(){
        this.findTimer = 0;
        this.movementTimer = 0;
        this.spawnTimer = 0;
    }
    
    private void respawn(){
        this.state = MobState.CHANGING;
        
        relocate(spawnX, spawnY);
        hp = UInt32.valueOf(monster.getHp());
        resetTimers();
        state = MobState.FINDING;
    }
    
    @Override
    public void update(long elapsed) {
        if(state == MobState.FINDING){
            findTimer += elapsed;
            if(findTimer >= monster.getFindInterval()){
                resetTimers();
                beginMove(position.getX().intValue() + 1, position.getY().intValue());                
            }                                    
        }else if(state == MobState.MOVING){
            movementTimer -= elapsed;
            if(movementTimer <= 0){
                resetTimers();
                endMove();                
            }
        }else if(state == MobState.DEADED){
            spawnTimer += elapsed;
            if(spawnTimer >= spawnInterval){
                respawn();
            }
        }
    }
}
