package org.mybatis.generator.merge.java;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;
import org.mybatis.generator.merge.MergeTestCase;

public class ShouldMergeInputsCorrectly implements MergeTestCase {
    @Override
    public Stream<Arguments> variants() {
        return Stream.of(Arguments.of(this, null));
    }

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
}
