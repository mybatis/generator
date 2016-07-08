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
package org.mybatis.generator.plugins;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

public class LogicallyDeletePlugin extends PluginAdapter {
	
	private boolean isSimple = false;

    public boolean validate(List<String> warnings) {
        return true;
    }
    
	public void setSuperClass(TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
	{
		String superClass = introspectedTable.getTableConfigurationProperty("superClass");
		if(superClass != null)
		{
			topLevelClass.setSuperClass(superClass);
		}
	}
	
    
	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub
		setSuperClass(topLevelClass, introspectedTable);
		return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
	}

	@Override
	public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub
		setSuperClass(topLevelClass, introspectedTable);
		return super.modelPrimaryKeyClassGenerated(topLevelClass, introspectedTable);
	}

	@Override
	public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub
		setSuperClass(topLevelClass, introspectedTable);
		return super.modelRecordWithBLOBsClassGenerated(topLevelClass, introspectedTable);
	}


	@Override
	public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub
		if(introspectedTable.getColumn("is_delete") != null)
		{
			element.addElement(new TextElement("and is_delete = 0"));
		}
		return super.sqlMapSelectByPrimaryKeyElementGenerated(element, introspectedTable);
	}

	@Override
	public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
			IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub
		addClientDeleteElements(interfaze, introspectedTable);
		addClientUpdateByPrimaryElements(interfaze, introspectedTable);
		addClientUpdateByPrimaryWithBLOBsElements(interfaze, introspectedTable);
		addClientUpdateByPrimarySelectiveElements(interfaze, introspectedTable);
		addClientSelectSelectiveElements(interfaze, introspectedTable);
		addClientSelectSelectiveWithPageElements(interfaze, introspectedTable);
		addClientCountSelectiveElements(interfaze, introspectedTable);
		
		return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
	}
	


	@Override
	public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
		// TODO Auto-generated method stub
		addSqlMapDeleteElements(document.getRootElement(), introspectedTable);
		addSqlMapUpdateByPrimaryElements(document.getRootElement(), introspectedTable);
		addSqlMapUpdateByPrimaryWithBLOBsElements(document.getRootElement(), introspectedTable);
		addSqlMapUpdateByPrimarySelectiveElements(document.getRootElement(), introspectedTable);
		addSqlMapSelectSelectiveElements(document.getRootElement(), introspectedTable);
		addSqlMapSelectSelectiveWithPageElements(document.getRootElement(), introspectedTable);
		addSqlMapCountSelectiveElements(document.getRootElement(), introspectedTable);
		
		return super.sqlMapDocumentGenerated(document, introspectedTable);
	}
	
	

	public void addClientDeleteElements(Interface interfaze, IntrospectedTable introspectedTable) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName(introspectedTable.getDeleteByPrimaryKeyStatementId());

        if (!isSimple && introspectedTable.getRules().generatePrimaryKeyClass()) {
            FullyQualifiedJavaType type = new FullyQualifiedJavaType(
                    introspectedTable.getPrimaryKeyType());
            importedTypes.add(type);
            method.addParameter(new Parameter(type, "key")); //$NON-NLS-1$
        } else {
            // no primary key class - fields are in the base class
            // if more than one PK field, then we need to annotate the
            // parameters
            // for MyBatis
            List<IntrospectedColumn> introspectedColumns = introspectedTable
                    .getPrimaryKeyColumns();
            boolean annotate = introspectedColumns.size() > 1;
            if (annotate) {
                importedTypes.add(new FullyQualifiedJavaType(
                        "org.apache.ibatis.annotations.Param")); //$NON-NLS-1$
            }
            StringBuilder sb = new StringBuilder();
            for (IntrospectedColumn introspectedColumn : introspectedColumns) {
                FullyQualifiedJavaType type = introspectedColumn
                        .getFullyQualifiedJavaType();
                importedTypes.add(type);
                Parameter parameter = new Parameter(type, introspectedColumn
                        .getJavaProperty());
                if (annotate) {
                    sb.setLength(0);
                    sb.append("@Param(\""); //$NON-NLS-1$
                    sb.append(introspectedColumn.getJavaProperty());
                    sb.append("\")"); //$NON-NLS-1$
                    parameter.addAnnotation(sb.toString());
                }
                method.addParameter(parameter);
            }
        }

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);
       
        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(method);
    }
	
    public void addClientUpdateByPrimaryElements(Interface interfaze, IntrospectedTable introspectedTable) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(
                introspectedTable.getBaseRecordType());
        importedTypes.add(parameterType);

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName(introspectedTable.getUpdateByPrimaryKeyStatementId());
        method.addParameter(new Parameter(parameterType, "record")); //$NON-NLS-1$

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);
        
            interfaze.addImportedTypes(importedTypes);
            interfaze.addMethod(method);
    }
    
    public void addClientUpdateByPrimaryWithBLOBsElements(Interface interfaze, IntrospectedTable introspectedTable) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        FullyQualifiedJavaType parameterType;

        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = new FullyQualifiedJavaType(introspectedTable
                    .getRecordWithBLOBsType());
        } else {
            parameterType = new FullyQualifiedJavaType(introspectedTable
                    .getBaseRecordType());
        }

        importedTypes.add(parameterType);

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());

        method.setName(introspectedTable
            .getUpdateByPrimaryKeyWithBLOBsStatementId());
        method.addParameter(new Parameter(parameterType, "record")); //$NON-NLS-1$

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(method);
    }
    
    public void addClientUpdateByPrimarySelectiveElements(Interface interfaze, IntrospectedTable introspectedTable) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        FullyQualifiedJavaType parameterType;

        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = new FullyQualifiedJavaType(introspectedTable
                    .getRecordWithBLOBsType());
        } else {
            parameterType = new FullyQualifiedJavaType(introspectedTable
                    .getBaseRecordType());
        }

        importedTypes.add(parameterType);

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName(introspectedTable
                .getUpdateByPrimaryKeySelectiveStatementId());
        method.addParameter(new Parameter(parameterType, "record")); //$NON-NLS-1$

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(method);
    }
    
    public void addClientSelectSelectiveElements(Interface interfaze, IntrospectedTable introspectedTable) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);

        FullyQualifiedJavaType returnType = FullyQualifiedJavaType
                .getNewListInstance();
        FullyQualifiedJavaType listType;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            listType = new FullyQualifiedJavaType(introspectedTable
                    .getRecordWithBLOBsType());
        } else {
            // the blob fields must be rolled up into the base class
            listType = new FullyQualifiedJavaType(introspectedTable
                    .getBaseRecordType());
        }

        importedTypes.add(listType);
        returnType.addTypeArgument(listType);
        method.setReturnType(returnType);
        method.setName("selectSelective");
        method.addParameter(new Parameter(listType, "record")); //$NON-NLS-1$

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);
        
        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(method);
    }
    
    public void addClientSelectSelectiveWithPageElements(Interface interfaze, IntrospectedTable introspectedTable) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);

        FullyQualifiedJavaType returnType = FullyQualifiedJavaType
                .getNewListInstance();
        FullyQualifiedJavaType listType;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            listType = new FullyQualifiedJavaType(introspectedTable
                    .getRecordWithBLOBsType());
        } else {
            // the blob fields must be rolled up into the base class
            listType = new FullyQualifiedJavaType(introspectedTable
                    .getBaseRecordType());
        }

        importedTypes.add(listType);
        returnType.addTypeArgument(listType);
        method.setReturnType(returnType);
        method.setName("selectSelectiveWithPage");
        method.addParameter(new Parameter(listType, "record")); //$NON-NLS-1$

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);
        
        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(method);
    }

    public void addClientCountSelectiveElements(Interface interfaze, IntrospectedTable introspectedTable) {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        FullyQualifiedJavaType parameterType;

        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = new FullyQualifiedJavaType(introspectedTable
                    .getRecordWithBLOBsType());
        } else {
            parameterType = new FullyQualifiedJavaType(introspectedTable
                    .getBaseRecordType());
        }

        importedTypes.add(parameterType);

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName("countSelective");
        method.addParameter(new Parameter(parameterType, "record")); //$NON-NLS-1$

        context.getCommentGenerator().addGeneralMethodComment(method,
                introspectedTable);

        interfaze.addImportedTypes(importedTypes);
        interfaze.addMethod(method);
    }

	
    public void addSqlMapDeleteElements(XmlElement parentElement, IntrospectedTable introspectedTable) {
    	if(introspectedTable.getColumn("is_delete") != null){
	        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$
	    	
	        answer.addAttribute(new Attribute(
	                "id", introspectedTable.getDeleteByPrimaryKeyStatementId())); //$NON-NLS-1$
	        String parameterClass;
	        if (!isSimple && introspectedTable.getRules().generatePrimaryKeyClass()) {
	            parameterClass = introspectedTable.getPrimaryKeyType();
	        } else {
	            // PK fields are in the base class. If more than on PK
	            // field, then they are coming in a map.
	            if (introspectedTable.getPrimaryKeyColumns().size() > 1) {
	                parameterClass = "map"; //$NON-NLS-1$
	            } else {
	                parameterClass = introspectedTable.getPrimaryKeyColumns()
	                        .get(0).getFullyQualifiedJavaType().toString();
	            }
	        }
	        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
	                parameterClass));
	
	        context.getCommentGenerator().addComment(answer);
	
	        StringBuilder sb = new StringBuilder();
	        sb.append("update "); //$NON-NLS-1$
	        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
	        answer.addElement(new TextElement(sb.toString()));
	
	        sb.setLength(0);
	        sb.append("set is_delete = 1");
	        answer.addElement(new TextElement(sb.toString()));
	        
	        boolean and = false;
	        for (IntrospectedColumn introspectedColumn : introspectedTable
	                .getPrimaryKeyColumns()) {
	            sb.setLength(0);
	            if (and) {
	                sb.append("  and "); //$NON-NLS-1$
	            } else {
	                sb.append("where "); //$NON-NLS-1$
	                and = true;
	            }
	
	            sb.append(MyBatis3FormattingUtilities
	                    .getEscapedColumnName(introspectedColumn));
	            sb.append(" = "); //$NON-NLS-1$
	            sb.append(MyBatis3FormattingUtilities
	                    .getParameterClause(introspectedColumn));
	            answer.addElement(new TextElement(sb.toString()));
	        }
	
	        parentElement.addElement(answer);    		
    	}else{
	        XmlElement answer = new XmlElement("delete"); //$NON-NLS-1$
	
	        answer.addAttribute(new Attribute(
	                "id", introspectedTable.getDeleteByPrimaryKeyStatementId())); //$NON-NLS-1$
	        String parameterClass;
	        if (!isSimple && introspectedTable.getRules().generatePrimaryKeyClass()) {
	            parameterClass = introspectedTable.getPrimaryKeyType();
	        } else {
	            // PK fields are in the base class. If more than on PK
	            // field, then they are coming in a map.
	            if (introspectedTable.getPrimaryKeyColumns().size() > 1) {
	                parameterClass = "map"; //$NON-NLS-1$
	            } else {
	                parameterClass = introspectedTable.getPrimaryKeyColumns()
	                        .get(0).getFullyQualifiedJavaType().toString();
	            }
	        }
	        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
	                parameterClass));
	
	        context.getCommentGenerator().addComment(answer);
	
	        StringBuilder sb = new StringBuilder();
	        sb.append("delete from "); //$NON-NLS-1$
	        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
	        answer.addElement(new TextElement(sb.toString()));
	
	        boolean and = false;
	        for (IntrospectedColumn introspectedColumn : introspectedTable
	                .getPrimaryKeyColumns()) {
	            sb.setLength(0);
	            if (and) {
	                sb.append("  and "); //$NON-NLS-1$
	            } else {
	                sb.append("where "); //$NON-NLS-1$
	                and = true;
	            }
	
	            sb.append(MyBatis3FormattingUtilities
	                    .getEscapedColumnName(introspectedColumn));
	            sb.append(" = "); //$NON-NLS-1$
	            sb.append(MyBatis3FormattingUtilities
	                    .getParameterClause(introspectedColumn));
	            answer.addElement(new TextElement(sb.toString()));
	        }
	
	        parentElement.addElement(answer);
    	}
    }
    
    public void addSqlMapUpdateByPrimaryElements(XmlElement parentElement, IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

        answer.addAttribute(new Attribute(
                "id", introspectedTable.getUpdateByPrimaryKeyStatementId())); //$NON-NLS-1$
        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                introspectedTable.getBaseRecordType()));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("update "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        // set up for first column
        sb.setLength(0);
        sb.append("set "); //$NON-NLS-1$

        Iterator<IntrospectedColumn> iter;
        if (isSimple) {
            iter = introspectedTable.getNonPrimaryKeyColumns().iterator();
        } else {
            iter = introspectedTable.getBaseColumns().iterator();
        }
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();
            
            if(MyBatis3FormattingUtilities
            .getEscapedColumnName(introspectedColumn).equals("is_delete")) continue;

            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));

            if (iter.hasNext()) {
                sb.append(',');
            }

            answer.addElement(new TextElement(sb.toString()));

            // set up for the next column
            if (iter.hasNext()) {
                sb.setLength(0);
                OutputUtilities.xmlIndent(sb, 1);
            }
        }

        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and "); //$NON-NLS-1$
            } else {
                sb.append("where "); //$NON-NLS-1$
                and = true;
            }

            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }
        if(introspectedTable.getColumn("is_delete") != null){
        	answer.addElement(new TextElement("and is_delete = 0"));
        }

        parentElement.addElement(answer);        
    }
    
    public void addSqlMapUpdateByPrimaryWithBLOBsElements(XmlElement parentElement, IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

        answer
                .addAttribute(new Attribute(
                        "id", introspectedTable.getUpdateByPrimaryKeyWithBLOBsStatementId())); //$NON-NLS-1$

        String parameterType;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = introspectedTable.getRecordWithBLOBsType();
        } else {
            parameterType = introspectedTable.getBaseRecordType();
        }

        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                parameterType));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("update "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        // set up for first column
        sb.setLength(0);
        sb.append("set "); //$NON-NLS-1$

        Iterator<IntrospectedColumn> iter = introspectedTable
                .getNonPrimaryKeyColumns().iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();
            
            if(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn).equals("is_delete")) continue;

            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));

            if (iter.hasNext()) {
                sb.append(',');
            }

            answer.addElement(new TextElement(sb.toString()));

            // set up for the next column
            if (iter.hasNext()) {
                sb.setLength(0);
                OutputUtilities.xmlIndent(sb, 1);
            }
        }

        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and "); //$NON-NLS-1$
            } else {
                sb.append("where "); //$NON-NLS-1$
                and = true;
            }

            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }
        
        if(introspectedTable.getColumn("is_delete") != null){
        	answer.addElement(new TextElement("and is_delete = 0"));
        }

        parentElement.addElement(answer);
    }
    
    public void addSqlMapUpdateByPrimarySelectiveElements(XmlElement parentElement, IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("update"); //$NON-NLS-1$

        answer
                .addAttribute(new Attribute(
                        "id", introspectedTable.getUpdateByPrimaryKeySelectiveStatementId())); //$NON-NLS-1$

        String parameterType;

        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = introspectedTable.getRecordWithBLOBsType();
        } else {
            parameterType = introspectedTable.getBaseRecordType();
        }

        answer.addAttribute(new Attribute("parameterType", //$NON-NLS-1$
                parameterType));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("update "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement dynamicElement = new XmlElement("set"); //$NON-NLS-1$
        answer.addElement(dynamicElement);

        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getNonPrimaryKeyColumns()) {
            if(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn).equals("is_delete")) continue;
        	
            XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" != null"); //$NON-NLS-1$
            isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
            dynamicElement.addElement(isNotNullElement);

            sb.setLength(0);
            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));
            sb.append(',');

            isNotNullElement.addElement(new TextElement(sb.toString()));
        }

        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  and "); //$NON-NLS-1$
            } else {
                sb.append("where "); //$NON-NLS-1$
                and = true;
            }

            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }

        if(introspectedTable.getColumn("is_delete") != null){
        	answer.addElement(new TextElement("and is_delete = 0"));
        }
        
        parentElement.addElement(answer);

    }
    
    public void addSqlMapSelectSelectiveElements(XmlElement parentElement, IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

        answer
                .addAttribute(new Attribute(
                        "id", "selectSelective")); //$NON-NLS-1$
        
        if (introspectedTable.getRules().generateResultMapWithBLOBs()) {
            answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                    introspectedTable.getResultMapWithBLOBsId()));
        } else {
            answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                    introspectedTable.getBaseResultMapId()));
        }

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("select * from "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement dynamicElement = new XmlElement("where"); //$NON-NLS-1$
        answer.addElement(dynamicElement);
        
        boolean and = false;
        if(introspectedTable.getColumn("is_delete") != null)
        {
        	sb.setLength(0);
        	sb.append("is_delete = 0");
        	dynamicElement.addElement(new TextElement(sb.toString()));
        	and = true;
        }

        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getNonPrimaryKeyColumns()) {
            if(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn).equals("is_delete")) continue;
        	
            XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" != null"); //$NON-NLS-1$
            isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
            dynamicElement.addElement(isNotNullElement);

            sb.setLength(0);
            if(and){
            	sb.append("and ");
            }
            and = true;
            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));

            isNotNullElement.addElement(new TextElement(sb.toString()));
        }
        
        parentElement.addElement(answer);

    }   
    
    public void addSqlMapSelectSelectiveWithPageElements(XmlElement parentElement, IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

        answer
                .addAttribute(new Attribute(
                        "id", "selectSelectiveWithPage")); //$NON-NLS-1$
        
        if (introspectedTable.getRules().generateResultMapWithBLOBs()) {
            answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                    introspectedTable.getResultMapWithBLOBsId()));
        } else {
            answer.addAttribute(new Attribute("resultMap", //$NON-NLS-1$
                    introspectedTable.getBaseResultMapId()));
        }

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("select * from "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement dynamicElement = new XmlElement("where"); //$NON-NLS-1$
        answer.addElement(dynamicElement);
        
        boolean and = false;
        if(introspectedTable.getColumn("is_delete") != null)
        {
        	sb.setLength(0);
        	sb.append("is_delete = 0");
        	dynamicElement.addElement(new TextElement(sb.toString()));
        	and = true;
        }

        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getNonPrimaryKeyColumns()) {
            if(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn).equals("is_delete")) continue;
        	
            XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" != null"); //$NON-NLS-1$
            isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
            dynamicElement.addElement(isNotNullElement);

            sb.setLength(0);
            if(and){
            	sb.append("and ");
            }
            and = true;
            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));

            isNotNullElement.addElement(new TextElement(sb.toString()));
        }
        
        answer.addElement(new TextElement("limit #{pageStartNum},#{pageSize}"));
        
        parentElement.addElement(answer);

    }   
    
    public void addSqlMapCountSelectiveElements(XmlElement parentElement, IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("select"); //$NON-NLS-1$

        answer.addAttribute(new Attribute(
                        "id", "countSelective")); //$NON-NLS-1$
               
        answer.addAttribute(new Attribute("resultType", "java.lang.Integer"));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("select count(*) from "); //$NON-NLS-1$
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement dynamicElement = new XmlElement("where"); //$NON-NLS-1$
        answer.addElement(dynamicElement);
        
        boolean and = false;
        if(introspectedTable.getColumn("is_delete") != null)
        {
        	sb.setLength(0);
        	sb.append("is_delete = 0");
        	dynamicElement.addElement(new TextElement(sb.toString()));
        	and = true;
        }

        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getNonPrimaryKeyColumns()) {
            if(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn).equals("is_delete")) continue;
        	
            XmlElement isNotNullElement = new XmlElement("if"); //$NON-NLS-1$
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" != null"); //$NON-NLS-1$
            isNotNullElement.addAttribute(new Attribute("test", sb.toString())); //$NON-NLS-1$
            dynamicElement.addElement(isNotNullElement);

            sb.setLength(0);
            if(and){
            	sb.append("and ");
            }
            and = true;
            sb.append(MyBatis3FormattingUtilities
                    .getEscapedColumnName(introspectedColumn));
            sb.append(" = "); //$NON-NLS-1$
            sb.append(MyBatis3FormattingUtilities
                    .getParameterClause(introspectedColumn));

            isNotNullElement.addElement(new TextElement(sb.toString()));
        }
        
        parentElement.addElement(answer);

    }   
}
