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

import org.mybatis.generator.api.dom.kotlin.KotlinFile;
import org.mybatis.generator.api.dom.kotlin.KotlinFunction;
import org.mybatis.generator.api.dom.kotlin.KotlinType;
import org.mybatis.generator.codegen.AbstractGenerator;

public abstract class AbstractKotlinFunctionGenerator extends AbstractGenerator {
    protected AbstractKotlinFunctionGenerator(AbstractGenerator.AbstractGeneratorBuilder<?> builder) {
        super(builder);
    }

    /**
     * Executes this generator, calls plugins, and applies the generated function and imports to
     * the Kotlin file. This variant expects the generated function to be a function in a type.
     *
     * @param kotlinFile The Kotlin file to which the generated imports will be added.
     * @param kotlinType The Kotlin type to which the generated function will be added.
     * @return true if the function and imports were successfully generated and added to the Kotlin file, false
     *     otherwise.
     */
    public boolean execute(KotlinFile kotlinFile, KotlinType kotlinType) {
        return generateFunctionAndImports()
                .filter(fi -> callPlugins(fi.getFunction(), kotlinFile))
                .map(mi -> {
                    kotlinType.addNamedItem(mi.getFunction());
                    kotlinFile.addImports(mi.getImports());
                    return true;
                })
                .orElse(false);
    }

    /**
     * Executes this generator, calls plugins, and applies the generated function and imports to
     * the Kotlin file. This variant expects the generated function to be a top-level function.
     *
     * @param kotlinFile The Kotlin file to which the generated function and imports will be added.
     * @return true if the function and imports were successfully generated and added to the Kotlin file, false
     *     otherwise.
     */
    public boolean execute(KotlinFile kotlinFile) {
        return generateFunctionAndImports()
                .filter(fi -> callPlugins(fi.getFunction(), kotlinFile))
                .map(mi -> {
                    kotlinFile.addNamedItem(mi.getFunction());
                    kotlinFile.addImports(mi.getImports());
                    return true;
                })
                .orElse(false);
    }

    public abstract Optional<KotlinFunctionAndImports> generateFunctionAndImports();

    public abstract boolean callPlugins(KotlinFunction function, KotlinFile kotlinFile);

}
