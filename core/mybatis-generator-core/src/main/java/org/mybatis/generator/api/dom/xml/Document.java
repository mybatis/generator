/**
 *    Copyright 2006-2018 the original author or authors.
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

import java.util.Optional;

public class Document {

    private DocType docType;

    private XmlElement rootElement;

    public Document(String publicId, String systemId) {
        super();
        docType = new PublicDocType(publicId, systemId);
    }

    public Document(String systemId) {
        super();
        docType = new SystemDocType(systemId);
    }

    public Document() {
        super();
    }

    public XmlElement getRootElement() {
        return rootElement;
    }

    public void setRootElement(XmlElement rootElement) {
        this.rootElement = rootElement;
    }
    
    public Optional<DocType> getDocType() {
        return Optional.ofNullable(docType);
    }
}
