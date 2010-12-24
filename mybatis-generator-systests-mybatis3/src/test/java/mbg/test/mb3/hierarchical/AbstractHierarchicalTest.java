package mbg.test.mb3.hierarchical;

import mbg.test.mb3.AbstractTest;

public abstract class AbstractHierarchicalTest extends AbstractTest {

    @Override
    public String getMyBatisConfigFile() {
        return "mbg/test/mb3/hierarchical/MapperConfig.xml";
    }
}
