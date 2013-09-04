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

import static br.com.joaodarcy.cronus.cabal.core.types.Unsigned.*;
import br.com.joaodarcy.cronus.cabal.core.types.UInt32;
import java.io.FileOutputStream;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class EP8Key implements Key{    
        
    private final static UInt32 COMMON_KEY_MULTIPLIER = uint32(0x4l);
    private final static UInt32 AUTH_KEY_MULTIPLIER = uint32(0x8l);
    
    private final byte data[];
    private final UInt32 decryptionMultiplier;    
    private final UInt32 encryptionMultiplier;
    
    EP8Key(KeyGenerator keyGenerator){
        this(keyGenerator, null);
    }
    
    EP8Key(KeyGenerator keyGenerator, UInt32 rootAuthKey) {
        if(rootAuthKey == null){
            this.data = keyGenerator.generateCommonKey();
            this.decryptionMultiplier = COMMON_KEY_MULTIPLIER;
        }else{
            this.data = keyGenerator.generateAuthKey(rootAuthKey);
            this.decryptionMultiplier = AUTH_KEY_MULTIPLIER;
        }
        this.encryptionMultiplier = COMMON_KEY_MULTIPLIER;
    }        
    
    /** DEBUG */
    public void write(String filename){
        FileOutputStream fos = null;
        
        try{
            fos = new FileOutputStream(filename);
            fos.write(data);            
        }catch(Throwable t){
            t.printStackTrace();
        }finally{
            if(fos != null){
                try{
                    fos.close();
                }catch(Throwable t){
                    t.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public UInt32 getKeyValue(int index) {
        return UInt32.fromByteArrayLE(data, index);
    }
        
    @Override
    public UInt32 getDecryptionMultiplier(){
        return decryptionMultiplier;
    }

    @Override
    public UInt32 getEncryptionMultiplier() {
        return encryptionMultiplier;
    }        
}
