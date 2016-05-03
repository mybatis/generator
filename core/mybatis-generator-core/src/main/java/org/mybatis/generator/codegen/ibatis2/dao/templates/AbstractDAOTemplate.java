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
package org.mybatis.generator.codegen.ibatis2.dao.templates;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;

/**
 * Base class for DAO templates. Subclasses should override any of the
 * configureXXX methods to specify the unique properties of the desired DAO
 * objects.
 * 
 * @author Jeff Butler
 */
public abstract class AbstractDAOTemplate {
    
    /** The interface imports. */
    private List<FullyQualifiedJavaType> interfaceImports;

    /** The implementation imports. */
    private List<FullyQualifiedJavaType> implementationImports;

    /** The super class. */
    private FullyQualifiedJavaType superClass;

    /** The checked exceptions. */
    private List<FullyQualifiedJavaType> checkedExceptions;

    /** The fields. */
    private List<Field> fields;

    /** The methods. */
    private List<Method> methods;

    /** The constructor template. */
    private Method constructorTemplate;

    /** The delete method template. */
    private String deleteMethodTemplate;

    /** The insert method template. */
    private String insertMethodTemplate;

    /** The update method template. */
    private String updateMethodTemplate;

    /** The query for object method template. */
    private String queryForObjectMethodTemplate;

    /** The query for list method template. */
    private String queryForListMethodTemplate;

    /** The configured. */
    private boolean configured;

    /**
     * Instantiates a new abstract dao template.
     */
    public AbstractDAOTemplate() {
        super();
        interfaceImports = new ArrayList<FullyQualifiedJavaType>();
        implementationImports = new ArrayList<FullyQualifiedJavaType>();
        fields = new ArrayList<Field>();
        methods = new ArrayList<Method>();
        checkedExceptions = new ArrayList<FullyQualifiedJavaType>();
        configured = false;
    }

    /**
     * Gets the constructor clone.
     *
     * @param commentGenerator
     *            the comment generator
     * @param type
     *            the type
     * @param introspectedTable
     *            the introspected table
     * @return the constructor clone
     */
    public final Method getConstructorClone(CommentGenerator commentGenerator,
            FullyQualifiedJavaType type, IntrospectedTable introspectedTable) {
        configure();
        Method answer = new Method();
        answer.setConstructor(true);
        answer.setName(type.getShortName());
        answer.setVisibility(constructorTemplate.getVisibility());
        for (Parameter parm : constructorTemplate.getParameters()) {
            answer.addParameter(parm);
        }

        for (String bodyLine : constructorTemplate.getBodyLines()) {
            answer.addBodyLine(bodyLine);
        }

        for (FullyQualifiedJavaType fqjt : constructorTemplate.getExceptions()) {
            answer.addException(fqjt);
        }

        commentGenerator.addGeneralMethodComment(answer, introspectedTable);

        return answer;
    }

    /**
     * Gets the delete method.
     *
     * @param sqlMapNamespace
     *            the sql map namespace
     * @param statementId
     *            the statement id
     * @param parameter
     *            the parameter
     * @return the delete method
     */
    public final String getDeleteMethod(String sqlMapNamespace,
            String statementId, String parameter) {
        configure();
        String answer = MessageFormat.format(deleteMethodTemplate,
                new Object[] { sqlMapNamespace, statementId, parameter });

        return answer;
    }

    /**
     * Gets the interface imports.
     *
     * @return the interface imports
     */
    public final List<FullyQualifiedJavaType> getInterfaceImports() {
        configure();
        return interfaceImports;
    }

    /**
     * Gets the implementation imports.
     *
     * @return the implementation imports
     */
    public final List<FullyQualifiedJavaType> getImplementationImports() {
        configure();
        return implementationImports;
    }

    /**
     * Gets the insert method.
     *
     * @param sqlMapNamespace
     *            the sql map namespace
     * @param statementId
     *            the statement id
     * @param parameter
     *            the parameter
     * @return the insert method
     */
    public final String getInsertMethod(String sqlMapNamespace,
            String statementId, String parameter) {
        configure();
        String answer = MessageFormat.format(insertMethodTemplate,
                new Object[] { sqlMapNamespace, statementId, parameter });

        return answer;
    }

    /**
     * Gets the query for list method.
     *
     * @param sqlMapNamespace
     *            the sql map namespace
     * @param statementId
     *            the statement id
     * @param parameter
     *            the parameter
     * @return the query for list method
     */
    public final String getQueryForListMethod(String sqlMapNamespace,
            String statementId, String parameter) {
        configure();
        String answer = MessageFormat.format(queryForListMethodTemplate,
                new Object[] { sqlMapNamespace, statementId, parameter });

        return answer;
    }

    /**
     * Gets the query for object method.
     *
     * @param sqlMapNamespace
     *            the sql map namespace
     * @param statementId
     *            the statement id
     * @param parameter
     *            the parameter
     * @return the query for object method
     */
    public final String getQueryForObjectMethod(String sqlMapNamespace,
            String statementId, String parameter) {
        configure();
        String answer = MessageFormat.format(queryForObjectMethodTemplate,
                new Object[] { sqlMapNamespace, statementId, parameter });

        return answer;
    }

    /**
     * Gets the super class.
     *
     * @return the super class
     */
    public final FullyQualifiedJavaType getSuperClass() {
        configure();
        return superClass;
    }

    /**
     * Gets the update method.
     *
     * @param sqlMapNamespace
     *            the sql map namespace
     * @param statementId
     *            the statement id
     * @param parameter
     *            the parameter
     * @return the update method
     */
    public final String getUpdateMethod(String sqlMapNamespace,
            String statementId, String parameter) {
        configure();
        String answer = MessageFormat.format(updateMethodTemplate,
                new Object[] { sqlMapNamespace, statementId, parameter });

        return answer;
    }

    /**
     * Gets the checked exceptions.
     *
     * @return the checked exceptions
     */
    public final List<FullyQualifiedJavaType> getCheckedExceptions() {
        configure();
        return checkedExceptions;
    }

    /**
     * Gets the field clones.
     *
     * @param commentGenerator
     *            the comment generator
     * @param introspectedTable
     *            the introspected table
     * @return the field clones
     */
    public final List<Field> getFieldClones(CommentGenerator commentGenerator,
            IntrospectedTable introspectedTable) {
        configure();
        List<Field> answer = new ArrayList<Field>();
        for (Field oldField : fields) {
            Field field = new Field();

            field.setInitializationString(oldField.getInitializationString());
            field.setFinal(oldField.isFinal());
            field.setStatic(oldField.isStatic());
            field.setName(oldField.getName());
            field.setType(oldField.getType());
            field.setVisibility(oldField.getVisibility());
            commentGenerator.addFieldComment(field, introspectedTable);
            answer.add(field);
        }

        return answer;
    }

    /**
     * Gets the method clones.
     *
     * @param commentGenerator
     *            the comment generator
     * @param introspectedTable
     *            the introspected table
     * @return the method clones
     */
    public final List<Method> getMethodClones(
            CommentGenerator commentGenerator,
            IntrospectedTable introspectedTable) {
        configure();
        List<Method> answer = new ArrayList<Method>();
        for (Method oldMethod : methods) {
            Method method = new Method();

            for (String bodyLine : oldMethod.getBodyLines()) {
                method.addBodyLine(bodyLine);
            }

            for (FullyQualifiedJavaType fqjt : oldMethod.getExceptions()) {
                method.addException(fqjt);
            }

            for (Parameter parm : oldMethod.getParameters()) {
                method.addParameter(parm);
            }

            method.setConstructor(oldMethod.isConstructor());
            method.setFinal(oldMethod.isFinal());
            method.setStatic(oldMethod.isStatic());
            method.setName(oldMethod.getName());
            method.setReturnType(oldMethod.getReturnType());
            method.setVisibility(oldMethod.getVisibility());

            commentGenerator.addGeneralMethodComment(method, introspectedTable);

            answer.add(method);
        }

        return answer;
    }

    /**
     * Sets the constructor template.
     *
     * @param constructorTemplate
     *            the new constructor template
     */
    protected void setConstructorTemplate(Method constructorTemplate) {
        this.constructorTemplate = constructorTemplate;
    }

    /**
     * Sets the delete method template.
     *
     * @param deleteMethodTemplate
     *            the new delete method template
     */
    protected void setDeleteMethodTemplate(String deleteMethodTemplate) {
        this.deleteMethodTemplate = deleteMethodTemplate;
    }

    /**
     * Adds the field.
     *
     * @param field
     *            the field
     */
    protected void addField(Field field) {
        fields.add(field);
    }

    /**
     * Sets the insert method template.
     *
     * @param insertMethodTemplate
     *            the new insert method template
     */
    protected void setInsertMethodTemplate(String insertMethodTemplate) {
        this.insertMethodTemplate = insertMethodTemplate;
    }

    /**
     * Adds the method.
     *
     * @param method
     *            the method
     */
    protected void addMethod(Method method) {
        methods.add(method);
    }

    /**
     * Sets the query for list method template.
     *
     * @param queryForListMethodTemplate
     *            the new query for list method template
     */
    protected void setQueryForListMethodTemplate(
            String queryForListMethodTemplate) {
        this.queryForListMethodTemplate = queryForListMethodTemplate;
    }

    /**
     * Sets the query for object method template.
     *
     * @param queryForObjectMethodTemplate
     *            the new query for object method template
     */
    protected void setQueryForObjectMethodTemplate(
            String queryForObjectMethodTemplate) {
        this.queryForObjectMethodTemplate = queryForObjectMethodTemplate;
    }

    /**
     * Sets the super class.
     *
     * @param superClass
     *            the new super class
     */
    protected void setSuperClass(FullyQualifiedJavaType superClass) {
        this.superClass = superClass;
    }

    /**
     * Sets the update method template.
     *
     * @param updateMethodTemplate
     *            the new update method template
     */
    protected void setUpdateMethodTemplate(String updateMethodTemplate) {
        this.updateMethodTemplate = updateMethodTemplate;
    }

    /**
     * Adds the interface import.
     *
     * @param type
     *            the type
     */
    protected void addInterfaceImport(FullyQualifiedJavaType type) {
        interfaceImports.add(type);
    }

    /**
     * Adds the implementation import.
     *
     * @param type
     *            the type
     */
    protected void addImplementationImport(FullyQualifiedJavaType type) {
        implementationImports.add(type);
    }

    /**
     * Adds the checked exception.
     *
     * @param type
     *            the type
     */
    protected void addCheckedException(FullyQualifiedJavaType type) {
        checkedExceptions.add(type);
    }

    /**
     * This method is called in the constructor to configure the DAO template.
     * Subclasses should implement the individual configureXXX methods to setup
     * the relevant parts of the DAO template (super class, extra methods, etc.)
     * that are relevant for this specific type of DAO.
     */
    private void configure() {
        if (!configured) {
            configureCheckedExceptions();
            configureConstructorTemplate();
            configureDeleteMethodTemplate();
            configureFields();
            configureImplementationImports();
            configureInsertMethodTemplate();
            configureInterfaceImports();
            configureMethods();
            configureQueryForListMethodTemplate();
            configureQueryForObjectMethodTemplate();
            configureSuperClass();
            configureUpdateMethodTemplate();
            configured = true;
        }
    }

    /**
     * Override this method to add checked exceptions to the throws clause of
     * any generated DAO method. When overriding this method, call
     * <code>addCheckedException(FullyQualifiedJavaType)</code> one or more
     * times to add checked exception(s) to all generated DAO methods.
     */
    protected void configureCheckedExceptions() {
    }

    /**
     * Override this method to add fields to any generated DAO implementation
     * class. When overriding this method, call <code>addField(Field)</code> one
     * or more times to add field(s) to the generated DAO implementation class.
     */
    protected void configureFields() {
    }

    /**
     * Override this method to add imports to generated DAO implementation
     * classes. When overriding this method, call
     * <code>addImplementationImport(FullyQualifiedJavaType)</code> one or more
     * times to add import(s) to generated DAO implementation classes.
     */
    protected void configureImplementationImports() {
    }

    /**
     * Override this method to add imports to generated DAO interface classes.
     * When overriding this method, call
     * <code>addInterfaceImport(FullyQualifiedJavaType)</code> one or more times
     * to add import(s) to generated DAO interface classes.
     */
    protected void configureInterfaceImports() {
    }

    /**
     * Override this method to add methods to generated DAO implementation
     * classes. When overriding this method, call <code>addMethod(Method)</code>
     * one or more times to add method(s) to generated DAO implementation
     * classes.
     */
    protected void configureMethods() {
    }

    /**
     * Override this method to set the superclass for any generated DAO
     * implementation class. When overriding this method call
     * <code>setSuperClass(FullyQualifiedJavaType)</code> to set the superclass
     * for generated DAO implementation classes.
     */
    protected void configureSuperClass() {
    }

    /**
     * Override this method to configure a constructor for generated DAO
     * implementation classes. During code generation, we will build a new
     * constructor using the visibility, parameters, body lines, and exceptions
     * set on the constructor template. When overriding this method, call
     * <code>setConstructorTemplate(Method)</code> to set the constructor
     * template.
     */
    protected abstract void configureConstructorTemplate();

    /**
     * Override this method to configure an insert method template. A method
     * template is a string with three substitution markers that we will
     * fill in when generating code. The substitution markers will be:
     * <ul>
     * <li>{0} - The SqlMap namespace</li>
     * <li>{1} - The SqlMap statement id</li>
     * <li>{2} - The parameter object</li>
     * </ul>
     * 
     * For example, when calling methods in the SqlMapClient interface, the
     * template would be:
     * 
     * sqlMapClient.insert(\"{0}.{1}\", {2});
     * 
     * Overriding methods should call the
     * <code>setInsertMethodTemplate(String)</code> method to set the template.
     * 
     */
    protected abstract void configureInsertMethodTemplate();

    /**
     * Override this method to configure a queryForList method template. A
     * method template is a string with three substitution markers that we
     * will fill in when generating code. The substitution markers will be:
     * <ul>
     * <li>{0} - The SqlMap namespace</li>
     * <li>{1} - The SqlMap statement id</li>
     * <li>{2} - The parameter object</li>
     * </ul>
     * 
     * For example, when calling methods in the SqlMapClient interface, the
     * template would be:
     * 
     * sqlMapClient.queryForList(\"{0}.{1}\", {2});
     * 
     * Overriding methods should call the
     * <code>setQueryForListMethodTemplate(String)</code> method to set the
     * template.
     */
    protected abstract void configureQueryForListMethodTemplate();

    /**
     * Override this method to configure a queryForObject method template. A
     * method template is a string with three substitution markers that we
     * will fill in when generating code. The substitution markers will be:
     * <ul>
     * <li>{0} - The SqlMap namespace</li>
     * <li>{1} - The SqlMap statement id</li>
     * <li>{2} - The parameter object</li>
     * </ul>
     * 
     * For example, when calling methods in the SqlMapClient interface, the
     * template would be:
     * 
     * sqlMapClient.queryForObject(\"{0}.{1}\", {2});
     * 
     * Overriding methods should call the
     * <code>setQueryForObjectMethodTemplate(String)</code> method to set the
     * template.
     */
    protected abstract void configureQueryForObjectMethodTemplate();

    /**
     * Override this method to configure an update method template. A method
     * template is a string with three substitution markers that we will
     * fill in when generating code. The substitution markers will be:
     * <ul>
     * <li>{0} - The SqlMap namespace</li>
     * <li>{1} - The SqlMap statement id</li>
     * <li>{2} - The parameter object</li>
     * </ul>
     * 
     * For example, when calling methods in the SqlMapClient interface, the
     * template would be:
     * 
     * sqlMapClient.update(\"{0}.{1}\", {2});
     * 
     * Overriding methods should call the
     * <code>setUpdateMethodTemplate(String)</code> method to set the template.
     */
    protected abstract void configureUpdateMethodTemplate();

    /**
     * Override this method to configure a delete method template. A method
     * template is a string with three substitution markers that we will
     * fill in when generating code. The substitution markers will be:
     * <ul>
     * <li>{0} - The SqlMap namespace</li>
     * <li>{1} - The SqlMap statement id</li>
     * <li>{2} - The parameter object</li>
     * </ul>
     * 
     * For example, when calling methods in the SqlMapClient interface, the
     * template would be:
     * 
     * sqlMapClient.delete(\"{0}.{1}\", {2});
     * 
     * Overriding methods should call the
     * <code>setDeleteMethodTemplate(String)</code> method to set the template.
     */
    protected abstract void configureDeleteMethodTemplate();
}
