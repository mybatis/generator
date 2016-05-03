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
package mbg.domtest;

import java.util.List;

import org.mybatis.generator.api.dom.java.CompilationUnit;

/**
 * Implementors of this interface will be called during the build.  The expectation is that
 * implementors will create hierarchies of classes/interfaces/enums of varying complexity.
 * After they are created, the build will attempt to compile everything that was generated.
 * If any code produced by the Java code generator is incorrect, the build should fail
 * with compile errors in the generated objects.
 * 
 * Use the IgnoreDomTest annotation to skip a generator class (for example,
 * when doing TDD to fix an error in the code generator and you want to commit the
 * test before committing the fix)
 * 
 */
public interface CompilationUnitGenerator {
    List<CompilationUnit> generate();
}
