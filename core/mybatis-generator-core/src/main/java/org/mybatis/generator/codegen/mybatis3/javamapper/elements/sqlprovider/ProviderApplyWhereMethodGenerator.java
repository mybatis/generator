/*
 *    Copyright 2006-2023 the original author or authors.
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
package org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class ProviderApplyWhereMethodGenerator extends AbstractJavaProviderMethodGenerator {

    private static final List<String> METHOD_LINES = getMethodLines();

    @Override
    public void addClassElements(TopLevelClass topLevelClass) {
        Set<FullyQualifiedJavaType> importedTypes = initializeImportedTypes("java.util.List"); //$NON-NLS-1$

        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        importedTypes.add(fqjt);
        importedTypes.add(new FullyQualifiedJavaType(
                String.format("%s.Criteria", fqjt.getFullyQualifiedName()))); //$NON-NLS-1$
        importedTypes.add(new FullyQualifiedJavaType(
                String.format("%s.Criterion", fqjt.getFullyQualifiedName()))); //$NON-NLS-1$

        Method method = new Method("applyWhere"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PROTECTED);
        method.addParameter(new Parameter(BUILDER_IMPORT, "sql")); //$NON-NLS-1$
        method.addParameter(new Parameter(fqjt, "example")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getBooleanPrimitiveInstance(),
                "includeExamplePhrase")); //$NON-NLS-1$

        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        METHOD_LINES.forEach(method::addBodyLine);

        if (context.getPlugins().providerApplyWhereMethodGenerated(method, topLevelClass, introspectedTable)) {
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }

    protected static List<String> getMethodLines() {
        List<String> answer = new ArrayList<>();

        InputStream is =
                ProviderApplyWhereMethodGenerator.class.getResourceAsStream("ApplyWhereMethod.txt"); //$NON-NLS-1$
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(is)))) {
            String line;
            boolean foundDelimiter = false;
            while ((line = br.readLine()) != null) {
                if (foundDelimiter) {
                    answer.add(line.trim());
                } else {
                    foundDelimiter = line.equals("--- method lines below ---"); //$NON-NLS-1$
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("IOException reading ApplyWhere method lines", e); //$NON-NLS-1$
        }

        return answer;
    }
}
