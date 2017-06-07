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
package org.mybatis.generator.internal;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.mybatis.generator.exception.ShellException;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * This class is used to generate a String representation of an XML document. It
 * is very much based on the class dom.Writer from the Apache Xerces examples,
 * but I've simplified and updated it.
 * 
 * @author Andy Clark, IBM (Original work)
 * @author Jeff Butler (derivation)
 */
public class DomWriter {

    protected PrintWriter printWriter;

    protected boolean isXML11;

    public DomWriter() {
        super();
    }

    public synchronized String toString(Document document)
            throws ShellException {
        StringWriter sw = new StringWriter();
        printWriter = new PrintWriter(sw);
        write(document);
        String s = sw.toString();
        return s;
    }

    protected Attr[] sortAttributes(NamedNodeMap attrs) {

        int len = (attrs != null) ? attrs.getLength() : 0;
        Attr[] array = new Attr[len];
        for (int i = 0; i < len; i++) {
            array[i] = (Attr) attrs.item(i);
        }
        for (int i = 0; i < len - 1; i++) {
            String name = array[i].getNodeName();
            int index = i;
            for (int j = i + 1; j < len; j++) {
                String curName = array[j].getNodeName();
                if (curName.compareTo(name) < 0) {
                    name = curName;
                    index = j;
                }
            }
            if (index != i) {
                Attr temp = array[i];
                array[i] = array[index];
                array[index] = temp;
            }
        }

        return array;

    }

    protected void normalizeAndPrint(String s, boolean isAttValue) {

        int len = (s != null) ? s.length() : 0;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            normalizeAndPrint(c, isAttValue);
        }
    }

    protected void normalizeAndPrint(char c, boolean isAttValue) {

        switch (c) {
        case '<': {
            printWriter.print("&lt;"); //$NON-NLS-1$
            break;
        }
        case '>': {
            printWriter.print("&gt;"); //$NON-NLS-1$
            break;
        }
        case '&': {
            printWriter.print("&amp;"); //$NON-NLS-1$
            break;
        }
        case '"': {
            // A '"' that appears in character data
            // does not need to be escaped.
            if (isAttValue) {
                printWriter.print("&quot;"); //$NON-NLS-1$
            } else {
                printWriter.print('"');
            }
            break;
        }
        case '\r': {
            // If CR is part of the document's content, it
            // must be printed as a literal otherwise
            // it would be normalized to LF when the document
            // is reparsed.
            printWriter.print("&#xD;"); //$NON-NLS-1$
            break;
        }
        case '\n': {
            // If LF is part of the document's content, it
            // should be printed back out with the system default
            // line separator.  XML parsing forces \n only after a parse,
            // but we should write it out as it was to avoid whitespace
            // commits on some version control systems.
            printWriter.print(System.getProperty("line.separator")); //$NON-NLS-1$
            break;
        }
        default: {
            // In XML 1.1, control chars in the ranges [#x1-#x1F, #x7F-#x9F]
            // must be escaped.
            //
            // Escape space characters that would be normalized to #x20 in
            // attribute values
            // when the document is reparsed.
            //
            // Escape NEL (0x85) and LSEP (0x2028) that appear in content
            // if the document is XML 1.1, since they would be normalized to LF
            // when the document is reparsed.
            if (isXML11
                    && ((c >= 0x01 && c <= 0x1F && c != 0x09 && c != 0x0A)
                            || (c >= 0x7F && c <= 0x9F) || c == 0x2028)
                    || isAttValue && (c == 0x09 || c == 0x0A)) {
                printWriter.print("&#x"); //$NON-NLS-1$
                printWriter.print(Integer.toHexString(c).toUpperCase());
                printWriter.print(';');
            } else {
                printWriter.print(c);
            }
        }
        }
    }

    /**
     * Extracts the XML version from the Document.
     *
     * @param document
     *            the document
     * @return the version
     */
    protected String getVersion(Document document) {
        if (document == null) {
            return null;
        }

        return document.getXmlVersion();
    }

    protected void writeAnyNode(Node node) throws ShellException {
        // is there anything to do?
        if (node == null) {
            return;
        }

        short type = node.getNodeType();
        switch (type) {
        case Node.DOCUMENT_NODE:
            write((Document) node);
            break;

        case Node.DOCUMENT_TYPE_NODE:
            write((DocumentType) node);
            break;

        case Node.ELEMENT_NODE:
            write((Element) node);
            break;

        case Node.ENTITY_REFERENCE_NODE:
            write((EntityReference) node);
            break;

        case Node.CDATA_SECTION_NODE:
            write((CDATASection) node);
            break;

        case Node.TEXT_NODE:
            write((Text) node);
            break;

        case Node.PROCESSING_INSTRUCTION_NODE:
            write((ProcessingInstruction) node);
            break;

        case Node.COMMENT_NODE:
            write((Comment) node);
            break;

        default:
            throw new ShellException(getString(
                    "RuntimeError.18", Short.toString(type))); //$NON-NLS-1$
        }
    }

    protected void write(Document node) throws ShellException {
        isXML11 = "1.1".equals(getVersion(node)); //$NON-NLS-1$
        if (isXML11) {
            printWriter.println("<?xml version=\"1.1\" encoding=\"UTF-8\"?>"); //$NON-NLS-1$
        } else {
            printWriter.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"); //$NON-NLS-1$
        }
        printWriter.flush();
        write(node.getDoctype());
        write(node.getDocumentElement());
    }

    protected void write(DocumentType node) throws ShellException {
        printWriter.print("<!DOCTYPE "); //$NON-NLS-1$
        printWriter.print(node.getName());
        String publicId = node.getPublicId();
        String systemId = node.getSystemId();
        if (publicId != null) {
            printWriter.print(" PUBLIC \""); //$NON-NLS-1$
            printWriter.print(publicId);
            printWriter.print("\" \""); //$NON-NLS-1$
            printWriter.print(systemId);
            printWriter.print('\"');
        } else if (systemId != null) {
            printWriter.print(" SYSTEM \""); //$NON-NLS-1$
            printWriter.print(systemId);
            printWriter.print('"');
        }

        String internalSubset = node.getInternalSubset();
        if (internalSubset != null) {
            printWriter.println(" ["); //$NON-NLS-1$
            printWriter.print(internalSubset);
            printWriter.print(']');
        }
        printWriter.println('>');
    }

    protected void write(Element node) throws ShellException {
        printWriter.print('<');
        printWriter.print(node.getNodeName());
        Attr[] attrs = sortAttributes(node.getAttributes());
        for (Attr attr : attrs) {
            printWriter.print(' ');
            printWriter.print(attr.getNodeName());
            printWriter.print("=\""); //$NON-NLS-1$
            normalizeAndPrint(attr.getNodeValue(), true);
            printWriter.print('"');
        }

        if (node.getChildNodes().getLength() == 0) {
            printWriter.print(" />"); //$NON-NLS-1$
            printWriter.flush();
        } else {
            printWriter.print('>');
            printWriter.flush();

            Node child = node.getFirstChild();
            while (child != null) {
                writeAnyNode(child);
                child = child.getNextSibling();
            }

            printWriter.print("</"); //$NON-NLS-1$
            printWriter.print(node.getNodeName());
            printWriter.print('>');
            printWriter.flush();
        }
    }

    protected void write(EntityReference node) {
        printWriter.print('&');
        printWriter.print(node.getNodeName());
        printWriter.print(';');
        printWriter.flush();
    }

    protected void write(CDATASection node) {
        printWriter.print("<![CDATA["); //$NON-NLS-1$
        String data = node.getNodeValue();
        // XML parsers normalize line endings to '\n'.  We should write
        // it out as it was in the original to avoid whitespace commits
        // on some version control systems
        int len = (data != null) ? data.length() : 0;
        for (int i = 0; i < len; i++) {
            char c = data.charAt(i);
            if (c == '\n') {
                printWriter.print(System.getProperty("line.separator")); //$NON-NLS-1$
            } else {
                printWriter.print(c);
            }
        }
        printWriter.print("]]>"); //$NON-NLS-1$
        printWriter.flush();
    }

    protected void write(Text node) {
        normalizeAndPrint(node.getNodeValue(), false);
        printWriter.flush();
    }

    protected void write(ProcessingInstruction node) {
        printWriter.print("<?"); //$NON-NLS-1$
        printWriter.print(node.getNodeName());
        String data = node.getNodeValue();
        if (data != null && data.length() > 0) {
            printWriter.print(' ');
            printWriter.print(data);
        }
        printWriter.print("?>"); //$NON-NLS-1$
        printWriter.flush();
    }

    protected void write(Comment node) {
        printWriter.print("<!--"); //$NON-NLS-1$
        String comment = node.getNodeValue();
        if (comment != null && comment.length() > 0) {
            normalizeAndPrint(comment, false);
        }
        printWriter.print("-->"); //$NON-NLS-1$
        printWriter.flush();
    }
}
