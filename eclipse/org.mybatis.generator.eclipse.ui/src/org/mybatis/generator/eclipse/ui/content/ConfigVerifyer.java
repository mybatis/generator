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
package org.mybatis.generator.eclipse.ui.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.resources.IFile;
import org.mybatis.generator.codegen.XmlConstants;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class does an elemental SAX parse to see if the given input
 * file represents a MyBatis Generator configuration file.
 * The tests performed include:
 * 
 * <ul>
 *   <li>Ensuring that the public ID is correct</li>
 *   <li>Ensuring that the root element is correct</li>
 * </ul>
 * 
 * @author Jeff Butler
 *
 */
public class ConfigVerifyer extends DefaultHandler {
    private File file;
    private boolean isConfig;
    private boolean rootElementRead;

    public ConfigVerifyer(IFile iFile) {
        this(iFile.getLocation().toFile());
    }

    public ConfigVerifyer(File file) {
        super();
        this.file = file;
        this.isConfig = false;
    }
    
    public boolean isConfigurationFile() {
        if (file == null) {
            return false;
        }
        
        String fileName = file.getName();
        if (fileName.length() > 4) {
            String extension = fileName.substring(fileName.length() - 4);
            if (!extension.equalsIgnoreCase(".xml")) { //$NON-NLS-1$
                return false;
            }
        } else {
            return false;
        }

        InputStream is;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return false;
        }
        
        
        boolean rc = isConfigFile(is);
        
        try {
            is.close();
        } catch (IOException e) {
            // ignore
        }
        
        return rc;
    }

    private boolean isConfigFile(InputStream inputStream) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
        
            parser.parse(inputStream, this);
        } catch (Exception e) {
            // ignore
        }
        
        return isConfig;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (rootElementRead) {
            // Root element was not correct
            throw new SAXException();
        }
        
        rootElementRead = true;
        
        if ("ibatorConfiguration".equals(qName)) { //$NON-NLS-1$
            isConfig = true;
            // ignore the rest of the file
            throw new SAXException();
        } else if  ("generatorConfiguration".equals(qName)) { //$NON-NLS-1$
            isConfig = true;
            // ignore the rest of the file
            throw new SAXException();
        }
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
        boolean hasCorrectDocType = false;
        
        if (XmlConstants.IBATOR_CONFIG_PUBLIC_ID.equals(publicId)) {
            hasCorrectDocType = true;
        } else if (XmlConstants.MYBATIS_GENERATOR_CONFIG_PUBLIC_ID.equals(publicId)) {
            hasCorrectDocType = true;
        }

        if (!hasCorrectDocType) {
            // Not a configuration file
            throw new SAXException();
        }
        
        // return a null InputSource - we don't want to go to the Internet
        StringReader nullStringReader = new StringReader(""); //$NON-NLS-1$
        return new InputSource(nullStringReader);
    }
}
