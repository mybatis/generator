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

package org.mybatis.generator.eclipse.ui.content;

import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.mybatis.generator.codegen.XmlConstants;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class does an elemental SAX parse to see if the given input
 * stream represents a MyBatis Generator configuration file.
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
    private InputStream inputStream;
    private boolean isConfig;
    private boolean rootElementRead;

    /**
     * 
     */
    public ConfigVerifyer(InputStream inputStream) {
        super();
        this.inputStream = inputStream;
        this.isConfig = false;
    }
    
    public boolean isConfigFile() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
        
            parser.parse(inputStream, this);
        } catch (Exception e) {
            // ignore
            ;
        }
        
        return isConfig;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (rootElementRead) {
            throw new SAXException("Root element was not correct");
        }
        
        rootElementRead = true;
        
        if ("ibatorConfiguration".equals(qName)) {
            isConfig = true;
            throw new SAXException("Ignore the rest of the file");
        } else if  ("generatorConfiguration".equals(qName)) {
            isConfig = true;
            throw new SAXException("Ignore the rest of the file");
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
            throw new SAXException("Not a configuration file");
        }
        
        // return a null InputSource - we don't want to go to the Internet
        StringReader nullStringReader = new StringReader("");
        return new InputSource(nullStringReader);
    }
}
