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

import static org.mybatis.generator.internal.util.StringUtility.removeLastEmptyLine;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mybatis.generator.api.Indenter;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.InnerRecord;
import org.mybatis.generator.api.dom.java.JavaDomUtils;
import org.mybatis.generator.internal.util.CustomCollectors;

public class InnerRecordRenderer extends AbstractJavaRenderer {
    private final ParameterRenderer parameterRenderer = new ParameterRenderer();

    public InnerRecordRenderer(Indenter indenter) {
        super(indenter);
    }

    public List<String> render(InnerRecord innerRecord, CompilationUnit compilationUnit) {
        List<String> lines = new ArrayList<>();

        lines.addAll(innerRecord.getJavaDocLines());
        lines.addAll(innerRecord.getAnnotations());
        lines.add(renderFirstLine(innerRecord, compilationUnit));
        lines.addAll(renderFields(innerRecord.getFields(), compilationUnit));
        lines.addAll(renderInitializationBlocks(innerRecord.getInitializationBlocks()));
        lines.addAll(renderClassOrEnumMethods(innerRecord.getMethods(), compilationUnit));
        lines.addAll(renderInnerTypes(innerRecord, compilationUnit));
        lines = removeLastEmptyLine(lines);

        lines.add("}"); //$NON-NLS-1$

        return lines;
    }

    private String renderFirstLine(InnerRecord innerRecord, CompilationUnit compilationUnit) {
        return innerRecord.getVisibility().getValue()
                + "record " //$NON-NLS-1$
                + innerRecord.getType().getShortName()
                + renderTypeParameters(innerRecord.getTypeParameters(), compilationUnit)
                + innerRecord.getParameters().stream()
                        .map(parameter -> parameterRenderer.render(parameter, compilationUnit))
                        .collect(Collectors.joining(", ", "(", ")")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + renderSuperInterfaces(innerRecord, compilationUnit)
                + " {"; //$NON-NLS-1$
    }

    // should return an empty string if no super interfaces
    private String renderSuperInterfaces(InnerRecord innerRecord, CompilationUnit compilationUnit) {
        return innerRecord.getSuperInterfaceTypes().stream()
                .map(tp -> JavaDomUtils.calculateTypeName(compilationUnit, tp))
                .collect(CustomCollectors.joining(", ", " implements ", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
