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

public class ShouldHandleClassToRecordConversions extends MergeTestCase {
    @Override
    public String existingContent(String parameter) {
        return
                """
                package foo;

                import java.io.Serializable;
                import javax.annotation.Generated;

                public class Name implements Serializable {
                    private static final long serialVersionUID = 1L;

                    @Generated("org.mybatis.generator.api.MyBatisGenerator")
                    private int id;
                    @Generated("org.mybatis.generator.api.MyBatisGenerator")
                    private String firstName;
                    @Generated("org.mybatis.generator.api.MyBatisGenerator")
                    private String lastName;

                    @Generated("org.mybatis.generator.api.MyBatisGenerator")
                    public int getId() {
                        return id;
                    }

                    @Generated("org.mybatis.generator.api.MyBatisGenerator")
                    public void setId(int id) {
                        this.id = id;
                    }

                    @Generated("org.mybatis.generator.api.MyBatisGenerator")
                    public String getFirstName() {
                        return firstName;
                    }

                    @Generated("org.mybatis.generator.api.MyBatisGenerator")
                    public void setFirstName(String firstName) {
                        this.firstName = firstName;
                    }

                    @Generated("org.mybatis.generator.api.MyBatisGenerator")
                    public String getLastName() {
                        return lastName;
                    }

                    @Generated("org.mybatis.generator.api.MyBatisGenerator")
                    public void setLastName(String lastName) {
                        this.lastName = lastName;
                    }

                    public String fullName() {
                        return firstName + " " + lastName;
                    }
                }
                """;
    }

    @Override
    public String newContent(String parameter) {
        return
                """
                package foo;

                import javax.annotation.Generated;

                @Generated("org.mybatis.generator.api.MyBatisGenerator")
                public record Name(int id, String firstName, String lastName) {}
                """;
    }

    @Override
    public String expectedContentAfterMerge(String parameter) {
        return
                """
                package foo;

                import java.io.Serializable;

                import javax.annotation.Generated;

                @Generated("org.mybatis.generator.api.MyBatisGenerator")
                public record Name(int id, String firstName, String lastName) implements Serializable {

                    private static final long serialVersionUID = 1L;

                    public String fullName() {
                        return firstName + " " + lastName;
                    }
                }
                """;
    }
}
