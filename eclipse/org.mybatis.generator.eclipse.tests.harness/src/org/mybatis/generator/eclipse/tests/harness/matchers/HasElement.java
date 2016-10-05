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
import org.mybatis.generator.eclipse.tests.harness.matchers.support.ClassExistenceChecker;
import org.mybatis.generator.eclipse.tests.harness.matchers.support.EnumExistenceChecker;
import org.mybatis.generator.eclipse.tests.harness.matchers.support.ExistenceChecker;
import org.mybatis.generator.eclipse.tests.harness.matchers.support.InterfaceExistenceChecker;
import org.mybatis.generator.eclipse.tests.harness.summary.AbstractSummary;

public class HasElement extends TypeSafeMatcher<AbstractSummary>{
    
    public enum Type {
        ANNOTATION("annotation", AnnotationExistenceChecker.class),
        CLASS("class", ClassExistenceChecker.class),
        ENUM("enum", EnumExistenceChecker.class),
        INTERFACE("interface", InterfaceExistenceChecker.class);
        
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
        description.appendText(type.getName() + " named " + matchString);
    }

    @Override
    protected boolean matchesSafely(AbstractSummary item) {
        return type.getExistenceChecker().exists(item, matchString);
    }

    @Override
    protected void describeMismatchSafely(AbstractSummary item, Description mismatchDescription) {
        mismatchDescription.appendText(matchString + " was not found");
    }
}
