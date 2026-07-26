/*
 *    Copyright 2006-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package mbg.test.mb3.common;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class IntegerWrapperTypeHandler implements TypeHandler<IntegerWrapper> {

    @Override
    public void setParameter(PreparedStatement ps, int i, IntegerWrapper parameter, JdbcType jdbcType)
            throws SQLException {
        if (parameter == null || parameter.getValue() == null) {
            ps.setNull(i, JdbcType.INTEGER.TYPE_CODE);
        } else {
            ps.setInt(i, parameter.getValue());
        }
    }

    @Override
    public IntegerWrapper getResult(ResultSet rs, String columnName) throws SQLException {
        int i = rs.getInt(columnName);
        return toWrapper(i, rs.wasNull());
    }

    @Override
    public IntegerWrapper getResult(ResultSet rs, int columnIndex) throws SQLException {
        int i = rs.getInt(columnIndex);
        return toWrapper(i, rs.wasNull());
    }

    @Override
    public IntegerWrapper getResult(CallableStatement cs, int columnIndex) throws SQLException {
        int i = cs.getInt(columnIndex);
        return toWrapper(i, cs.wasNull());
    }

    private IntegerWrapper toWrapper(int value, boolean wasNull) {
        if (wasNull) {
            return null;
        } else {
            IntegerWrapper iw = new IntegerWrapper();
            iw.setValue(value);
            return iw;
        }
    }
}
