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

import static org.hamcrest.core.IsEqual.equalTo;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.mybatis.generator.eclipse.tests.harness.summary.ClassSummary;

public class HasSuperClass extends TypeSafeMatcher<ClassSummary>{

    private Matcher<?> matcher;
    
    public HasSuperClass(Matcher<?> matcher) {
        this.matcher = matcher;
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText("superclass is ").appendDescriptionOf(matcher);
    }

    @Override
    protected boolean matchesSafely(ClassSummary item) {
        return matcher.matches(item.getSuperClass());
    }

    @Override
    protected void describeMismatchSafely(ClassSummary item, Description mismatchDescription) {
        mismatchDescription.appendText("the superclass of " + item.getName() + " is " + item.getSuperClass());
    }
    
    public static HasSuperClass hasSuperClass(Matcher<?> matcher) {
        return new HasSuperClass(matcher);
    }

    public static HasSuperClass hasSuperClass(String superClass) {
        return new HasSuperClass(equalTo(superClass));
    }
}
