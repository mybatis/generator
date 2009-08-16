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
package org.apache.ibatis.abator.api.dom.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.ibatis.abator.api.dom.OutputUtilities;

/**
 * @author Jeff Butler
 */
public class XmlElement extends Element {
    private List attributes;

    private List elements;

    private String name;

    /**
     *  
     */
    public XmlElement(String name) {
        super();
        attributes = new ArrayList();
        elements = new ArrayList();
        this.name = name;
    }

    /**
     * @return Returns the attributes.
     */
    public List getAttributes() {
        return attributes;
    }

    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    /**
     * @return Returns the elements.
     */
    public List getElements() {
        return elements;
    }

    public void addElement(Element element) {
        elements.add(element);
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ibatis.abator.api.dom.xml.Element#getFormattedContent(int)
     */
    public String getFormattedContent(int indentLevel) {
        StringBuffer sb = new StringBuffer();

        OutputUtilities.xmlIndent(sb, indentLevel);
        sb.append('<');
        sb.append(name);

        Iterator iter = attributes.iterator();
        while (iter.hasNext()) {
            Attribute att = (Attribute) iter.next();
            sb.append(' ');
            sb.append(att.getFormattedContent());
        }

        if (elements.size() > 0) {
            sb.append(" >"); //$NON-NLS-1$
            iter = elements.iterator();
            while (iter.hasNext()) {
                Element element = (Element) iter.next();
                OutputUtilities.newLine(sb);
                sb.append(element.getFormattedContent(indentLevel + 1));
            }
            OutputUtilities.newLine(sb);
            OutputUtilities.xmlIndent(sb, indentLevel);
            sb.append("</"); //$NON-NLS-1$
            sb.append(name);
            sb.append('>');

        } else {
            sb.append(" />"); //$NON-NLS-1$
        }

        return sb.toString();
    }
}
