package abatortest;

import junit.framework.Test;
import junit.framework.TestSuite;
import abatortest.execute.conditional.java2.ConditionalJava2Tests;
import abatortest.execute.conditional.java5.ConditionalJava5Tests;
import abatortest.execute.conditional.legacy.ConditionalLegacyTests;
import abatortest.execute.flat.java2.FlatJava2Tests;
import abatortest.execute.flat.java5.FlatJava5Tests;
import abatortest.execute.flat.legacy.FlatLegacyTests;
import abatortest.execute.hierarchical.java2.HierarchicalJava2Tests;
import abatortest.execute.hierarchical.java5.HierarchicalJava5Tests;
import abatortest.execute.hierarchical.legacy.HierarchicalLegacyTests;
import abatortest.execute.miscellaneous.MiscellaneousTests;

public class CompleteJava5TestSuite {
    
    public static Test suite() {
        TestSuite suite = new TestSuite("All Java 5 Tests");
        
        suite.addTestSuite(ConditionalJava2Tests.class);
        suite.addTestSuite(ConditionalJava5Tests.class);
        suite.addTestSuite(ConditionalLegacyTests.class);
        suite.addTestSuite(FlatJava2Tests.class);
        suite.addTestSuite(FlatJava5Tests.class);
        suite.addTestSuite(FlatLegacyTests.class);
        suite.addTestSuite(HierarchicalJava2Tests.class);
        suite.addTestSuite(HierarchicalJava5Tests.class);
        suite.addTestSuite(HierarchicalLegacyTests.class);
        suite.addTestSuite(MiscellaneousTests.class);
        
        return suite;
    }

}
