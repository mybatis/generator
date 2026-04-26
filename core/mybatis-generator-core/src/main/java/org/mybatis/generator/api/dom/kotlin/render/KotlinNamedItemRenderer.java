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
package org.mybatis.generator.api.dom.kotlin.render;

import java.util.List;
import java.util.Objects;

import org.mybatis.generator.api.dom.Indenter;
import org.mybatis.generator.api.dom.kotlin.KotlinFunction;
import org.mybatis.generator.api.dom.kotlin.KotlinNamedItem;
import org.mybatis.generator.api.dom.kotlin.KotlinNamedItemVisitor;
import org.mybatis.generator.api.dom.kotlin.KotlinProperty;
import org.mybatis.generator.api.dom.kotlin.KotlinType;

public class KotlinNamedItemRenderer implements KotlinNamedItemVisitor<List<String>> {
    private final Indenter indenter;

    public KotlinNamedItemRenderer(Indenter indenter) {
        this.indenter = Objects.requireNonNull(indenter);
    }

    public List<String> render(KotlinNamedItem namedItem) {
        return namedItem.accept(this);
    }

    @Override
    public List<String> visit(KotlinType kotlinType) {
        return new KotlinTypeRenderer(indenter).render(kotlinType);
    }

    @Override
    public List<String> visit(KotlinProperty kotlinProperty) {
        return new KotlinPropertyRenderer().render(kotlinProperty);
    }

    @Override
    public List<String> visit(KotlinFunction kotlinFunction) {
        return new KotlinFunctionRenderer(indenter).render(kotlinFunction);
    }
}
