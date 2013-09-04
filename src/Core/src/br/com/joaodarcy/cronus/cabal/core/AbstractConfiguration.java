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

import java.io.FileInputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Joao Darcy Tinoco Sant´Anna Neto <jdtsncomp@gmail.com>
 */
public abstract class AbstractConfiguration {    
    protected final Logger logger;
    protected final Properties settings;
            
    protected AbstractConfiguration(String settingsFileName) {
        this.logger = LoggerFactory.getLogger(AbstractConfiguration.class);
        this.settings = loadSettings(settingsFileName);
    }         
        
    private Properties loadSettings(String settingsFileName){
        Properties loadingSettings = new Properties();
        
        logger.info("Loading server settings...");
        
        FileInputStream fis = null;
        
        try{
            fis = new FileInputStream(settingsFileName);                        
            loadingSettings.load(fis);
            return loadingSettings;
        }catch(Throwable t){
            logger.error("Cannot load server settings, falling back to default settings, reason: {}", t.getMessage(), t);
        }finally{
            if(fis != null){
                try{
                    fis.close();
                }catch(Throwable t){
                    // FIXME: Not important but writting anyway
                    t.printStackTrace();
                }
            }
        }
        
        // TODO: Load default settings
        
        return loadingSettings;
    }
    
    protected boolean booleanSetting(String key){
        return Boolean.valueOf(stringSetting(key));
    }
    
    protected byte byteSetting(String key){
        return Byte.valueOf(stringSetting(key));
    }
    
    protected short shortValue(String key){
        return Short.valueOf(stringSetting(key));
    }
    
    protected int intSetting(String key){
        return Integer.valueOf(stringSetting(key));
    }
    
    protected String stringSetting(String key){
        return settings.getProperty(key);
    }
    
}
