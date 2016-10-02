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
package org.mybatis.generator.eclipse.tests.harness.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.mybatis.generator.eclipse.tests.harness.Utilities;
import org.mybatis.generator.eclipse.tests.harness.matchers.support.AnnotationExistenceChecker;
import org.mybatis.generator.eclipse.tests.harness.matchers.support.AnnotationMemberExistenceChecker;
import org.mybatis.generator.eclipse.tests.harness.matchers.support.ClassExistenceChecker;
import org.mybatis.generator.eclipse.tests.harness.matchers.support.EnumConstantExistenceChecker;
import org.mybatis.generator.eclipse.tests.harness.matchers.support.EnumExistenceChecker;
import org.mybatis.generator.eclipse.tests.harness.matchers.support.ExistenceChecker;
import org.mybatis.generator.eclipse.tests.harness.matchers.support.FieldExistenceChecker;
import org.mybatis.generator.eclipse.tests.harness.matchers.support.ImportExistenceChecker;
import org.mybatis.generator.eclipse.tests.harness.matchers.support.InterfaceExistenceChecker;
import org.mybatis.generator.eclipse.tests.harness.matchers.support.MethodExistenceChecker;
import org.mybatis.generator.eclipse.tests.harness.matchers.support.SuperClassExistenceChecker;
import org.mybatis.generator.eclipse.tests.harness.matchers.support.SuperInterfaceExistenceChecker;
import org.mybatis.generator.eclipse.tests.harness.summary.MatcherSupport;

public class HasElement extends TypeSafeMatcher<MatcherSupport>{
    
    public enum Type {
        ANNOTATION("annotation", AnnotationExistenceChecker.class),
        ANNOTATION_MEMBER("annotation member", AnnotationMemberExistenceChecker.class),
        CLASS("class", ClassExistenceChecker.class),
        ENUM("enum", EnumExistenceChecker.class),
        ENUM_CONSTANT("enum constant", EnumConstantExistenceChecker.class),
        FIELD("field", FieldExistenceChecker.class),
        IMPORT("import", ImportExistenceChecker.class),
        INTERFACE("interface", InterfaceExistenceChecker.class),
        METHOD("method", MethodExistenceChecker.class),
        SUPER_CLASS("super class", SuperClassExistenceChecker.class),
        SUPER_INTERFACE("super interface", SuperInterfaceExistenceChecker.class);
        
        private String name;
        private Class<? extends ExistenceChecker> existenceCheckerClass;
        
        private Type(String name, Class<? extends ExistenceChecker> existenceCheckerClass) {
            this.name = name;
            this.existenceCheckerClass = existenceCheckerClass;
        }
        
        public ExistenceChecker getExistenceChecker() {
            return Utilities.newInstance(existenceCheckerClass);
        }
        
        public String getName() {
            return name;
        }
    }

    private String matchString;
    private Type type;
    
    public HasElement(String matchString, Type type) {
        this.matchString = matchString;
        this.type = type;
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText(type.getName() + " named " + matchString + " exists");
    }

    @Override
    protected boolean matchesSafely(MatcherSupport item) {
        return type.getExistenceChecker().exists(item, matchString);
    }

    @Override
    protected void describeMismatchSafely(MatcherSupport item, Description mismatchDescription) {
        mismatchDescription.appendText(type.getName() + " " + matchString + " was not found");
    }
}
