/*
 *    Copyright 2013-2014 the original author or authors.
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


package org.mybatis.generator.plugins.jaxbAnnotations;


import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.JavaElement;
import org.mybatis.generator.api.Plugin;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;



/**
JaxbAnnotations is the abstract base class for plugins that add JAXB Annotation
support for XML marshalling and unmarshalling of the generated model classes.
This class defines most of the methods that subclasses will call for the actual
insertion of the JAXB annotations into the generated model classes.

<p/>The only abstract method in this class is:
<code>public abstract String getXmlAccessTypeConstant</code>, which returns
a <code>String</code> indicating the JAXB <code>XMLAccessType</code> that will
be used to annotate the generated model classes.
<br/>Subclasses must override this method to return one of the supported JAXB
XMLAccessTypes, such as <code>XmlAccessType.FIELD</code>, 
<code>XmlAccessType.PUBLIC_MEMBER</code>, etc.

<p/>For example, a subclass can specify that the generated model class will be
annotated with type <code>XmlAccessType.FIELD</code>, by overriding the
<code>getXmlAccessTypeConstant</code> method to return the <code>String</code>:
<code>"@XmlAccessorType(XmlAccessType.FIELD)"</code>

<p/>This class does not support the marshalling and unmarshalling of the
auto generated Example Classes, which are also generated along with other model
classes. Unlike the other auto generated model classes, Example classes are not
a "Domain Object" (or "DTO") type of class and so the marshalling and
unmarshalling of these classes should not be needed. Subclasses are however
free to add support for marshalling and unmarshalling of the Example Classes,
if they so choose.

@author Mahiar Mody
@version 1.0.0
@see org.mybatis.generator.plugins.jaxbAnnotations.FieldTypeJaxbAnnotations  FieldTypeJaxbAnnotations Class
@see org.mybatis.generator.plugins.jaxbAnnotations.PropertyTypeJaxbAnnotations  PropertyTypeJaxbAnnotations Class
*/
public abstract class AbstractJaxbAnnotations extends PluginAdapter {

    public static final String MARSHAL_BLOB_COLUMNS = "marshalBlobColumns";
    protected static final String IMPORT_PREFIX = "javax.xml.bind.annotation.";

    protected Map<String, String> mapIgnoreCasePropLookup;
    protected boolean marshalBlobColumns;

    /**
    Returns a <code>String</code> object that indicates the JAXB
    <code>XMLAccessType</code> that will be used to annotate the
    generated model classes.
    <br/>Subclasses must override this method to return one of the supported
    JAXB XMLAccessTypes, such as <code>XmlAccessType.FIELD</code>, 
    <code>XmlAccessType.PUBLIC_MEMBER</code>, etc.
    <br />For Example, a return value of
    <code>"@XmlAccessorType(XmlAccessType.FIELD)"</code>
    would indicate that all MyBatis Generator generated model class will be
    annotated with XmlAccessType.FIELD

    @return        the <code>String</code> object representing the XmlAccessType
                to use for annotating the generated model classes
    */
    public abstract String getXmlAccessTypeConstant(); //Subclasses will override to return correct XmlAccessType


    protected AbstractJaxbAnnotations() {
        mapIgnoreCasePropLookup = new HashMap<String,String>();
        marshalBlobColumns = false; //default value is false. So don't marshal BLOB columns by default.
    }


    @Override
    public boolean validate(List<String> warnings) {

        marshalBlobColumns = Boolean.parseBoolean(properties.getProperty(MARSHAL_BLOB_COLUMNS));
        properties.remove(MARSHAL_BLOB_COLUMNS);

        String errMsg = null;

        Enumeration<?> enmr = properties.propertyNames();

        while(enmr.hasMoreElements()) {
            String propKey = (String) enmr.nextElement();

            errMsg = getValidationErrorForProperty(propKey);

            if(errMsg == null) {
                mapIgnoreCasePropLookup.put(propKey.toUpperCase(), propKey);
            }
            else {
                warnings.add(errMsg);
            }
        }

        return true;
    }


    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        annotateGeneratedClass(topLevelClass, introspectedTable);
        return true;
    }


    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        annotateGeneratedClass(topLevelClass, introspectedTable);
        return true;
    }


    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        /*
        When marshalBlobColumns is true, annotate the RecordWithBlob class. Otherwise, don't
        annotate the RecordWithBlob class at all because the RecordWithBlob class only contains
        BLOB columns, none of which are to be marshalled. That's what the "if" condition below
        is checking.
        */
        if(marshalBlobColumns) {
            annotateGeneratedClass(topLevelClass, introspectedTable);
        }

        return true;
    }



    protected void annotateGeneratedClass(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        String fullyQualifiedTblName = introspectedTable.getFullyQualifiedTableNameAtRuntime().toUpperCase();

        if(mapIgnoreCasePropLookup.containsKey(fullyQualifiedTblName)) {

            annotateJavaElement(topLevelClass, topLevelClass, properties.getProperty(mapIgnoreCasePropLookup.get(fullyQualifiedTblName)));

            /*
            Removal of the "fullyQualifiedTblName" from the lookup map viz. "mapIgnoreCasePropLookup" for optimization
            reasons cannot be done because, depending upon the model type specified in the MyBatis Generator's XML config
            file, multiple classes can be generated for a single table. E.g. Base, Key, and WithBLOBs classes can all be
            generated for a single table in the hierarchical model type. Since the user specified custom JAXB annotations
            will need to be looked up each time, the Base, Key, and WithBLOBs classes are generated, "fullyQualifiedTblName"
            cannot be removed from the lookup map mapIgnoreCasePropLookup.            
            */
            //mapIgnoreCasePropLookup.remove(fullyQualifiedTblName);
        }
        else {
            addDefaultAnnotationsToClass(topLevelClass);
        }

        addDefaultNoArgConstructor(topLevelClass, introspectedTable);
    }




    protected void addImportsForAnnotations(TopLevelClass topLevelClass, String... annotations) {

        for(String strAnnot : annotations) {
            topLevelClass.addImportedType(new FullyQualifiedJavaType(IMPORT_PREFIX + strAnnot));
        }
    }



    protected void addDefaultAnnotationsToClass(TopLevelClass topLevelClass) {

        String thisClassName = topLevelClass.getType().getShortName();

        //Start of code to change the first letter of the Java Class name to lower case in the Xml Root Element

        StringBuilder sb = new StringBuilder(thisClassName);
        sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        sb.insert(0, "@XmlRootElement(name=\"");
        sb.append("\")");

        //End of code to change the first letter of the Java Class name to lower case in the Xml Root Element


        //Add default JAXB imports
        addImportsForAnnotations(topLevelClass, "XmlAccessorType", "XmlAccessType", "XmlRootElement");


        //Add default JAXB annotations
        topLevelClass.addAnnotation(getXmlAccessTypeConstant()); //Returns either: @XmlAccessorType(XmlAccessType.FIELD) or @XmlAccessorType(XmlAccessType.PROPERTY)
        topLevelClass.addAnnotation(sb.toString());
    }



    protected void annotateJavaElement(TopLevelClass topLevelClass, JavaElement javaElement, String tildSeparatedAnnotations) {

        List<String> lstUserAnnotations = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();
        String annot = null, bracketFragment = null;

        if(javaElement instanceof TopLevelClass && !tildSeparatedAnnotations.contains(getXmlAccessTypeConstant())) {
            tildSeparatedAnnotations += '~' + getXmlAccessTypeConstant();
        }

        for(String annotation : tildSeparatedAnnotations.split("~")) {

            javaElement.addAnnotation(annotation);


            sb.append(annotation);
            sb.deleteCharAt(0); //remove the leading @ sign from the annotation.

            int ind = sb.indexOf("(");
            if(ind != -1) {

                annot = sb.substring(0, ind);
                bracketFragment = sb.substring(ind+1);

                if(bracketFragment.startsWith("XmlAccessType.")) {
                    lstUserAnnotations.add("XmlAccessType"); //Add the import for XmlAccessType annotation that is inside the parenthesis of XmlAccessorType: (XmlAccessType.FIELD) or (XmlAccessType.PROPERTY). Example: @XmlAccessorType(XmlAccessType.FIELD)
                }
                else
                if(bracketFragment.startsWith("XmlAccessOrder.")) {
                    lstUserAnnotations.add("XmlAccessOrder"); //Add the import for XmlAccessOrder annotation that is inside the parenthesis of XmlAccessorOrder: (XmlAccessorOrder.ALPHABETICAL) or (XmlAccessorOrder.UNDEFINED). Example: @XmlAccessorOrder(XmlAccessorOrder.ALPHABETICAL) 
                }
            }
            else {
                annot = sb.toString();
            }

            sb.delete(0, sb.length());

            if(!lstUserAnnotations.contains(annot)) {
                lstUserAnnotations.add(annot);
            }
        }

        addImportsForAnnotations(topLevelClass, lstUserAnnotations.toArray(new String[0]));
    }



    protected void addDefaultNoArgConstructor(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {

        Method defaultNoArgConstructor = new Method();
        defaultNoArgConstructor.setVisibility(JavaVisibility.PUBLIC);
        defaultNoArgConstructor.setName(topLevelClass.getType().getShortName());
        defaultNoArgConstructor.setConstructor(true);
        defaultNoArgConstructor.addBodyLine("//Needed For JAXB serialization."); 

        context.getCommentGenerator().addGeneralMethodComment(defaultNoArgConstructor, introspectedTable);

        topLevelClass.addMethod(defaultNoArgConstructor);
    }



    protected String getValidationErrorForProperty(String propKey) {

        String[] parts = {propKey, properties.getProperty(propKey)};
        String[] type = {"key", "value"};
        String[] sepSeq = {"..", "~~"};
        char[] sepChar = {'.', '~'};

        for(int i=0; i<parts.length; ++i) {

            if(parts[i].charAt(0) == sepChar[i] || parts[i].charAt(parts[i].length()-1) == sepChar[i]) {
                return "Property ignored. Property " + type[i] + " cannot start or end with a '" + sepChar + "' character: " + parts[i];
            }

            if(parts[i].indexOf(' ') != -1 || parts[i].indexOf('\t') != -1) {
                return "Property ignored. Property " + type[i] + " cannot contain whitespaces: " + parts[i];
            }

            if(parts[i].indexOf(sepSeq[i]) != -1) {
                return "Property ignored. Property " + type[i] + " cannot contain consecutive '" + sepChar + "' characters: " + parts[i];
            }

            if(parts[i].length() == 0) {
                return "Property ignored. Property " + type[i] + " cannot be blank, or empty or unspecified.";
            }
        }

        return null;
    }
}
