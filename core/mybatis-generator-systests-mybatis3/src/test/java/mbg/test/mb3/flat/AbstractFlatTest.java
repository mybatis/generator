package mbg.test.mb3.flat;

import mbg.test.mb3.AbstractTest;

public abstract class AbstractFlatTest extends AbstractTest {

    @Override
    public String getMyBatisConfigFile() {
        return "mbg/test/mb3/flat/MapperConfig.xml";
    }
}
