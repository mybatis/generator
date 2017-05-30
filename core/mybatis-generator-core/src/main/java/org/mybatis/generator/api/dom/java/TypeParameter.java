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
package org.mybatis.generator.api.dom.java;

import java.util.ArrayList;
import java.util.List;

public class TypeParameter {

    private String name;

    private List<FullyQualifiedJavaType> extendsTypes;

    public TypeParameter(String name) {
        this(name, new ArrayList<FullyQualifiedJavaType>());
    }

    public TypeParameter(String name, List<FullyQualifiedJavaType> extendsTypes) {
        super();
        this.name = name;
        this.extendsTypes = extendsTypes;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Returns the extends types.
     */
    public List<FullyQualifiedJavaType> getExtendsTypes() {
        return extendsTypes;
    }

    public String getFormattedContent(CompilationUnit compilationUnit) {
        StringBuilder sb = new StringBuilder();

        sb.append(name);
        if (!extendsTypes.isEmpty()) {

            sb.append(" extends "); //$NON-NLS-1$
            boolean addAnd = false;
            for (FullyQualifiedJavaType type : extendsTypes) {
                if (addAnd) {
                    sb.append(" & "); //$NON-NLS-1$
                } else {
                    addAnd = true;
                }
                sb.append(JavaDomUtils.calculateTypeName(compilationUnit, type));
            }
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return getFormattedContent(null);
    }
}
