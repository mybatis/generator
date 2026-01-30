package org.mybatis.generator.merge;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

public interface MergeTestCase {
    Stream<Arguments> variants();
    String existingContent(String parameter);
    String newContent(String parameter);
    String expectedContentAfterMerge(String parameter);
}
