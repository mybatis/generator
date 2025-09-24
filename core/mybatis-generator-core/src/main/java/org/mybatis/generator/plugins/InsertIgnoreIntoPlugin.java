/*
 *    Copyright 2006-2025 the original author or authors.
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
package org.mybatis.generator.plugins;

import java.util.List;
import java.util.stream.Collectors;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;

/**
 * This plugin adds INSERT IGNORE methods to generated mapper interfaces.
 *
 * <p>The plugin generates methods for both single record and batch INSERT IGNORE operations,
 * which will insert records only if they don't already exist (based on primary key or unique constraints).
 *
 * @author Goody
 */
public class InsertIgnoreIntoPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, IntrospectedTable introspectedTable) {
        // add import
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Insert"));
        interfaze.addImportedType(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));
        interfaze.addImportedType(new FullyQualifiedJavaType("java.util.Collection"));
        // add method
        interfaze.addMethod(this.insertIgnoreIntoOne(interfaze, introspectedTable));
        interfaze.addMethod(this.insertIgnoreIntoBatch(interfaze, introspectedTable));

        return super.clientGenerated(interfaze, introspectedTable);
    }

    private Method insertIgnoreIntoOne(Interface interfaze, IntrospectedTable introspectedTable) {
        // Clazz
        final FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        // Clazz record
        final Parameter parameters = new Parameter(type, "record");
        // @Param("item") Clazz record
        parameters.addAnnotation("@Param(\"item\")");

        // void insertIgnoreCustom() {}
        final Method method = new Method("insertIgnoreCustom");
        // void insertIgnoreCustom();
        method.setAbstract(true);
        // void insertIgnoreCustom(@Param("item") Collection<Clazz> records);
        method.addParameter(parameters);

        final String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        final String columnNames = introspectedTable.getAllColumns()
            .stream()
            .map(column -> String.format("`%s`", column.getActualColumnName()))
            .collect(Collectors.joining(", "));
        final String columnValueNames = introspectedTable.getAllColumns()
            .stream()
            .map(column -> String.format("#{item.%s}", column.getJavaProperty()))
            .collect(Collectors.joining(", "));
        String insertIgnore = String.format("@Insert({" +
            "\"<script>\" +\n" +
            "            \" INSERT IGNORE INTO %s\" +\n" +
            "              \" (%s)\" +\n" +
            "            \" VALUES\" +\n" +
            "              \"(%s)\" +\n" +
            "        \"</script>\"" +
            "})", tableName, columnNames, columnValueNames);
        method.addAnnotation(insertIgnore);
        return method;
    }

    private Method insertIgnoreIntoBatch(Interface interfaze, IntrospectedTable introspectedTable) {
        // Collection<Clazz>
        final FullyQualifiedJavaType type = new FullyQualifiedJavaType(String.format("Collection<%s>", introspectedTable.getBaseRecordType()));
        // Collection<Clazz> records
        final Parameter parameters = new Parameter(type, "records");
        // @Param("items") Collection<Clazz> records
        parameters.addAnnotation("@Param(\"items\")");

        // void insertIgnoreBatchCustom() {}
        final Method method = new Method("insertIgnoreBatchCustom");
        // void insertIgnoreBatchCustom();
        method.setAbstract(true);
        // void insertIgnoreBatchCustom(@Param("items") Collection<Clazz> records);
        method.addParameter(parameters);

        final String tableName = introspectedTable.getFullyQualifiedTableNameAtRuntime();
        final String columnNames = introspectedTable.getAllColumns()
            .stream()
            .map(column -> String.format("`%s`", column.getActualColumnName()))
            .collect(Collectors.joining(", "));
        final String columnValueNames = introspectedTable.getAllColumns()
            .stream()
            .map(column -> String.format("#{item.%s}", column.getJavaProperty()))
            .collect(Collectors.joining(", "));
        String insertIgnore = String.format("@Insert({" +
            "\"<script>\" +\n" +
            "            \" INSERT IGNORE INTO %s\" +\n" +
            "            \" (%s)\" +\n" +
            "            \" VALUES\" +\n" +
            "            \"<foreach collection='items' item='item' separator=','>\" +\n" +
            "               \"(%s)\" +\n" +
            "            \"</foreach> \" +\n" +
            "        \"</script>\"" +
            "})", tableName, columnNames, columnValueNames);
        method.addAnnotation(insertIgnore);
        return method;
    }
}
