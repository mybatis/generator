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

import java.util.List;

import org.mybatis.generator.merge.MergeTestCase;

public class ShouldPreserveCustomMethodsFromOtherGenerators extends MergeTestCase {
    @Override
    public String existingContent(String parameter) {
        return String.format("""
                package com.example;

                import %s;

                public class TestMapper {

                    @Generated(value="foo.bar.Generator")
                    public int annotationVariant1() {
                        return 0;
                    }

                    @Generated("org.mybatis.generator.api.MyBatisGenerator")
                    public int annotationVariant2() {
                        return 0;
                    }

                    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2026-01-30T16:13:03.730861-05:00", comments="Source field: awful table.first name")
                    public int deleteByPrimaryKey(Integer id) {
                        return 0;
                    }

                    // This is a custom method that should be preserved
                    public void customMethod() {
                        System.out.println("Custom method");
                    }
                }
                """, parameter);
    }

    @Override
    public String newContent(String parameter) {
        return String.format("""
                package com.example;

                import %s;

                public class TestMapper {

                    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2026-01-30T16:13:03.730861-05:00", comments="Source field: awful table.first name")
                    public int deleteByPrimaryKey(Integer id) {
                        // Updated implementation
                        return 1;
                    }

                    @Generated(value="org.mybatis.generator.api.MyBatisGenerator", date="2026-01-30T16:13:03.730861-05:00", comments="Source field: awful table.first name")
                    public int insert(Object record) {
                        return 0;
                    }
                }
                """, parameter);
    }

    @Override
    public String expectedContentAfterMerge(String parameter) {
        return String.format("""
                package com.example;

                import %s;

                public class TestMapper {

                    @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2026-01-30T16:13:03.730861-05:00", comments = "Source field: awful table.first name")
                    public int deleteByPrimaryKey(Integer id) {
                        // Updated implementation
                        return 1;
                    }

                    @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", date = "2026-01-30T16:13:03.730861-05:00", comments = "Source field: awful table.first name")
                    public int insert(Object record) {
                        return 0;
                    }

                    @Generated(value = "foo.bar.Generator")
                    public int annotationVariant1() {
                        return 0;
                    }

                    // This is a custom method that should be preserved
                    public void customMethod() {
                        System.out.println("Custom method");
                    }
                }
                """, parameter);
    }

    @Override
    public List<String> parameterVariants() {
        return List.of("javax.annotation.Generated", "jakarta.annotation.Generated");
    }
}
