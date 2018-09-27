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
package org.mybatis.generator.api.dom.xml.render;

import org.mybatis.generator.api.dom.xml.DocTypeVisitor;
import org.mybatis.generator.api.dom.xml.PublicDocType;
import org.mybatis.generator.api.dom.xml.SystemDocType;

public class DocTypeRenderer implements DocTypeVisitor<String> {

    @Override
    public String visit(PublicDocType docType) {
        return "PUBLIC \"" //$NON-NLS-1$
                + docType.getDtdName()
                + "\" \"" //$NON-NLS-1$
                + docType.getDtdLocation()
                + "\""; //$NON-NLS-1$
    }

    @Override
    public String visit(SystemDocType docType) {
        return "SYSTEM \"" //$NON-NLS-1$
                + docType.getDtdLocation()
                + "\""; //$NON-NLS-1$
    }
}
