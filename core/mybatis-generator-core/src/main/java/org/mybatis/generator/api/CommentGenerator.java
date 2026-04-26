/*
 *    Copyright 2006-2026 the original author or authors.
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
package org.mybatis.generator.api;

import java.util.Properties;
import java.util.Set;

import org.mybatis.generator.api.dom.Indenter;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.InnerRecord;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinFunction;
import org.mybatis.generator.api.dom.kotlin.KotlinProperty;
import org.mybatis.generator.api.dom.kotlin.KotlinType;
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
     * <p>This method will be called before any of the other methods.
     *
     * @param properties
     *            All properties from the configuration
     */
    void addConfigurationProperties(Properties properties);

    void setIndenter(Indenter indenter);

    /**
     * Adds a comment for a model class.  The Java code merger should
     * be notified not to delete the entire class in case any manual
     * changes have been made.  So this method will always use the
     * "do not delete" annotation.
     *
     * <p>Because of difficulties with the Java file merger, the default implementation
     * of this method should NOT add comments.  Comments should only be added if
     * specifically requested by the user (for example, by enabling table remark comments).
     *
     * @param topLevelClass
     *            the top level class
     * @param introspectedTable
     *            the introspected table
     */
    default void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {}

    /**
     * Adds a comment for a model class.
     *
     * @param modelClass
     *            the generated KotlinType for the model
     * @param introspectedTable
     *            the introspected table
     */
    default void addModelClassComment(KotlinType modelClass, IntrospectedTable introspectedTable) {}

    /**
     * This method is called to add a file level comment to a generated java file. This method
     * could be used to add a general file comment (such as a copyright notice). However, note
     * that the Java file merge function may not preserve this comment. If you run
     * the generator repeatedly, you will only retain the comment from the initial run.
     *
     * <p>The default implementation does nothing.
     *
     * @param compilationUnit
     *            the compilation unit
     */
    default void addJavaFileComment(CompilationUnit compilationUnit) {}

    /**
     * This method should add a suitable comment as a child element of the specified xmlElement to warn users that the
     * element was generated and is subject to regeneration.
     *
     * @param xmlElement
     *            the xml element
     */
    default void addComment(XmlElement xmlElement) {}

    /**
     * This method is called to add a comment as the first child of the root element. This method
     * could be used to add a general file comment (such as a copyright notice). However, note
     * that the XML file merge function does not deal with this comment. If you run the generator
     * repeatedly, you will only retain the comment from the initial run.
     *
     * <p>The default implementation does nothing.
     *
     * @param rootElement
     *            the root element
     */
    default void addRootComment(XmlElement rootElement) {}

    /**
     * Adds a @Generated annotation to a method.
     *
     * @param method
     *            the method
     * @param introspectedTable
     *            the introspected table
     * @param imports
     *     the comment generator may add a required imported type to this list
     *
     * @since 1.3.6
     */
    default void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable,
            Set<FullyQualifiedJavaType> imports) {}

    /**
     * Adds a @Generated annotation to a method.
     *
     * @param method
     *            the method
     * @param introspectedTable
     *            the introspected table
     * @param introspectedColumn
     *     the introspected column
     * @param imports
     *     the comment generator may add a required imported type to this list
     *
     * @since 1.3.6
     */
    default void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable,
            IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {}

    /**
     * Adds a @Generated annotation to a field.
     *
     * @param field
     *            the field
     * @param introspectedTable
     *            the introspected table
     * @param imports
     *     the comment generator may add a required imported type to this list
     *
     * @since 1.3.6
     */
    default void addFieldAnnotation(Field field, IntrospectedTable introspectedTable,
            Set<FullyQualifiedJavaType> imports) {}

    /**
     * Adds a @Generated annotation to a field.
     *
     * @param field
     *            the field
     * @param introspectedTable
     *            the introspected table
     * @param introspectedColumn
     *            the introspected column
     * @param imports
     *     the comment generator may add a required imported type to this list
     *
     * @since 1.3.6
     */
    default void addFieldAnnotation(Field field, IntrospectedTable introspectedTable,
            IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {}

    /**
     * Adds a @Generated annotation to a class.
     *
     * @param innerClass
     *            the class
     * @param introspectedTable
     *            the introspected table
     * @param imports
     *     the comment generator may add a required imported type to this list
     *
     * @since 1.3.6
     */
    default void addClassAnnotation(InnerClass innerClass, IntrospectedTable introspectedTable,
            Set<FullyQualifiedJavaType> imports) {}

    /**
     * Adds a @Generated annotation to a class and marks it as do not delete. This means that the class should survive
     * a Java merge operation even if a newly generated method matches it (the new method will be discarded).
     *
     * @param innerClass the class
     * @param introspectedTable the introspected table
     * @param imports
     *     the comment generator may add a required imported type to this list
     */
    default void addClassAnnotationAndMarkAsDoNotDelete(InnerClass innerClass, IntrospectedTable introspectedTable,
                                                        Set<FullyQualifiedJavaType> imports) { }

    /**
     * Adds a @Generated annotation to a record.
     *
     * @param innerRecord
     *            the record
     * @param introspectedTable
     *            the introspected table
     * @param imports
     *     the comment generator may add a required imported type to this list
     *
     * @since 2.0.0
     */
    default void addRecordAnnotation(InnerRecord innerRecord, IntrospectedTable introspectedTable,
                                     Set<FullyQualifiedJavaType> imports) {}

    /**
     * Adds a @Generated annotation to an enum.
     *
     * @param innerEnum the enum
     * @param introspectedTable the introspected table
     * @param imports
     *     the comment generator may add a required imported type to this list
     *
     * @since 2.0.0
     */
    default void addEnumAnnotation(InnerEnum innerEnum, IntrospectedTable introspectedTable,
                                   Set<FullyQualifiedJavaType> imports) {}

    /**
     * This method is called to add a file level comment to a generated Kotlin file. This method
     * could be used to add a general file comment (such as a copyright notice).
     *
     * <p>The default implementation does nothing.
     *
     * @param kotlinFile
     *            the Kotlin file
     */
    default void addFileComment(KotlinFile kotlinFile) {}

    default void addGeneralFunctionComment(KotlinFunction kf, IntrospectedTable introspectedTable,
            Set<String> imports) {}

    default void addGeneralPropertyComment(KotlinProperty property, IntrospectedTable introspectedTable,
            Set<String> imports) {}
}
