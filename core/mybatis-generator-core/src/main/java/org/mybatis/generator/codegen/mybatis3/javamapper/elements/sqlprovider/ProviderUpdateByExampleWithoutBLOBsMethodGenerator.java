/*
 *  Copyright 2010 The MyBatis Team
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider;

import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getAliasedEscapedColumnName;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getParameterClause;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;

import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class ProviderUpdateByExampleWithoutBLOBsMethodGenerator extends
        AbstractJavaProviderMethodGenerator {

    public ProviderUpdateByExampleWithoutBLOBsMethodGenerator() {
        super();
    }

    @Override
    public void addClassElements(TopLevelClass topLevelClass) {
        topLevelClass.addStaticImport("org.apache.ibatis.jdbc.SqlBuilder.BEGIN"); //$NON-NLS-1$
        topLevelClass.addStaticImport("org.apache.ibatis.jdbc.SqlBuilder.UPDATE"); //$NON-NLS-1$
        topLevelClass.addStaticImport("org.apache.ibatis.jdbc.SqlBuilder.SET"); //$NON-NLS-1$
        topLevelClass.addStaticImport("org.apache.ibatis.jdbc.SqlBuilder.SQL"); //$NON-NLS-1$

        topLevelClass.addImportedType(new FullyQualifiedJavaType("java.util.Map")); //$NON-NLS-1$
        
        Method method = new Method(getMethodName());
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(new FullyQualifiedJavaType("java.util.Map<java.lang.String, java.lang.Object>"), //$NON-NLS-1$
                "parameter")); //$NON-NLS-1$
        
        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        method.addBodyLine("BEGIN();"); //$NON-NLS-1$
        
        method.addBodyLine(String.format("UPDATE(\"%s\");", //$NON-NLS-1$
                escapeStringForJava(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
        method.addBodyLine(""); //$NON-NLS-1$
        
        for (IntrospectedColumn introspectedColumn : getColumns()) {
            StringBuilder sb = new StringBuilder();
            sb.append(getParameterClause(introspectedColumn));
            sb.insert(2, "record."); //$NON-NLS-1$
            
            method.addBodyLine(String.format("SET(\"%s = %s\");", //$NON-NLS-1$
                    escapeStringForJava(getAliasedEscapedColumnName(introspectedColumn)),
                    sb.toString()));
                
            method.addBodyLine(""); //$NON-NLS-1$
        }
        
        FullyQualifiedJavaType example =
            new FullyQualifiedJavaType(introspectedTable.getExampleType());
        topLevelClass.addImportedType(example);
        method.addBodyLine(String.format("%s example = (%s) parameter.get(\"example\");", //$NON-NLS-1$
                example.getShortName(), example.getShortName()));
        
        method.addBodyLine("applyWhere(example, true);"); //$NON-NLS-1$
        method.addBodyLine("return SQL();"); //$NON-NLS-1$
        
        topLevelClass.addMethod(method);
    }
    
    public String getMethodName() {
        return introspectedTable.getUpdateByExampleStatementId();        
    }
    
    public List<IntrospectedColumn> getColumns() {
        return introspectedTable.getNonBLOBColumns();
    }
}
