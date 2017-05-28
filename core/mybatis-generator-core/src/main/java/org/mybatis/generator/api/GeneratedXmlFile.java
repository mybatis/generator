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
package org.mybatis.generator.api;

import org.mybatis.generator.api.dom.xml.Document;

/**
 * The Class GeneratedXmlFile.
 *
 * @author Jeff Butler
 */
public class GeneratedXmlFile extends GeneratedFile {

    /** The document. */
    private Document document;

    /** The file name. */
    private String fileName;

    /** The target package. */
    private String targetPackage;

    /** The is mergeable. */
    private boolean isMergeable;

    /** The xml formatter. */
    private XmlFormatter xmlFormatter;

    /**
     * Instantiates a new generated xml file.
     *
     * @param document
     *            the document
     * @param fileName
     *            the file name
     * @param targetPackage
     *            the target package
     * @param targetProject
     *            the target project
     * @param isMergeable
     *            true if the file can be merged by the built in XML file merger.
     * @param xmlFormatter
     *            the xml formatter
     */
    public GeneratedXmlFile(Document document, String fileName,
            String targetPackage, String targetProject, boolean isMergeable,
            XmlFormatter xmlFormatter) {
        super(targetProject);
        this.document = document;
        this.fileName = fileName;
        this.targetPackage = targetPackage;
        this.isMergeable = isMergeable;
        this.xmlFormatter = xmlFormatter;
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.GeneratedFile#getFormattedContent()
     */
    @Override
    public String getFormattedContent() {
        return xmlFormatter.getFormattedContent(document);
    }

    /**
     * Gets the file name.
     *
     * @return Returns the fileName.
     */
    @Override
    public String getFileName() {
        return fileName;
    }

    /**
     * Gets the target package.
     *
     * @return Returns the targetPackage.
     */
    @Override
    public String getTargetPackage() {
        return targetPackage;
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.GeneratedFile#isMergeable()
     */
    @Override
    public boolean isMergeable() {
        return isMergeable;
    }

    public void setMergeable(boolean isMergeable) {
        this.isMergeable = isMergeable;
    }
}
