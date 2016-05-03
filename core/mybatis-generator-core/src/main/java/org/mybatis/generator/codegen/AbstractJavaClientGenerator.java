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
package org.mybatis.generator.codegen;

/**
 * This class exists to that Java client generators can specify whether
 * an XML generator is required to match the methods in the
 * Java client.  For example, a Java client built entirely with
 * annotations does not need matching XML.
 * 
 * @author Jeff Butler
 *
 */
public abstract class AbstractJavaClientGenerator extends AbstractJavaGenerator {

    private boolean requiresXMLGenerator;
    
    public AbstractJavaClientGenerator(boolean requiresXMLGenerator) {
        super();
        this.requiresXMLGenerator = requiresXMLGenerator;
    }

    /**
     * @return true if matching XML is required
     */
    public boolean requiresXMLGenerator() {
        return requiresXMLGenerator;
    }
    
    /**
     * This method returns an instance of the XML generator associated
     * with this client generator.
     * 
     * @return the matched XML generator.  May return null if no
     * XML is required by this generator
     */
    public abstract AbstractXmlGenerator getMatchedXMLGenerator();
}
