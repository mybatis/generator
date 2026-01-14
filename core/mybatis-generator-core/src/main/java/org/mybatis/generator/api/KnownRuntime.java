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
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3SimpleImpl;
import org.mybatis.generator.runtime.dynamic.sql.IntrospectedTableMyBatis3DynamicSqlImpl;
import org.mybatis.generator.runtime.kotlin.IntrospectedTableKotlinImpl;

public enum KnownRuntime {
    MYBATIS3("MyBatis3", IntrospectedTableMyBatis3Impl.Builder.class.getName(), true, false),
    MYBATIS3_SIMPLE("MyBatis3Simple",IntrospectedTableMyBatis3SimpleImpl.Builder.class.getName(), true, false),
    MYBATIS3_DYNAMIC_SQL("MyBatis3DynamicSql", IntrospectedTableMyBatis3DynamicSqlImpl.Builder.class.getName(), false, true),
    MYBATIS3_KOTLIN("MyBatis3Kotlin", IntrospectedTableKotlinImpl.Builder.class.getName(), false, true),
    UNKNOWN("Unknown", "Unknown", false, false);

    private final String alias;
    private final String builderClassName;
    private final boolean requiresXMLGenerator;
    private final boolean isDynamicSqlBased;

    public String getBuilderClassName() {
        return builderClassName;
    }

    public String getAlias() {
        return alias;
    }

    public boolean requiresXMLGenerator() {
        return requiresXMLGenerator;
    }

    public boolean isDynamicSqlBased() {
        return isDynamicSqlBased;
    }

    KnownRuntime(String alias, String builderClassName, boolean requiresXMLGenerator,
                 boolean isDynamicSqlBased) {
        this.alias = alias;
        this.builderClassName = builderClassName;
        this.requiresXMLGenerator = requiresXMLGenerator;
        this.isDynamicSqlBased = isDynamicSqlBased;
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
