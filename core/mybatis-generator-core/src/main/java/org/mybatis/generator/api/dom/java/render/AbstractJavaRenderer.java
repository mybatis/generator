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

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jspecify.annotations.Nullable;
import org.mybatis.generator.api.Indenter;
import org.mybatis.generator.api.dom.java.AbstractJavaType;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InitializationBlock;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.InnerInterface;
import org.mybatis.generator.api.dom.java.InnerRecord;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TypeParameter;
import org.mybatis.generator.internal.util.CustomCollectors;

public abstract class AbstractJavaRenderer {
    protected final Indenter indenter;
    private final FieldRenderer fieldRenderer;
    private final MethodRenderer methodRenderer;
    private final TypeParameterRenderer typeParameterRenderer;
    private @Nullable InnerClassRenderer innerClassRenderer;
    private @Nullable InnerEnumRenderer innerEnumRenderer;
    private @Nullable InnerInterfaceRenderer innerInterfaceRenderer;
    private @Nullable InnerRecordRenderer innerRecordRenderer;

    protected AbstractJavaRenderer(Indenter indenter) {
        this.indenter = Objects.requireNonNull(indenter);
        fieldRenderer = new FieldRenderer();
        methodRenderer = new MethodRenderer(indenter);
        typeParameterRenderer = new TypeParameterRenderer();
    }

    // should return an empty string if no type parameters
    public String renderTypeParameters(List<TypeParameter> typeParameters, CompilationUnit compilationUnit) {
        return typeParameters.stream()
                .map(tp -> typeParameterRenderer.render(tp, compilationUnit))
                .collect(CustomCollectors.joining(", ", "<", "> ")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    public List<String> renderFields(List<Field> fields, CompilationUnit compilationUnit) {
        return fields.stream()
                .flatMap(f -> renderField(f, compilationUnit))
                .toList();
    }

    private Stream<String> renderField(Field field, CompilationUnit compilationUnit) {
        return addEmptyLine(fieldRenderer.render(field, compilationUnit).stream()
                .map(this::javaIndent));
    }

    public List<String> renderInitializationBlocks(List<InitializationBlock> initializationBlocks) {
        return initializationBlocks.stream()
                .flatMap(this::renderInitializationBlock)
                .toList();
    }

    private Stream<String> renderInitializationBlock(InitializationBlock initializationBlock) {
        InitializationBlockRenderer initializationBlockRenderer = new InitializationBlockRenderer(indenter);
        return addEmptyLine(initializationBlockRenderer.render(initializationBlock).stream()
                .map(this::javaIndent));
    }

    public List<String> renderClassOrEnumMethods(List<Method> methods, CompilationUnit compilationUnit) {
        return methods.stream()
                .flatMap(m -> renderMethod(m, false, compilationUnit))
                .toList();
    }

    public List<String> renderInterfaceMethods(List<Method> methods, CompilationUnit compilationUnit) {
        return methods.stream()
                .flatMap(m -> renderMethod(m, true, compilationUnit))
                .toList();
    }

    private Stream<String> renderMethod(Method method, boolean inInterface, CompilationUnit compilationUnit) {
        return addEmptyLine(methodRenderer.render(method, inInterface, compilationUnit).stream()
                .map(this::javaIndent));
    }

    public List<String> renderInnerClasses(List<InnerClass> innerClasses, CompilationUnit compilationUnit) {
        return innerClasses.stream()
                .flatMap(ic -> renderInnerClass(ic, compilationUnit))
                .toList();
    }

    private Stream<String> renderInnerClass(InnerClass innerClass, CompilationUnit compilationUnit) {
        innerClassRenderer = Objects.requireNonNullElseGet(innerClassRenderer, () -> new InnerClassRenderer(indenter));
        return addEmptyLine(innerClassRenderer.render(innerClass, compilationUnit).stream()
                .map(this::javaIndent));
    }

    public List<String> renderInnerInterfaces(List<InnerInterface> innerInterfaces, CompilationUnit compilationUnit) {
        return innerInterfaces.stream()
                .flatMap(ii -> renderInnerInterface(ii, compilationUnit))
                .toList();
    }

    private Stream<String> renderInnerInterface(InnerInterface innerInterface, CompilationUnit compilationUnit) {
        innerInterfaceRenderer =
                Objects.requireNonNullElseGet(innerInterfaceRenderer, () -> new InnerInterfaceRenderer(indenter));
        return addEmptyLine(innerInterfaceRenderer.render(innerInterface, compilationUnit).stream()
                .map(this::javaIndent));
    }

    public List<String> renderInnerEnums(List<InnerEnum> innerEnums, CompilationUnit compilationUnit) {
        return innerEnums.stream()
                .flatMap(ie -> renderInnerEnum(ie, compilationUnit))
                .toList();
    }

    private Stream<String> renderInnerEnum(InnerEnum innerEnum, CompilationUnit compilationUnit) {
        innerEnumRenderer = Objects.requireNonNullElseGet(innerEnumRenderer, () -> new InnerEnumRenderer(indenter));
        return addEmptyLine(innerEnumRenderer.render(innerEnum, compilationUnit).stream()
                .map(this::javaIndent));
    }

    public List<String> renderInnerRecords(List<InnerRecord> innerRecords, CompilationUnit compilationUnit) {
        return innerRecords.stream()
                .flatMap(ir -> renderInnerRecord(ir, compilationUnit))
                .toList();
    }

    private Stream<String> renderInnerRecord(InnerRecord innerRecord, CompilationUnit compilationUnit) {
        innerRecordRenderer =
                Objects.requireNonNullElseGet(innerRecordRenderer, () -> new InnerRecordRenderer(indenter));
        return addEmptyLine(innerRecordRenderer.render(innerRecord, compilationUnit).stream()
                .map(this::javaIndent));
    }

    public List<String> renderPackage(CompilationUnit compilationUnit) {
        List<String> answer = new ArrayList<>();

        String pack = compilationUnit.getType().getPackageName();
        if (stringHasValue(pack)) {
            answer.add("package " + pack + ";"); //$NON-NLS-1$ //$NON-NLS-2$
            answer.add(""); //$NON-NLS-1$
        }
        return answer;
    }

    public List<String> renderStaticImports(CompilationUnit compilationUnit) {
        if (compilationUnit.getStaticImports().isEmpty()) {
            return Collections.emptyList();
        }

        return addEmptyLine(compilationUnit.getStaticImports().stream()
                .map(s -> "import static " + s + ";")) //$NON-NLS-1$ //$NON-NLS-2$
                .toList();
    }

    public List<String> renderImports(CompilationUnit compilationUnit) {
        Set<String> imports = renderImports(compilationUnit.getImportedTypes());

        if (imports.isEmpty()) {
            return Collections.emptyList();
        }

        return addEmptyLine(imports.stream()).toList();
    }

    private Set<String> renderImports(Set<FullyQualifiedJavaType> imports) {
        return imports.stream()
                .map(FullyQualifiedJavaType::getImportList)
                .flatMap(List::stream)
                .map(this::toFullImport)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    private String toFullImport(String s) {
        return "import " + s + ";"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    protected List<String> renderInnerTypes(AbstractJavaType abstractJavaType, CompilationUnit compilationUnit) {
        List<String> lines = new ArrayList<>();

        if (!abstractJavaType.getInnerClasses().isEmpty()) {
            lines.addAll(renderInnerClasses(abstractJavaType.getInnerClasses(), compilationUnit));
        }

        if (!abstractJavaType.getInnerInterfaces().isEmpty()) {
            lines.addAll(renderInnerInterfaces(abstractJavaType.getInnerInterfaces(), compilationUnit));
        }

        if (!abstractJavaType.getInnerEnums().isEmpty()) {
            lines.addAll(renderInnerEnums(abstractJavaType.getInnerEnums(), compilationUnit));
        }

        if (!abstractJavaType.getInnerRecords().isEmpty()) {
            lines.addAll(renderInnerRecords(abstractJavaType.getInnerRecords(), compilationUnit));
        }

        return lines;
    }

    private Stream<String> addEmptyLine(Stream<String> in) {
        return Stream.of(in, Stream.of("")) //$NON-NLS-1$
                .flatMap(Function.identity());
    }

    private String javaIndent(String in) {
        if (in.isEmpty()) {
            return in; // don't indent empty lines
        }

        return indenter.javaIndent(1) + in;
    }
}
