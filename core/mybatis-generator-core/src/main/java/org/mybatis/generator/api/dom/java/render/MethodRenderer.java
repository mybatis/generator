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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.JavaDomUtils;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.internal.util.CustomCollectors;

public class MethodRenderer {
    private TypeParameterRenderer typeParameterRenderer = new TypeParameterRenderer();
    private ParameterRenderer parameterRenderer = new ParameterRenderer();
    private BodyLineRenderer bodyLineRenderer = new BodyLineRenderer();
    
    public List<String> render(Method method, boolean inInterface, CompilationUnit compilationUnit) {
        List<String> lines = new ArrayList<>();

        lines.addAll(method.getJavaDocLines());
        lines.addAll(method.getAnnotations());
        lines.add(getFirstLine(method, inInterface, compilationUnit));
        
        if (!method.isAbstract() && !method.isNative()) {
            lines.addAll(bodyLineRenderer.render(method.getBodyLines()));
            lines.add("}"); //$NON-NLS-1$
        }

        return lines;
    }

    private String getFirstLine(Method method, boolean inInterface, CompilationUnit compilationUnit) {
        StringBuilder sb = new StringBuilder();

        sb.append(renderVisibility(method, inInterface));

        if (method.isAbstract() && !inInterface) {
            sb.append("abstract "); //$NON-NLS-1$
        }

        if (method.isDefault()) {
            sb.append("default "); //$NON-NLS-1$
        }

        if (method.isStatic()) {
            sb.append("static "); //$NON-NLS-1$
        }

        if (method.isFinal()) {
            sb.append("final "); //$NON-NLS-1$
        }

        if (method.isSynchronized()) {
            sb.append("synchronized "); //$NON-NLS-1$
        }

        if (method.isNative()) {
            sb.append("native "); //$NON-NLS-1$
        }

        sb.append(renderTypeParameters(method, compilationUnit));
        
        if (!method.isConstructor()) {
            sb.append(method.getReturnType()
                    .map(t -> JavaDomUtils.calculateTypeName(compilationUnit, t))
                    .orElse("void")); //$NON-NLS-1$
            
            sb.append(' ');
        }

        sb.append(method.getName());
        
        sb.append(renderParameters(method, compilationUnit));
        
        sb.append(renderExceptions(method, compilationUnit));
        
        if (method.isAbstract() || method.isNative()) {
            sb.append(';');
        } else {
            sb.append(" {"); //$NON-NLS-1$
        }
        return sb.toString();

    }

    private String renderVisibility(Method method, boolean inInterface) {
        if (inInterface && method.getVisibility() == JavaVisibility.PUBLIC) {
            return ""; //$NON-NLS-1$
        }
        
        return method.getVisibility().getValue();
    }
    
    // should return an empty string if no type parameters
    private String renderTypeParameters(Method method, CompilationUnit compilationUnit) {
        return method.getTypeParameters().stream()
                .map(tp -> typeParameterRenderer.render(tp, compilationUnit))
                .collect(CustomCollectors.joining(", ", "<", "> ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    private String renderParameters(Method method, CompilationUnit compilationUnit) {
        return method.getParameters().stream()
                .map(p -> parameterRenderer.render(p, compilationUnit))
                .collect(Collectors.joining(", ", "(", ")")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    // should return an empty string if no exceptions
    private String renderExceptions(Method method, CompilationUnit compilationUnit) {
        return method.getExceptions().stream()
                .map(jt -> JavaDomUtils.calculateTypeName(compilationUnit, jt))
                .collect(CustomCollectors.joining(", ", " throws ", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
