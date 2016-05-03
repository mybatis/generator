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

import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;

public class ProviderApplyWhereMethodGenerator extends
        AbstractJavaProviderMethodGenerator {

    private static final String[] BEGINNING_METHOD_LINES = {
        "if (example == null) {", //$NON-NLS-1$
        "return;", //$NON-NLS-1$
        "}", //$NON-NLS-1$
        "", //$NON-NLS-1$
        "String parmPhrase1;", //$NON-NLS-1$
        "String parmPhrase1_th;", //$NON-NLS-1$
        "String parmPhrase2;", //$NON-NLS-1$
        "String parmPhrase2_th;", //$NON-NLS-1$
        "String parmPhrase3;", //$NON-NLS-1$
        "String parmPhrase3_th;", //$NON-NLS-1$
        "if (includeExamplePhrase) {", //$NON-NLS-1$
        "parmPhrase1 = \"%s #{example.oredCriteria[%d].allCriteria[%d].value}\";", //$NON-NLS-1$
        "parmPhrase1_th = \"%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}\";", //$NON-NLS-1$
        "parmPhrase2 = \"%s #{example.oredCriteria[%d].allCriteria[%d].value} and #{example.oredCriteria[%d].criteria[%d].secondValue}\";", //$NON-NLS-1$
        "parmPhrase2_th = \"%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}\";", //$NON-NLS-1$
        "parmPhrase3 = \"#{example.oredCriteria[%d].allCriteria[%d].value[%d]}\";", //$NON-NLS-1$
        "parmPhrase3_th = \"#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}\";", //$NON-NLS-1$
        "} else {", //$NON-NLS-1$
        "parmPhrase1 = \"%s #{oredCriteria[%d].allCriteria[%d].value}\";", //$NON-NLS-1$
        "parmPhrase1_th = \"%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}\";", //$NON-NLS-1$
        "parmPhrase2 = \"%s #{oredCriteria[%d].allCriteria[%d].value} and #{oredCriteria[%d].criteria[%d].secondValue}\";", //$NON-NLS-1$
        "parmPhrase2_th = \"%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}\";", //$NON-NLS-1$
        "parmPhrase3 = \"#{oredCriteria[%d].allCriteria[%d].value[%d]}\";", //$NON-NLS-1$
        "parmPhrase3_th = \"#{oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}\";", //$NON-NLS-1$
        "}", //$NON-NLS-1$
        "", //$NON-NLS-1$
        "StringBuilder sb = new StringBuilder();", //$NON-NLS-1$
        "List<Criteria> oredCriteria = example.getOredCriteria();", //$NON-NLS-1$
        "boolean firstCriteria = true;", //$NON-NLS-1$
        "for (int i = 0; i < oredCriteria.size(); i++) {", //$NON-NLS-1$
        "Criteria criteria = oredCriteria.get(i);", //$NON-NLS-1$
        "if (criteria.isValid()) {", //$NON-NLS-1$
        "if (firstCriteria) {", //$NON-NLS-1$
        "firstCriteria = false;", //$NON-NLS-1$
        "} else {", //$NON-NLS-1$
        "sb.append(\" or \");", //$NON-NLS-1$
        "}", //$NON-NLS-1$
        "", //$NON-NLS-1$        
        "sb.append('(');", //$NON-NLS-1$
        "List<Criterion> criterions = criteria.getAllCriteria();", //$NON-NLS-1$
        "boolean firstCriterion = true;", //$NON-NLS-1$
        "for (int j = 0; j < criterions.size(); j++) {", //$NON-NLS-1$
        "Criterion criterion = criterions.get(j);", //$NON-NLS-1$
        "if (firstCriterion) {", //$NON-NLS-1$
        "firstCriterion = false;", //$NON-NLS-1$
        "} else {", //$NON-NLS-1$
        "sb.append(\" and \");", //$NON-NLS-1$
        "}", //$NON-NLS-1$
        "", //$NON-NLS-1$
        "if (criterion.isNoValue()) {", //$NON-NLS-1$
        "sb.append(criterion.getCondition());", //$NON-NLS-1$
        "} else if (criterion.isSingleValue()) {", //$NON-NLS-1$
        "if (criterion.getTypeHandler() == null) {", //$NON-NLS-1$
        "sb.append(String.format(parmPhrase1, criterion.getCondition(), i, j));", //$NON-NLS-1$
        "} else {", //$NON-NLS-1$
        "sb.append(String.format(parmPhrase1_th, criterion.getCondition(), i, j,criterion.getTypeHandler()));", //$NON-NLS-1$
        "}", //$NON-NLS-1$
        "} else if (criterion.isBetweenValue()) {", //$NON-NLS-1$
        "if (criterion.getTypeHandler() == null) {", //$NON-NLS-1$
        "sb.append(String.format(parmPhrase2, criterion.getCondition(), i, j, i, j));", //$NON-NLS-1$
        "} else {", //$NON-NLS-1$
        "sb.append(String.format(parmPhrase2_th, criterion.getCondition(), i, j, criterion.getTypeHandler(), i, j, criterion.getTypeHandler()));", //$NON-NLS-1$
        "}", //$NON-NLS-1$
        "} else if (criterion.isListValue()) {", //$NON-NLS-1$
        "sb.append(criterion.getCondition());", //$NON-NLS-1$
        "sb.append(\" (\");", //$NON-NLS-1$
        "List<?> listItems = (List<?>) criterion.getValue();", //$NON-NLS-1$
        "boolean comma = false;", //$NON-NLS-1$
        "for (int k = 0; k < listItems.size(); k++) {", //$NON-NLS-1$
        "if (comma) {", //$NON-NLS-1$
        "sb.append(\", \");", //$NON-NLS-1$
        "} else {", //$NON-NLS-1$
        "comma = true;", //$NON-NLS-1$
        "}", //$NON-NLS-1$
        "if (criterion.getTypeHandler() == null) {", //$NON-NLS-1$
        "sb.append(String.format(parmPhrase3, i, j, k));", //$NON-NLS-1$
        "} else {", //$NON-NLS-1$
        "sb.append(String.format(parmPhrase3_th, i, j, k, criterion.getTypeHandler()));", //$NON-NLS-1$
        "}", //$NON-NLS-1$
        "}", //$NON-NLS-1$
        "sb.append(')');", //$NON-NLS-1$
        "}", //$NON-NLS-1$
        "}", //$NON-NLS-1$
        "sb.append(')');", //$NON-NLS-1$
        "}", //$NON-NLS-1$
        "}", //$NON-NLS-1$
        "" //$NON-NLS-1$
    };
    
    private static final String[] LEGACY_ENDING_METHOD_LINES = {
        "if (sb.length() > 0) {", //$NON-NLS-1$
        "WHERE(sb.toString());", //$NON-NLS-1$
        "}" //$NON-NLS-1$
    };
    
    private static final String[] ENDING_METHOD_LINES = {
        "if (sb.length() > 0) {", //$NON-NLS-1$
        "sql.WHERE(sb.toString());", //$NON-NLS-1$
        "}" //$NON-NLS-1$
    };
    
    public ProviderApplyWhereMethodGenerator(boolean useLegacyBuilder) {
        super(useLegacyBuilder);
    }

    @Override
    public void addClassElements(TopLevelClass topLevelClass) {
        Set<String> staticImports = new TreeSet<String>();
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();

        if (useLegacyBuilder) {
        	staticImports.add("org.apache.ibatis.jdbc.SqlBuilder.WHERE"); //$NON-NLS-1$
        } else {
        	importedTypes.add(NEW_BUILDER_IMPORT);
        }
        
        importedTypes.add(new FullyQualifiedJavaType(
                "java.util.List")); //$NON-NLS-1$
        
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(introspectedTable.getExampleType());
        importedTypes.add(fqjt);
        importedTypes.add(new FullyQualifiedJavaType(
                String.format("%s.Criteria", fqjt.getFullyQualifiedName()))); //$NON-NLS-1$
        importedTypes.add(new FullyQualifiedJavaType(
                String.format("%s.Criterion", fqjt.getFullyQualifiedName()))); //$NON-NLS-1$

        Method method = new Method("applyWhere"); //$NON-NLS-1$
        method.setVisibility(JavaVisibility.PROTECTED);
        if (!useLegacyBuilder) {
            method.addParameter(new Parameter(NEW_BUILDER_IMPORT, "sql")); //$NON-NLS-1$
        }
        method.addParameter(new Parameter(fqjt, "example")); //$NON-NLS-1$
        method.addParameter(new Parameter(FullyQualifiedJavaType.getBooleanPrimitiveInstance(), "includeExamplePhrase")); //$NON-NLS-1$
        
        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);
        
        for (String methodLine : BEGINNING_METHOD_LINES) {
            method.addBodyLine(methodLine);
        }
        
        if (useLegacyBuilder) {
        	for (String methodLine : LEGACY_ENDING_METHOD_LINES) {
        		method.addBodyLine(methodLine);
        	}
        } else {
        	for (String methodLine : ENDING_METHOD_LINES) {
        		method.addBodyLine(methodLine);
        	}
        }
        
        if (context.getPlugins().providerApplyWhereMethodGenerated(method, topLevelClass,
                introspectedTable)) {
            topLevelClass.addStaticImports(staticImports);
            topLevelClass.addImportedTypes(importedTypes);
            topLevelClass.addMethod(method);
        }
    }
}
