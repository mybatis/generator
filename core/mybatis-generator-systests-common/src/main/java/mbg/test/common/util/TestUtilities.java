package mbg.test.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        if (blob1 == null) {
            return blob2 == null;
        }

        if (blob2 == null) {
            return blob1 == null;
        }

        boolean rc = blob1.length == blob2.length;

        if (rc) {
            for (int i = 0; i < blob1.length; i++) {
                if (blob1[i] != blob2[i]) {
                    rc = false;
                    break;
                }
            }
        }

        return rc;
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
