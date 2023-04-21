/*
 *    Copyright 2006-2023 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */
package org.mybatis.generator.eclipse.ui.content;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.io.InputStream;

import org.junit.jupiter.api.Test;


public class ConfigVerifyerTest {
    
    @Test
    public void testValidFile() {
        ConfigVerifyer cf = new ConfigVerifyer((File) null);
        InputStream resource = getClass()
                .getResourceAsStream("/org/mybatis/generator/eclipse/ui/content/GoodConfigFile.xml");
        
        assertThat(cf.isConfigFile(resource), is(true));
    }

    @Test
    public void testInvalidFile() {
        ConfigVerifyer cf = new ConfigVerifyer((File) null);
        InputStream resource = getClass()
                .getResourceAsStream("/org/mybatis/generator/eclipse/ui/content/NotAConfigFile.xml");
        
        assertThat(cf.isConfigFile(resource), is(false));
    }
}
