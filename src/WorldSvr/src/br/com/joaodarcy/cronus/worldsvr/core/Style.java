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
import br.com.joaodarcy.cronus.worldsvr.character.CharacterClass;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class Style {
    
    /*
    // byte  class;                                                             // 09 GU / 0A DU / 0B MA / 0C AA / 0D GA / 0E EA
    // byte  face;                                                              // Min: 00, Max: 04
    // byte  hairStyle;                                                         // Min: 00, Max: 0C ??????
    // byte  sex;                                                               // 00 = Male , 04 = Female
    // byte  guild;                                                             // 00 = No Beginner Guild, 01 = Join Beginner Guild
    // byte  unk3;
    // byte  nameLength;
    // char  name[nameLength];*/
    
    private final static UInt32 CHARACTER_CLASS_WARRIOR             = UInt32.valueOf(0x1l);
    private final static UInt32 CHARACTER_CLASS_BLADER              = UInt32.valueOf(0x2l);
    private final static UInt32 CHARACTER_CLASS_WIZARD              = UInt32.valueOf(0x3l);
    private final static UInt32 CHARACTER_CLASS_FORCE_ARCHER        = UInt32.valueOf(0x4l);
    private final static UInt32 CHARACTER_CLASS_FORCE_SHIELDER      = UInt32.valueOf(0x5l);
    private final static UInt32 CHARACTER_CLASS_FORCE_BLADER        = UInt32.valueOf(0x6l);
        
    private final static UInt32 CHARACTER_SEX_FEMALE                = UInt32.valueOf(0x04000000l);
        
    /**
     * Formato
     *  1  2  3  4
     * 00 00 00 00
     * 
     * 1 = Sex
     * 2 = Hair
     * 3 = Face
     * 4 = Class 
     */
    private UInt32 style;
    
    private boolean male;
    private CharacterClass characterClass;

    public Style() throws IllegalStateException {
        this(UInt32.ZERO);
    }
    
    public Style(UInt32 style) throws IllegalStateException {
        this.style = style;
        if(!style.equals(UInt32.ZERO)){
            this.updateStyleInfo();
        }
    }
    
    public Style(long style) throws IllegalStateException {
        this(UInt32.valueOf(style));
    }
    
    public CharacterClass getCharacterClass() {
        return characterClass;
    }
            
    public UInt32 getStyle() {        
        return style;
    }    
    
    public boolean isFemale(){
        return !male;
    }
    
    public boolean isMale(){
        return male;
    }

    private boolean isMaskSet(UInt32 mask){
        return style.bitwiseAnd(mask).equals(mask);
    }
    
    public void setStyle(UInt32 style) {        
        this.style = style;  
        this.updateStyleInfo();
    }    
    //0402800B
    private void updateStyleInfo() throws IllegalStateException {
        if(isMaskSet(CHARACTER_CLASS_FORCE_BLADER)){
            this.characterClass = CharacterClass.FORCE_BLADER;
        }else if(isMaskSet(CHARACTER_CLASS_FORCE_SHIELDER)){
            this.characterClass = CharacterClass.FORCE_SHIELDER;
        }else if(isMaskSet(CHARACTER_CLASS_FORCE_ARCHER)){
            this.characterClass = CharacterClass.FORCE_ARCHER;
        }else if(isMaskSet(CHARACTER_CLASS_WIZARD)){
            this.characterClass = CharacterClass.WIZARD;
        }else if(isMaskSet(CHARACTER_CLASS_BLADER)){
            this.characterClass = CharacterClass.BLADER;
        }else if(isMaskSet(CHARACTER_CLASS_WARRIOR)){
            this.characterClass = CharacterClass.WARRIOR;
        }else{
            throw new IllegalStateException("Invalid class value");            
        }
        
        this.male = !isMaskSet(CHARACTER_SEX_FEMALE);
    }

    @Override
    public String toString() {
        return String.format("Class: %s, Sex: %s", characterClass, male ? "Male" : "Female");
    }    
}
