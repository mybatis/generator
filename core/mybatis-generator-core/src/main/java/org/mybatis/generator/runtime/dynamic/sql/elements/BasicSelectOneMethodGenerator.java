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
package org.mybatis.generator.runtime.dynamic.sql.elements;

import java.util.HashSet;
import java.util.Set;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;

public class BasicSelectOneMethodGenerator extends AbstractMethodGenerator {
    
    private FullyQualifiedJavaType recordType;
    private String resultMapId;
    private FragmentGenerator fragmentGenerator;
    
    private BasicSelectOneMethodGenerator(Builder builder) {
        super(builder);
        recordType = builder.recordType;
        resultMapId = builder.resultMapId;
        fragmentGenerator = builder.fragmentGenerator;
    }

    @Override
    public MethodAndImports generateMethodAndImports() {
        if (!introspectedTable.getRules().generateSelectByPrimaryKey()) {
            return null;
        }
        
        Set<FullyQualifiedJavaType> imports = new HashSet<>();
        
        boolean reuseResultMap = introspectedTable.getRules().generateSelectByExampleWithBLOBs()
                || introspectedTable.getRules().generateSelectByExampleWithoutBLOBs();
                
        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType("org.mybatis.dynamic.sql.select.render.SelectStatementProvider"); //$NON-NLS-1$
        FullyQualifiedJavaType adapter = new FullyQualifiedJavaType("org.mybatis.dynamic.sql.util.SqlProviderAdapter"); //$NON-NLS-1$
        FullyQualifiedJavaType annotation = new FullyQualifiedJavaType("org.apache.ibatis.annotations.SelectProvider"); //$NON-NLS-1$
        
        imports.add(parameterType);
        imports.add(adapter);
        imports.add(annotation);
        
        Method method = new Method("selectOne"); //$NON-NLS-1$
        method.setAbstract(true);

        imports.add(recordType);
        method.setReturnType(recordType);
        method.addParameter(new Parameter(parameterType, "selectStatement")); //$NON-NLS-1$
        context.getCommentGenerator().addGeneralMethodAnnotation(method, introspectedTable, imports);
        method.addAnnotation("@SelectProvider(type=SqlProviderAdapter.class, method=\"select\")"); //$NON-NLS-1$
        
        MethodAndImports.Builder builder = MethodAndImports.withMethod(method)
                .withImports(imports);

        if (introspectedTable.isConstructorBased()) {
            MethodParts methodParts = fragmentGenerator.getAnnotatedConstructorArgs();
            acceptParts(builder, method, methodParts);
        } else {
            if (reuseResultMap) {
                FullyQualifiedJavaType rmAnnotation = new FullyQualifiedJavaType("org.apache.ibatis.annotations.ResultMap"); //$NON-NLS-1$
                builder.withImport(rmAnnotation);
                method.addAnnotation("@ResultMap(\"" + resultMapId + "\")"); //$NON-NLS-1$ //$NON-NLS-2$
            } else {
                MethodParts methodParts = fragmentGenerator.getAnnotatedResults();
                acceptParts(builder, method, methodParts);
            }
        }

        return builder.build();
    }

    @Override
    public boolean callPlugins(Method method, Interface interfaze) {
        return context.getPlugins().clientBasicSelectOneMethodGenerated(method, interfaze, introspectedTable);
    }

    public static class Builder extends BaseBuilder<Builder, BasicSelectOneMethodGenerator> {

        private FullyQualifiedJavaType recordType;
        private String resultMapId;
        private FragmentGenerator fragmentGenerator;
        
        public Builder withRecordType(FullyQualifiedJavaType recordType) {
            this.recordType = recordType;
            return this;
        }
        
        public Builder withResultMapId(String resultMapId) {
            this.resultMapId = resultMapId;
            return this;
        }
        
        public Builder withFragmentGenerator(FragmentGenerator fragmentGenerator) {
            this.fragmentGenerator = fragmentGenerator;
            return this;
        }
        
        @Override
        public Builder getThis() {
            return this;
        }

        @Override
        public BasicSelectOneMethodGenerator build() {
            return new BasicSelectOneMethodGenerator(this);
        }
    }
}
