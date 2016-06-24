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
package org.mybatis.generator.api;

import java.util.Properties;

import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * Implementations of this interface are used to generate comments for the
 * various artifacts.
 * 
 * @author Jeff Butler
 */
public interface CommentGenerator {

    /**
     * Adds properties for this instance from any properties configured in the
     * CommentGenerator configuration.
     * 
     * This method will be called before any of the other methods.
     * 
     * @param properties
     *            All properties from the configuration
     */
    void addConfigurationProperties(Properties properties);

    /**
     * This method should add a Javadoc comment to the specified field. The field is related to the specified table and
     * is used to hold the value of the specified column.
     * <p>
     * 
     * <b>Important:</b> This method should add a the nonstandard JavaDoc tag "@mbggenerated" to the comment. Without
     * this tag, the Eclipse based Java merge feature will fail.
     *
     * @param field
     *            the field
     * @param introspectedTable
     *            the introspected table
     * @param introspectedColumn
     *            the introspected column
     */
    void addFieldComment(Field field,
            IntrospectedTable introspectedTable,
            IntrospectedColumn introspectedColumn);

    /**
     * Adds the field comment.
     *
     * @param field
     *            the field
     * @param introspectedTable
     *            the introspected table
     */
    void addFieldComment(Field field, IntrospectedTable introspectedTable);

    /**
     * Adds a comment for a model class.  The Java code merger should
     * be notified not to delete the entire class in case any manual
     * changes have been made.  So this method will always use the 
     * "do not delete" annotation.
     * 
     * Because of difficulties with the Java file merger, the default implementation
     * of this method should NOT add comments.  Comments should only be added if
     * specifically requested by the user (for example, by enabling table remark comments).
     *
     * @param topLevelClass
     *            the top level class
     * @param introspectedTable
     *            the introspected table
     */
    void addModelClassComment(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable);

    /**
     * Adds the inner class comment.
     *
     * @param innerClass
     *            the inner class
     * @param introspectedTable
     *            the introspected table
     */
    void addClassComment(InnerClass innerClass,
            IntrospectedTable introspectedTable);

    /**
     * Adds the inner class comment.
     *
     * @param innerClass
     *            the inner class
     * @param introspectedTable
     *            the introspected table
     * @param markAsDoNotDelete
     *            the mark as do not delete
     */
    void addClassComment(InnerClass innerClass,
            IntrospectedTable introspectedTable, boolean markAsDoNotDelete);

    /**
     * Adds the enum comment.
     *
     * @param innerEnum
     *            the inner enum
     * @param introspectedTable
     *            the introspected table
     */
    void addEnumComment(InnerEnum innerEnum,
            IntrospectedTable introspectedTable);

    /**
     * Adds the getter comment.
     *
     * @param method
     *            the method
     * @param introspectedTable
     *            the introspected table
     * @param introspectedColumn
     *            the introspected column
     */
    void addGetterComment(Method method,
            IntrospectedTable introspectedTable,
            IntrospectedColumn introspectedColumn);

    /**
     * Adds the setter comment.
     *
     * @param method
     *            the method
     * @param introspectedTable
     *            the introspected table
     * @param introspectedColumn
     *            the introspected column
     */
    void addSetterComment(Method method,
            IntrospectedTable introspectedTable,
            IntrospectedColumn introspectedColumn);

    /**
     * Adds the general method comment.
     *
     * @param method
     *            the method
     * @param introspectedTable
     *            the introspected table
     */
    void addGeneralMethodComment(Method method,
            IntrospectedTable introspectedTable);

    /**
     * This method is called to add a file level comment to a generated java file. This method could be used to add a
     * general file comment (such as a copyright notice). However, note that the Java file merge function in Eclipse
     * does not deal with this comment. If you run the generator repeatedly, you will only retain the comment from the
     * initial run.
     * <p>
     * 
     * The default implementation does nothing.
     *
     * @param compilationUnit
     *            the compilation unit
     */
    void addJavaFileComment(CompilationUnit compilationUnit);

    /**
     * This method should add a suitable comment as a child element of the specified xmlElement to warn users that the
     * element was generated and is subject to regeneration.
     *
     * @param xmlElement
     *            the xml element
     */
    void addComment(XmlElement xmlElement);

    /**
     * This method is called to add a comment as the first child of the root element. This method could be used to add a
     * general file comment (such as a copyright notice). However, note that the XML file merge function does not deal
     * with this comment. If you run the generator repeatedly, you will only retain the comment from the initial run.
     * <p>
     * 
     * The default implementation does nothing.
     *
     * @param rootElement
     *            the root element
     */
    void addRootComment(XmlElement rootElement);
}
