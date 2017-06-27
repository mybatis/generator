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
package org.mybatis.generator.api.dom.xml;

import org.mybatis.generator.api.dom.OutputUtilities;

/**
 * The Class TextElement.
 *
 * @author Jeff Butler
 */
public class TextElement extends Element {

    /** The content. */
    private String content;

    /**
     * Instantiates a new text element.
     *
     * @param content
     *            the content
     */
    public TextElement(String content) {
        super();
        this.content = content;
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.dom.xml.Element#getFormattedContent(int)
     */
    @Override
    public String getFormattedContent(int indentLevel) {
        StringBuilder sb = new StringBuilder();
        OutputUtilities.xmlIndent(sb, indentLevel);
        sb.append(content);
        return sb.toString();
    }

    /**
     * Gets the content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }
}
