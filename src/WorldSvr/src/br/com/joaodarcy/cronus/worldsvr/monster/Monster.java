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

package br.com.joaodarcy.cronus.worldsvr.monster;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class Monster {
    private short id;
    private float moveSpeed;
    private float chasSpeed;
    private boolean aggressive;        
    private long findInterval;
    private long moveInterval;
    private long chasInterval;
    private int scale;
    private int limitRange0;
    private int limitRange1;
    private int level;
    private int xp;
    private int hp;
    private int defense;
    private int attackRate;
    private int defenseRate;
    private int phyAttackMin1;
    private int phyAttackMax1;
    private int range;
    private boolean boss;

    private float movePerSecond;
    
    public Monster() {
        // TODO:
        this.id = 1;
        this.moveSpeed = 1.05f;
        this.chasSpeed = 2.90f;
        this.aggressive = false;
        this.scale = 1;
        this.findInterval = 10000l;
        this.moveInterval = 500l;
        this.chasInterval = 100l;
        this.limitRange0 = 32;
        this.limitRange1 = 40;
        this.level = 1;
        this.xp = 36;
        this.hp = 65;
        this.defense = 1;
        this.attackRate = 8;
        this.defenseRate = 3;
        this.phyAttackMin1 = 13;
        this.phyAttackMax1 = 17;
        this.range = 1;
        this.boss = false;
        
        this.movePerSecond = moveSpeed / moveInterval;
    }

    public short getId() {
        return id;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public float getChasSpeed() {
        return chasSpeed;
    }

    public boolean isAggressive() {
        return aggressive;
    }

    public float getMovePerSecond() {
        return movePerSecond;
    }
    
    public long getFindInterval() {
        return findInterval;
    }

    public long getMoveInterval() {
        return moveInterval;
    }

    public long getChasInterval() {
        return chasInterval;
    }

    public int getScale() {
        return scale;
    }

    public int getLimitRange0() {
        return limitRange0;
    }

    public int getLimitRange1() {
        return limitRange1;
    }

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }

    public int getHp() {
        return hp;
    }

    public int getDefense() {
        return defense;
    }

    public int getAttackRate() {
        return attackRate;
    }

    public int getDefenseRate() {
        return defenseRate;
    }

    public int getPhyAttackMin1() {
        return phyAttackMin1;
    }

    public int getPhyAttackMax1() {
        return phyAttackMax1;
    }

    public int getRange() {
        return range;
    }

    public boolean isBoss() {
        return boss;
    }    
}
