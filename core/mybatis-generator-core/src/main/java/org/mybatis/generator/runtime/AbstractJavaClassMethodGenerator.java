/*
 *    Copyright 2006-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.runtime;

import java.util.Optional;

import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractGenerator;

public abstract class AbstractJavaClassMethodGenerator extends AbstractGenerator {
    protected AbstractJavaClassMethodGenerator(AbstractGeneratorBuilder<?> builder) {
        super(builder);
    }

    /**
     * Executes this generator, calls plugins, and applies the generated method to the class.
     *
     * @param topLevelClass The class to which the method will be added.
     * @return true if the method was successfully generated and added to the class, false otherwise.
     */
    public boolean execute(TopLevelClass topLevelClass) {
        return generateMethodAndImports()
                .filter(mi -> callPlugins(mi.getMethod(), topLevelClass))
                .map(mi -> {
                    topLevelClass.addMethod(mi.getMethod());
                    topLevelClass.addImportedTypes(mi.getImports());
                    topLevelClass.addStaticImports(mi.getStaticImports());
                    return true;
                })
                .orElse(false);
    }

    public abstract Optional<JavaMethodAndImports> generateMethodAndImports();

    public abstract boolean callPlugins(Method method, TopLevelClass topLevelClass);
}
