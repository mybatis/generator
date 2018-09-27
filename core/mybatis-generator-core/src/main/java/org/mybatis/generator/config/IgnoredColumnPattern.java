/**
 *    Copyright 2006-2018 the original author or authors.
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
package org.mybatis.generator.config;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class IgnoredColumnPattern {

    private String patternRegex;
    private Pattern pattern;
    private List<IgnoredColumnException> exceptions = new ArrayList<>();

    public IgnoredColumnPattern(String patternRegex) {
        this.patternRegex = patternRegex;
        pattern = Pattern.compile(patternRegex);
    }

    public void addException(IgnoredColumnException exception) {
        exceptions.add(exception);
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
            errors.add(getString("ValidationError.27", //$NON-NLS-1$
                    tableName));
        }
    }
}
