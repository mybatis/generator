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
package org.mybatis.generator.api.dom.kotlin.render;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mybatis.generator.api.dom.kotlin.KotlinFunction;

public class KotlinFunctionRenderer {
    public List<String> render(KotlinFunction function) {

        List<String> answer = new ArrayList<>(function.getAnnotations());

        answer.add(renderFirstLine(function));

        answer.addAll(function.getCodeLines().stream().map(KotlinRenderingUtilities::kotlinIndent)
                .collect(Collectors.toList()));

        if (!function.getCodeLines().isEmpty() && !function.isOneLineFunction()) {
            answer.add("}"); //$NON-NLS-1$
        }

        return answer;
    }

    private String renderFirstLine(KotlinFunction function) {
        String firstLine = KotlinRenderingUtilities.renderModifiers(function.getModifiers())
                + "fun " //$NON-NLS-1$
                + function.getName()
                + "("; //$NON-NLS-1$

        firstLine += renderArguments(function);
        firstLine += ")"; //$NON-NLS-1$
        firstLine += renderReturnType(function);

        if (!function.getCodeLines().isEmpty()) {
            if (function.isOneLineFunction()) {
                firstLine += " ="; //$NON-NLS-1$
            } else {
                firstLine += " {"; //$NON-NLS-1$
            }
        }

        return firstLine;
    }

    private String renderArguments(KotlinFunction function) {
        KotlinArgRenderer argRenderer = new KotlinArgRenderer();

        return function.getArguments().stream()
                .map(argRenderer::render)
                .collect(Collectors.joining(", ")); //$NON-NLS-1$
    }

    private String renderReturnType(KotlinFunction function) {
        return function.getExplicitReturnType()
                .map(s -> ": " + s) //$NON-NLS-1$
                .orElse(""); //$NON-NLS-1$
    }
}
