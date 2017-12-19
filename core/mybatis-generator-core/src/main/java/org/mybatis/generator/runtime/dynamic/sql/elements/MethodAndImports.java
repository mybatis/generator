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
package org.mybatis.generator.runtime.dynamic.sql.elements;

import java.util.HashSet;
import java.util.Set;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Method;

public class MethodAndImports {

    private Method method;
    private Set<FullyQualifiedJavaType> imports;
    private Set<String> staticImports;
    
    private MethodAndImports(Builder builder) {
        method = builder.method;
        imports = builder.imports;
        staticImports = builder.staticImports;
    }
    
    public Method getMethod() {
        return method;
    }
    
    public Set<FullyQualifiedJavaType> getImports() {
        return imports;
    }
    
    public Set<String> getStaticImports() {
        return staticImports;
    }
    
    public static Builder withMethod(Method method) {
        return new Builder().withMethod(method);
    }
    
    public static class Builder {
        private Method method;
        private Set<FullyQualifiedJavaType> imports = new HashSet<FullyQualifiedJavaType>();
        private Set<String> staticImports = new HashSet<String>();
        
        public Builder withMethod(Method method) {
            this.method = method;
            return this;
        }
        
        public Builder withImport(FullyQualifiedJavaType importedType) {
            this.imports.add(importedType);
            return this;
        }
        
        public Builder withImports(Set<FullyQualifiedJavaType> imports) {
            this.imports.addAll(imports);
            return this;
        }
        
        public Builder withStaticImport(String staticImport) {
            this.staticImports.add(staticImport);
            return this;
        }

        public Builder withStaticImports(Set<String> staticImports) {
            this.staticImports.addAll(staticImports);
            return this;
        }

        public MethodAndImports build() {
            return new MethodAndImports(this);
        }
    }
}
