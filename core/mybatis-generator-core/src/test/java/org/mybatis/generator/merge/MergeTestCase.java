package org.mybatis.generator.merge;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

public interface MergeTestCase<T extends MergeTestCase<T>> {
    String existingContent(String parameter);
    String newContent(String parameter);
    String expectedContentAfterMerge(String parameter);
    T self();

    /**
     * Provide a list of parameter variants. If empty, then the test case will be run one time with null passed as a
     * parameter to the content methods. If not empty, the test case will be run multiple times - once for each value
     * in the list, and the value will be passed to the content methods.
     *
     * @return a list of parameter variants. Empty by default.
     */
    default List<String> parameterVariants() {
        return Collections.emptyList();
    }

    default Stream<Arguments> variants() {
        T self = self();
        String name = "testCase = " + self.getClass().getSimpleName() + ", parameter = ";
        if (parameterVariants().isEmpty()) {
            return Stream.of(Arguments.argumentSet(name + "null", self, null));
        } else {
            return parameterVariants().stream().map(v -> Arguments.argumentSet(name + v, self, v));
        }
    }
}
