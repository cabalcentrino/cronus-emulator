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

/**
 * A chave do personagem é uma composição onde os 3 bits menos significativos
 * representão o slot onde o personagem esta cadastrado e o restante é o código
 * da conta do usuário deslocado para a esquerda em 3 posições.<br>
 * <br>
 * Para facilitar a utilização e a verificação de corretude dos valores passados
 * este objeto imutável deverá ser utilizado.
 * 
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class CharacterIdx {
    
    private final static byte SLOT_MIN = 0x00;
    private final static byte SLOT_MAX = 0x05;
    
    private final long idx;
    private final byte bytes[];

    public CharacterIdx(long idConta, byte slot) throws IllegalArgumentException {        
        if(slot >= SLOT_MIN && slot <= SLOT_MAX){
            this.idx = (((idConta & 0xFFFFFFFFl) << 3) | slot);
            this.bytes = makeBytes();
        }else{
            throw new IllegalArgumentException("Slot value must be in interval " + SLOT_MIN + " - " + SLOT_MAX + ", value entered: " + slot);
        }
    }
    
    public CharacterIdx(long characterIdx) {
        this.idx = (characterIdx & 0xFFFFFFFFl);
        this.bytes = makeBytes();
    }

    public CharacterIdx(int idx) {
        this.idx = (idx & 0xFFFFFFFFl);
        this.bytes = makeBytes();
    }   
    
    private byte[] makeBytes(){
        return new byte[]{
            (byte)((idx & 0x00000000000000FFl)),
            (byte)((idx & 0x000000000000FF00l) >> 8 ),
            (byte)((idx & 0x0000000000FF0000l) >> 16),
            (byte)((idx & 0x00000000FF000000l) >> 24)            
        };
    }

    public byte[] getBytes() {
        return bytes;
    }
    
    public long getCharacterId() {
        return idx;
    }
    
    public long getAccountId(){
        return ((idx & 0xFFFFFFF8l) >> 3);
    }
    
    public byte getSlot(){
        return (byte)(idx & 0xFFl);
    }

    @Override
    public String toString() {
        return String.format("{IDX: %08X}", idx);
    }
}
