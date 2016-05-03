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
package org.mybatis.generator.codegen.mybatis3.javamapper.elements.sqlprovider;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getGetterMethodName;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForJava;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getAliasedEscapedColumnName;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getParameterClause;

import java.util.Set;
import java.util.TreeSet;

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
public class ProviderUpdateByExampleSelectiveMethodGenerator extends
        AbstractJavaProviderMethodGenerator {

	public ProviderUpdateByExampleSelectiveMethodGenerator(boolean useLegacyBuilder) {
		super(useLegacyBuilder);
	}
	
    @Override
    public void addClassElements(TopLevelClass topLevelClass) {
        Set<String> staticImports = new TreeSet<String>();
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();

        if (useLegacyBuilder) {
        	staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.BEGIN"); //$NON-NLS-1$
        	staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.UPDATE"); //$NON-NLS-1$
        	staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SET"); //$NON-NLS-1$
        	staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.SQL"); //$NON-NLS-1$
        } else {
        	importedTypes.add(NEW_BUILDER_IMPORT);
        }

        importedTypes.add(new FullyQualifiedJavaType("java.util.Map")); //$NON-NLS-1$
        
        Method method = new Method(introspectedTable.getUpdateByExampleSelectiveStatementId());
        method.setReturnType(FullyQualifiedJavaType.getStringInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.addParameter(new Parameter(new FullyQualifiedJavaType("java.util.Map<java.lang.String, java.lang.Object>"), //$NON-NLS-1$
                "parameter")); //$NON-NLS-1$
        
        FullyQualifiedJavaType record =
            introspectedTable.getRules().calculateAllFieldsClass();
        importedTypes.add(record);
        method.addBodyLine(String.format("%s record = (%s) parameter.get(\"record\");", //$NON-NLS-1$
                record.getShortName(), record.getShortName()));

        FullyQualifiedJavaType example =
            new FullyQualifiedJavaType(introspectedTable.getExampleType());
        importedTypes.add(example);
        method.addBodyLine(String.format("%s example = (%s) parameter.get(\"example\");", //$NON-NLS-1$
                example.getShortName(), example.getShortName()));

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        method.addBodyLine(""); //$NON-NLS-1$
        
        if (useLegacyBuilder) {
        	method.addBodyLine("BEGIN();"); //$NON-NLS-1$
        } else {
        	method.addBodyLine("SQL sql = new SQL();"); //$NON-NLS-1$
        }
        
        method.addBodyLine(String.format("%sUPDATE(\"%s\");", //$NON-NLS-1$
        		builderPrefix,
                escapeStringForJava(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime())));
        method.addBodyLine(""); //$NON-NLS-1$
        
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                method.addBodyLine(String.format("if (record.%s() != null) {", //$NON-NLS-1$
                    getGetterMethodName(introspectedColumn.getJavaProperty(),
                            introspectedColumn.getFullyQualifiedJavaType())));
            }

            StringBuilder sb = new StringBuilder();
            sb.append(getParameterClause(introspectedColumn));
            sb.insert(2, "record."); //$NON-NLS-1$
            
            method.addBodyLine(String.format("%sSET(\"%s = %s\");", //$NON-NLS-1$
                    builderPrefix,
            		escapeStringForJava(getAliasedEscapedColumnName(introspectedColumn)),
                    sb.toString()));
                
            if (!introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                method.addBodyLine("}"); //$NON-NLS-1$
            }

            method.addBodyLine(""); //$NON-NLS-1$
        }
        
        if (useLegacyBuilder) {
        	method.addBodyLine("applyWhere(example, true);"); //$NON-NLS-1$
        	method.addBodyLine("return SQL();"); //$NON-NLS-1$
        } else {
        	method.addBodyLine("applyWhere(sql, example, true);"); //$NON-NLS-1$
        	method.addBodyLine("return sql.toString();"); //$NON-NLS-1$
        }
        
        if (context.getPlugins().providerUpdateByExampleSelectiveMethodGenerated(method, topLevelClass,
                introspectedTable)) {
            topLevelClass.addStaticImports(staticImports);
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }
}
