package org.mybatis.generator.merge.java;

import org.mybatis.generator.merge.MergeTestCase;

public class ShouldReturnNewFileUnmodified extends MergeTestCase {
    @Override
    public String existingContent(String parameter) {
        return
                """
                package foo;
                
                import javax.annotation.Generated;

                public class Bar {
                    @Generated("org.mybatis.generator.api.MyBatisGenerator")
                    public String hello() {
                        return "hello";
                    }
                }
                """;
    }

    @Override
    public String newContent(String parameter) {
        // imports are intentionally in a crazy order - this test covers the case where the
        // new file should be returned as is
        return
                """
                package foo;
                
                import java.sql.Date;
                import static foo.Thing.thing;
                import javax.annotation.Generated;
                
                public class Bar {
                    @Generated("org.mybatis.generator.api.MyBatisGenerator")
                    public String hello() {
                        return "hello";
                    }

                    @Generated("org.mybatis.generator.api.MyBatisGenerator")
                    public Date someDate() {
                        return thing();
                    }
                }
                """;
    }

    @Override
    public String expectedContentAfterMerge(String parameter) {
        return newContent(parameter);
    }
}
