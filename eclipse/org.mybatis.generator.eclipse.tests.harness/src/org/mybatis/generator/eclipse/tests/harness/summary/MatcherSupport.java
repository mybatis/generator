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
package org.mybatis.generator.eclipse.tests.harness.summary;

/**
 * This class is the base class for most summary classes.  It contains methods
 * used by the matchers to verify different aspects of parsed code.
 * 
 * Some methods have a default return value here and should be overridden as
 * appropriate in a subclass.
 * 
 * @author Jeff Butler
 *
 */
public abstract class MatcherSupport {
    /**
     * Should be overridden by the AnnotationSummary class
     * @return
     */
    public int getAnnotationMemberCount() {
        return 0;
    }

    /**
     * Should be overridden by the AnnotationSummary class
     * @return
     */
    public boolean hasAnnotationMember(String matchString) {
        return false;
    }

    /**
     * Should be overridden by the EnumSummary class
     * @return
     */
    public int getEnumConstantCount() {
        return 0;
    }

    /**
     * Should be overridden by the EnumSummary class
     * @return
     */
    public boolean hasEnumConstant(String matchString) {
        return false;
    }

    /**
     * Should be overridden by AnnotationSummary, ClassSummary, EnumSummary, and InterfaceSummary classes
     * @return
     */
    public int getFieldCount() {
        return 0;
    }

    /**
     * Should be overridden by AnnotationSummary, ClassSummary, EnumSummary, and InterfaceSummary classes
     * @return
     */
    public FieldSummary getField(String matchString) {
        return null;
    }
    
    /**
     * Should be overridden by the CompilationUnitSummary class
     * @return
     */
    public int getImportCount() {
        return 0;
    }

    /**
     * Should be overridden by the CompilationUnitSummary class
     * @return
     */
    public boolean hasImportDeclaration(String matchString) {
        return false;
    }

    /**
     * Should be overridden by ClassSummary, EnumSummary, and InterfaceSummary classes
     * @return
     */
    public int getMethodCount() {
        return 0;
    }

    /**
     * Should be overridden by ClassSummary, EnumSummary, and InterfaceSummary classes
     * @return
     */
    public boolean hasMethod(String matchString) {
        return false;
    }

    /**
     * Should be overridden by the ClassSummary class
     * @return
     */
    public String getSuperClass() {
        return null;
    }

    /**
     * Should be overridden by ClassSummary, EnumSummary, and InterfaceSummary classes
     * @return
     */
    public int getSuperInterfaceCount() {
        return 0;
    }

    /**
     * Should be overridden by ClassSummary, EnumSummary, and InterfaceSummary classes
     * @return
     */
    public boolean hasSuperInterface(String matchString) {
        return false;
    }

    public abstract ClassSummary getClassSummary(String name);
    public abstract int getClassCount();
    public abstract InterfaceSummary getInterfaceSummary(String name);
    public abstract int getInterfaceCount();
    public abstract EnumSummary getEnumSummary(String name);
    public abstract int getEnumCount();
    public abstract AnnotationSummary getAnnotationSummary(String name);
    public abstract int getAnnotationCount();
}
