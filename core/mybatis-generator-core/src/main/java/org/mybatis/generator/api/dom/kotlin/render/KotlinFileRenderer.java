/*
 *    Copyright 2006-2025 the original author or authors.
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinNamedItem;

public class KotlinFileRenderer {

    public String render(KotlinFile kotlinFile) {
        List<String> lines = new ArrayList<>();
        KotlinNamedItemRenderer renderer = new KotlinNamedItemRenderer();

        kotlinFile.getPackage().ifPresent(p -> lines.add("package " + p)); //$NON-NLS-1$

        lines.addAll(prependBlankLineIfNotEmpty(lines.size(), renderImports(kotlinFile)));

        for (KotlinNamedItem item : kotlinFile.getNamedItems()) {
            lines.addAll(prependBlankLineIfNotEmpty(lines.size(), renderer.render(item)));
        }

        lines.addAll(0, kotlinFile.getFileCommentLines());
        return lines.stream()
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private List<String> prependBlankLineIfNotEmpty(int currentLength, List<String> in) {
        if (in.isEmpty() || currentLength == 0) {
            return in;
        }

        return Stream.of(Stream.of(""), in.stream()) //$NON-NLS-1$
                .flatMap(Function.identity())
                .toList();
    }

    private List<String> renderImports(KotlinFile kotlinFile) {
        return kotlinFile.getImports().stream()
                .map(s -> "import " + s) //$NON-NLS-1$
                .toList();
    }
}
