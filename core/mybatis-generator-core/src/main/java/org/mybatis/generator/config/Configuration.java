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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.exception.InvalidConfigurationException;

public class Configuration {
    private final List<Context> contexts;
    private final List<String> classPathEntries;
    private final @Nullable IndentationConfiguration indentationConfiguration;
    private final @Nullable JavaMergeConfiguration javaMergeConfiguration;

    private Configuration(Builder builder) {
        contexts = builder.contexts;
        classPathEntries = builder.classPathEntries;
        indentationConfiguration = builder.indentationConfiguration;
        javaMergeConfiguration = builder.javaMergeConfiguration;
    }

    public List<String> getClassPathEntries() {
        return classPathEntries;
    }

    public List<Context> getContexts() {
        return contexts;
    }

    public Optional<IndentationConfiguration> getIndentationConfiguration() {
        return Optional.ofNullable(indentationConfiguration);
    }

    public Optional<JavaMergeConfiguration> getJavaMergeConfiguration() {
        return Optional.ofNullable(javaMergeConfiguration);
    }

    /**
     * This method does a simple validation, it makes sure that all required fields have been filled in and that all
     * implementation classes exist and are of the proper type. It does not do any more complex operations such as
     * validating that database tables exist or validating that named columns exist
     *
     * @throws InvalidConfigurationException
     *             the invalid configuration exception
     */
    public void validate() throws InvalidConfigurationException {
        List<String> errors = new ArrayList<>();

        for (String classPathEntry : classPathEntries) {
            if (!stringHasValue(classPathEntry)) {
                errors.add(getString("ValidationError.19")); //$NON-NLS-1$
                // only need to state this error once
                break;
            }
        }

        if (contexts.isEmpty()) {
            errors.add(getString("ValidationError.11")); //$NON-NLS-1$
        } else {
            for (Context context : contexts) {
                context.validate(errors);
            }
        }

        if (!errors.isEmpty()) {
            throw new InvalidConfigurationException(getString("ValidationError.32"), errors); //$NON-NLS-1$
        }
    }

    public static class Builder {
        private final List<Context> contexts = new ArrayList<>();
        private final List<String> classPathEntries = new ArrayList<>();
        private @Nullable IndentationConfiguration indentationConfiguration;
        private @Nullable JavaMergeConfiguration javaMergeConfiguration;

        public Builder withContext(Context context) {
            contexts.add(context);
            return this;
        }

        public Builder withClassPathEntry(@Nullable  String classPathEntry) {
            if (classPathEntry != null) {
                classPathEntries.add(classPathEntry);
            }
            return this;
        }

        public Builder withIndentationConfiguration(IndentationConfiguration indentationConfiguration) {
            this.indentationConfiguration = indentationConfiguration;
            return this;
        }

        public Builder withJavaMergeConfiguration(JavaMergeConfiguration javaMergeConfiguration) {
            this.javaMergeConfiguration = javaMergeConfiguration;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }
    }
}
