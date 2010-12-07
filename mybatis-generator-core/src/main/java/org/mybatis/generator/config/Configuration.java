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
package org.mybatis.generator.config;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.exception.InvalidConfigurationException;

/**
 * 
 * @author Jeff Butler
 */
public class Configuration {

    private List<Context> contexts;
    private List<String> classPathEntries;

    public Configuration() {
        super();
        contexts = new ArrayList<Context>();
        classPathEntries = new ArrayList<String>();
    }

    public void addClasspathEntry(String entry) {
        classPathEntries.add(entry);
    }

    /**
     * @return Returns the classPathEntries.
     */
    public List<String> getClassPathEntries() {
        return classPathEntries;
    }

    /**
     * This method does a simple validate, it makes sure that all required
     * fields have been filled in and that all implementation classes exist and
     * are of the proper type. It does not do any more complex operations such
     * as: validating that database tables exist or validating that named
     * columns exist
     */
    public void validate() throws InvalidConfigurationException {
        List<String> errors = new ArrayList<String>();

        for (String classPathEntry : classPathEntries) {
            if (!stringHasValue(classPathEntry)) {
                errors.add(getString("ValidationError.19")); //$NON-NLS-1$
                // only need to state this error once
                break;
            }
        }

        if (contexts.size() == 0) {
            errors.add(getString("ValidationError.11")); //$NON-NLS-1$
        } else {
            for (Context context : contexts) {
                context.validate(errors);
            }
        }

        if (errors.size() > 0) {
            throw new InvalidConfigurationException(errors);
        }
    }

    public List<Context> getContexts() {
        return contexts;
    }

    public void addContext(Context context) {
        contexts.add(context);
    }

    public Context getContext(String id) {
        for (Context context : contexts) {
            if (id.equals(context.getId())) {
                return context;
            }
        }

        return null;
    }

    /**
     * Builds an XML representation of this configuration. This can be used to
     * persist a programtically generated configuration.
     * 
     * @return the XML representation of this configuration
     */
    public Document toDocument() {
        // note that this method will not reconstruct a properties
        // element - that element is only used in XML parsing

        Document document = new Document(
                XmlConstants.MYBATIS_GENERATOR_CONFIG_PUBLIC_ID,
                XmlConstants.MYBATIS_GENERATOR_CONFIG_SYSTEM_ID);
        XmlElement rootElement = new XmlElement("generatorConfiguration"); //$NON-NLS-1$
        document.setRootElement(rootElement);

        for (String classPathEntry : classPathEntries) {
            XmlElement cpeElement = new XmlElement("classPathEntry"); //$NON-NLS-1$
            cpeElement.addAttribute(new Attribute("location", classPathEntry)); //$NON-NLS-1$
            rootElement.addElement(cpeElement);
        }

        for (Context context : contexts) {
            rootElement.addElement(context.toXmlElement());
        }

        return document;
    }
}
