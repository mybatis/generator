package ibatortest;

import ibatortest.execute.conditional.java2.ConditionalJava2Tests;
import ibatortest.execute.conditional.java5.ConditionalJava5Tests;
import ibatortest.execute.flat.java2.FlatJava2Tests;
import ibatortest.execute.flat.java5.FlatJava5Tests;
import ibatortest.execute.hierarchical.java2.HierarchicalJava2Tests;
import ibatortest.execute.hierarchical.java5.HierarchicalJava5Tests;
import ibatortest.execute.miscellaneous.MiscellaneousTests;
import junit.framework.Test;
import junit.framework.TestSuite;

public class CompleteJava5TestSuite {
    
    public static Test suite() {
        TestSuite suite = new TestSuite("All Java 5 Tests");
        
        suite.addTestSuite(ConditionalJava2Tests.class);
        suite.addTestSuite(ConditionalJava5Tests.class);
        suite.addTestSuite(FlatJava2Tests.class);
        suite.addTestSuite(FlatJava5Tests.class);
        suite.addTestSuite(HierarchicalJava2Tests.class);
        suite.addTestSuite(HierarchicalJava5Tests.class);
        suite.addTestSuite(MiscellaneousTests.class);
        
        return suite;
    }

}
