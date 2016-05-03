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
package mbg.test.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class TestUtilities {
    
    private static DateFormat dateOnlyFormat = SimpleDateFormat.getDateInstance();
    private static DateFormat timeOnlyFormat = SimpleDateFormat.getTimeInstance();
    
    public static byte[] generateRandomBlob() {
        Random random = new Random();

        byte[] answer = new byte[256];

        random.nextBytes(answer);

        return answer;
    }

    public static boolean blobsAreEqual(byte[] blob1, byte[] blob2) {
        return Arrays.equals(blob1, blob2);
    }
    
    public static boolean datesAreEqual(Date date1, Date date2) {
        if (date1 == null) {
            return date2 == null;
        }

        if (date2 == null) {
            return date1 == null;
        }

        return dateOnlyFormat.format(date1).equals(dateOnlyFormat.format(date2));
    }
    
    public static boolean timesAreEqual(Date date1, Date date2) {
        if (date1 == null) {
            return date2 == null;
        }

        if (date2 == null) {
            return date1 == null;
        }

        return timeOnlyFormat.format(date1).equals(timeOnlyFormat.format(date2));
    }

    public static void createDatabase() throws Exception {
        SqlScriptRunner runner = new SqlScriptRunner(
                "classpath:mbg/test/common/scripts/CreateDB.sql",
                "org.hsqldb.jdbcDriver",
                "jdbc:hsqldb:mem:aname",
                "sa",
                "");
            
        runner.executeScript();
    }
}
