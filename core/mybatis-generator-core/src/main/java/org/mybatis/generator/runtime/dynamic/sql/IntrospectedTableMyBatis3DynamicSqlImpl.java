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
package org.mybatis.generator.runtime.dynamic.sql;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;

public class IntrospectedTableMyBatis3DynamicSqlImpl extends IntrospectedTableMyBatis3Impl {

    public IntrospectedTableMyBatis3DynamicSqlImpl() {
        targetRuntime = TargetRuntime.MYBATIS3_DSQL;
    }

    @Override
    protected void calculateXmlMapperGenerator(@Nullable AbstractJavaClientGenerator javaClientGenerator,
            List<String> warnings,
            ProgressCallback progressCallback) {
        // no XML with dynamic SQL support
        xmlMapperGenerator = null;
    }

    @Override
    protected Optional<AbstractJavaClientGenerator> createJavaClientGenerator() {
        return getContext().getJavaClientGeneratorConfiguration()
                .map(c -> new DynamicSqlMapperGenerator(getClientProject()));
    }

    @Override
    protected void calculateJavaModelGenerators(List<String> warnings, ProgressCallback progressCallback) {
        AbstractJavaGenerator javaGenerator = new DynamicSqlModelGenerator(getModelProject());
        initializeAbstractGenerator(javaGenerator, warnings,
                progressCallback);
        javaGenerators.add(javaGenerator);
    }

    @Override
    public boolean requiresXMLGenerator() {
        return false;
    }
}
