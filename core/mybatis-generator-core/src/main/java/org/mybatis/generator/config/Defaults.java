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

import org.mybatis.generator.api.KnownRuntime;
import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.DefaultKotlinFormatter;
import org.mybatis.generator.api.dom.DefaultXmlFormatter;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.GenericConnectionFactory;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

public final class Defaults {
    private Defaults() {
        // utility class - no instances
    }

    public static final ModelType DEFAULT_MODEL_TYPE = ModelType.CONDITIONAL;
    public static final String DEFAULT_BEGINNING_DELIMITER = "\"";
    public static final String DEFAULT_ENDING_DELIMITER = "\"";
    public static final String DEFAULT_COMMENT_GENERATOR = DefaultCommentGenerator.class.getName();
    public static final String DEFAULT_KOTLIN_FORMATTER = DefaultKotlinFormatter.class.getName();
    public static final String DEFAULT_XML_FORMATTER = DefaultXmlFormatter.class.getName();
    public static final String DEFAULT_JAVA_FORMATTER = DefaultJavaFormatter.class.getName();
    public static final String DEFAULT_GENERIC_CONNECTION_FACTORY = GenericConnectionFactory.class.getName();
    public static final String DEFAULT_JAVA_TYPE_RESOLVER = JavaTypeResolverDefaultImpl.class.getName();
    public static final KnownRuntime DEFAULT_RUNTIME = KnownRuntime.MYBATIS3_DYNAMIC_SQL;
}
