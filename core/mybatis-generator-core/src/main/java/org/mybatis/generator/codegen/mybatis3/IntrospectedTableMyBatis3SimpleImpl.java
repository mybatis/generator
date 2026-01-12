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
package org.mybatis.generator.codegen.mybatis3;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.SimpleAnnotatedClientGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.SimpleJavaClientGenerator;
import org.mybatis.generator.codegen.mybatis3.model.SimpleModelGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.SimpleXMLMapperGenerator;
import org.mybatis.generator.config.TypedPropertyHolder;
import org.mybatis.generator.internal.ObjectFactory;

/**
 * Introspected table implementation for generating simple MyBatis3 artifacts.
 *
 * <p>Simple means no "by example" methods, flat model, etc.
 *
 * @author Jeff Butler
 */
public class IntrospectedTableMyBatis3SimpleImpl extends IntrospectedTableMyBatis3Impl {
    public IntrospectedTableMyBatis3SimpleImpl() {
        super();
    }

    @Override
    protected void calculateXmlMapperGenerator(@Nullable AbstractJavaClientGenerator javaClientGenerator,
                                               List<String> warnings,
                                               ProgressCallback progressCallback) {
        if (javaClientGenerator == null) {
            if (getContext().getSqlMapGeneratorConfiguration().isPresent()) {
                xmlMapperGenerator = new SimpleXMLMapperGenerator.Builder()
                        .withContext(getContext())
                        .withIntrospectedTable(this)
                        .withProgressCallback(progressCallback)
                        .withWarnings(warnings)
                        .build();
            }
        } else {
            xmlMapperGenerator = javaClientGenerator.getMatchedXMLGenerator().orElse(null);
        }
    }

    @Override
    protected Optional<AbstractJavaClientGenerator> createJavaClientGenerator() {
        return getContext().getJavaClientGeneratorConfiguration().flatMap(TypedPropertyHolder::getConfigurationType)
                .map(t -> {
                    if ("XMLMAPPER".equalsIgnoreCase(t)) { //$NON-NLS-1$
                        return new SimpleJavaClientGenerator(getClientProject());
                    } else if ("ANNOTATEDMAPPER".equalsIgnoreCase(t)) { //$NON-NLS-1$
                        return new SimpleAnnotatedClientGenerator(getClientProject());
                    } else if ("MAPPER".equalsIgnoreCase(t)) { //$NON-NLS-1$
                        return new SimpleJavaClientGenerator(getClientProject());
                    } else {
                        return ObjectFactory.createInternalObject(t, AbstractJavaClientGenerator.class);
                    }
                });
    }

    @Override
    protected void calculateJavaModelGenerators(List<String> warnings, ProgressCallback progressCallback) {
        AbstractJavaGenerator javaGenerator = new SimpleModelGenerator.Builder()
                .withProject(getModelProject())
                .withContext(getContext())
                .withIntrospectedTable(this)
                .withProgressCallback(progressCallback)
                .withWarnings(warnings)
                .build();

        javaGenerators.add(javaGenerator);
    }
}
