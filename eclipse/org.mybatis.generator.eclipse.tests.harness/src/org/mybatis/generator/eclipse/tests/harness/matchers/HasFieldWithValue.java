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

import org.hamcrest.Condition;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.mybatis.generator.eclipse.tests.harness.summary.AbstractBodyElementSummary;

public class HasFieldWithValue extends TypeSafeDiagnosingMatcher<AbstractBodyElementSummary>{

    private String matchString;
    private Matcher<Object> matcher;
    
    @SuppressWarnings("unchecked")
    public HasFieldWithValue(String matchString, Matcher<?> matcher) {
        this.matchString = matchString;
        this.matcher = (Matcher<Object>) matcher;
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText("field named " + matchString + ", and ")
            .appendDescriptionOf(matcher);
    }

    @Override
    protected boolean matchesSafely(AbstractBodyElementSummary item, Description mismatch) {
        return containsElement(item, mismatch)
                .matching(matcher);
    }

    private Condition<Object> containsElement(AbstractBodyElementSummary item, Description mismatch) {
        Object summary = item.getField(matchString);

        if (summary == null) {
            mismatch.appendText(matchString + " was not found");
            return Condition.notMatched();
        } else {
            return Condition.matched(summary, mismatch);
        }
    }
}
