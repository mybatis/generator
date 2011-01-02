package mbg.test.mb3.mixed.conditional;

import mbg.test.mb3.AbstractTest;

public abstract class AbstractMixedConditionalTest extends AbstractTest {

    @Override
    public String getMyBatisConfigFile() {
        return "mbg/test/mb3/mixed/conditional/MapperConfig.xml";
    }
}
