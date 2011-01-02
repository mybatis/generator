package mbg.test.mb3.mixed.miscellaneous;

import mbg.test.mb3.AbstractTest;

public abstract class AbstractMixedMiscellaneousTest extends AbstractTest {

    @Override
    public String getMyBatisConfigFile() {
        return "mbg/test/mb3/mixed/miscellaneous/MapperConfig.xml";
    }
}
