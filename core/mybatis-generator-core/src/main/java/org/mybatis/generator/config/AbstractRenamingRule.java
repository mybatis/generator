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

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import org.jspecify.annotations.Nullable;

public abstract class AbstractRenamingRule {
    protected final String searchString;
    protected final String replaceString;
    protected final Pattern pattern;

    protected AbstractRenamingRule(@Nullable String searchString, @Nullable String replaceString) {
        this.searchString = Objects.requireNonNull(searchString);
        this.replaceString = Objects.requireNonNullElse(replaceString, ""); //$NON-NLS-1$
        pattern = Pattern.compile(searchString);
    }

    public Pattern pattern() {
        return pattern;
    }

    public String searchString() {
        return searchString;
    }

    public String replaceString() {
        return replaceString;
    }

    public abstract void validate(List<String> errors, String tableName);
}
