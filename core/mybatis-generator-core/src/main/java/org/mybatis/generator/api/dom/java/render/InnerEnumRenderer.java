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

import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.JAVA_INDENT;
import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.renderClassOrEnumMethods;
import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.renderFields;
import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.renderInitializationBlocks;
import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.renderInnerClasses;
import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.renderInnerEnums;
import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.renderInnerInterfaces;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.JavaDomUtils;
import org.mybatis.generator.internal.util.CustomCollectors;

public class InnerEnumRenderer {
    
    public List<String> render(InnerEnum innerEnum, CompilationUnit compilationUnit) {
        List<String> lines = new ArrayList<>();
        
        lines.addAll(innerEnum.getJavaDocLines());
        lines.addAll(innerEnum.getAnnotations());
        lines.add(renderFirstLine(innerEnum, compilationUnit));
        lines.addAll(renderEnumConstants(innerEnum));
        lines.addAll(renderFields(innerEnum.getFields(), compilationUnit));
        lines.addAll(renderInitializationBlocks(innerEnum.getInitializationBlocks()));
        lines.addAll(renderClassOrEnumMethods(innerEnum.getMethods(), compilationUnit));
        lines.addAll(renderInnerClasses(innerEnum.getInnerClasses(), compilationUnit));
        lines.addAll(renderInnerInterfaces(innerEnum.getInnerInterfaces(), compilationUnit));
        lines.addAll(renderInnerEnums(innerEnum.getInnerEnums(), compilationUnit));

        // last line might be blank, remove it if so
        if (lines.get(lines.size() - 1).isEmpty()) {
            lines = lines.subList(0, lines.size() - 1);
        }

        lines.add("}"); //$NON-NLS-1$

        return lines;
    }

    private String renderFirstLine(InnerEnum innerEnum, CompilationUnit compilationUnit) {
        StringBuilder sb = new StringBuilder();

        sb.append(innerEnum.getVisibility().getValue());

        if (innerEnum.isStatic()) {
            sb.append("static "); //$NON-NLS-1$
        }

        sb.append("enum "); //$NON-NLS-1$
        sb.append(innerEnum.getType().getShortName());
        sb.append(renderSuperInterfaces(innerEnum, compilationUnit));
        sb.append(" {"); //$NON-NLS-1$
        
        return sb.toString();
    }
    
    private List<String> renderEnumConstants(InnerEnum innerEnum) {
        List<String> answer = new ArrayList<>();
        
        Iterator<String> iter = innerEnum.getEnumConstants().iterator();
        while (iter.hasNext()) {
            String enumConstant = iter.next();

            if (iter.hasNext()) {
                answer.add(JAVA_INDENT + enumConstant + ","); //$NON-NLS-1$
            } else {
                answer.add(JAVA_INDENT + enumConstant + ";"); //$NON-NLS-1$
            }
        }
        
        answer.add(""); //$NON-NLS-1$
        return answer;
    }

    // should return an empty string if no super interfaces
    private String renderSuperInterfaces(InnerEnum innerEnum, CompilationUnit compilationUnit) {
        return innerEnum.getSuperInterfaceTypes().stream()
                .map(tp -> JavaDomUtils.calculateTypeName(compilationUnit, tp))
                .collect(CustomCollectors.joining(", ", " implements ", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
