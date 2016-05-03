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
package mbg.test.ib2j5.common;

import java.sql.SQLException;
import java.sql.Types;

import mbg.test.common.FirstName;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

/**
 * @author Jeff Butler
 *
 */
public class FirstNameTypeHandler implements TypeHandlerCallback {

    /**
     * 
     */
    public FirstNameTypeHandler() {
        super();
    }

    /* (non-Javadoc)
     * @see com.ibatis.sqlmap.client.extensions.TypeHandlerCallback#setParameter(com.ibatis.sqlmap.client.extensions.ParameterSetter, java.lang.Object)
     */
    public void setParameter(ParameterSetter arg0, Object arg1)
            throws SQLException {
        if (arg1 == null) {
            arg0.setNull(Types.VARCHAR);
        } else {
            arg0.setString(((FirstName) arg1).getValue());
        }
    }

    /* (non-Javadoc)
     * @see com.ibatis.sqlmap.client.extensions.TypeHandlerCallback#getResult(com.ibatis.sqlmap.client.extensions.ResultGetter)
     */
    public Object getResult(ResultGetter arg0) throws SQLException {
        return valueOf(arg0.getString());
    }

    /* (non-Javadoc)
     * @see com.ibatis.sqlmap.client.extensions.TypeHandlerCallback#valueOf(java.lang.String)
     */
    public Object valueOf(String arg0) {
        if (arg0 == null) {
            return null;
        } else {
            FirstName answer = new FirstName();
            answer.setValue(arg0);
            return answer;
        }
    }

}
