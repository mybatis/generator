package ibatortest.java2;

import ibatortest.java2.execute.conditional.java2.ConditionalJava2TestSuite;
import ibatortest.java2.execute.flat.java2.FlatJava2Tests;
import ibatortest.java2.execute.hierarchical.java2.HierarchicalJava2Tests;
import ibatortest.java2.execute.miscellaneous.MiscellaneousTests;
import junit.framework.Test;
import junit.framework.TestSuite;

public class CompleteJava2TestSuite {
    
    public static Test suite() {
        TestSuite suite = new TestSuite("All Java 2 Tests");
        
        suite.addTest(ConditionalJava2TestSuite.suite());
        suite.addTestSuite(FlatJava2Tests.class);
        suite.addTestSuite(HierarchicalJava2Tests.class);
        suite.addTestSuite(MiscellaneousTests.class);
        
        return suite;
    }

}
