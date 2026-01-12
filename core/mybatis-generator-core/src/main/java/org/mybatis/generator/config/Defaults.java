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

import org.mybatis.generator.api.dom.DefaultJavaFormatter;
import org.mybatis.generator.api.dom.DefaultKotlinFormatter;
import org.mybatis.generator.api.dom.DefaultXmlFormatter;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.GenericConnectionFactory;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

public interface Defaults {
    ModelType DEFAULT_MODEL_TYPE = ModelType.CONDITIONAL;
    String DEFAULT_BEGINNING_DELIMITER = "\"";
    String DEFAULT_ENDING_DELIMITER = "\"";
    String DEFAULT_COMMENT_GENERATOR = DefaultCommentGenerator.class.getName();
    String DEFAULT_KOTLIN_FORMATTER = DefaultKotlinFormatter.class.getName();
    String DEFAULT_XML_FORMATTER = DefaultXmlFormatter.class.getName();
    String DEFAULT_JAVA_FORMATTER = DefaultJavaFormatter.class.getName();
    String DEFAULT_GENERIC_CONNECTION_FACTORY = GenericConnectionFactory.class.getName();
    String DEFAULT_JAVA_TYPE_RESOLVER = JavaTypeResolverDefaultImpl.class.getName();
}
