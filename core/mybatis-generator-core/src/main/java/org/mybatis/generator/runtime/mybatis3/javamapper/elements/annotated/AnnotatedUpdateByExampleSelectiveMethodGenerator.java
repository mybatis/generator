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
package org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.runtime.JavaMethodParts;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.UpdateByExampleSelectiveMethodGenerator;

public class AnnotatedUpdateByExampleSelectiveMethodGenerator extends UpdateByExampleSelectiveMethodGenerator {

    protected AnnotatedUpdateByExampleSelectiveMethodGenerator(Builder builder) {
        super(builder);
    }

    @Override
    protected JavaMethodParts extraMethodParts() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedTable.getMyBatis3SqlProviderType());

        String annotation = "@UpdateProvider(type=" //$NON-NLS-1$
                + fqjt.getShortName()
                + ".class, method=\"" //$NON-NLS-1$
                + introspectedTable.getUpdateByExampleSelectiveStatementId()
                + "\")"; //$NON-NLS-1$

        return new JavaMethodParts.Builder()
                .withAnnotation(annotation)
                .withImport(new FullyQualifiedJavaType("org.apache.ibatis.annotations.UpdateProvider")) //$NON-NLS-1$
                .build();
    }

    public static class Builder extends UpdateByExampleSelectiveMethodGenerator.Builder {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public AnnotatedUpdateByExampleSelectiveMethodGenerator build() {
            return new AnnotatedUpdateByExampleSelectiveMethodGenerator(this);
        }
    }
}
