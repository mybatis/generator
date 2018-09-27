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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractJavaType extends JavaElement {

    private FullyQualifiedJavaType type;

    private Set<FullyQualifiedJavaType> superInterfaceTypes = new LinkedHashSet<>();

    private List<InnerClass> innerClasses = new ArrayList<>();

    private List<InnerEnum> innerEnums = new ArrayList<>();

    private List<InnerInterface> innerInterfaces = new ArrayList<>();

    private List<Field> fields = new ArrayList<>();

    private List<Method> methods = new ArrayList<>();
    
    public AbstractJavaType(FullyQualifiedJavaType type) {
        this.type = type;
    }

    public AbstractJavaType(String type) {
        this.type = new FullyQualifiedJavaType(type);
    }

    public List<InnerClass> getInnerClasses() {
        return innerClasses;
    }

    public void addInnerClass(InnerClass innerClass) {
        innerClasses.add(innerClass);
    }

    public List<InnerEnum> getInnerEnums() {
        return innerEnums;
    }

    public void addInnerEnum(InnerEnum innerEnum) {
        innerEnums.add(innerEnum);
    }

    public List<InnerInterface> getInnerInterfaces() {
        return innerInterfaces;
    }

    public void addInnerInterface(InnerInterface innerInterface) {
        innerInterfaces.add(innerInterface);
    }

    public List<Field> getFields() {
        return fields;
    }

    public void addField(Field field) {
        fields.add(field);
    }

    public List<Method> getMethods() {
        return methods;
    }

    public void addMethod(Method method) {
        methods.add(method);
    }

    public void addSuperInterface(FullyQualifiedJavaType superInterface) {
        superInterfaceTypes.add(superInterface);
    }

    public FullyQualifiedJavaType getType() {
        return type;
    }

    public Set<FullyQualifiedJavaType> getSuperInterfaceTypes() {
        return superInterfaceTypes;
    }
}
