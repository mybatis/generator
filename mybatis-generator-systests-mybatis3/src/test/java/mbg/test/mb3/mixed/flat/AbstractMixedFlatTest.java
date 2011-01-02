package mbg.test.mb3.mixed.flat;

import mbg.test.mb3.AbstractTest;

public abstract class AbstractMixedFlatTest extends AbstractTest {

    @Override
    public String getMyBatisConfigFile() {
        return "mbg/test/mb3/mixed/flat/MapperConfig.xml";
    }
}
