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

public abstract class KotlinNamedItem {
    private final String name;
    private final List<KotlinModifier> modifiers = new ArrayList<>();
    private final List<String> annotations = new ArrayList<>();

    protected KotlinNamedItem(AbstractBuilder<?> builder) {
        name = builder.name;
        modifiers.addAll(builder.modifiers);
        annotations.addAll(builder.annotations);
    }

    public String getName() {
        return name;
    }

    public List<KotlinModifier> getModifiers() {
        return modifiers;
    }

    public void addAnnotation(String annotation) {
        annotations.add(annotation);
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public abstract <R> R accept(KotlinNamedItemVisitor<R> visitor);

    public abstract static class AbstractBuilder<T extends AbstractBuilder<T>> {
        private final String name;
        private final List<KotlinModifier> modifiers = new ArrayList<>();
        private final List<String> annotations = new ArrayList<>();

        protected AbstractBuilder(String name) {
            this.name = name;
        }

        public T withModifier(KotlinModifier modifier) {
            modifiers.add(modifier);
            return getThis();
        }

        public T withAnnotation(String annotation) {
            annotations.add(annotation);
            return getThis();
        }

        protected abstract T getThis();
    }
}
