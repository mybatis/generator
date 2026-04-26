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
package org.mybatis.generator.api.dom.java.render;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.mybatis.generator.api.dom.Indenter;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.JavaDomUtils;
import org.mybatis.generator.internal.util.CustomCollectors;

public class InnerClassRenderer {
    private final Indenter indenter;

    public InnerClassRenderer(Indenter indenter) {
        this.indenter = Objects.requireNonNull(indenter);
    }

    public List<String> render(InnerClass innerClass, CompilationUnit compilationUnit) {
        List<String> lines = new ArrayList<>();

        lines.addAll(innerClass.getJavaDocLines());
        lines.addAll(innerClass.getAnnotations());
        lines.add(renderFirstLine(innerClass, compilationUnit));
        lines.addAll(RenderingUtilities.renderFields(indenter, innerClass.getFields(), compilationUnit));
        lines.addAll(RenderingUtilities.renderInitializationBlocks(indenter, innerClass.getInitializationBlocks()));
        lines.addAll(RenderingUtilities.renderClassOrEnumMethods(indenter, innerClass.getMethods(), compilationUnit));
        lines.addAll(RenderingUtilities.renderInnerClasses(indenter, innerClass.getInnerClasses(), compilationUnit));
        lines.addAll(RenderingUtilities.renderInnerInterfaces(indenter, innerClass.getInnerInterfaces(),
                compilationUnit));
        lines.addAll(RenderingUtilities.renderInnerEnums(indenter, innerClass.getInnerEnums(), compilationUnit));
        lines.addAll(RenderingUtilities.renderInnerRecords(indenter, innerClass.getInnerRecords(), compilationUnit));

        lines = RenderingUtilities.removeLastEmptyLine(lines);

        lines.add("}"); //$NON-NLS-1$

        return lines;
    }

    private String renderFirstLine(InnerClass innerClass, CompilationUnit compilationUnit) {
        StringBuilder sb = new StringBuilder();

        sb.append(innerClass.getVisibility().getValue());

        if (innerClass.isAbstract()) {
            sb.append("abstract "); //$NON-NLS-1$
        }

        if (innerClass.isStatic()) {
            sb.append("static "); //$NON-NLS-1$
        }

        if (innerClass.isFinal()) {
            sb.append("final "); //$NON-NLS-1$
        }

        sb.append("class "); //$NON-NLS-1$
        sb.append(innerClass.getType().getShortName());
        sb.append(RenderingUtilities.renderTypeParameters(innerClass.getTypeParameters(), compilationUnit));
        sb.append(renderSuperClass(innerClass, compilationUnit));
        sb.append(renderSuperInterfaces(innerClass, compilationUnit));
        sb.append(" {"); //$NON-NLS-1$

        return sb.toString();
    }

    private String renderSuperClass(InnerClass innerClass, CompilationUnit compilationUnit) {
        return innerClass.getSuperClass()
                .map(sc -> " extends " + JavaDomUtils.calculateTypeName(compilationUnit, sc)) //$NON-NLS-1$
                .orElse(""); //$NON-NLS-1$
    }

    // should return an empty string if no super interfaces
    private String renderSuperInterfaces(InnerClass innerClass, CompilationUnit compilationUnit) {
        return innerClass.getSuperInterfaceTypes().stream()
                .map(tp -> JavaDomUtils.calculateTypeName(compilationUnit, tp))
                .collect(CustomCollectors.joining(", ", " implements ", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
