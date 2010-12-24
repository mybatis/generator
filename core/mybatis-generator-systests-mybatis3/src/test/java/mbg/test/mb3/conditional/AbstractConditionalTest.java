package mbg.test.mb3.conditional;

import mbg.test.mb3.AbstractTest;

public abstract class AbstractConditionalTest extends AbstractTest {

    @Override
    public String getMyBatisConfigFile() {
        return "mbg/test/mb3/conditional/MapperConfig.xml";
    }
}
