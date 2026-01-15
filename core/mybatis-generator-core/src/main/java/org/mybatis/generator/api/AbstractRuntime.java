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
package org.mybatis.generator.api;

import java.util.List;

import org.mybatis.generator.codegen.AbstractGenerator;

public abstract class AbstractRuntime extends AbstractGenerator {
    protected AbstractRuntime(AbstractRuntimeBuilder<?> builder) {
        super(builder);
    }

    /**
     * This method should return a list of generated Java files related to this
     * table. This list could include various types of model classes, as well as
     * DAO classes.
     *
     * @return the list of generated Java files for this table
     */
    public abstract List<GeneratedJavaFile> getGeneratedJavaFiles();

    /**
     * This method should return a list of generated XML files related to this
     * table. Most implementations will only return one file - the generated
     * SqlMap file.
     *
     * @return the list of generated XML files for this table
     */
    public abstract List<GeneratedXmlFile> getGeneratedXmlFiles();

    /**
     * This method should return a list of generated Kotlin files related to this
     * table. This list could include a data classes, a mapper interface, extension methods, etc.
     *
     * @return the list of generated Kotlin files for this table
     */
    public abstract List<GeneratedKotlinFile> getGeneratedKotlinFiles();

    /**
     * This method should return the number of progress messages that will be
     * sent during the generation phase.
     *
     * @return the number of progress messages
     */
    public abstract int getGenerationSteps();

    public IntrospectedTable getIntrospectedTable() {
        return introspectedTable;
    }

    public abstract static class AbstractRuntimeBuilder<T extends AbstractRuntimeBuilder<T>>
            extends AbstractGeneratorBuilder<T> {
        public abstract AbstractRuntime build();
    }
}
