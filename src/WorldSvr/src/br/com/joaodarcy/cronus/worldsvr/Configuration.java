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

package br.com.joaodarcy.cronus.worldsvr;

import br.com.joaodarcy.cronus.cabal.core.AbstractConfiguration;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public final class Configuration extends AbstractConfiguration {

    private static final Configuration instance;
    
    static{
        // FIXME: Allow to change serveridx and channelidx
        instance = new Configuration("config/WorldSvr_01_01.properties");
    }

    private Configuration(String settingsFileName) {
        super(settingsFileName);
    }            
    
    public static int channelId(){
        return instance.intSetting("channel.id");
    }
    
    public static int channelPort(){
        return instance.intSetting("channel.port");
    }
    
    public static String databaseDriver(){
        return instance.stringSetting("database.driver");
    }
    
    public static String databaseUrl(){
        return instance.stringSetting("database.url");
    }
    
    public static String databaseUsername(){
        return instance.stringSetting("database.user");
    }
    
    public static String databasePassword(){
        return instance.stringSetting("database.pass");
    }        
    
    public static boolean developmentOpenConsole(){
        return instance.booleanSetting("development.openconsole");
    }
    
    public static int maxStr(){
        return instance.intSetting("server.maxStr");
    }
    
    public static int maxInt(){
        return instance.intSetting("server.maxInt");
    }
    
    public static int maxDex(){
        return instance.intSetting("server.maxDex");
    }
    
    public static int maxOpenQuests(){
        return instance.intSetting("server.maxOpenQuests");
    }
    
    public static int maxTrackingQuests(){
        return instance.intSetting("server.maxTrackingQuests");
    }
    
    public static String serverChatIp(){
        return instance.stringSetting("server.chatIp");
    }
    
    public static int serverChatPort(){
        return instance.intSetting("server.chatPort");
    }
    
    public static int serverMaxLevel(){
        return instance.intSetting("server.maxLevel");
    }
    
    public static byte serverId(){
        return instance.byteSetting("server.id");
    }                        
}
