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

package br.com.joaodarcy.cronus.cabal.core;

import br.com.joaodarcy.cronus.cabal.core.crypt.*;
import br.com.joaodarcy.cronus.cabal.core.crypt.EP8CryptationFactory;
import br.com.joaodarcy.cronus.cabal.core.crypt.EP8KeyFactory;
import br.com.joaodarcy.cronus.cabal.core.patterns.Factory;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class CabalS2CypherTest {

    private final UInt32 authKey = new UInt32(0x34bc821bl);
    private Factory<Cryptation> cryptationFactory = new EP8CryptationFactory();
    private KeyFactory keyFactory = new EP8KeyFactory();    
    
    private class PacketData {
        private final byte encrypted[];
        private final byte decrypted[];
        private final boolean setupAuthKey;        
        
        public PacketData(String label, byte[] encrypted, byte[] decrypted, boolean setupAuthKey) {            
            this.encrypted = encrypted;
            this.decrypted = decrypted;
            this.setupAuthKey = setupAuthKey;
        }                        
    }
    
    private final List<PacketData> clientPacketDataList;
    private final List<PacketData> serverPacketDataList;

    public CabalS2CypherTest() {
        this.clientPacketDataList = new ArrayList<PacketData>();
        this.serverPacketDataList = new ArrayList<PacketData>();
        
        this.initClientPacketDataList();
        this.initServerPacketDataList();
    }
    
    private void addPacketData(List<PacketData> out, final PacketData packetData){
        out.add(packetData);
    }
    
    private void addClientPacketData(char[] encrypted, char[] decrypted){
        addClientPacketData(encrypted, decrypted, false);
    }
    
    private void addClientPacketData(char[] encrypted, char[] decrypted, boolean swap){
        addPacketData(clientPacketDataList, null, encrypted, decrypted, swap);
    }
    
    private void addServerPacketData(char[] encrypted, char[] decrypted){
        addServerPacketData(encrypted, decrypted, false);
    }
    
    private void addServerPacketData(char[] encrypted, char[] decrypted, boolean swap){
        addPacketData(serverPacketDataList, null, encrypted, decrypted, swap);
    }
    
    private void addPacketData(List<PacketData> out, String label, char[] encrypted, char[] decrypted, boolean swapKey){
        addPacketData(
            out,
            new PacketData(label, toByteArray(encrypted), toByteArray(decrypted), swapKey)
        );
    }                    
    
    private void initServerPacketDataList(){
        addServerPacketData(
            new char[] {0x13, 0x3B, 0xA1, 0x7A, 0xE1, 0xE9, 0xF8, 0xFE, 0xEA, 0x6A, 0x07, 0x8B, 0xFA, 0x16, 0x2E, 0x0C, 0x22, 0x60},
            new char[] {0xE2, 0xB7, 0x12, 0x00, 0x65, 0x00, 0x1B, 0x82, 0xBC, 0x34, 0x08, 0xCD, 0xD2, 0x80, 0x5E, 0x06, 0xEA, 0x1C}                                 
        );                
        
        addServerPacketData(
            new char[] {0x13, 0x3B, 0xA5, 0x7A, 0xFE, 0xE9, 0x32, 0x6A, 0xE5, 0xFC, 0xDA, 0x0B, 0x37, 0x24, 0xAD, 0x38, 0xEC, 0xBD, 0x92, 0xC1, 0x04, 0xD9},
            new char[] {0xE2, 0xB7, 0x16, 0x00, 0x7A, 0x00, 0xD1, 0x16, 0x00, 0x00, 0x7C, 0x07, 0x59, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            true
        );        
        
        addServerPacketData(
            new char[] {0x13, 0x3B, 0xF5, 0x7A, 0xE3, 0xE9, 0xC2, 0x7C, 0x86, 0x61, 0x81, 0x8A, 0xA0, 0xD4, 0xE4, 0xCC, 0x61, 0xB7, 0x0A, 0xC6, 0xBA, 0x42, 0x38, 0x02, 0xBA, 0x9A, 0x67, 0x93, 0x0C, 0xEC, 0xBF, 0xA9, 0xB8, 0xE7, 0x74, 0xE4, 0xC8, 0xE1, 0x53, 0x04, 0xFB, 0x41, 0x15, 0xA3, 0x30, 0x07, 0xF6, 0x8F, 0x13, 0xF5, 0xC0, 0x08, 0x72, 0xF8, 0xBA, 0xE5, 0x61, 0x40, 0x0E, 0x84, 0x3F, 0x3F, 0xF4, 0x44, 0x84, 0x0D, 0xD8, 0x7D, 0x08, 0x85},
            new char[] {0xE2, 0xB7, 0x46, 0x00, 0x67, 0x00, 0x21, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}
        );
    } 
    
    private void initClientPacketDataList(){
        addClientPacketData(            
            new char[] {0xA8, 0xD1, 0x11, 0x87, 0xCC, 0x52, 0x90, 0xA0, 0x79, 0xB4, 0x0D, 0x84, 0x43, 0xE7},
            new char[] {0xE2, 0xB7, 0x0E, 0x00, 0x00, 0x00, 0x00, 0x00, 0x65, 0x00, 0x50, 0x54, 0x24, 0xCF}
        );
        
        addClientPacketData(
            new char[] {0xCD, 0xC1, 0x72, 0xB3, 0xD2, 0xF5, 0xAB, 0x51, 0x92, 0x97, 0xFA, 0x55, 0xD6, 0xAA, 0x1D, 0x54, 0xD3, 0xCB, 0xB9, 0xF0, 0x6F, 0x31, 0xC7, 0xE8, 0x51, 0x51},
            new char[] {0xE2, 0xB7, 0x1A, 0x00, 0x00, 0x00, 0x00, 0x00, 0x7A, 0x00, 0xD1, 0x16, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},
            true
        );        
        
        addClientPacketData(
            new char[] {0x34, 0x24, 0x32, 0xBC, 0x07, 0x54, 0x10, 0x21, 0x30, 0x39, 0xB2, 0x0B, 0x53, 0x69, 0x4E, 0xA5, 0x74, 0xA0, 0xBB, 0x7F, 0x2E},
            new char[] {0xE2, 0xB7, 0x15, 0x00, 0x00, 0x00, 0x00, 0x00, 0x67, 0x00, 0x09, 0x06, 0x50, 0x69, 0x72, 0x61, 0x74, 0x61, 0x31, 0x32, 0x33}
        );
    }
    
    @Test
    public void testDecryptation(){        
        Cryptation cryptation = cryptationFactory.create();
        Key clientKey = keyFactory.create();
        boolean authKeySetuped = false;
        
        for(PacketData current : clientPacketDataList){            
            if(current.setupAuthKey){                
                if(!authKeySetuped){
                    System.out.printf(">> Setupping AuthKey %s...\n", authKey.hexString());
                    clientKey = keyFactory.create(authKey);
                    authKeySetuped = !authKeySetuped;
                }
            }
                        
            cryptation.decrypt(current.encrypted, clientKey);                        
            
            if(!Arrays.equals(current.encrypted, current.decrypted)){
                printArray(current.encrypted, "Decrypted Data");
                printArray(current.decrypted, "Expected Decrypted Data");
                assert(false);
            }
        }        
    }
    
    @Test
    public void testEncryptation(){
        Cryptation cryptation = cryptationFactory.create();
        Key clientKey = keyFactory.create();        
        boolean authKeySetuped = false;
        
        for(PacketData current : serverPacketDataList){            
            if(current.setupAuthKey){                
                if(!authKeySetuped){
                    System.out.printf(">> Setupping AuthKey %s...\n", authKey.hexString());
                    clientKey = keyFactory.create(authKey);
                    authKeySetuped = !authKeySetuped;
                }
            }
                        
            cryptation.encrypt(current.decrypted, clientKey);                       
            
            if(!Arrays.equals(current.encrypted, current.decrypted)){
                printArray(current.encrypted, "Encrypted Data");
                printArray(current.decrypted, "Expected Encrypted Data");
                assert(false);
            }
        }        
    }
    
    private void printArray(byte array[], String info){
        System.out.printf("\nI= %s\n", info);
        
        for(int i = 0 ; i < array.length ; i++){
            System.out.printf(" %02X", array[i]);
            if(i % 16 == 0){
                System.out.println();
            }
        }
        System.out.printf("\nF= %s\n", info);
    }        
    
    private byte[] toByteArray(char charArray[]){
        byte[] bValue = new byte[charArray.length];
        for(int i  = 0 ; i < charArray.length ; i++){
            bValue[i] = (byte)(charArray[i] & 0xFF);
        }        
        return bValue;
    }
    
}
