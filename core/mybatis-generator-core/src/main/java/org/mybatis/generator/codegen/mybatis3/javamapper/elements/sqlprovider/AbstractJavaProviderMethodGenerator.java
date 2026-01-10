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
package org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractGenerator;
import org.mybatis.generator.config.Context;

public abstract class AbstractJavaProviderMethodGenerator extends AbstractGenerator {

    protected static final FullyQualifiedJavaType BUILDER_IMPORT =
            new FullyQualifiedJavaType("org.apache.ibatis.jdbc.SQL"); //$NON-NLS-1$

    protected AbstractJavaProviderMethodGenerator(AbstractJavaProviderMethodGeneratorBuilder<?> builder) {
        // TODO - shim
        setContext(builder.context);
        setIntrospectedTable(builder.introspectedTable);
        setProgressCallback(builder.progressCallback);
        setWarnings(builder.warnings);
    }

    protected Set<FullyQualifiedJavaType> initializeImportedTypes() {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<>();

        importedTypes.add(BUILDER_IMPORT);

        return importedTypes;
    }

    protected Set<FullyQualifiedJavaType> initializeImportedTypes(String extraType) {
        return initializeImportedTypes(new FullyQualifiedJavaType(extraType));
    }

    protected Set<FullyQualifiedJavaType> initializeImportedTypes(FullyQualifiedJavaType extraType) {
        Set<FullyQualifiedJavaType> importedTypes = initializeImportedTypes();

        importedTypes.add(extraType);

        return importedTypes;
    }

    public abstract void addClassElements(TopLevelClass topLevelClass);

    public abstract static class AbstractJavaProviderMethodGeneratorBuilder
            <T extends AbstractJavaProviderMethodGeneratorBuilder<T>> {
        // TODO - this is a shim so we can do a more limited refactoring. Ultimately this should move to an abstract
        //  builder in the AbstractGenerator
        private @Nullable Context context;
        private @Nullable IntrospectedTable introspectedTable;
        private @Nullable List<String> warnings;
        private @Nullable ProgressCallback progressCallback;

        public T withContext(Context context) {
            this.context = context;
            return getThis();
        }

        public T withIntrospectedTable(IntrospectedTable introspectedTable) {
            this.introspectedTable = introspectedTable;
            return getThis();
        }

        public T withWarnings(List<String> warnings) {
            this.warnings = warnings;
            return getThis();
        }

        public T withProgressCallback(ProgressCallback progressCallback) {
            this.progressCallback = progressCallback;
            return getThis();
        }

        protected abstract T getThis();

        public abstract AbstractJavaProviderMethodGenerator build();
    }
}
