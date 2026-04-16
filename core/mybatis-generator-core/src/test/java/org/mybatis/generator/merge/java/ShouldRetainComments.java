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

public class ShouldRetainComments extends JavaMergeTestCase {

    @Override
    public String existingContent(String parameter) {
        return """
            package fr.cncc.aglae.portail.domaine;

            import jakarta.annotation.Generated;
            import lombok.AllArgsConstructor;
            import lombok.Builder;
            import lombok.Data;
            import lombok.NoArgsConstructor;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @Builder
            public class ApplicationScriptSql {
                @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source field: APPLICATION_SCRIPT_SQL.ASS_COMMENTAIRE")
                private String commentaire;

                // this kind of comment is destroyed at generation
                /** this this kind of comment is also destroyed at generation **/

                public final static int STATUT_EN_COURS = 0;

                public boolean isEnCours() {
                    return getStatut() == STATUT_EN_COURS;
                }
            }
            """;
    }

    @Override
    public String newContent(String parameter) {
        return """
            package fr.cncc.aglae.portail.domaine;

            import jakarta.annotation.Generated;
            import lombok.AllArgsConstructor;
            import lombok.Builder;
            import lombok.Data;
            import lombok.NoArgsConstructor;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @Builder
            public class ApplicationScriptSql {
                @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source field: APPLICATION_SCRIPT_SQL.ASS_COMMENTAIRE")
                private String commentaire;

                @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source field: APPLICATION_SCRIPT_SQL.ASS_FILE_NAME")
                private String fileName;
            }
            """;
    }

    @Override
    public String expectedContentAfterMerge(String parameter, String id) {
        return switch (id) {
            case "Eclipse" -> expectedEclipseContent();
            case "LexicalPreserving" -> expectedLexicalPreservingContent();
            case "MergeIntoOld" -> expectedMergeIntoOldContent();
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    private String expectedEclipseContent() {
        // TODO - the comments are completely lost
        return """
            package fr.cncc.aglae.portail.domaine;

            import jakarta.annotation.Generated;
            import lombok.AllArgsConstructor;
            import lombok.Builder;
            import lombok.Data;
            import lombok.NoArgsConstructor;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @Builder
            public class ApplicationScriptSql {

                @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source field: APPLICATION_SCRIPT_SQL.ASS_COMMENTAIRE")
                private String commentaire;

                @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source field: APPLICATION_SCRIPT_SQL.ASS_FILE_NAME")
                private String fileName;

                public final static int STATUT_EN_COURS = 0;

                public boolean isEnCours() {
                    return getStatut() == STATUT_EN_COURS;
                }
            }
            """;
    }

    private String expectedLexicalPreservingContent() {
        // TODO - the comments are completely lost
        return """
            package fr.cncc.aglae.portail.domaine;

            import jakarta.annotation.Generated;
            import lombok.AllArgsConstructor;
            import lombok.Builder;
            import lombok.Data;
            import lombok.NoArgsConstructor;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @Builder
            public class ApplicationScriptSql {
                @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source field: APPLICATION_SCRIPT_SQL.ASS_COMMENTAIRE")
                private String commentaire;

                @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source field: APPLICATION_SCRIPT_SQL.ASS_FILE_NAME")
                private String fileName;
               \s
                public final static int STATUT_EN_COURS = 0;
               \s
                public boolean isEnCours() {
                    return getStatut() == STATUT_EN_COURS;
                }
            }
            """;
    }

    private String expectedMergeIntoOldContent() {
        // TODO - this isn't great. The regular comment ends up in a weird place. But it is acceptable.
        return """
            package fr.cncc.aglae.portail.domaine;

            import jakarta.annotation.Generated;
            import lombok.AllArgsConstructor;
            import lombok.Builder;
            import lombok.Data;
            import lombok.NoArgsConstructor;

            @Data
            @AllArgsConstructor
            @NoArgsConstructor
            @Builder
            public class ApplicationScriptSql {

                /**
                 * this this kind of comment is also destroyed at generation *
                 */
                public final static int STATUT_EN_COURS = 0;

                public boolean isEnCours() {
                    return getStatut() == STATUT_EN_COURS;
                }

                @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source field: APPLICATION_SCRIPT_SQL.ASS_COMMENTAIRE")
                private String commentaire;

                // this kind of comment is destroyed at generation
                @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source field: APPLICATION_SCRIPT_SQL.ASS_FILE_NAME")
                private String fileName;
            }
            """;
    }

    @Override
    public List<MergeConfigurationAndId> mergeConfigurations() {
        MergeConfiguration eclipse = new MergeConfiguration.Builder()
                .withImportSortType(MergeConfiguration.ImportSortType.ECLIPSE)
                .build();

        MergeConfiguration lexicalPreserving = new MergeConfiguration.Builder()
                .isLexicalPreserving(true)
                .build();

        MergeConfiguration mergeIntoOld = new MergeConfiguration.Builder()
                .withMergeStrategy(MergeConfiguration.MergeStrategy.MERGE_INTO_EXISTING)
                .build();

        return List.of(new MergeConfigurationAndId("Eclipse", eclipse),
                new MergeConfigurationAndId("LexicalPreserving", lexicalPreserving),
                new MergeConfigurationAndId("MergeIntoOld", mergeIntoOld));
    }
}
