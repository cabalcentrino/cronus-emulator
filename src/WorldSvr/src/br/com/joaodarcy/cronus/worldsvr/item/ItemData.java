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

package br.com.joaodarcy.cronus.worldsvr.item;

import br.com.joaodarcy.cronus.worldsvr.character.CharacterClass;
import br.com.joaodarcy.cronus.worldsvr.character.StatBonus;
import br.com.joaodarcy.cronus.worldsvr.core.Player;
import br.com.joaodarcy.npersistence.core.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class ItemData {
    //@Column(value="",ignore=true)
    private final static Logger logger = LoggerFactory.getLogger(ItemData.class);    
    
    @Column("idt_itn_cod_item")
    private Integer id;
    @Column("idt_itp_cod_item_type")
    private ItemType type;
    @Column("idt_cla_cod_classe")
    private CharacterClass limitClass;
    @Column("idt_cla_cod_classe1")
    private CharacterClass class1;
    @Column("idt_cla_cod_classe2")
    private CharacterClass class2;
    @Column("idt_ipr_cod_item_property")
    private ItemPropertie propertie;
    @Column("idt_isi_cod_item_size")
    private ItemSize size;
    @Column("idt_limit_level")
    private Integer limitLevel;
    @Column("idt_limit_reputation")
    private Integer limitReputation;
    @Column("idt_stat_str2")
    private Integer statStr2;
    @Column("idt_stat_int2")
    private Integer statDex2;
    @Column("idt_stat_dex2")
    private Integer statInt2;
    @Column("idt_price_sell")
    private Integer priceSell;
    @Column("idt_value_lv")
    private Integer level;
    /** attack_rate__opt_1 */
    @Column("idt_att_rate")
    private Integer val0;
    /** defen_rate__opt_1_val__phy_att_max */
    @Column("idt_def_rate")
    private Integer val1;
    /** defense__lev_lmt__mag_att_val */
    @Column("idt_def_lev_lmt_mag_att_val")
    private Integer val2;
    @Column("idt_max_core")
    private Integer maxCore;
    /** str_lmt1__opt2 */
    @Column("idt_str_lmt1_opt2")      
    private Integer val3;
    /** dex_lmt_1__opt_2_val */
    @Column("idt_dex_lmt1_opt2_val")
    private Integer val4;
    /** int_lmt_1__opt_3 */
    @Column("idt_int_lmt1_opt3")
    private Integer val5;
    /** str_lmt_2__opt_3_val */
    @Column("idt_str_lmt2_opt3_val")
    private Integer val6;
    /** dex_lmt_2__opt_4 */
    @Column("idt_dex_lmt2_opt4")
    private Integer val7;
    /** int_lmt_2__opt_4_val */
    @Column("idt_int_lmt2_opt4_val")
    private Integer val8;
    @Column("idt_stat_str1")
    private Integer statStr1;
    @Column("idt_stat_dex1")
    private Integer statDex1;
    @Column("idt_stat_int1")
    private Integer statInt1;
    @Column("idt_grade")
    private Byte grade;
    @Column("idt_enchant_code_lnk")
    private Byte enchantCodeLnk;
    @Column("idt_period_type")
    private Byte periodType;
    @Column("idt_period_use")
    private Byte periodUse;
  
    public boolean canBeEquiped(Player player){
        if(isEquipable()){
            if(!hasNeededClass(player)){
                logger.error("HACK: Player {} cannot equip item, it is only for class {}", player.getCharacterIdx(), limitClass);
                return false;
            }
            if(!hasNeededLevel(player)){
                logger.error("HACK: Player {} cannot equip item, only level {} or higher can equip that", player.getCharacterIdx(), limitLevel);
                return false;
            }    
            if(!hasRequiredHonor(player)){
                logger.error("HACK: Player {} cannot equip item, does not have required honor {}", player.getCharacterIdx(), player.getHonorPoints());
                return false;
            }
            if(hasStatsLimitation()){
                if(!hasRequiredStats(player)){
                    logger.error("HACK: Player {} cannot equip item, does not have min needed stat", player.getCharacterIdx());
                    return false;
                }
            }
            return true;
        }else{
            logger.error("HACK: Player {} cannot equip an unequipable item {}", player.getCharacterIdx(), type);
            return false;
        }        
    }
    
    public Integer getAttackRate() {
        return val0;
    }

    public CharacterClass getClass1() {
        return class1;
    }

    public CharacterClass getClass2() {
        return class2;
    }
        
    public Integer getDefense(){
        return val2;
    }
    
    public Integer getDefenseRate(){
        return val1;
    }
    
    public Integer getDexLmt1(){
        return val4;
    }
    
    public Integer getDexLmt2(){
        return val7;
    }

    public Byte getEnchantCodeLnk() {
        return enchantCodeLnk;
    }

    public Byte getGrade() {
        return grade;
    }
    
    public Integer getId() {
        return id;
    }
    
    public Integer getIntLmt1(){
        return val5;
    }
    
    public Integer getIntLmt2(){
        return val8;
    }

    public Integer getLevel() {
        return level;
    }

    public CharacterClass getLimitClass() {
        return limitClass;
    }

    public Integer getLimitLevel() {
        return limitLevel;
    }

    public Integer getLimitReputation() {
        return limitReputation;
    }
   
    /**
     * Magic Attack
     * 
     * @return 
     */
    public Integer getMagicAttack(){
        return val2;
    }

    public Integer getMaxCore() {
        return maxCore;
    }
    
    public Integer getMaxLevel(){
        return val2;
    }
    
    public Integer getOpt1(){
       return val0; 
    }
            
    public Integer getOpt1Val(){
        return val1;
    }
    
    public Integer getOpt2(){
        return val3;
    }
    
    public Integer getOpt2Val(){
        return val4;
    }
    
    public Integer getOpt3(){
        return val5;
    }
    
    public Integer getOpt3Val(){
        return val6;
    }
    
    public Integer getOpt4(){
        return val7;
    }
    
    public Integer getOpt4Val(){
        return val8;
    }

    public Byte getPeriodType() {
        return periodType;
    }

    public Byte getPeriodUse() {
        return periodUse;
    }
    
    /**
     * Physical Attack
     * 
     * @return 
     */
    public Integer getAttack(){
        return val1;
    }

    public Integer getPriceSell() {
        return priceSell;
    }

    public ItemPropertie getPropertie() {
        return propertie;
    }

    public ItemSize getSize() {
        return size;
    }

    public Integer getStatDex1() {
        return statDex1;
    }

    public Integer getStatDex2() {
        return statDex2;
    }

    public Integer getStatInt1() {
        return statInt1;
    }

    public Integer getStatInt2() {
        return statInt2;
    }

    public Integer getStatStr1() {
        return statStr1;
    }

    public Integer getStatStr2() {
        return statStr2;
    }
        
    public Integer getStrLmt1(){
        return val3;
    }
    
    public Integer getStrLmt2(){
        return val6;                
    }    

    public ItemType getType() {
        return type;
    }
    
    protected boolean hasNeededClass(Player player){
        if(limitClass == CharacterClass.ALL){
            return true;
        }
        return player.getStyle().getCharacterClass() == limitClass;
    }
            
    protected boolean hasNeededLevel(Player player){
        return player.getLevel().intValue() >= limitLevel;
    }
    
    protected boolean hasRequiredHonor(Player player){
        return player.getHonorPoints().intValue() >= limitReputation;
    }
    
    protected boolean hasStatsLimitation() { 
        switch(type){
            case CONTROLADOR_DE_FORCA_ARCANA:
            case TWO_HANDS_SWORD:
            case ONE_HAND_SWORD:
            case GREVA:
            case LUVA:
            case TRAJE:
            case VISOR:
            case VISOR2:
                return true;
        }
        return false;
    }
                  
    protected boolean isEquipable(){
        switch(type){
            case BRACELETE:
            case BRINCO:
            case CONTROLADOR_DE_FORCA_ARCANA:
            case EPAULET:
            case EPAULET2:
            case TWO_HANDS_SWORD:
            case ONE_HAND_SWORD:
            case GEMA_ANIMA:
            case GREVA:
            case LUVA:
            case TRAJE:
            case VISOR:
            case VISOR2:
                return true;
        }
        return false;
    }
    
    public boolean isPerfectCore(){
        if(type == ItemType.UPGRADE_CORE){
            return getOpt1() == 1;
        }
        logger.warn("Item is not a upgrade core.");
        return false;
    }
    
    protected boolean hasRequiredStats(Player player){
        StatBonus statBonus = player.getStatBonus();
        if(statBonus.getStr() >= getStatStr1() || statBonus.getStr() >= getStatStr2()){
            if(statBonus.getDex() >= getStatDex1() || statBonus.getDex() >= getStatDex2()){
                if(statBonus.getInt() >= getStatInt1() || statBonus.getInt() >= getStatInt2()){
                    return true;
                }else{
                    logger.error("HACK: Player {} does not have min INT value", player.getCharacterIdx());
                }
            }else{
                logger.error("HACK: Player {} does not have min DEX value", player.getCharacterIdx());
            }
        }else{
            logger.error("HACK: Player {} does not have min STR value", player.getCharacterIdx());
        }
        return false;
    }
    
}
