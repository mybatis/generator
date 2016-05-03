/**
 *    Copyright 2006-2016 the original author or authors.
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

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.UpdateByExampleWithoutBLOBsMethodGenerator;

/**
 * 
 * @author Jeff Butler
 */
public class AnnotatedUpdateByExampleWithoutBLOBsMethodGenerator extends
    UpdateByExampleWithoutBLOBsMethodGenerator {

    public AnnotatedUpdateByExampleWithoutBLOBsMethodGenerator() {
        super();
    }

    @Override
    public void addMapperAnnotations(Interface interfaze, Method method) {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedTable.getMyBatis3SqlProviderType());
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.UpdateProvider")); //$NON-NLS-1$
        StringBuilder sb = new StringBuilder();
        sb.append("@UpdateProvider(type="); //$NON-NLS-1$
        sb.append(fqjt.getShortName());
        sb.append(".class, method=\""); //$NON-NLS-1$
        sb.append(introspectedTable.getUpdateByExampleStatementId());
        sb.append("\")"); //$NON-NLS-1$
        
        method.addAnnotation(sb.toString());
    }
}
