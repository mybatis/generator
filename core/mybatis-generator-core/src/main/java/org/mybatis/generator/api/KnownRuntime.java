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
import org.mybatis.generator.runtime.dynamicsql.java.JavaDynamicSqlRuntime;
import org.mybatis.generator.runtime.dynamicsql.kotlin.KotlinDynamicSqlRuntime;
import org.mybatis.generator.runtime.mybatis3.LegacyJavaRuntime;
import org.mybatis.generator.runtime.mybatis3.LegacySimpleJavaRuntime;

public enum KnownRuntime {
    MYBATIS3("MyBatis3", LegacyJavaRuntime.Builder.class.getName(), false, true),
    MYBATIS3_SIMPLE("MyBatis3Simple", LegacySimpleJavaRuntime.Builder.class.getName(), false, true),
    MYBATIS3_DYNAMIC_SQL("MyBatis3DynamicSql", JavaDynamicSqlRuntime.Builder.class.getName(), true, false),
    MYBATIS3_KOTLIN("MyBatis3Kotlin", KotlinDynamicSqlRuntime.Builder.class.getName(), true, false),
    UNKNOWN("Unknown", "Unknown", false, false);

    private final String alias;
    private final String builderClassName;
    private final boolean isDynamicSqlBased;
    private final boolean isLegacyMyBatis3Based;

    public String getBuilderClassName() {
        return builderClassName;
    }

    public String getAlias() {
        return alias;
    }

    public boolean isDynamicSqlBased() {
        return isDynamicSqlBased;
    }

    public boolean isLegacyMyBatis3Based() {
        return isLegacyMyBatis3Based;
    }

    KnownRuntime(String alias, String builderClassName, boolean isDynamicSqlBased, boolean isLegacyMyBatis3Based) {
        this.alias = alias;
        this.builderClassName = builderClassName;
        this.isDynamicSqlBased = isDynamicSqlBased;
        this.isLegacyMyBatis3Based = isLegacyMyBatis3Based;
    }

    public static KnownRuntime getByAlias(@Nullable String alias) {
        for (KnownRuntime rt : values()) {
            if (rt.alias.equalsIgnoreCase(alias)) {
                return rt;
            }
        }
        return UNKNOWN;
    }
}
