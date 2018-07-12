/**
 *    Copyright 2006-2017 the original author or authors.
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
package org.mybatis.generator.api.dom.xml;

import org.mybatis.generator.api.dom.OutputUtilities;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.w3c.dom.Node.*;

/**
 * The Class Document.
 *
 * @author Jeff Butler
 */
public class Document {

    /** The public id. */
    private String publicId;

    /** The system id. */
    private String systemId;

    /** The root element. */
    private XmlElement rootElement;

    /**
     * Instantiates a new document.
     *
     * @param publicId
     *            the public id
     * @param systemId
     *            the system id
     */
    public Document(String publicId, String systemId) {
        super();
        this.publicId = publicId;
        this.systemId = systemId;
    }

    /**
     * Instantiates a new document.
     *
     * @param document
     *            w3c document
     */
    public Document(org.w3c.dom.Document document) {
        super();
        this.systemId = document.getDoctype().getSystemId();
        this.publicId = document.getDoctype().getPublicId();
        this.rootElement = toXmlElement(document.getDocumentElement());
    }

    /**
     * Instantiates a new document.
     */
    public Document() {
        super();
    }

    /**
     * Gets the root element.
     *
     * @return Returns the rootElement.
     */
    public XmlElement getRootElement() {
        return rootElement;
    }

    /**
     * Sets the root element.
     *
     * @param rootElement
     *            The rootElement to set.
     */
    public void setRootElement(XmlElement rootElement) {
        this.rootElement = rootElement;
    }

    /**
     * Gets the public id.
     *
     * @return Returns the publicId.
     */
    public String getPublicId() {
        return publicId;
    }

    /**
     * Gets the system id.
     *
     * @return Returns the systemId.
     */
    public String getSystemId() {
        return systemId;
    }

    /**
     * Gets the formatted content.
     *
     * @return the formatted content
     */
    public String getFormattedContent() {
        StringBuilder sb = new StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"); //$NON-NLS-1$

        if (publicId != null && systemId != null) {
            OutputUtilities.newLine(sb);
            sb.append("<!DOCTYPE "); //$NON-NLS-1$
            sb.append(rootElement.getName());
            sb.append(" PUBLIC \""); //$NON-NLS-1$
            sb.append(publicId);
            sb.append("\" \""); //$NON-NLS-1$
            sb.append(systemId);
            sb.append("\">"); //$NON-NLS-1$
        }

        OutputUtilities.newLine(sb);
        sb.append(rootElement.getFormattedContent(0));

        return sb.toString();
    }

    /**
     * Converts to xml element
     * @param node
     *            w3c node
     * @return Returns the xml element.
     */
    private XmlElement toXmlElement(Node node) {
        if (node.getNodeType() != ELEMENT_NODE) {
            throw new IllegalArgumentException("this node must be an element node");
        }
        XmlElement element = new XmlElement(node.getNodeName());
        NamedNodeMap attrs = node.getAttributes();
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                Node attr = attrs.item(i);
                element.addAttribute(new Attribute(attr.getNodeName(), attr.getTextContent()));
            }
        }
        if (!node.hasChildNodes()) {
            return element;
        }
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node curNode = childNodes.item(i);
            switch (curNode.getNodeType()) {
                case ELEMENT_NODE:
                    element.addElement(toXmlElement(curNode));
                    break;
                case CDATA_SECTION_NODE: {
                    element.addElement(new TextElement("<![CDATA["));
                    String content = curNode.getTextContent();
                    for (String line : content.split(content.substring(content.lastIndexOf('\n')))) {
                        if (line.length() > 0) {
                            element.addElement(new TextElement(line));
                        }
                    }
                    element.addElement(new TextElement("]]>"));
                }
                break;
                case COMMENT_NODE: {
                    element.addElement(new TextElement("<!--"));
                    String content = curNode.getTextContent();
                    for (String line : content.split(content.substring(content.lastIndexOf('\n')))) {
                        if (line.length() > 0) {
                            element.addElement(new TextElement(line));
                        }
                    }
                    element.addElement(new TextElement("-->"));
                }
                break;
                case TEXT_NODE: {
                    String content = curNode.getTextContent();
                    while (!content.matches("^(.|\n)*\n\\s*$")) {
                        Node nextNode = curNode.getNextSibling();
                        if (nextNode != null) {
                            curNode = curNode.getNextSibling();
                            if (curNode.getNodeType() == CDATA_SECTION_NODE) {
                                content += "<![CDATA[" + curNode.getTextContent() + "]]>";
                            } else {
                                content += curNode.getTextContent();
                            }
                            i++;
                        } else {
                            break;
                        }
                    }
                    for (String line : content.split("\n")) {
                        line = line.trim();
                        if (line.length() > 0) {
                            element.addElement(new TextElement(line));
                        }
                    }
                }
                break;
                default:
                    element.addElement(new TextElement(curNode.getTextContent()));
                    break;
            }
        }
        return element;
    }
}
