/*
 *    Copyright 2006-2020 the original author or authors.
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
package org.mybatis.generator.api.dom.kotlin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class KotlinFunction extends KotlinNamedItem {
    private final List<KotlinArg> arguments = new ArrayList<>();
    private final List<String> codeLines = new ArrayList<>();
    private final String explicitReturnType;
    private final boolean isOneLineFunction;

    private KotlinFunction(Builder builder) {
        super(builder);
        arguments.addAll(builder.arguments);
        codeLines.addAll(builder.codeLines);
        explicitReturnType = builder.explicitReturnType;
        isOneLineFunction = builder.isOneLineFunction;
    }

    public void addArgument(KotlinArg argument) {
        arguments.add(argument);
    }

    public List<KotlinArg> getArguments() {
        return arguments;
    }

    public void addCodeLine(String codeLine) {
        this.codeLines.add(codeLine);
    }

    public void addCodeLines(List<String> codeLines) {
        this.codeLines.addAll(codeLines);
    }

    public List<String> getCodeLines() {
        return codeLines;
    }

    public Optional<String> getExplicitReturnType() {
        return Optional.ofNullable(explicitReturnType);
    }

    public boolean isOneLineFunction() {
        return isOneLineFunction;
    }

    @Override
    public <R> R accept(KotlinNamedItemVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public static Builder newOneLineFunction(String name) {
        return new Builder(name, true);
    }

    public static Builder newMultiLineFunction(String name) {
        return new Builder(name, false);
    }

    public static class Builder extends AbstractBuilder<Builder> {
        private final boolean isOneLineFunction;
        private final List<KotlinArg> arguments = new ArrayList<>();
        private final List<String> codeLines = new ArrayList<>();
        private String explicitReturnType;

        private Builder(String name, boolean isOneLineFunction) {
            super(name);
            this.isOneLineFunction = isOneLineFunction;
        }

        public Builder withArgument(KotlinArg argument) {
            arguments.add(argument);
            return this;
        }

        public Builder withCodeLine(String codeLine) {
            codeLines.add(codeLine);
            return this;
        }

        public Builder withExplicitReturnType(String explicitReturnType) {
            this.explicitReturnType = explicitReturnType;
            return this;
        }

        public Builder getThis() {
            return this;
        }

        public KotlinFunction build() {
            return new KotlinFunction(this);
        }
    }
}
