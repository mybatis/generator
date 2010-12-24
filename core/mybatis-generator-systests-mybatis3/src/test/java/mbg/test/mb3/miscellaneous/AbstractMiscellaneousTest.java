package mbg.test.mb3.miscellaneous;

import mbg.test.mb3.AbstractTest;

public abstract class AbstractMiscellaneousTest extends AbstractTest {

    @Override
    public String getMyBatisConfigFile() {
        return "mbg/test/mb3/miscellaneous/MapperConfig.xml";
    }
}
