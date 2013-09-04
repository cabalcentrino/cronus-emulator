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

package br.com.joaodarcy.cronus.worldsvr.character;

import br.com.joaodarcy.cronus.worldsvr.Configuration;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class StatBonus {
    
    private final static int MIN_STAT_POINTS    = 0;
    
    private final static int MIN_STR            = 0;
    private final static int MIN_INT            = 0;
    private final static int MIN_DEX            = 0; 
    
    private final static int MAX_STR            = Configuration.maxStr();
    private final static int MAX_INT            = Configuration.maxInt();
    private final static int MAX_DEX            = Configuration.maxDex();
    
    /** STR */
    private int strenght;
    
    /** INT */
    private int inteligence;
    
    /** DEX */
    private int dextr; // FIXME: I don´t know this world

    private int statPoints;
    
    public StatBonus() {
        this(MIN_STR, MIN_INT, MIN_DEX, MIN_STAT_POINTS);
    }
    
    public StatBonus(int strenght, int inteligence, int dextr/* FIXME: I don´t know this world */, int statPoints) {
        this.strenght = strenght;
        this.inteligence = inteligence;
        this.dextr = dextr;
        this.statPoints = statPoints;
    }
    
    public synchronized void addStatPoint(){
        ++statPoints;
    }
    
    public synchronized boolean addDex(){
        if(dextr < MAX_DEX && decStatPoints()){
            ++dextr;
            return true;
        }
        return false;
    }
    
    public synchronized boolean addStr(){
        if(strenght < MAX_STR && decStatPoints()){
            ++strenght;
            return true;
        }
        return false;
    }
            
    public synchronized boolean addInt(){
        if(inteligence < MAX_INT && decStatPoints()){
            ++inteligence;
            return true;
        }
        return false;
    }
    
    public synchronized boolean decDex(){
        if(dextr > MIN_DEX){
            --dextr;
            addStatPoint();
            return true;
        }
        return false;
    }
    
    public synchronized boolean decStr(){
        if(strenght > MIN_STR){
            --strenght;            
            addStatPoint();
            return true;
        }
        return false;
    }
    
    public synchronized boolean decInt(){
        if(inteligence > MIN_INT){
            --inteligence;
            addStatPoint();
            return true;
        }
        return false;
    }
    
    private boolean decStatPoints(){
        if(statPoints > MIN_STAT_POINTS){
            --statPoints;
            return true;
        }
        return false;
    }
            
    public int getStr() {
        return strenght;
    }

    public int getInt() {
        return inteligence;
    }

    public int getDex() {
        return dextr;
    }  

    public int getStatPoints() {
        return statPoints;
    }

    public void setStr(int strenght) {
        this.strenght = strenght;
    }

    public void setInt(int inteligence) {
        this.inteligence = inteligence;
    }

    public void setDestreza(int destreza) {
        this.dextr = destreza;
    }

    public void setStatPoints(int statPoints) {
        this.statPoints = statPoints;
    }
}
