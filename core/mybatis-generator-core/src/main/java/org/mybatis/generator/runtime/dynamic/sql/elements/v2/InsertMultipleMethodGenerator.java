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
package org.mybatis.generator.runtime.dynamic.sql.elements.v2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.runtime.dynamic.sql.elements.AbstractMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.MethodAndImports;

public class InsertMultipleMethodGenerator extends AbstractMethodGenerator {
    private FullyQualifiedJavaType recordType;
    
    private InsertMultipleMethodGenerator(Builder builder) {
        super(builder);
        recordType = builder.recordType;
    }

    @Override
    public MethodAndImports generateMethodAndImports() {
        if (!Utils.generateMultipleRowInsert(introspectedTable)) {
            return null;
        }
        
        Set<FullyQualifiedJavaType> imports = new HashSet<>();

        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils")); //$NON-NLS-1$
        imports.add(recordType);
        
        Method method = new Method("insertMultiple"); //$NON-NLS-1$
        method.setDefault(true);
        context.getCommentGenerator().addGeneralMethodAnnotation(method, introspectedTable, imports);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        
        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType("java.util.Collection"); //$NON-NLS-1$
        parameterType.addTypeArgument(recordType);
        imports.add(parameterType);
        
        method.addParameter(new Parameter(parameterType, "records")); //$NON-NLS-1$
        
        method.addBodyLine("return MyBatis3Utils.insertMultiple(this::insertMultiple, records, " +
                tableFieldName + //$NON-NLS-1$
                ", c ->"); //$NON-NLS-1$
        
        List<IntrospectedColumn> columns =
                ListUtilities.removeIdentityAndGeneratedAlwaysColumns(introspectedTable.getAllColumns());
        boolean first = true;
        for (IntrospectedColumn column : columns) {
            String fieldName = calculateFieldName(column);
            
            if (first) {
                method.addBodyLine("    c.map(" + fieldName //$NON-NLS-1$
                        + ").toProperty(\"" + column.getJavaProperty() //$NON-NLS-1$
                        + "\")"); //$NON-NLS-1$
                first = false;
            } else {
                method.addBodyLine("    .map(" + fieldName //$NON-NLS-1$
                        + ").toProperty(\"" + column.getJavaProperty() //$NON-NLS-1$
                        + "\")"); //$NON-NLS-1$
            }
        }
        
        method.addBodyLine(");"); //$NON-NLS-1$

        return MethodAndImports.withMethod(method)
                .withImports(imports)
                .build();
    }

    @Override
    public boolean callPlugins(Method method, Interface interfaze) {
        return context.getPlugins().clientInsertMultipleMethodGenerated(method, interfaze, introspectedTable);
    }

    public static class Builder extends BaseBuilder<Builder, InsertMultipleMethodGenerator> {
        private FullyQualifiedJavaType recordType;
        
        public Builder withRecordType(FullyQualifiedJavaType recordType) {
            this.recordType = recordType;
            return this;
        }
        
        @Override
        public Builder getThis() {
            return this;
        }

        @Override
        public InsertMultipleMethodGenerator build() {
            return new InsertMultipleMethodGenerator(this);
        }
    }
}
