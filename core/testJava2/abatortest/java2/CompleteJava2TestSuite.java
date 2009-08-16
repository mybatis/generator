package abatortest.java2;

import junit.framework.Test;
import junit.framework.TestSuite;
import abatortest.java2.execute.conditional.java2.ConditionalJava2TestSuite;
import abatortest.java2.execute.conditional.legacy.ConditionalLegacyTestSuite;
import abatortest.java2.execute.flat.java2.FlatJava2Tests;
import abatortest.java2.execute.flat.legacy.FlatLegacyTests;
import abatortest.java2.execute.hierarchical.java2.HierarchicalJava2Tests;
import abatortest.java2.execute.hierarchical.legacy.HierarchicalLegacyTests;
import abatortest.java2.execute.miscellaneous.MiscellaneousTests;

public class CompleteJava2TestSuite {
    
    public static Test suite() {
        TestSuite suite = new TestSuite("All Java 2 Tests");
        
        suite.addTest(ConditionalJava2TestSuite.suite());
        suite.addTest(ConditionalLegacyTestSuite.suite());
        suite.addTestSuite(FlatJava2Tests.class);
        suite.addTestSuite(FlatLegacyTests.class);
        suite.addTestSuite(HierarchicalJava2Tests.class);
        suite.addTestSuite(HierarchicalLegacyTests.class);
        suite.addTestSuite(MiscellaneousTests.class);
        
        return suite;
    }

}
