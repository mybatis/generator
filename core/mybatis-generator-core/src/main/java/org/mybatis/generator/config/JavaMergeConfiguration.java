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
package org.mybatis.generator.config;

import java.util.Objects;

import org.jspecify.annotations.Nullable;

public class JavaMergeConfiguration {
    private final boolean isLexicalPreserving;
    private final ImportSortType importSortType;
    private final MergeStrategy mergeStrategy;

    private JavaMergeConfiguration(Builder builder) {
        isLexicalPreserving = builder.isLexicalPreserving;
        importSortType = Objects.requireNonNullElse(builder.importSortType, ImportSortType.ECLIPSE);
        mergeStrategy = Objects.requireNonNullElse(builder.mergeStrategy, MergeStrategy.MERGE_INTO_EXISTING);
    }

    public boolean isLexicalPreserving() {
        return isLexicalPreserving;
    }

    public MergeStrategy mergeStrategy() {
        return mergeStrategy;
    }

    public ImportSortType importSortType() {
        return importSortType;
    }

    public enum ImportSortType {
        ECLIPSE,
        INTELLIJ,
        DEFAULT
    }

    public enum MergeStrategy {
        MERGE_INTO_EXISTING,
        MERGE_INTO_NEW
    }

    public static JavaMergeConfiguration defaultMergeConfiguration() {
        return new Builder()
                .isLexicalPreserving(false)
                .withMergeStrategy(MergeStrategy.MERGE_INTO_EXISTING)
                .withImportSortType(ImportSortType.ECLIPSE)
                .build();
    }

    public static class Builder {
        private boolean isLexicalPreserving;
        private @Nullable ImportSortType importSortType;
        private @Nullable MergeStrategy mergeStrategy;

        public Builder isLexicalPreserving(boolean isLexicalPreserving) {
            this.isLexicalPreserving = isLexicalPreserving;
            return this;
        }

        public Builder withImportSortType(@Nullable ImportSortType importSortType) {
            this.importSortType = importSortType;
            return this;
        }

        public Builder withMergeStrategy(@Nullable MergeStrategy mergeStrategy) {
            this.mergeStrategy = mergeStrategy;
            return this;
        }

        public JavaMergeConfiguration build() {
            return new JavaMergeConfiguration(this);
        }
    }
}
