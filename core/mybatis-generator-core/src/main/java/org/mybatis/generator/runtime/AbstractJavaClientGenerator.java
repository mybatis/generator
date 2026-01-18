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
package org.mybatis.generator.runtime;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.AbstractXmlGenerator;

import java.util.Objects;
import java.util.Optional;

/**
 * This class exists to that Java client generators can specify whether
 * an XML generator is required to match the methods in the
 * Java client.  For example, a Java client built entirely with
 * annotations does not need matching XML.
 *
 * @author Jeff Butler
 */
public abstract class AbstractJavaClientGenerator extends AbstractJavaGenerator {
    protected final String myBatis3JavaMapperType;

    protected AbstractJavaClientGenerator(AbstractJavaClientGeneratorBuilder<?> builder) {
        super(builder);
        myBatis3JavaMapperType = Objects.requireNonNull(builder.myBatis3JavaMapperType);
    }

    /**
     * Returns an instance of the XML generator associated
     * with this client generator.
     *
     * @return the matched XML generator if any.
     */
    public abstract Optional<AbstractXmlGenerator> getMatchedXMLGenerator();

    public abstract static class AbstractJavaClientGeneratorBuilder<T extends AbstractJavaClientGeneratorBuilder<T>>
            extends AbstractJavaGeneratorBuilder<T> {
        private @Nullable String myBatis3JavaMapperType;

        public T withMyBatis3JavaMapperType(String myBatis3JavaMapperType) {
            this.myBatis3JavaMapperType = myBatis3JavaMapperType;
            return getThis();
        }

        public abstract AbstractJavaClientGenerator build();
    }
}
