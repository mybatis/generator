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
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelRecord;
import org.mybatis.generator.internal.util.JavaBeansUtil;

public class RecordWitherPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelRecordGenerated(TopLevelRecord topLevelRecord, IntrospectedTable introspectedTable) {
        if (Boolean.parseBoolean(introspectedTable.getTableConfigurationProperty("skipRecordWither"))) { //$NON-NLS-1$
            // TODO - document this property - good name?
            return true;
        }

        List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
        if (allColumns.size() < 4) {
            // TODO - make this configurable
            return true;
        }

        for (IntrospectedColumn column : allColumns) {
            Method method = generateWithMethod(introspectedTable, column, allColumns);
            commentGenerator.addGeneralMethodAnnotation(method, introspectedTable, topLevelRecord.getImportedTypes());
            topLevelRecord.addMethod(method);
        }

        return true;
    }

    private Method generateWithMethod(IntrospectedTable table, IntrospectedColumn currentColumn,
                                      List<IntrospectedColumn> columns) {
        FullyQualifiedJavaType returnType = new FullyQualifiedJavaType(table.getBaseRecordType());
        String methodName = "with" + JavaBeansUtil.uppercaseFirstLetterIfNecessary(currentColumn.getJavaProperty()); //$NON-NLS-1$
        Method method = new Method(methodName);
        method.setReturnType(returnType);
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(currentColumn.getFullyQualifiedJavaType(), currentColumn.getJavaProperty()));

        String line = columns.stream()
                .map(IntrospectedColumn::getJavaProperty)
                .collect(Collectors.joining(", ", "return new " + returnType.getShortName() + "(", ");"));
        method.addBodyLine(line);
        return method;
    }
}
