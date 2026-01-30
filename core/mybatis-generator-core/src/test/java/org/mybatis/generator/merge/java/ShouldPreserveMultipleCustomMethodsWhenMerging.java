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
package org.mybatis.generator.merge.java;

import org.mybatis.generator.merge.MergeTestCase;

public class ShouldPreserveMultipleCustomMethodsWhenMerging
        implements MergeTestCase<ShouldPreserveMultipleCustomMethodsWhenMerging> {

    @Override
    public String existingContent(String parameter) {
        return  """
                package com.example;

                public class TestMapper {

                    /**
                     * @mbg.generated
                     */
                    public int generatedMethod() {
                        return 0;
                    }

                    public void customMethod1() {
                        System.out.println("Custom method 1");
                    }

                    public void customMethod2() {
                        System.out.println("Custom method 2");
                    }
                }
                """;
    }

    @Override
    public String newContent(String parameter) {
        return """
                package com.example;

                public class TestMapper {

                    /**
                     * @mbg.generated
                     */
                    public int generatedMethod() {
                        return 1; // Updated
                    }

                    /**
                     * @mbg.generated
                     */
                    public int newGeneratedMethod() {
                        return 0;
                    }
                }
                """;
    }

    @Override
    public String expectedContentAfterMerge(String parameter) {
        return """
                package com.example;

                public class TestMapper {

                    /**
                     * @mbg.generated
                     */
                    public int generatedMethod() {
                        // Updated
                        return 1;
                    }

                    /**
                     * @mbg.generated
                     */
                    public int newGeneratedMethod() {
                        return 0;
                    }

                    public void customMethod1() {
                        System.out.println("Custom method 1");
                    }

                    public void customMethod2() {
                        System.out.println("Custom method 2");
                    }
                }
                """;
    }

    @Override
    public ShouldPreserveMultipleCustomMethodsWhenMerging self() {
        return this;
    }
}
