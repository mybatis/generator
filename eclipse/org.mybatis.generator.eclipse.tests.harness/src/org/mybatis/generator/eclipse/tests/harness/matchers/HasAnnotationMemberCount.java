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
import org.mybatis.generator.eclipse.tests.harness.summary.AnnotationSummary;

public class HasAnnotationMemberCount extends TypeSafeMatcher<AnnotationSummary>{

    private int count;
    
    public HasAnnotationMemberCount(int count) {
        this.count = count;
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText(count + " annotation members");
    }

    @Override
    protected boolean matchesSafely(AnnotationSummary item) {
        return count == item.getAnnotationMemberCount();
    }

    @Override
    protected void describeMismatchSafely(AnnotationSummary item, Description mismatchDescription) {
        mismatchDescription.appendText("was " + item.getAnnotationMemberCount());
    }
}
