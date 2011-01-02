package mbg.test.mb3.mixed.hierarchical;

import mbg.test.mb3.AbstractTest;

public abstract class AbstractMixedHierarchicalTest extends AbstractTest {

    @Override
    public String getMyBatisConfigFile() {
        return "mbg/test/mb3/mixed/hierarchical/MapperConfig.xml";
    }
}
