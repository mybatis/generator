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
package org.mybatis.generator.api;

import org.jspecify.annotations.Nullable;

public enum IndentType {
    SPACES("Spaces"),
    TABS("Tabs");

    private final String alias;

    IndentType(String alias) {
        this.alias = alias;
    }

    public static @Nullable IndentType getByAlias(String alias) {
        for (IndentType it : values()) {
            if (it.alias.equalsIgnoreCase(alias)) {
                return it;
            }
        }
        return null;
    }
}
