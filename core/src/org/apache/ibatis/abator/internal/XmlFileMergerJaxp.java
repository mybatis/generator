/*
 *  Copyright 2005 The Apache Software Foundation
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
package org.apache.ibatis.abator.internal;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.ibatis.abator.api.GeneratedXmlFile;
import org.apache.ibatis.abator.exception.ShellException;
import org.apache.ibatis.abator.internal.util.messages.Messages;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class handles the task of merging changes into an existing XML file.
 * 
 * @author Jeff Butler
 */
public class XmlFileMergerJaxp {
    private static class NullEntityResolver implements EntityResolver {
        /**
         * returns an empty reader. This is done so that the parser doesn't
         * attempt to read a DTD. We don't need that support for the merge and
         * it can cause problems on systems that aren't Internet connected.
         */
        public InputSource resolveEntity(String publicId, String systemId)
                throws SAXException, IOException {

            StringReader sr = new StringReader(""); //$NON-NLS-1$

            return new InputSource(sr);
        }
    }

    /**
     * Utility class - no instances allowed
     */
    private XmlFileMergerJaxp() {
        super();
    }

    public static String getMergedSource(GeneratedXmlFile generatedXmlFile,
            File existingFile) throws ShellException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            factory.setExpandEntityReferences(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new NullEntityResolver());

            Document existingDocument = builder.parse(existingFile);
            StringReader sr = new StringReader(generatedXmlFile
                    .getFormattedContent());
            Document newDocument = builder.parse(new InputSource(sr));

            DocumentType newDocType = newDocument.getDoctype();
            DocumentType existingDocType = existingDocument.getDoctype();

            if (!newDocType.getName().equals(existingDocType.getName())) {
                throw new ShellException(Messages.getString("Warning.12", //$NON-NLS-1$
                        existingFile.getName()));
            }

            Element existingRootElement = existingDocument.getDocumentElement();
            Element newRootElement = newDocument.getDocumentElement();

            // reconcile the namespace
            String namespace = newRootElement.getAttribute("namespace"); //$NON-NLS-1$
            existingRootElement.setAttribute("namespace", namespace); //$NON-NLS-1$

            // remove the old Abator generated elements and any
            // white space before the old abator nodes
            List nodesToDelete = new ArrayList();
            NodeList children = existingRootElement.getChildNodes();
            int length = children.getLength();
            for (int i = 0; i < length; i++) {
                Node node = children.item(i);
                if (isAnAbatorNode(node)) {
                    nodesToDelete.add(node);
                } else if (isWhiteSpace(node) && isAnAbatorNode(children.item(i + 1))) {
                    nodesToDelete.add(node);
                }
            }

            Iterator iter = nodesToDelete.iterator();
            while (iter.hasNext()) {
                existingRootElement.removeChild((Node) iter.next());
            }

            // add the new Abator generated elements
            children = newRootElement.getChildNodes();
            length = children.getLength();
            Node firstChild = existingRootElement.getFirstChild();
            for (int i = 0; i < length; i++) {
                Node node = children.item(i);
                // don't add the last node if it is only white space
                if (i == length - 1) {
                    if (isWhiteSpace(node)) {
                        break;
                    }
                }

                Node newNode = existingDocument.importNode(node, true);
                if (firstChild == null) {
                    existingRootElement.appendChild(newNode);
                } else {
                    existingRootElement.insertBefore(newNode, firstChild);
                }
            }

            // pretty print the result
            return prettyPrint(existingDocument);
        } catch (Exception e) {
            throw new ShellException(Messages.getString("Warning.13", //$NON-NLS-1$
                    existingFile.getName()), e);
        }
    }

    private static String prettyPrint(Document document) throws ShellException {
         DomWriter dw = new DomWriter();
         String s = dw.toString(document);
         return s;
    }

    private static boolean isAnAbatorNode(Node node) {
        boolean rc = false;

        if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            String id = element.getAttribute("id"); //$NON-NLS-1$
            if (id != null && id.startsWith("abatorgenerated_")) { //$NON-NLS-1$
                rc = true;
            }
        }

        return rc;
    }

    private static boolean isWhiteSpace(Node node) {
        boolean rc = false;

        if (node != null && node.getNodeType() == Node.TEXT_NODE) {
            Text tn = (Text) node;
            if (tn.getData().trim().length() == 0) {
                rc = true;
            }
        }

        return rc;
    }
}
