/*
 *    Copyright 2006-2021 the original author or authors.
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
package org.mybatis.generator.codegen.mybatis3.javamapper.elements.annotated;

import static org.mybatis.generator.api.dom.OutputUtilities.javaIndent;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.DeleteByPrimaryKeyMethodGenerator;

public class AnnotatedDeleteByPrimaryKeyMethodGenerator extends DeleteByPrimaryKeyMethodGenerator {

    public AnnotatedDeleteByPrimaryKeyMethodGenerator(boolean isSimple) {
        super(isSimple);
    }

    @Override
    public void addMapperAnnotations(Method method) {

        method.addAnnotation("@Delete({"); //$NON-NLS-1$

        StringBuilder sb = new StringBuilder();
        javaIndent(sb, 1);
        sb.append("\"delete from "); //$NON-NLS-1$
        sb.append(escapeStringForJava(introspectedTable.getFullyQualifiedTableNameAtRuntime()));
        sb.append("\","); //$NON-NLS-1$
        method.addAnnotation(sb.toString());

        buildByPrimaryKeyWhereClause().forEach(method::addAnnotation);

        method.addAnnotation("})"); //$NON-NLS-1$
    }

    @Override
    public void addExtraImports(Interface interfaze) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Delete")); //$NON-NLS-1$
    }
}
