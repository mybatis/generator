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
package org.mybatis.generator.api.dom.java;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.dom.java.render.TypeParameterRenderer;

public class TypeParameter {

    private String name;

    private List<FullyQualifiedJavaType> extendsTypes = new ArrayList<>();

    public TypeParameter(String name) {
        super();
        this.name = name;
    }

    public TypeParameter(String name, List<FullyQualifiedJavaType> extendsTypes) {
        super();
        this.name = name;
        this.extendsTypes.addAll(extendsTypes);
    }

    public String getName() {
        return name;
    }

    public List<FullyQualifiedJavaType> getExtendsTypes() {
        return extendsTypes;
    }

    @Override
    public String toString() {
        return new TypeParameterRenderer().render(this, null);
    }
}
