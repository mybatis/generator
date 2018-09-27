/**
 *    Copyright 2006-2018 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package mbg.test.mb3.annotated.miscellaneous;

import org.junit.jupiter.api.BeforeEach;

import mbg.test.mb3.AbstractTest;
import mbg.test.mb3.generated.annotated.miscellaneous.mapper.EnumtestMapper;
import mbg.test.mb3.generated.annotated.miscellaneous.mapper.GeneratedalwaystestMapper;
import mbg.test.mb3.generated.annotated.miscellaneous.mapper.GeneratedalwaystestnoupdatesMapper;
import mbg.test.mb3.generated.annotated.miscellaneous.mapper.MyObjectMapper;
import mbg.test.mb3.generated.annotated.miscellaneous.mapper.RegexrenameMapper;

public abstract class AbstractAnnotatedMiscellaneousTest extends AbstractTest {

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        sqlSessionFactory.getConfiguration().addMapper(EnumtestMapper.class);
        sqlSessionFactory.getConfiguration().addMapper(MyObjectMapper.class);
        sqlSessionFactory.getConfiguration().addMapper(RegexrenameMapper.class);
        sqlSessionFactory.getConfiguration().addMapper(GeneratedalwaystestMapper.class);
        sqlSessionFactory.getConfiguration().addMapper(GeneratedalwaystestnoupdatesMapper.class);
    }

    @Override
    public String getMyBatisConfigFile() {
        return "mbg/test/mb3/annotated/MapperConfig.xml";
    }
}
