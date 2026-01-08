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
import java.util.Objects;
import java.util.regex.Pattern;

import org.jspecify.annotations.Nullable;

public class IgnoredColumnPattern {

    private final String patternRegex;
    private final Pattern pattern;
    private final List<IgnoredColumnException> exceptions;

    protected IgnoredColumnPattern(Builder builder) {
        this.patternRegex = Objects.requireNonNull(builder.pattern);
        pattern = Pattern.compile(patternRegex);
        exceptions = builder.exceptions;
    }

    public boolean matches(String columnName) {
        boolean matches = pattern.matcher(columnName).matches();

        if (matches) {
            for (IgnoredColumnException exception : exceptions) {
                if (exception.matches(columnName)) {
                    matches = false;
                    break;
                }
            }
        }

        return matches;
    }

    public void validate(List<String> errors, String tableName) {
        if (!stringHasValue(patternRegex)) {
            errors.add(getString("ValidationError.27", tableName)); //$NON-NLS-1$
        }
    }

    public static class Builder {
        private @Nullable String pattern;
        private final List<IgnoredColumnException> exceptions = new ArrayList<>();

        public Builder withPattern(@Nullable String pattern) {
            this.pattern = pattern;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder addException(IgnoredColumnException exception) {
            exceptions.add(exception);
            return this;
        }

        public IgnoredColumnPattern build() {
            return new IgnoredColumnPattern(this);
        }
    }
}
