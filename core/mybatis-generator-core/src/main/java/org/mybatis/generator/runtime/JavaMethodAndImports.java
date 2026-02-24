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
package org.mybatis.generator.runtime;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.exception.InternalException;

public class JavaMethodAndImports {

    private final Method method;
    private final Set<FullyQualifiedJavaType> imports;
    private final Set<String> staticImports;

    private JavaMethodAndImports(Builder builder) {
        method = Objects.requireNonNull(builder.method);
        imports = builder.imports;
        staticImports = builder.staticImports;
    }

    public Method getMethod() {
        return method;
    }

    public Set<FullyQualifiedJavaType> getImports() {
        return imports;
    }

    public Set<String> getStaticImports() {
        return staticImports;
    }

    public static Builder withMethod(Method method) {
        return new Builder().withMethod(method);
    }

    public static class Builder {
        private @Nullable Method method;
        private final Set<FullyQualifiedJavaType> imports = new HashSet<>();
        private final Set<String> staticImports = new HashSet<>();

        public Builder withMethod(Method method) {
            this.method = method;
            return this;
        }

        public Builder withImport(FullyQualifiedJavaType importedType) {
            this.imports.add(importedType);
            return this;
        }

        public Builder withImports(Set<FullyQualifiedJavaType> imports) {
            this.imports.addAll(imports);
            return this;
        }

        public Builder withStaticImport(String staticImport) {
            this.staticImports.add(staticImport);
            return this;
        }

        public Builder withStaticImports(Set<String> staticImports) {
            this.staticImports.addAll(staticImports);
            return this;
        }

        public Builder withExtraMethodParts(JavaMethodParts javaMethodParts) {
            if (method == null) {
                throw new InternalException(getString("RuntimeError.31")); //$NON-NLS-1$
            }

            for (Parameter parameter : javaMethodParts.getParameters()) {
                method.addParameter(parameter);
            }

            for (String annotation : javaMethodParts.getAnnotations()) {
                method.addAnnotation(annotation);
            }

            method.addBodyLines(javaMethodParts.getBodyLines());

            withImports(javaMethodParts.getImports());
            return this;
        }

        public JavaMethodAndImports build() {
            return new JavaMethodAndImports(this);
        }

        public Optional<JavaMethodAndImports> buildOptional() {
            return Optional.of(build());
        }
    }
}
