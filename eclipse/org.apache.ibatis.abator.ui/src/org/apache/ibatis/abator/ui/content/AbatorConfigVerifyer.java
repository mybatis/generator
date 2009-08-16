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

package org.apache.ibatis.abator.ui.content;

import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.ibatis.abator.internal.sqlmap.XmlConstants;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class does an elemental SAX parse to see if the given input
 * stream represents an Abator configuration file.  The tests performed include:
 * 
 * <ul>
 *   <li>Ensuring that the public ID is correct</li>
 *   <li>Ensuring that the root element is abatorConfiguration</li>
 * </ul>
 * 
 * @author Jeff Butler
 *
 */
public class AbatorConfigVerifyer extends DefaultHandler {
    private InputStream inputStream;
    private boolean isAbatorConfig;
    private boolean rootElementRead;

    /**
     * 
     */
    public AbatorConfigVerifyer(InputStream inputStream) {
        super();
        this.inputStream = inputStream;
    }
    
    public boolean isAbatorConfigFile() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
        
            parser.parse(inputStream, this);
        } catch (Exception e) {
            // ignore
            ;
        }
        
        return isAbatorConfig;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (rootElementRead) {
            throw new SAXException("Root element was not abatorConfiguration");
        }
        
        rootElementRead = true;
        
        if ("abatorConfiguration".equals(qName)) {
            isAbatorConfig = true;
            throw new SAXException("Ignore the rest of the file");
        }
    }

    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
        if (!XmlConstants.ABATOR_CONFIG_PUBLIC_ID.equals(publicId)) {
            throw new SAXException("Not an Abator configuration file");
        }
        
        // return a null InpputSource - we don't want to go to the Internet
        StringReader nullStringReader = new StringReader("");
        return new InputSource(nullStringReader);
    }
}
