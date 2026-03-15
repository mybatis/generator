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
import java.util.stream.Collectors;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelRecord;

public abstract class BaseRecordPlugin extends PluginAdapter {
    private static final String messageTemplate =
            "Invalid fieldCountTrigger property value for plugin %s in context %s. " //$NON-NLS-1$
                    + "Using default value of 4."; //$NON-NLS-1$

    private final String skipProperty;
    private int fieldCountTrigger = 4;

    protected BaseRecordPlugin(String skipProperty) {
        this.skipProperty = skipProperty;
    }

    @Override
    public boolean validate(List<String> warnings) {
        if (properties.containsKey("fieldCountTrigger")) { //$NON-NLS-1$
            try {
                fieldCountTrigger = Integer.parseInt(properties.getProperty("fieldCountTrigger")); //$NON-NLS-1$
            } catch (NumberFormatException e) {
                warnings.add(String.format(messageTemplate, getClass().getName(), context.getId())); //$NON-NLS-1$
            }
        }

        return true;
    }

    @Override
    public boolean modelRecordGenerated(TopLevelRecord topLevelRecord, IntrospectedTable introspectedTable) {
        if (Boolean.parseBoolean(introspectedTable.getTableConfigurationProperty(skipProperty))) {
            return true;
        }

        if (introspectedTable.getColumnCount() < fieldCountTrigger) {
            return true;
        }

        execute(topLevelRecord, introspectedTable);
        return true;
    }

    protected String calculateReturnNewLine(List<IntrospectedColumn> columns, FullyQualifiedJavaType returnType) {
        String prefix = "return new " + returnType.getShortName() + "("; //$NON-NLS-1$ //$NON-NLS-2$
        return columns.stream()
                .map(IntrospectedColumn::getJavaProperty)
                .collect(Collectors.joining(", ", prefix, ");")); //$NON-NLS-1$ //$NON-NLS-2$

    }

    protected abstract void execute(TopLevelRecord topLevelRecord, IntrospectedTable introspectedTable);
}
