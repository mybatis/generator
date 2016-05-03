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

import static org.mybatis.generator.api.dom.OutputUtilities.javaIndent;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getSelectListPhrase;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;

import java.util.Iterator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.SelectAllMethodGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * 
 * @author Jeff Butler
 */
public class AnnotatedSelectAllMethodGenerator extends
    SelectAllMethodGenerator {
    
    public AnnotatedSelectAllMethodGenerator() {
        super();
    }

    @Override
    public void addMapperAnnotations(Interface interfaze, Method method) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Select")); //$NON-NLS-1$

        StringBuilder sb = new StringBuilder();
        method.addAnnotation("@Select({"); //$NON-NLS-1$
        javaIndent(sb, 1);
        sb.append("\"select\","); //$NON-NLS-1$
        method.addAnnotation(sb.toString());
        
        Iterator<IntrospectedColumn> iter = introspectedTable
            .getAllColumns().iterator();
        sb.setLength(0);
        javaIndent(sb, 1);
        sb.append('"');
        boolean hasColumns = false;
        while (iter.hasNext()) {
            sb.append(escapeStringForJava(getSelectListPhrase(iter.next())));
            hasColumns = true;

            if (iter.hasNext()) {
                sb.append(", "); //$NON-NLS-1$
            }

            if (sb.length() > 80) {
                sb.append("\","); //$NON-NLS-1$
                method.addAnnotation(sb.toString());
                
                sb.setLength(0);
                javaIndent(sb, 1);
                sb.append('"');
                hasColumns = false;
            }
        }

        if (hasColumns) {
            sb.append("\","); //$NON-NLS-1$
            method.addAnnotation(sb.toString());
        }
        
        String orderByClause = introspectedTable.getTableConfigurationProperty(PropertyRegistry.TABLE_SELECT_ALL_ORDER_BY_CLAUSE);
        boolean hasOrderBy = StringUtility.stringHasValue(orderByClause);
        
        sb.setLength(0);
        javaIndent(sb, 1);
        sb.append("\"from "); //$NON-NLS-1$
        sb.append(escapeStringForJava(introspectedTable
                .getAliasedFullyQualifiedTableNameAtRuntime()));
        sb.append('\"');
        if (hasOrderBy) {
            sb.append(',');
        }
        method.addAnnotation(sb.toString());
        
        if (hasOrderBy) {
            sb.setLength(0);
            javaIndent(sb, 1);
            sb.append("\"order by "); //$NON-NLS-1$
            sb.append(orderByClause);
            sb.append('\"');
            method.addAnnotation(sb.toString());
        }
        
        method.addAnnotation("})"); //$NON-NLS-1$

        addAnnotatedResults(interfaze, method);
    }
    
    private void addAnnotatedResults(Interface interfaze, Method method) {
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.type.JdbcType")); //$NON-NLS-1$
        
        if (introspectedTable.isConstructorBased()) {
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Arg")); //$NON-NLS-1$
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.ConstructorArgs")); //$NON-NLS-1$
            method.addAnnotation("@ConstructorArgs({"); //$NON-NLS-1$
        } else {
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Result")); //$NON-NLS-1$
            interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Results")); //$NON-NLS-1$
            method.addAnnotation("@Results({"); //$NON-NLS-1$
        }
        
        StringBuilder sb = new StringBuilder();
        
        Iterator<IntrospectedColumn> iterPk = introspectedTable.getPrimaryKeyColumns().iterator();
        Iterator<IntrospectedColumn> iterNonPk = introspectedTable.getNonPrimaryKeyColumns().iterator();
        while (iterPk.hasNext()) {
            IntrospectedColumn introspectedColumn = iterPk.next();
            sb.setLength(0);
            javaIndent(sb, 1);
            sb.append(getResultAnnotation(interfaze, introspectedColumn, true,
                    introspectedTable.isConstructorBased()));
            
            if (iterPk.hasNext() || iterNonPk.hasNext()) {
                sb.append(',');
            }
            
            method.addAnnotation(sb.toString());
        }

        while (iterNonPk.hasNext()) {
            IntrospectedColumn introspectedColumn = iterNonPk.next();
            sb.setLength(0);
            javaIndent(sb, 1);
            sb.append(getResultAnnotation(interfaze, introspectedColumn, false,
                    introspectedTable.isConstructorBased()));
            
            if (iterNonPk.hasNext()) {
                sb.append(',');
            }
            
            method.addAnnotation(sb.toString());
        }
        
        method.addAnnotation("})"); //$NON-NLS-1$
    }
}
