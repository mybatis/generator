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
package org.mybatis.generator.runtime.dynamic.sql.elements;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.runtime.AbstractJavaMethodGenerator;
import org.mybatis.generator.runtime.JavaMethodAndImports;

public class UpdateAllColumnsMethodGenerator extends AbstractJavaMethodGenerator {
    private final FullyQualifiedJavaType recordType;
    private final FragmentGenerator fragmentGenerator;

    private UpdateAllColumnsMethodGenerator(Builder builder) {
        super(builder);
        recordType = Objects.requireNonNull(builder.recordType);
        fragmentGenerator = Objects.requireNonNull(builder.fragmentGenerator);
    }

    @Override
    public JavaMethodAndImports generateMethodAndImports() {
        Set<FullyQualifiedJavaType> imports = new HashSet<>();

        FullyQualifiedJavaType parameterAndReturnType = new FullyQualifiedJavaType(
                "org.mybatis.dynamic.sql.update.UpdateDSL"); //$NON-NLS-1$
        parameterAndReturnType.addTypeArgument(new FullyQualifiedJavaType(
                "org.mybatis.dynamic.sql.update.UpdateModel")); //$NON-NLS-1$
        imports.add(parameterAndReturnType);

        imports.add(recordType);

        Method method = new Method("updateAllColumns"); //$NON-NLS-1$
        method.setStatic(true);
        commentGenerator.addGeneralMethodAnnotation(method, introspectedTable, imports);

        method.setReturnType(parameterAndReturnType);
        method.addParameter(new Parameter(recordType, "row")); //$NON-NLS-1$
        method.addParameter(new Parameter(parameterAndReturnType, "dsl")); //$NON-NLS-1$

        method.addBodyLines(fragmentGenerator.getSetEqualLines(introspectedTable.getAllColumns(),
                "return dsl", "        ", true)); //$NON-NLS-1$ //$NON-NLS-2$

        return JavaMethodAndImports.withMethod(method)
                .withImports(imports)
                .build();
    }

    @Override
    public boolean callPlugins(Method method, Interface interfaze) {
        return pluginAggregator.clientUpdateAllColumnsMethodGenerated(method, interfaze, introspectedTable);
    }

    public static class Builder extends AbstractGeneratorBuilder<Builder> {
        private @Nullable FullyQualifiedJavaType recordType;
        private @Nullable FragmentGenerator fragmentGenerator;

        public Builder withRecordType(FullyQualifiedJavaType recordType) {
            this.recordType = recordType;
            return this;
        }

        public Builder withFragmentGenerator(FragmentGenerator fragmentGenerator) {
            this.fragmentGenerator = fragmentGenerator;
            return this;
        }

        @Override
        public Builder getThis() {
            return this;
        }

        public UpdateAllColumnsMethodGenerator build() {
            return new UpdateAllColumnsMethodGenerator(this);
        }
    }
}
