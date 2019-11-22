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
package org.mybatis.generator.runtime.kotlin;

import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getEscapedColumnName;
import static org.mybatis.generator.internal.util.StringUtility.escapeStringForKotlin;

import java.util.List;
import java.util.Objects;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.kotlin.FullyQualifiedKotlinType;
import org.mybatis.generator.api.dom.kotlin.JavaToKotlinTypeConverter;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinProperty;
import org.mybatis.generator.api.dom.kotlin.KotlinType;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.util.StringUtility;

public class KotlinDynamicSqlSupportClassGenerator {
    private IntrospectedTable introspectedTable;
    private Context context;
    private KotlinFile kotlinFile;
    private KotlinType innerObject;
    private KotlinType outerObject;
    
    public KotlinDynamicSqlSupportClassGenerator(Context context, IntrospectedTable introspectedTable) {
        this.introspectedTable = Objects.requireNonNull(introspectedTable);
        this.context = Objects.requireNonNull(context);
        generate();
    }
    
    private void generate() {
        FullyQualifiedJavaType type =
                new FullyQualifiedJavaType(introspectedTable.getMyBatisDynamicSqlSupportType());

        kotlinFile = buildBasicFile(type);
        
        outerObject = buildOuterObject(kotlinFile, type);
        
        innerObject = buildInnerObject();
        outerObject.addNamedItem(innerObject);
        
        List<IntrospectedColumn> columns = introspectedTable.getAllColumns();
        for (IntrospectedColumn column : columns) {
            handleColumn(kotlinFile, innerObject, column);
        }
    }
    
    public KotlinFile getKotlinFile() {
        return kotlinFile;
    }
    
    public KotlinType getInnerObject() {
        return innerObject;
    }
    
    public String getInnerObjectImport() {
        return kotlinFile.getPackage().map(s -> s + ".").orElse("") //$NON-NLS-1$ //$NON-NLS-2$
                + outerObject.getName()
                + "." //$NON-NLS-1$
                + innerObject.getName();
    }
    
    private KotlinFile buildBasicFile(FullyQualifiedJavaType type) {
        KotlinFile kf = new KotlinFile(type.getShortNameWithoutTypeArguments());
        kf.setPackage(type.getPackageName());
        context.getCommentGenerator().addFileComment(kf);
        
        return kf;
    }

    private KotlinType buildOuterObject(KotlinFile kotlinFile, FullyQualifiedJavaType type) {
        KotlinType outerObject = KotlinType.newObject(type.getShortNameWithoutTypeArguments())
                .build();
                
        kotlinFile.addImport("org.mybatis.dynamic.sql.SqlTable"); //$NON-NLS-1$
        kotlinFile.addImport("java.sql.JDBCType"); //$NON-NLS-1$
        kotlinFile.addNamedItem(outerObject);
        return outerObject;
    }
    
    
    private KotlinType buildInnerObject() {
        String domainObjectName = introspectedTable.getFullyQualifiedTable().getDomainObjectName();

        return KotlinType.newObject(domainObjectName)
                .withSuperType("SqlTable(\"" //$NON-NLS-1$
                        + escapeStringForKotlin(introspectedTable.getFullyQualifiedTableNameAtRuntime())
                        + "\")") //$NON-NLS-1$
                .build();
    }

    private void handleColumn(KotlinFile kotlinFile, KotlinType kotlinType,
            IntrospectedColumn column) {
        
        FullyQualifiedKotlinType kt = JavaToKotlinTypeConverter.convert(column.getFullyQualifiedJavaType());
        
        kotlinFile.addImports(kt.getImportList());
        
        String fieldName = column.getJavaProperty();
        
        KotlinProperty property = KotlinProperty.newVal(fieldName)
                .withInitializationString(calculateInnerInitializationString(column, kt))
                .build();
        
        kotlinType.addNamedItem(property);
    }

    private String calculateInnerInitializationString(IntrospectedColumn column, FullyQualifiedKotlinType kt) {
        StringBuilder initializationString = new StringBuilder();
        
        initializationString.append(String.format("column<%s>(\"%s\", JDBCType.%s", //$NON-NLS-1$
                kt.getShortNameWithTypeArguments(),
                escapeStringForKotlin(getEscapedColumnName(column)),
                column.getJdbcTypeName()));
        
        if (StringUtility.stringHasValue(column.getTypeHandler())) {
            initializationString.append(String.format(", \"%s\")", column.getTypeHandler())); //$NON-NLS-1$
        } else {
            initializationString.append(')');
        }
        
        return initializationString.toString();
    }
}
