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

import org.jspecify.annotations.Nullable;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.List;
import java.util.Objects;

public class JavaModelGeneratorConfiguration extends PropertyHolder {

    private final String targetPackage;
    private final String targetProject;

    protected JavaModelGeneratorConfiguration(Builder builder) {
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
            errors.add(getString("ValidationError.0", contextId)); //$NON-NLS-1$
        }

        if (!stringHasValue(targetPackage)) {
            errors.add(getString("ValidationError.12", //$NON-NLS-1$
                    "JavaModelGenerator", contextId)); //$NON-NLS-1$
        }
    }

    public static class Builder extends AbstractBuilder<Builder> {
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

        public JavaModelGeneratorConfiguration build() {
            return new JavaModelGeneratorConfiguration(this);
        }

        @Override
        protected Builder getThis() {
            return this;
        }
    }
}
