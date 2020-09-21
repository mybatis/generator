/*
 *    Copyright 2006-2020 the original author or authors.
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
package org.mybatis.generator.runtime.kotlin;

import java.util.List;

import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.codegen.AbstractKotlinGenerator;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;

public class IntrospectedTableKotlinImpl extends IntrospectedTableMyBatis3Impl {

    public IntrospectedTableKotlinImpl() {
        super();
        targetRuntime = TargetRuntime.MYBATIS3_DSQL;
    }

    @Override
    public void calculateGenerators(List<String> warnings, ProgressCallback progressCallback) {
        calculateKotlinDataClassGenerator(warnings, progressCallback);
        if (contextHasClientConfiguration() && rules.generateJavaClient()) {
            calculateKotlinMapperAndExtensionsGenerator(warnings, progressCallback);
        }
    }

    private boolean contextHasClientConfiguration() {
        return context.getJavaClientGeneratorConfiguration() != null;
    }

    protected void calculateKotlinMapperAndExtensionsGenerator(List<String> warnings,
            ProgressCallback progressCallback) {
        AbstractKotlinGenerator kotlinGenerator = new KotlinMapperAndExtensionsGenerator(getClientProject());
        initializeAbstractGenerator(kotlinGenerator, warnings,
                progressCallback);
        kotlinGenerators.add(kotlinGenerator);
    }

    protected void calculateKotlinDataClassGenerator(List<String> warnings,
            ProgressCallback progressCallback) {
        AbstractKotlinGenerator kotlinGenerator = new KotlinDataClassGenerator(getModelProject());
        initializeAbstractGenerator(kotlinGenerator, warnings, progressCallback);
        kotlinGenerators.add(kotlinGenerator);
    }

    @Override
    public boolean requiresXMLGenerator() {
        return false;
    }
}
