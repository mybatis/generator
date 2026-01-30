/*
 *    Copyright 2006-2026 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/**
 * This package holds test cases for the Java merger. Adding a new test case should be fairly simple:
 *
 * <p>Create a new test case class in this package that extends {@link org.mybatis.generator.merge.MergeTestCase}.
 *     The class should have a no-argument constructor.
 *     A test case has methods that return existing content, new content, and expected content after a merge.
 *     If there are multiple variants of the test case (for example, if you want to run the test for every value of
 *     an old merge tag), then overrid the {@link org.mybatis.generator.merge.MergeTestCase#parameterVariants()}
 *     method and provide a list of values for the variants.
 * </p>
 *
 * <p>The test case should be automatically picked up by the test runner and all variants will be run.</p>
 */
@NullMarked
package org.mybatis.generator.merge.java;

import org.jspecify.annotations.NullMarked;
