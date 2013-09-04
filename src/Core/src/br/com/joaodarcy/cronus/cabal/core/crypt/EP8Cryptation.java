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

package br.com.joaodarcy.cronus.cabal.core.crypt;

import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import static br.com.joaodarcy.cronus.cabal.core.types.Unsigned.*;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
class EP8Cryptation implements Cryptation {        
    
    private final static UInt32 DECRYPT_V1 = uint32(0xb7e2l);            
    private final static UInt32 CIPHER_KEY_AND = uint32(0x3FFFl);
    private final static UInt32 ENCRYPTION_XOR_KEY = uint32(0x7AB38CF1l);    
        
    private final static UInt32 MASKS[] = {
        uint32(~0xFFFFFFFFl), 
        uint32(~0xFFFFFF00l), 
        uint32(~0xFFFF0000l), 
        uint32(~0xFF000000l)
    };

    EP8Cryptation() {
        
    }
    
    private UInt32 getEncryptionCipherKey(UInt32 data, Key clientKey){
        UInt32 cipherKeyIndex = data.bitwiseAnd(CIPHER_KEY_AND).mul(clientKey.getEncryptionMultiplier());
        return clientKey.getKeyValue(cipherKeyIndex.intValue());
    }
    
    private UInt32 getDecryptionCipherKey(UInt32 data, Key clientKey){
        UInt32 cipherKeyIndex = data.bitwiseAnd(CIPHER_KEY_AND).mul(clientKey.getDecryptionMultiplier());
        return clientKey.getKeyValue(cipherKeyIndex.intValue());
    }

    /* 
     * FIXME: The need of packetLength for decrypting header must be avoided, 
     * need to fix this on future     
     */
    @Override
    public byte[] decryptHeader(int packetLength) {        
        return uint32(packetLength * 0x10000).add(DECRYPT_V1).getBytesLE();
    }
    
    @Override
    public void decrypt(byte packet[], Key clientKey){
        UInt32 data, key;
        int i = 8;
                
        data = UInt32.fromByteArrayLE(packet, 0);
        uint32(packet.length * 0x10000).add(DECRYPT_V1).copyValueLE(packet, 0);
                
        // Obtem a primeira chave a ser utilizada
        key = getDecryptionCipherKey(data, clientKey);
        
        int t = (packet.length - i) >> 2;
        
        while(t > 0){
            data = UInt32.fromByteArrayLE(packet, i);
            data.bitwiseXor(key).copyValueLE(packet, i);
            
            key = getDecryptionCipherKey(data, clientKey);
            
            i += 4;
            --t;
        }
        
        if(i < packet.length){        
            data = MASKS[(packet.length - 8) & 3];
            data = data.bitwiseAnd(key).bitwiseXor(UInt32.fromByteArrayLE(packet, i));
            data.copyValueLE(packet, i);
        }
        
        // FIXME: This bytes are the checksum we need to check it on future
        packet[4] = packet[5] = packet[6] = packet[7] = 0;                
    }

    @Override
    public void encrypt(byte[] packet, Key clientKey) {                
        UInt32 data, key;
        int i = 4;
        
        data = UInt32.fromByteArrayLE(packet, 0).bitwiseXor(ENCRYPTION_XOR_KEY);
        data.copyValueLE(packet, 0);
        
        // Obtem a primeira chave a ser utilizada
        key = getEncryptionCipherKey(data, clientKey);
        
        int t = (packet.length - i) >> 2;
        
        while(t > 0){
            data = UInt32.fromByteArrayLE(packet, i);
            data.bitwiseXor(key).copyValueLE(packet, i);
            
            key = getEncryptionCipherKey(data.bitwiseXor(key), clientKey);
            
            i += 4;
            t = t-1;
        }
        
        if(i < packet.length){        
            data = MASKS[(packet.length - 8) & 3];
            data = data.bitwiseAnd(key).bitwiseXor(UInt32.fromByteArrayLE(packet, i));
            data.copyValueLE(packet, i);
        }
    }            
}
