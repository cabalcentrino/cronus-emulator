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

import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import br.com.joaodarcy.cronus.worldsvr.inventory.InventoryItem;
import br.com.joaodarcy.cronus.worldsvr.item.ItemData;
import br.com.joaodarcy.cronus.worldsvr.item.ItemManager;

/**
 * O item é composto por ID + DURATION + OPTIONS os três valores são uint32, 
 * e são utilizados nesta ordem.
 *  
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class Item {
                        
    /**
     * Mascara que mantém somente o código puro do item, remove inclusive
     * o status de ligado a conta ou não.
     */
    private final static UInt32 RAW_ITEM_MASK = UInt32.valueOf(0x00000FFF);
    
    /**
     * Mascara que mantém o status de ligado a conta ou não.
     */
    private final static UInt32 RAW_ITEM_MASK_EX = UInt32.valueOf(0x00001FFF);
    
    /*
     * ID Masks
     */    
    private final static UInt32 ACCOUNT_BOUND       = UInt32.valueOf(0x01000);
    
    private final static UInt32 UPGRADE_LEVEL[] = {
        UInt32.valueOf(0x02000),    // +1
        UInt32.valueOf(0x04000),    // +2
        UInt32.valueOf(0x06000),    // +3
        UInt32.valueOf(0x08000),    // +4
        UInt32.valueOf(0x0A000),    // +5
        UInt32.valueOf(0x0C000),    // +6
        UInt32.valueOf(0x0E000),    // +7
        UInt32.valueOf(0x10000),    // +8
        UInt32.valueOf(0x12000),    // +9
        UInt32.valueOf(0x14000),    // +10
        UInt32.valueOf(0x16000),    // +11
        UInt32.valueOf(0x18000),    // +12
        UInt32.valueOf(0x1A000),    // +13
        UInt32.valueOf(0x1C000),    // +14
        UInt32.valueOf(0x1E000)     // +15
    };           
    
    private final static byte MAX_UPGRADE_LEVEL = (byte)UPGRADE_LEVEL.length;
    
    private final static UInt32 OPTIONS_SLOT_WITHOUT_SLOT_MASK = UInt32.valueOf(0x0FFFFFFF);
    
    /*
     * Option Masks
     * 
     * TODO: Extensible and Unextensible
     */
    private final static UInt32 SLOTS[] = {
        UInt32.valueOf(0x10000000),     // 1 Slot
        UInt32.valueOf(0x20000000),     // 2 Slots
        UInt32.valueOf(0x30000000),     // 3 Slots
        UInt32.valueOf(0x40000000)      // 4 Slots
    };
        
    private final static byte MAX_SLOTS = (byte)SLOTS.length;
    
    private final static UInt32 CRAFT_EFFECT_OPTIONS[] = {
        UInt32.valueOf(0x1),
        UInt32.valueOf(0x2),
        UInt32.valueOf(0x3),
        UInt32.valueOf(0x4),
        UInt32.valueOf(0x5),
        UInt32.valueOf(0x6),
        UInt32.valueOf(0x7),
        UInt32.valueOf(0x8),
        UInt32.valueOf(0x9),
        UInt32.valueOf(0xA),
        UInt32.valueOf(0xB),
        UInt32.valueOf(0xC),
        UInt32.valueOf(0xD),
        UInt32.valueOf(0xE),
        UInt32.valueOf(0xF)
    };
    
    /**
     * Mascara para remoção da opção de craft escolhida
     */
    private final static UInt32 CLEAR_CRAFT_EFFECT_OPTION = UInt32.valueOf(0xFFFFFFF0);
    
    private final static UInt32 CRAFT_EFFECT_LEVEL[] = {
        UInt32.valueOf(0x90),
        UInt32.valueOf(0xA0),
        UInt32.valueOf(0xB0),
        UInt32.valueOf(0xC0),
        UInt32.valueOf(0xD0),
        UInt32.valueOf(0xE0),
        UInt32.valueOf(0xF0)
    };
    
    /**
     * Mascara para remoção do nível de effeito somente
     */
    private final static UInt32 CLEAR_CRAFT_EFFECT_LEVEL = UInt32.valueOf(0xFFFFFF0F);
    
    /**
     * Mascara para remoção do effeito de craft e do nível do efeito
     */
    private final static UInt32 CLEAR_CRAFT_EFFECT = UInt32.valueOf(0xFFFFFF00);
    
    /*
     * 0xFFFFFFFF
     *   ||||||||
     *   ||||||| ItemId
     *   |||||| ItemId
     *   ||||| ItemId
     *   |||| Upgrade Level - Account Bound
     *   ||| Upgrade Level
     *   || Unk
     *   | Unk
     *    Unk
     */
    private UInt32 id;   
    private UInt32 duration;
    
     /*
     * 0xFFFFFFFF
     *   ||||||||
     *   ||||||| Craft Effect or Item Count
     *   |||||| Craft Level or Item Count
     *   ||||| Unk
     *   |||| Unk
     *   ||| Unk
     *   || Unk
     *   | Unk
     *    Slots
     */
    private UInt32 options;

    private byte upgradeLevel;
    
    public Item(UInt32 itemId) {
        this(itemId , UInt32.ZERO, UInt32.ZERO);
    }
        
    public Item(UInt32 id, UInt32 duration, UInt32 options) {
        this.id = id;
        this.duration = duration;
        this.options = options;
        
        this.upgradeLevel = getUpgradeLevel(id);
    }
            
    public synchronized void decUpgradeLevel(){
        decUpgradeLevel((byte)1);
    }
    
    public synchronized void decUpgradeLevel(byte amount){
        setUpgradeLevel((byte)(upgradeLevel - amount));
    }
    
    public static Item from(InventoryItem item){
        return new Item(item.getItemId(), item.getItemOpt1(), item.getItemOpt2());
    }                                  
                                        
    public UInt32 getDuration() {
        return duration;
    }
    
    public byte getCraftLevel(){
        byte eflOptions = (byte)(CRAFT_EFFECT_LEVEL.length - 1);
        for( ; eflOptions >= 0 ; --eflOptions){
            if(options.bitwiseAnd(CRAFT_EFFECT_LEVEL[eflOptions]).equals(CRAFT_EFFECT_LEVEL[eflOptions])){
                break;
            }
        }
        return ++eflOptions;
    }
    
    public UInt32 getId() {
        return id;
    }
    
    public ItemData getItemData(){
        return ItemManager.getItemDataById(getRawItemId().intValue());
    }
    
    public UInt32 getOptions() {
        return options;
    }      
    
    public byte getSlotCount(){
         // TODO: Verificar se existe algum informação de slot antes
        byte slOptions = (byte)(MAX_SLOTS - 1);        
        for( ; slOptions >= 0 ; --slOptions){
            if(options.bitwiseAnd(SLOTS[slOptions]).equals(SLOTS[slOptions])){
                break;
            }
        }
        return ++slOptions;
    }
    
    public byte getUpgradeLevel() {
        return upgradeLevel;
    }
    
    protected static byte getUpgradeLevel(UInt32 itemId){
        // TODO: Verificar se existe algum informação de nível do item antes
        byte level = (byte)(MAX_UPGRADE_LEVEL - 1);        
        for( ; level >= 0 ; --level){
            if(itemId.bitwiseAnd(UPGRADE_LEVEL[level]).equals(UPGRADE_LEVEL[level])){
                break;
            }
        }
        return ++level;
    }        

    /**
     * Obtem o código puro do item, ignorando informações de aprimoramento.
     * 
     * @return 
     */
    public UInt32 getRawItemId(){
        return id.bitwiseAnd(RAW_ITEM_MASK);
    }
    
    public synchronized void incUpgradeLevel(){
        incUpgradeLevel((byte)1);
    }
    
    public synchronized void incUpgradeLevel(byte amount){
        setUpgradeLevel((byte)(upgradeLevel + amount));
    }
    
    public boolean isAccountBound(){
        return ACCOUNT_BOUND.bitwiseAnd(id).equals(ACCOUNT_BOUND);
    }
    
    public synchronized void setBound(boolean bound){
        if(bound){
            if(!isAccountBound()){
                this.id = id.or(ACCOUNT_BOUND);
            }
        }else{
            if(isAccountBound()){
                this.id = id.bitwiseXor(ACCOUNT_BOUND);
            }
        }
    }
    
    public void setDuration(UInt32 duration) {
        this.duration = duration;
    }
    
    public synchronized void setCraftEffect(byte effect){
        if(effect == 0){
            options = options.bitwiseAnd(CLEAR_CRAFT_EFFECT);
        }else{
            UInt32 effMask = CRAFT_EFFECT_OPTIONS[effect - 1];
            options = options.bitwiseAnd(CLEAR_CRAFT_EFFECT_OPTION).or(effMask);
        }
    }
    
    public synchronized void setCraftEffectLevel(byte effectLevel){
        if(effectLevel == 0){
            options = options.bitwiseAnd(CLEAR_CRAFT_EFFECT);
        }else{
            UInt32 eflMask = CRAFT_EFFECT_LEVEL[effectLevel - 1];
            options = options.bitwiseAnd(CLEAR_CRAFT_EFFECT_LEVEL).or(eflMask);
        }
    }    
     
    public void setId(UInt32 id) {
        this.id = id;
    }
    
    public void setOptions(UInt32 options) {
        this.options = options;
    }
    
    public synchronized  void setSlotsCount(byte slotsCount){
        // Ajusta para um intervalo permitido
        //slotsCount = (byte)(slotsCount & MAX_SLOTS); // Se descomentado da problema
        if(slotsCount == 0){
            options = options.bitwiseAnd(OPTIONS_SLOT_WITHOUT_SLOT_MASK);
        }else{            
            UInt32 slMask = SLOTS[slotsCount - 1];
            options = options.bitwiseAnd(OPTIONS_SLOT_WITHOUT_SLOT_MASK).or(slMask);
        }
    }
    
    public synchronized void setUpgradeLevel(byte upgradeLevel){
        // Ajusta para um intervalo permitido
        upgradeLevel = (byte)(upgradeLevel & MAX_UPGRADE_LEVEL);
                
        if(upgradeLevel == 0){
            this.id = id.bitwiseAnd(RAW_ITEM_MASK_EX);
        }else{
            // Obtem a mascara para o nível desejado
            UInt32 ulMask = UPGRADE_LEVEL[upgradeLevel - 1];
            this.id = id.bitwiseAnd(RAW_ITEM_MASK_EX).or(ulMask);
        }
        
        this.upgradeLevel = upgradeLevel;
    }
    
    @Override
    public String toString() {
        return String.format("{Item: %08X %08X %08X +%d [%d] Craft: %d}", id.longValue(), duration.longValue(), options.longValue(), upgradeLevel, getSlotCount(), getCraftLevel());
    }    
}
