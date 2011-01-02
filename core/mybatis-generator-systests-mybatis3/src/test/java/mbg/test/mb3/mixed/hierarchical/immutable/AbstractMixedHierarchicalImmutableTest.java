package mbg.test.mb3.mixed.hierarchical.immutable;

import mbg.test.mb3.AbstractTest;

public abstract class AbstractMixedHierarchicalImmutableTest extends AbstractTest {

    @Override
    public String getMyBatisConfigFile() {
        return "mbg/test/mb3/mixed/hierarchical/immutable/MapperConfig.xml";
    }
}
