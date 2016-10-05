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
import org.mybatis.generator.eclipse.tests.harness.summary.AbstractTypeOrEnumSummary;

public class HasMethod extends TypeSafeMatcher<AbstractTypeOrEnumSummary>{

    private String methodName;
    
    public HasMethod(String methodName) {
        this.methodName = methodName;
    }
    
    @Override
    public void describeTo(Description description) {
        description.appendText("method with signature " + methodName);
    }

    @Override
    protected boolean matchesSafely(AbstractTypeOrEnumSummary item) {
        return item.hasMethod(methodName);
    }

    @Override
    protected void describeMismatchSafely(AbstractTypeOrEnumSummary item, Description mismatchDescription) {
        mismatchDescription.appendText(methodName + " was not found");
    }
}
