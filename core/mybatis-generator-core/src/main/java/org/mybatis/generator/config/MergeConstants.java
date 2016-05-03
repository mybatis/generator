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
package org.mybatis.generator.config;

/**
 * This class holds constants useful in the XML and Java merging operations.
 * 
 * @author Jeff Butler
 * 
 */
public class MergeConstants {

    /**
     * Utility class - no instances
     * 
     */
    private MergeConstants() {
    }

    public static final String[] OLD_XML_ELEMENT_PREFIXES = {
            "ibatorgenerated_", "abatorgenerated_" }; //$NON-NLS-1$ //$NON-NLS-2$

    public static final String NEW_ELEMENT_TAG = "@mbggenerated"; //$NON-NLS-1$
    public static final String[] OLD_ELEMENT_TAGS = {
            "@ibatorgenerated", "@abatorgenerated", "@mbggenerated" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

}
