package mbg.test.mb3.hierarchical.immutable;

import mbg.test.mb3.AbstractTest;

public abstract class AbstractHierarchicalImmutableTest extends AbstractTest {

    @Override
    public String getMyBatisConfigFile() {
        return "mbg/test/mb3/hierarchical/immutable/MapperConfig.xml";
    }
}
