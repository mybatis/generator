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

package org.apache.ibatis.ibator.api.dom.java;

/**
 * Encapsulates the idea of a wildcard type to be used in
 * a type argument list
 * 
 * @author Jeff Butler
 */
public class JavaWildcardType extends FullyQualifiedJavaType {
    private boolean extendsType;

    /**
     * @param fullyQualifiedName the fully qualified base type name
     * @param extendsType if true, this is an "extends" wildcard, else it is
     *  a "super" wildcard
     */
    public JavaWildcardType(String fullyQualifiedName, boolean extendsType) {
        super(fullyQualifiedName);
        this.extendsType = extendsType;
    }

    @Override
    public String getShortName() {
        StringBuilder sb = new StringBuilder();
        if (extendsType) {
            sb.append("? extends "); //$NON-NLS-1$
        } else {
            sb.append("? super "); //$NON-NLS-1$
        }
        sb.append(super.getShortName());

        return sb.toString();
    }
}
