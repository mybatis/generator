/*
 *  Copyright 2008 The Apache Software Foundation
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

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * @author Jeff Butler
 * 
 */
public class CommentGeneratorConfiguration extends TypedPropertyHolder {

    /**
     * 
     */
    public CommentGeneratorConfiguration() {
    }

    public XmlElement toXmlElement() {
        XmlElement answer = new XmlElement("commentGenerator"); //$NON-NLS-1$
        if (getConfigurationType() != null) {
            answer.addAttribute(new Attribute("type", getConfigurationType())); //$NON-NLS-1$
        }

        addPropertyXmlElements(answer);

        return answer;
    }
}
