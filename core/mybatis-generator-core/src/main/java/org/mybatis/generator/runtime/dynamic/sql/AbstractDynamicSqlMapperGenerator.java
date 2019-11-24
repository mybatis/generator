/**
 *    Copyright 2006-2019 the original author or authors.
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
package org.mybatis.generator.runtime.dynamic.sql;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.runtime.dynamic.sql.elements.AbstractMethodGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.FragmentGenerator;
import org.mybatis.generator.runtime.dynamic.sql.elements.MethodAndImports;

public abstract class AbstractDynamicSqlMapperGenerator extends AbstractJavaClientGenerator {

    // record type for insert, select, update
    protected FullyQualifiedJavaType recordType;
    
    // id to use for the common result map
    protected String resultMapId;
    
    // name of the field containing the table in the support class
    protected String tableFieldName;
    
    protected FragmentGenerator fragmentGenerator;

    public AbstractDynamicSqlMapperGenerator(String project) {
        super(project, false);
    }

    protected void preCalculate() {
        recordType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        resultMapId = recordType.getShortNameWithoutTypeArguments() + "Result"; //$NON-NLS-1$
        tableFieldName =
                JavaBeansUtil.getValidPropertyName(introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        fragmentGenerator = new FragmentGenerator.Builder()
                .withIntrospectedTable(introspectedTable)
                .withResultMapId(resultMapId)
                .withTableFieldName(tableFieldName)
                .build();
    }

    protected Interface createBasicInterface() {
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(
                introspectedTable.getMyBatis3JavaMapperType());
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        context.getCommentGenerator().addJavaFileComment(interfaze);
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper")); //$NON-NLS-1$
        interfaze.addAnnotation("@Mapper"); //$NON-NLS-1$

        String rootInterface = introspectedTable
                .getTableConfigurationProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        if (!stringHasValue(rootInterface)) {
            rootInterface = context.getJavaClientGeneratorConfiguration()
                    .getProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        }

        if (stringHasValue(rootInterface)) {
            FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(rootInterface);
            interfaze.addSuperInterface(fqjt);
            interfaze.addImportedType(fqjt);
        }
        return interfaze;
    }

    protected TopLevelClass getSupportClass() {
        return DynamicSqlSupportClassGenerator.of(
                introspectedTable, context.getCommentGenerator(), warnings).generate();
    }

    protected void generate(Interface interfaze, AbstractMethodGenerator generator) {
        MethodAndImports mi = generator.generateMethodAndImports();
        if (mi != null && generator.callPlugins(mi.getMethod(), interfaze)) {
            interfaze.addMethod(mi.getMethod());
            interfaze.addImportedTypes(mi.getImports());
            interfaze.addStaticImports(mi.getStaticImports());
        }
    }

    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator() {
        return null;
    }
}
