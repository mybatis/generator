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
package org.mybatis.generator.eclipse.core.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.mybatis.generator.eclipse.core.tests.callback.EclipseShellCallbackTest;
import org.mybatis.generator.eclipse.core.tests.merge.ExistingJavaFileVisitorTest;
import org.mybatis.generator.eclipse.core.tests.merge.JavaFileMergerTest;
import org.mybatis.generator.eclipse.core.tests.merge.NewJavaFileVisitorTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    EclipseShellCallbackTest.class,
    ExistingJavaFileVisitorTest.class,
    JavaFileMergerTest.class,
    NewJavaFileVisitorTest.class
})
/**
 * This is for running the tests interactively.  Use Run As>JUnit Plugin Test
 * 
 * Also, setup the run configuration to run headless
 * (main tab, run an application > headless mode)
 *  
 * @author Jeff Butler
 */
public class RunAllTests {
}
