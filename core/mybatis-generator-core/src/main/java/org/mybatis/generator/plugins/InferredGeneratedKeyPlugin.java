/*
 *    Copyright 2006-2026 the original author or authors.
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
 */
package org.mybatis.generator.plugins;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.config.GeneratedKey;
import org.mybatis.generator.exception.InvalidConfigurationException;

public class InferredGeneratedKeyPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        boolean skipped = Boolean.parseBoolean(introspectedTable
                .getTableConfigurationProperty("skipInferredGeneratedKeyPlugin")); //$NON-NLS-1$
        if (skipped) {
            return;
        }

        // if there is already a generated key, do not override it
        if (introspectedTable.getGeneratedKey().isPresent()) {
            return;
        }

        // we only do this for single primary keys
        if (introspectedTable.getPrimaryKeyColumns().size() != 1) {
            return;
        }

        var column = introspectedTable.getPrimaryKeyColumns().get(0);

        if (!columnsHasGeneratedValue(column)) {
            return;
        }

        GeneratedKey generatedKey = new GeneratedKey(column.getActualColumnName(), "JDBC", true); //$NON-NLS-1$

        try {
            introspectedTable.getTableConfiguration().setGeneratedKey(generatedKey, getContext(), getKnownRuntime());
            column.setIdentity(true);
            column.setSequenceColumn(false);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException("InvalidConfigurationException changing generated key for table "
                    + introspectedTable.getFullyQualifiedTable(), e);
        }
    }

    private boolean columnsHasGeneratedValue(IntrospectedColumn column) {
        return column.isGeneratedColumn() || column.isAutoIncrement();
    }
}
