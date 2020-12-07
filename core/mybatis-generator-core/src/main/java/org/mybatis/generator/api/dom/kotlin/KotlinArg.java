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
import java.util.Objects;
import java.util.Optional;

public class KotlinArg {

    private final String name;
    private final String dataType;
    private final String initializationString;
    private final List<String> annotations;

    private KotlinArg(Builder builder) {
        name = Objects.requireNonNull(builder.name);
        dataType = builder.dataType;
        initializationString = builder.initializationString;
        annotations = builder.annotations;
    }

    public String getName() {
        return name;
    }

    public Optional<String> getInitializationString() {
        return Optional.ofNullable(initializationString);
    }

    public Optional<String> getDataType() {
        return Optional.ofNullable(dataType);
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public static Builder newArg(String name) {
        return new Builder(name);
    }

    public static class Builder {
        private final String name;
        private String dataType;
        private String initializationString;
        private final List<String> annotations = new ArrayList<>();

        private Builder(String name) {
            this.name = name;
        }

        public Builder withInitializationString(String initializationString) {
            this.initializationString = initializationString;
            return this;
        }

        public Builder withDataType(String dataType) {
            this.dataType = dataType;
            return this;
        }

        public Builder withAnnotation(String annotation) {
            annotations.add(annotation);
            return this;
        }

        public KotlinArg build() {
            return new KotlinArg(this);
        }
    }
}
