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

package br.com.joaodarcy.cronus.worldsvr.sqltypes;

import br.com.joaodarcy.cronus.cabal.core.types.UInt8;
import br.com.joaodarcy.cronus.worldsvr.channel.Channel;
import br.com.joaodarcy.cronus.worldsvr.world.World;
import br.com.joaodarcy.npersistence.core.Type;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * @author João Darcy Tinoco Sant'Anna Neto <jdtsncomp@gmail.com>
 */
public class WorldType implements Type{

    private static World getWorldById(UInt8 worldId){
        return Channel.getWorldById(worldId);
    }
    
    @Override
    public Class<?> getTypeClass() {
        return WorldType.class;
    }

    @Override
    public int getSQLType() {
        return Types.TINYINT;
    }

    @Override
    public Object getValue(ResultSet resultSet, String columnLabel) throws SQLException {
        return getWorldById(UInt8.valueOf(resultSet.getByte(columnLabel)));
    }

    @Override
    public Object getValue(ResultSet resultSet, int columnIndex) throws SQLException {
        return getWorldById(UInt8.valueOf(resultSet.getByte(columnIndex)));
    }

    @Override
    public Object getValue(CallableStatement callableStatement, int parameterIndex) throws SQLException {
        return getWorldById(UInt8.valueOf(callableStatement.getByte(parameterIndex)));
    }

}
