/**
 *    Copyright 2006-2018 the original author or authors.
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
package org.mybatis.generator.api.dom.java.render;

import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.renderFields;
import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.renderInnerClasses;
import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.renderInnerEnums;
import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.renderInnerInterfaces;
import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.renderInterfaceMethods;
import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.renderTypeParameters;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.InnerInterface;
import org.mybatis.generator.api.dom.java.JavaDomUtils;
import org.mybatis.generator.internal.util.CustomCollectors;

public class InnerInterfaceRenderer {
    
    public List<String> render(InnerInterface innerInterface, CompilationUnit compilationUnit) {
        List<String> lines = new ArrayList<>();
        
        lines.addAll(innerInterface.getJavaDocLines());
        lines.addAll(innerInterface.getAnnotations());
        lines.add(renderFirstLine(innerInterface, compilationUnit));
        lines.addAll(renderFields(innerInterface.getFields(), compilationUnit));
        lines.addAll(renderInterfaceMethods(innerInterface.getMethods(), compilationUnit));
        lines.addAll(renderInnerClasses(innerInterface.getInnerClasses(), compilationUnit));
        lines.addAll(renderInnerInterfaces(innerInterface.getInnerInterfaces(), compilationUnit));
        lines.addAll(renderInnerEnums(innerInterface.getInnerEnums(), compilationUnit));

        // last line might be blank, remove it if so
        if (lines.get(lines.size() - 1).isEmpty()) {
            lines = lines.subList(0, lines.size() - 1);
        }

        lines.add("}"); //$NON-NLS-1$

        return lines;
    }

    private String renderFirstLine(InnerInterface innerInterface, CompilationUnit compilationUnit) {
        StringBuilder sb = new StringBuilder();

        sb.append(innerInterface.getVisibility().getValue());

        if (innerInterface.isStatic()) {
            sb.append("static "); //$NON-NLS-1$
        }

        sb.append("interface "); //$NON-NLS-1$
        sb.append(innerInterface.getType().getShortName());
        sb.append(renderTypeParameters(innerInterface.getTypeParameters(), compilationUnit));
        sb.append(renderSuperInterfaces(innerInterface, compilationUnit));
        sb.append(" {"); //$NON-NLS-1$
        
        return sb.toString();
    }

    // should return an empty string if no super interfaces
    private String renderSuperInterfaces(InnerInterface innerInterface, CompilationUnit compilationUnit) {
        return innerInterface.getSuperInterfaceTypes().stream()
                .map(tp -> JavaDomUtils.calculateTypeName(compilationUnit, tp))
                .collect(CustomCollectors.joining(", ", " extends ", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
