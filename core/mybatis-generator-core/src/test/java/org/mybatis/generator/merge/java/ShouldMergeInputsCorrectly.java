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

public class ShouldMergeInputsCorrectly implements MergeTestCase<ShouldMergeInputsCorrectly> {
    @Override
    public String existingContent(String parameter) {
        return """
                package com.example;

                import java.util.Set;
                import java.util.Date;
                import java.sql.Connection;

                public class TestMapper {
                    public void customMethod() {}
                }
                """;
    }

    @Override
    public String newContent(String parameter) {
        return """
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
    }

    @Override
    public String expectedContentAfterMerge(String parameter) {
        return """
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
    }

    @Override
    public ShouldMergeInputsCorrectly self() {
        return this;
    }
}
