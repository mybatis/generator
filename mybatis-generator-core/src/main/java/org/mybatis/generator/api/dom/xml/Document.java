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
package org.mybatis.generator.api.dom.xml;

import org.mybatis.generator.api.dom.OutputUtilities;

/**
 * @author Jeff Butler
 */
public class Document {
    private String publicId;

    private String systemId;

    private XmlElement rootElement;

    /**
     *  
     */
    public Document(String publicId, String systemId) {
        super();
        this.publicId = publicId;
        this.systemId = systemId;
    }

    public Document() {
        super();
    }

    /**
     * @return Returns the rootElement.
     */
    public XmlElement getRootElement() {
        return rootElement;
    }

    /**
     * @param rootElement
     *            The rootElement to set.
     */
    public void setRootElement(XmlElement rootElement) {
        this.rootElement = rootElement;
    }

    /**
     * @return Returns the publicId.
     */
    public String getPublicId() {
        return publicId;
    }

    /**
     * @return Returns the systemId.
     */
    public String getSystemId() {
        return systemId;
    }

    public String getFormattedContent() {
        StringBuilder sb = new StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"); //$NON-NLS-1$

        if (publicId != null && systemId != null) {
            OutputUtilities.newLine(sb);
            sb.append("<!DOCTYPE "); //$NON-NLS-1$
            sb.append(rootElement.getName());
            sb.append(" PUBLIC \""); //$NON-NLS-1$
            sb.append(publicId);
            sb.append("\" \""); //$NON-NLS-1$
            sb.append(systemId);
            sb.append("\" >"); //$NON-NLS-1$
        }

        OutputUtilities.newLine(sb);
        sb.append(rootElement.getFormattedContent(0));

        return sb.toString();
    }
}
