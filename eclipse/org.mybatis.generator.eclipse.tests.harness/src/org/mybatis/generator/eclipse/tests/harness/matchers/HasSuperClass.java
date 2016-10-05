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
import org.mybatis.generator.eclipse.tests.harness.summary.ClassSummary;

public class HasSuperClass extends TypeSafeMatcher<ClassSummary>{

    private String superClass;
    
    public HasSuperClass(String superClass) {
        this.superClass = superClass;
    }
    
    @Override
    public void describeTo(Description description) {
        if (superClass == null) {
            description.appendText("class has no superclass");
        } else {
            description.appendText("class has superclass " + superClass);
        }
    }

    @Override
    protected boolean matchesSafely(ClassSummary item) {
        if (superClass == null) {
            return item.getSuperClass() == null;
        } else {
            return superClass.equals(item.getSuperClass());
        }
    }

    @Override
    protected void describeMismatchSafely(ClassSummary item, Description mismatchDescription) {
        if (item.getSuperClass() == null) {
            mismatchDescription.appendText("class has no super class");
        } else {
            mismatchDescription.appendText("super class was " + item.getSuperClass());
        }
    }
}
