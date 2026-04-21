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

import org.jspecify.annotations.Nullable;

public class MergeConfiguration {
    private final boolean isLexicalPreserving;
    private final int indentSize;
    private final IndentType indentType;
    private final ImportSortType importSortType;
    private final MergeStrategy mergeStrategy;

    private static final IndentType DEFAULT_INDENT_TYPE = IndentType.SPACE;
    private static final ImportSortType DEFAULT_IMPORT_SORT_TYPE = ImportSortType.ECLIPSE;
    public static final MergeStrategy DEFAULT_MERGE_STRATEGY = MergeStrategy.MERGE_INTO_NEW;

    private MergeConfiguration(Builder builder) {
        isLexicalPreserving = builder.isLexicalPreserving;
        indentType = builder.indentType == null ? DEFAULT_INDENT_TYPE : builder.indentType;
        importSortType = builder.importSortType == null ? DEFAULT_IMPORT_SORT_TYPE : builder.importSortType;
        mergeStrategy = builder.mergeStrategy == null ? DEFAULT_MERGE_STRATEGY : builder.mergeStrategy;

        if (builder.indentSize == null) {
            if (indentType == IndentType.TAB) {
                indentSize = 1;
            } else {
                indentSize = 4;
            }
        } else {
            indentSize = builder.indentSize;
        }
    }

    public boolean isLexicalPreserving() {
        return isLexicalPreserving;
    }

    public MergeStrategy mergeStrategy() {
        return mergeStrategy;
    }

    public int indentSize() {
        return indentSize;
    }

    public IndentType indentType() {
        return indentType;
    }

    public ImportSortType importSortType() {
        return importSortType;
    }

    public enum IndentType {
        TAB,
        SPACE
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

    public static MergeConfiguration defaultMergeConfiguration() {
        return new MergeConfiguration.Builder().build();
    }

    public static class Builder {
        private boolean isLexicalPreserving;
        private @Nullable Integer indentSize;
        private @Nullable IndentType indentType;
        private @Nullable ImportSortType importSortType;
        private @Nullable MergeStrategy mergeStrategy;

        public Builder isLexicalPreserving(boolean isLexicalPreserving) {
            this.isLexicalPreserving = isLexicalPreserving;
            return this;
        }

        public Builder withIndentSize(int indentSize) {
            this.indentSize = indentSize;
            return this;
        }

        public Builder withIndentType(@Nullable IndentType indentType) {
            this.indentType = indentType;
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

        public MergeConfiguration build() {
            return new MergeConfiguration(this);
        }
    }
}
