/**
 *    Copyright 2006-2019 the original author or authors.
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
package org.mybatis.generator.api.dom.kotlin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KotlinType extends KotlinNamedItemContainer {

    private List<KotlinProperty> constructorProperties = new ArrayList<>();
    private Type type;
    private List<String> superTypes = new ArrayList<>();

    public enum Type {
        CLASS("class"), //$NON-NLS-1$
        INTERFACE("interface"), //$NON-NLS-1$
        OBJECT("object"); //$NON-NLS-1$

        private String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private KotlinType(Builder builder) {
        super(builder);
        this.type = Objects.requireNonNull(builder.type);
        constructorProperties.addAll(builder.constructorProperties);
        superTypes.addAll(builder.superTypes);
    }
    
    public List<KotlinProperty> getConstructorProperties() {
        return constructorProperties;
    }

    public Type getType() {
        return type;
    }

    public List<String> getSuperTypes() {
        return superTypes;
    }
    
    public void addConstructorProperty(KotlinProperty property) {
        constructorProperties.add(property);
    }

    @Override
    public <R> R accept(KotlinNamedItemVisitor<R> visitor) {
        return visitor.visit(this);
    }

    public static Builder newClass(String name) {
        return new Builder(Type.CLASS, name);
    }

    public static Builder newInterface(String name) {
        return new Builder(Type.INTERFACE, name);
    }

    public static Builder newObject(String name) {
        return new Builder(Type.OBJECT, name);
    }

    public static class Builder extends NamedItemContainerBuilder<Builder> {
        private Type type;
        private List<KotlinProperty> constructorProperties = new ArrayList<>();
        private List<String> superTypes = new ArrayList<>();

        private Builder(Type type, String name) {
            super(name);
            this.type = type;
        }

        public Builder withConstructorProperty(KotlinProperty kotlinProperty) {
            constructorProperties.add(kotlinProperty);
            return this;
        }

        public Builder withSuperType(String superType) {
            superTypes.add(superType);
            return this;
        }

        public Builder getThis() {
            return this;
        }

        public KotlinType build() {
            return new KotlinType(this);
        }
    }
}
