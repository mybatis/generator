/*
 *    Copyright 2006-2025 the original author or authors.
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
package org.mybatis.generator.internal;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mybatis.generator.config.MergeConstants;

import static org.assertj.core.api.Assertions.assertThat;

class JavaFileMergerTest {

    @Nested
    class GetMergedSourceTests {

        @Test
        void shouldAddNewGeneratedMethodsWhenMergingWithJavadocTag() throws Exception {
            // Arrange
            var existingFileContent = """
                package com.example;

                public class TestMapper {
                    public void customMethod() {
                        System.out.println("Custom method");
                    }
                }
                """;

            var newFileContent = """
                package com.example;

                public class TestMapper {
                    /**
                     * @mbg.generated
                     */
                    public int insert(Object record) {
                        return 0;
                    }
                }
                """;

            var javadocTags = MergeConstants.getOldElementTags();

            // Act
            var actual = JavaFileMerger.getMergedSource(newFileContent, existingFileContent, javadocTags);

            // Assert
            var expected = """
                package com.example;

                public class TestMapper {

                    /**
                     * @mbg.generated
                     */
                    public int insert(Object record) {
                        return 0;
                    }

                    public void customMethod() {
                        System.out.println("Custom method");
                    }
                }
                """;
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldMergeImportsCorrectly() throws Exception {
            // Arrange
            var existingFileContent = """
                package com.example;

                import java.util.Set;
                import java.util.Date;
                import java.sql.Connection;

                public class TestMapper {
                    public void customMethod() {}
                }
                """;

            var newFileContent = """
                package com.example;

                import java.util.List;
                import java.util.Map;
                import java.util.Date;
                import java.sql.PreparedStatement;

                public class TestMapper {
                    /**
                     * @mbg.generated
                     */
                    public Map<String, Object> getMap() {
                        return null;
                    }
                }
                """;

            var javadocTags = MergeConstants.getOldElementTags();

            // Act
            var actual = JavaFileMerger.getMergedSource(newFileContent, existingFileContent, javadocTags);

            // Assert
            var expected = """
                package com.example;

                import java.sql.Connection;
                import java.sql.PreparedStatement;
                import java.util.Date;
                import java.util.List;
                import java.util.Map;
                import java.util.Set;

                public class TestMapper {

                    /**
                     * @mbg.generated
                     */
                    public Map<String, Object> getMap() {
                        return null;
                    }

                    public void customMethod() {
                    }
                }
                """;
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @ValueSource(strings = {"@ibatorgenerated", "@abatorgenerated", "@mbggenerated", "@mbg.generated"})
        void shouldPreserveCustomMethodsWithAllSupportedJavadocTags(String javadocTag) throws Exception {
            // Arrange
            var existingFileContent = String.format("""
                package com.example;

                public class TestMapper {

                    /**
                     * %s
                     */
                    public int oldGeneratedMethod() {
                        return 0;
                    }

                    public void customMethod() {
                        System.out.println("Custom method");
                    }
                }
                """, javadocTag);

            var newFileContent = """
                package com.example;

                public class TestMapper {

                    /**
                     * @mbg.generated
                     */
                    public int newGeneratedMethod() {
                        return 1;
                    }
                }
                """;

            var javadocTags = MergeConstants.getOldElementTags();

            // Act
            var actual = JavaFileMerger.getMergedSource(newFileContent, existingFileContent, javadocTags);

            // Assert
            var expected = """
                package com.example;

                public class TestMapper {

                    /**
                     * @mbg.generated
                     */
                    public int newGeneratedMethod() {
                        return 1;
                    }

                    public void customMethod() {
                        System.out.println("Custom method");
                    }
                }
                """;
            assertThat(actual).isEqualTo(expected);
        }

        @ParameterizedTest
        @ValueSource(strings = {"javax.annotation.Generated", "jakarta.annotation.Generated"})
        void shouldPreserveCustomMethodsWhenMergingWithGeneratedAnnotation(String annotationClass) throws Exception {
            // Arrange
            var existingFileContent = String.format("""
                package com.example;

                import %s;

                public class TestMapper {

                    @Generated("MyBatis Generator")
                    public int deleteByPrimaryKey(Integer id) {
                        return 0;
                    }

                    // This is a custom method that should be preserved
                    public void customMethod() {
                        System.out.println("Custom method");
                    }
                }
                """, annotationClass);

            var newFileContent = String.format("""
                package com.example;

                import %s;

                public class TestMapper {

                    @Generated("MyBatis Generator")
                    public int deleteByPrimaryKey(Integer id) {
                        // Updated implementation
                        return 1;
                    }

                    @Generated("MyBatis Generator")
                    public int insert(Object record) {
                        return 0;
                    }
                }
                """, annotationClass);

            var javadocTags = MergeConstants.getOldElementTags();

            // Act
            var actual = JavaFileMerger.getMergedSource(newFileContent, existingFileContent, javadocTags);

            // Assert
            var expected = String.format("""
                package com.example;

                import %s;

                public class TestMapper {

                    @Generated("MyBatis Generator")
                    public int deleteByPrimaryKey(Integer id) {
                        // Updated implementation
                        return 1;
                    }

                    @Generated("MyBatis Generator")
                    public int insert(Object record) {
                        return 0;
                    }

                    // This is a custom method that should be preserved
                    public void customMethod() {
                        System.out.println("Custom method");
                    }
                }
                """, annotationClass);
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void shouldPreserveMultipleCustomMethodsWhenMerging() throws Exception {
            // Arrange
            var existingFileContent = """
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

            var newFileContent = """
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

            var javadocTags = MergeConstants.getOldElementTags();

            // Act
            var actual = JavaFileMerger.getMergedSource(newFileContent, existingFileContent, javadocTags);

            // Assert
            var expected = """
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
            assertThat(actual).isEqualTo(expected);
        }
    }
}
