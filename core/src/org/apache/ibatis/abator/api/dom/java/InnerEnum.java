/*
 *  Copyright 2006 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.ibatis.abator.api.dom.java;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.abator.api.dom.OutputUtilities;

/**
 * This class encapsulates the idea of an inner enum - it has methods that make
 * it easy to generate inner enum.
 * 
 * @author Jeff Butler
 */
public class InnerEnum extends JavaElement {

    private List fields;
    
    private List innerClasses;
    
    private List innerEnums;

    private FullyQualifiedJavaType type;

    private Set superInterfaceTypes;

    private List methods;
    
    private List enumConstants;
    
    /**
     * 
     */
    public InnerEnum(FullyQualifiedJavaType type) {
        super();
        this.type = type;
        fields = new ArrayList();
        innerClasses = new ArrayList();
        innerEnums = new ArrayList();
        superInterfaceTypes = new HashSet();
        methods = new ArrayList();
        enumConstants = new ArrayList();
    }

    /**
     * @return Returns the fields.
     */
    public List getFields() {
        return fields;
    }

    public void addField(Field field) {
        fields.add(field);
    }

    /**
     * @return Returns the innerClasses.
     */
    public List getInnerClasses() {
        return innerClasses;
    }

    public void addInnerClass(InnerClass innerClass) {
        innerClasses.add(innerClass);
    }

    public List getInnerEnums() {
        return innerEnums;
    }

    public void addInnerEnum(InnerEnum innerEnum) {
        innerEnums.add(innerEnum);
    }
    
    public List getEnumConstants() {
        return enumConstants;
    }
    
    public void addEnumConstant(String enumConstant) {
        enumConstants.add(enumConstant);
    }
    
    public String getFormattedContent(int indentLevel) {
        StringBuffer sb = new StringBuffer();

        Iterator iter = getJavaDocLines().iterator();
        while (iter.hasNext()) {
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append(iter.next());
            OutputUtilities.newLine(sb);
        }
        
        iter = getAnnotations().iterator();
        while (iter.hasNext()) {
            OutputUtilities.javaIndent(sb, indentLevel);
            sb.append(iter.next());
            OutputUtilities.newLine(sb);
        }
        
        OutputUtilities.javaIndent(sb, indentLevel);
        if (getVisibility() == JavaVisibility.PUBLIC) {
            sb.append("public "); //$NON-NLS-1$
        }
        
        sb.append("enum "); //$NON-NLS-1$
        sb.append(getType().getShortName());
        
        if (superInterfaceTypes.size() > 0) {
            sb.append(" implements "); //$NON-NLS-1$
            
            iter = superInterfaceTypes.iterator();
            boolean comma = false;
            while (iter.hasNext()) {
                if (comma) {
                    sb.append(", "); //$NON-NLS-1$
                } else {
                    comma = true;
                }
                
                FullyQualifiedJavaType fqjt = (FullyQualifiedJavaType) iter.next();
                sb.append(fqjt.getShortName());
            }
        }
        
        sb.append(" {"); //$NON-NLS-1$
        indentLevel++;

        iter = enumConstants.iterator();
        while (iter.hasNext()) {
            OutputUtilities.newLine(sb);
            OutputUtilities.javaIndent(sb, indentLevel);
            String enumConstant = (String) iter.next();
            sb.append(enumConstant);
            
            if (iter.hasNext()) {
                sb.append(',');
            } else {
                sb.append(';');
            }
        }
        
        if (fields.size() > 0) {
            OutputUtilities.newLine(sb);
        }
        iter = fields.iterator();
        while (iter.hasNext()) {
            OutputUtilities.newLine(sb);
            Field field = (Field) iter.next();
            sb.append(field.getFormattedContent(indentLevel));
            if (iter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }
        
        if (methods.size() > 0) {
            OutputUtilities.newLine(sb);
        }
        iter = methods.iterator();
        while (iter.hasNext()) {
            OutputUtilities.newLine(sb);
            Method method = (Method) iter.next();
            sb.append(method.getFormattedContent(indentLevel, false));
            if (iter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }
        
        if (innerClasses.size() > 0) {
            OutputUtilities.newLine(sb);
        }
        iter = innerClasses.iterator();
        while (iter.hasNext()) {
            OutputUtilities.newLine(sb);
            InnerClass innerClass = (InnerClass) iter.next();
            sb.append(innerClass.getFormattedContent(indentLevel));
            if (iter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }
        
        if (innerEnums.size() > 0) {
            OutputUtilities.newLine(sb);
        }
        iter = innerEnums.iterator();
        while (iter.hasNext()) {
            OutputUtilities.newLine(sb);
            InnerEnum innerEnum = (InnerEnum) iter.next();
            sb.append(innerEnum.getFormattedContent(indentLevel));
            if (iter.hasNext()) {
                OutputUtilities.newLine(sb);
            }
        }
        
        indentLevel--;
        OutputUtilities.newLine(sb);
        OutputUtilities.javaIndent(sb, indentLevel);
        sb.append('}');
        
        return sb.toString();
    }
    
    /**
     * @return Returns the superInterfaces.
     */
    public Set getSuperInterfaceTypes() {
        return superInterfaceTypes;
    }

    public void addSuperInterface(FullyQualifiedJavaType superInterface) {
        superInterfaceTypes.add(superInterface);
    }

    /**
     * @return Returns the methods.
     */
    public List getMethods() {
        return methods;
    }

    public void addMethod(Method method) {
        methods.add(method);
    }

    /**
     * @return Returns the type.
     */
    public FullyQualifiedJavaType getType() {
        return type;
    }
}
