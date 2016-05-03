/**
 *    Copyright 2006-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
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

import mbg.test.common.FirstName;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * @author Jeff Butler
 *
 */
public class FirstNameTypeHandler implements TypeHandler<FirstName> {

    /**
     * 
     */
    public FirstNameTypeHandler() {
        super();
    }

    public FirstName getResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        FirstName answer = null;
        String string = cs.getString(columnIndex);
        if (string != null) {
            answer = new FirstName();
            answer.setValue(string);
        }
        
        return answer;
    }

    public FirstName getResult(ResultSet rs, String columnName)
            throws SQLException {
        FirstName answer = null;
        String string = rs.getString(columnName);
        if (string != null) {
            answer = new FirstName();
            answer.setValue(string);
        }
        
        return answer;
    }

    public FirstName getResult(ResultSet rs, int columnIndex)
            throws SQLException {
        FirstName answer = null;
        String string = rs.getString(columnIndex);
        if (string != null) {
            answer = new FirstName();
            answer.setValue(string);
        }
        
        return answer;
    }

    public void setParameter(PreparedStatement ps, int i, FirstName parameter,
            JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setNull(i, jdbcType.TYPE_CODE);
        } else {
            ps.setString(i, parameter.getValue());
        }
    }
}
