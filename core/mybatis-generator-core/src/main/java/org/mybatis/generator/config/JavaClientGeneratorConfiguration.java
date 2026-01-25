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
package org.mybatis.generator.config;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.Nullable;

public class JavaClientGeneratorConfiguration extends TypedPropertyHolder {
    // Known types...
    public static final String ANNOTATED_MAPPER = "AnnotatedMapper"; //$NON-NLS-1$
    public static final String MAPPER = "Mapper"; //$NON-NLS-1$
    public static final String MIXED_MAPPER = "MixedMapper"; //$NON-NLS-1$
    public static final String XML_MAPPER = "XMLMapper"; //$NON-NLS-1$

    private final String targetPackage;
    private final String targetProject;

    protected JavaClientGeneratorConfiguration(Builder builder) {
        super(builder);
        this.targetPackage = Objects.requireNonNull(builder.targetPackage);
        this.targetProject = Objects.requireNonNull(builder.targetProject);
    }

    public String getTargetProject() {
        return targetProject;
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public void validate(List<String> errors, String contextId) {
        if (!stringHasValue(targetProject)) {
            errors.add(getString("ValidationError.2", contextId)); //$NON-NLS-1$
        }

        if (!stringHasValue(targetPackage)) {
            errors.add(getString("ValidationError.12", "javaClientGenerator", contextId)); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    public boolean requiresXmlMapper() {
        return XML_MAPPER.equalsIgnoreCase(configurationType) || MIXED_MAPPER.equalsIgnoreCase(configurationType);
    }

    public static class Builder extends TypedBuilder<Builder> {
        private @Nullable String targetPackage;
        private @Nullable String targetProject;

        public Builder withTargetPackage(@Nullable String targetPackage) {
            this.targetPackage = targetPackage;
            return this;
        }

        public Builder withTargetProject(@Nullable String targetProject) {
            this.targetProject = targetProject;
            return this;
        }

        public JavaClientGeneratorConfiguration build() {
            return new JavaClientGeneratorConfiguration(this);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
