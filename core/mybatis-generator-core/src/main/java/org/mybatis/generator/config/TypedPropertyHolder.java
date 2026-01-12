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

import java.util.Optional;

import org.jspecify.annotations.Nullable;

public abstract class TypedPropertyHolder extends PropertyHolder {

    protected final @Nullable String configurationType;

    protected TypedPropertyHolder(TypedBuilder<?> builder) {
        super(builder);
        this.configurationType = builder.configurationType;
    }

    public Optional<String> getConfigurationType() {
        return Optional.ofNullable(configurationType);
    }

    public abstract static class TypedBuilder<T extends TypedBuilder<T>> extends AbstractBuilder<T> {
        protected @Nullable String configurationType;

        /**
         * Sets the value of the type specified in the configuration. If the special
         * value DEFAULT is specified, then the value will be ignored.
         *
         * @param configurationType the value of the type specified in the configuration. For some
         *      subclasses, if the special value DEFAULT is specified, then the value will be ignored.
         */
        public T withConfigurationType(@Nullable String configurationType) {
            this.configurationType = configurationType;
            return getThis();
        }
    }
}
