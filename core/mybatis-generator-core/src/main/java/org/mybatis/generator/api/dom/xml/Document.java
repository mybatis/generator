/*
 *    Copyright 2006-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.api.dom.xml;

import org.jspecify.annotations.Nullable;

import java.util.Optional;

public class Document {

    private @Nullable DocType docType;

    private final XmlElement rootElement;

    public Document(String publicId, String systemId, XmlElement rootElement) {
        this(rootElement);
        docType = new PublicDocType(publicId, systemId);
    }

    public Document(String systemId, XmlElement rootElement) {
        this(rootElement);
        docType = new SystemDocType(systemId);
    }

    public Document(XmlElement rootElement) {
        this.rootElement = rootElement;
    }

    public XmlElement getRootElement() {
        return rootElement;
    }

    public Optional<DocType> getDocType() {
        return Optional.ofNullable(docType);
    }
}
