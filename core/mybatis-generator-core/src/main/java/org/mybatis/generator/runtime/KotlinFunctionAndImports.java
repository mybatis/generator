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
import org.mybatis.generator.api.dom.kotlin.KotlinArg;
import org.mybatis.generator.api.dom.kotlin.KotlinFunction;
import org.mybatis.generator.exception.InternalException;

public class KotlinFunctionAndImports {

    private final KotlinFunction function;
    private final Set<String> imports;

    private KotlinFunctionAndImports(Builder builder) {
        function = Objects.requireNonNull(builder.function);
        imports = builder.imports;
    }

    public KotlinFunction getFunction() {
        return function;
    }

    public Set<String> getImports() {
        return imports;
    }

    public static Builder withFunction(KotlinFunction function) {
        return new Builder().withFunction(function);
    }

    public static class Builder {
        private @Nullable KotlinFunction function;
        private final Set<String> imports = new HashSet<>();

        public Builder withFunction(KotlinFunction function) {
            this.function = function;
            return this;
        }

        public Builder withImport(String im) {
            this.imports.add(im);
            return this;
        }

        public Builder withImports(Set<String> imports) {
            this.imports.addAll(imports);
            return this;
        }

        public Builder withExtraFunctionParts(KotlinFunctionParts kotlinFunctionParts) {
            if (function == null) {
                throw new InternalException(getString("RuntimeError.31")); //$NON-NLS-1$
            }

            for (KotlinArg argument : kotlinFunctionParts.getArguments()) {
                function.addArgument(argument);
            }

            for (String annotation : kotlinFunctionParts.getAnnotations()) {
                function.addAnnotation(annotation);
            }

            function.addCodeLines(kotlinFunctionParts.getCodeLines());
            imports.addAll(kotlinFunctionParts.getImports());
            return this;
        }

        public KotlinFunctionAndImports build() {
            return new KotlinFunctionAndImports(this);
        }

        public Optional<KotlinFunctionAndImports> buildOptional() {
            return Optional.of(build());
        }
    }
}
