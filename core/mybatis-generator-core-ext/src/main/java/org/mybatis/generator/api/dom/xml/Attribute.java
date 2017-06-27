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

/**
 * The Class Attribute.
 *
 * @author Jeff Butler
 */
public class Attribute implements Comparable<Attribute> {

    /** The name. */
    private String name;

    /** The value. */
    private String value;

    /**
     * Instantiates a new attribute.
     *
     * @param name
     *            the name
     * @param value
     *            the value
     */
    public Attribute(String name, String value) {
        super();
        this.name = name;
        this.value = value;
    }

    /**
     * Gets the name.
     *
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the value.
     *
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the formatted content.
     *
     * @return the formatted content
     */
    public String getFormattedContent() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append("=\""); //$NON-NLS-1$
        sb.append(value);
        sb.append('\"');

        return sb.toString();
    }

    @Override
    public int compareTo(Attribute o) {
        if (this.name == null) {
            return o.name == null ? 0 : -1;
        } else {
            if (o.name == null) {
                return 0;
            } else {
                return this.name.compareTo(o.name);
            }
        }
    }
}
