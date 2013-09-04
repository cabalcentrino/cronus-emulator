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

import br.com.joaodarcy.npersistence.ConnectionFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
final class DatabaseConnectionFactory implements ConnectionFactory {

    static{
        final Logger logger = LoggerFactory.getLogger(DatabaseConnectionFactory.class);
        
        logger.info("Registering SQL driver...");
        
        try{
            Class.forName(Configuration.databaseDriver());
        }catch(Throwable t){
            logger.error("Cannot register SQL driver: {}", t.getMessage(), t);
        }               
    }
    
    @Override
    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection(
            Configuration.databaseUrl(),
            Configuration.databaseUsername(),
            Configuration.databasePassword()
        );
    }

}
