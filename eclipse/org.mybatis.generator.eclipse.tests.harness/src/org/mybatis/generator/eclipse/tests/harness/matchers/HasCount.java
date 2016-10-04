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
import org.mybatis.generator.eclipse.tests.harness.matchers.support.AnnotationCounter;
import org.mybatis.generator.eclipse.tests.harness.matchers.support.ClassCounter;
import org.mybatis.generator.eclipse.tests.harness.matchers.support.Counter;
import org.mybatis.generator.eclipse.tests.harness.matchers.support.EnumCounter;
import org.mybatis.generator.eclipse.tests.harness.matchers.support.InterfaceCounter;
import org.mybatis.generator.eclipse.tests.harness.summary.AbstractSummary;

public class HasCount extends TypeSafeMatcher<AbstractSummary>{
    
    public enum Type {
        ANNOTATION("annotation", AnnotationCounter.class),
        CLASS("class", ClassCounter.class),
        ENUM("enum", EnumCounter.class),
        INTERFACE("interface", InterfaceCounter.class);
        
        private String name;
        private Class<? extends Counter> counterClass;
        
        private Type(String name, Class<? extends Counter> counterClass) {
            this.name = name;
            this.counterClass = counterClass;
        }
        
        public Counter getCounter() {
            return Utilities.newInstance(counterClass);
        }
        
        public String getName() {
            return name;
        }
    }

    private int count;
    private Type type;
    
    public HasCount(int count, Type type) {
        this.count = count;
        this.type = type;
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText(type.getName() + " count " + count);
    }

    @Override
    protected boolean matchesSafely(AbstractSummary item) {
        return count == type.getCounter().getCount(item);
    }

    @Override
    protected void describeMismatchSafely(AbstractSummary item, Description mismatchDescription) {
        mismatchDescription.appendText("was " + type.getCounter().getCount(item));
    }
}
