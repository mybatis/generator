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
import java.sql.Time;
import java.sql.Types;
import java.util.Calendar;

import mbg.test.common.MyTime;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

/**
 * @author Jeff Butler
 * 
 */
public class MyTimeTypeHandler implements TypeHandlerCallback {

    /**
     * 
     */
    public MyTimeTypeHandler() {
        super();
    }

    public Object getResult(ResultGetter resultGetter) throws SQLException {
        Time time = resultGetter.getTime();
        if (time == null) {
            return null;
        } else {

            MyTime answer = new MyTime();

            Calendar c = Calendar.getInstance();
            c.setTime(time);

            answer.setHours(c.get(Calendar.HOUR_OF_DAY));
            answer.setMinutes(c.get(Calendar.MINUTE));
            answer.setSeconds(c.get(Calendar.SECOND));

            return answer;
        }
    }

    public void setParameter(ParameterSetter parameterSetter, Object value)
            throws SQLException {
        if (value == null) {
            parameterSetter.setNull(Types.TIME);
        } else {
            MyTime myTime = (MyTime) value;

            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, myTime.getHours());
            c.set(Calendar.MINUTE, myTime.getMinutes());
            c.set(Calendar.SECOND, myTime.getSeconds());

            Time time = new Time(c.getTime().getTime());

            parameterSetter.setTime(time);
        }
    }

    public Object valueOf(String arg0) {
        return arg0;
    }
}
