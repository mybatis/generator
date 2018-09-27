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
package mbg.test.mb3.annotated.hierarchical;

import org.junit.jupiter.api.BeforeEach;

import mbg.test.mb3.AbstractTest;
import mbg.test.mb3.generated.annotated.hierarchical.mapper.AwfulTableMapper;
import mbg.test.mb3.generated.annotated.hierarchical.mapper.FieldsblobsMapper;
import mbg.test.mb3.generated.annotated.hierarchical.mapper.FieldsonlyMapper;
import mbg.test.mb3.generated.annotated.hierarchical.mapper.PkblobsMapper;
import mbg.test.mb3.generated.annotated.hierarchical.mapper.PkfieldsMapper;
import mbg.test.mb3.generated.annotated.hierarchical.mapper.PkfieldsblobsMapper;
import mbg.test.mb3.generated.annotated.hierarchical.mapper.PkonlyMapper;

public abstract class AbstractAnnotatedHierarchicalTest extends AbstractTest {

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
        sqlSessionFactory.getConfiguration().addMapper(AwfulTableMapper.class);
        sqlSessionFactory.getConfiguration().addMapper(FieldsblobsMapper.class);
        sqlSessionFactory.getConfiguration().addMapper(FieldsonlyMapper.class);
        sqlSessionFactory.getConfiguration().addMapper(PkblobsMapper.class);
        sqlSessionFactory.getConfiguration().addMapper(PkfieldsblobsMapper.class);
        sqlSessionFactory.getConfiguration().addMapper(PkfieldsMapper.class);
        sqlSessionFactory.getConfiguration().addMapper(PkonlyMapper.class);
    }

    @Override
    public String getMyBatisConfigFile() {
        return "mbg/test/mb3/annotated/MapperConfig.xml";
    }
}
