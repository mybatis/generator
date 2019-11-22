/**
 *    Copyright 2006-2019 the original author or authors.
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
package org.mybatis.generator.api.dom.kotlin.render;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.mybatis.generator.api.dom.java.render.RenderingUtilities;
import org.mybatis.generator.api.dom.kotlin.KotlinNamedItem;
import org.mybatis.generator.api.dom.kotlin.KotlinProperty;
import org.mybatis.generator.api.dom.kotlin.KotlinType;
import org.mybatis.generator.internal.util.CustomCollectors;

public class KotlinTypeRenderer {

    public List<String> render(KotlinType kotlinType) {
        List<String> answer = new ArrayList<>();
        KotlinNamedItemRenderer renderer = new KotlinNamedItemRenderer();

        answer.addAll(kotlinType.getAnnotations());

        String line = KotlinRenderingUtilities.renderModifiers(kotlinType.getModifiers())
                + kotlinType.getType().getValue() + " " //$NON-NLS-1$
                + kotlinType.getName();

        line += kotlinType.getSuperTypes().stream()
                .collect(CustomCollectors.joining(", ", " : ", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

        if (kotlinType.getNamedItems().isEmpty()) {
            if (kotlinType.getConstructorProperties().isEmpty()) {
                answer.add(line);
            } else {
                answer.add(line + "("); //$NON-NLS-1$
                answer.addAll(renderConstructorItems(kotlinType));
                answer.add(")"); //$NON-NLS-1$
            }
        } else {
            if (kotlinType.getConstructorProperties().isEmpty()) {
                answer.add(line + " {"); //$NON-NLS-1$
            } else {
                answer.add(line + "("); //$NON-NLS-1$
                answer.addAll(renderConstructorItems(kotlinType));
                answer.add(") {"); //$NON-NLS-1$
            }
        }

        for (KotlinNamedItem namedItem : kotlinType.getNamedItems()) {
            answer.addAll(renderer.render(namedItem).stream().map(KotlinRenderingUtilities::kotlinIndent)
                    .collect(Collectors.toList()));
            answer.add(""); //$NON-NLS-1$
        }

        answer = RenderingUtilities.removeLastEmptyLine(answer);

        if (!kotlinType.getNamedItems().isEmpty()) {
            answer.add("}"); //$NON-NLS-1$
        }

        return answer;
    }

    private List<String> renderConstructorItems(KotlinType kotlinType) {
        List<String> lines = new ArrayList<>();
        KotlinPropertyRenderer renderer = new KotlinPropertyRenderer();

        Iterator<KotlinProperty> iter = kotlinType.getConstructorProperties().iterator();
        while (iter.hasNext()) {
            lines.addAll(renderer.render(iter.next()).stream().map(KotlinRenderingUtilities::kotlinIndent)
                    .collect(Collectors.toList()));
            if (iter.hasNext()) {
                lines.set(lines.size() - 1,
                        lines.get(lines.size() - 1) + ","); //$NON-NLS-1$
            }
        }

        return lines;
    }
}
