/**
 *    Copyright 2006-2019 the original author or authors.
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
package org.mybatis.generator.plugins.dsql;

import java.util.List;

import org.mybatis.generator.api.CompositePlugin;

/**
 * Disables delete, insert, delete, and update methods in the MyBatisDynamicSQLV2 runtime.
 * 
 * @author Jeff Butler
 *
 */
public class ReadOnlyPlugin extends CompositePlugin {

    public ReadOnlyPlugin () {
        addPlugin(new DisableDeletePlugin());
        addPlugin(new DisableInsertPlugin());
        addPlugin(new DisableUpdatePlugin());
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }
}
