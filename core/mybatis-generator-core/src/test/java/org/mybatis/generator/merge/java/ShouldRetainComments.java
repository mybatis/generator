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

import org.mybatis.generator.config.JavaMergeConfiguration;

public class ShouldRetainComments extends JavaMergeTestCase {
    public ShouldRetainComments() {
        addMergeConfiguration("MergeIntoNew", new JavaMergeConfiguration.Builder()
                .withMergeStrategy(JavaMergeConfiguration.MergeStrategy.MERGE_INTO_NEW)
                .build());

        addMergeConfiguration("MergeIntoNewLP", new JavaMergeConfiguration.Builder()
                .isLexicalPreserving(true)
                .withMergeStrategy(JavaMergeConfiguration.MergeStrategy.MERGE_INTO_NEW)
                .build());

        // disabled - the non JavaDoc comment ends up in a weird place
        addMergeConfiguration(false, "MergeIntoOld", new JavaMergeConfiguration.Builder()
                .withMergeStrategy(JavaMergeConfiguration.MergeStrategy.MERGE_INTO_EXISTING)
                .build());

        addMergeConfiguration("MergeIntoOldLP", new JavaMergeConfiguration.Builder()
                .isLexicalPreserving(true)
                .withMergeStrategy(JavaMergeConfiguration.MergeStrategy.MERGE_INTO_EXISTING)
                .build());
    }

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
            case "MergeIntoNew" -> expectedMergeIntoNewContent();
            case "MergeIntoNewLP" -> expectedMergeIntoNewLPContent();
            case "MergeIntoOld" -> expectedMergeIntoOldContent();
            case "MergeIntoOldLP" -> expectedMergeIntoOldLPContent();
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    private String expectedMergeIntoNewContent() {
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

    private String expectedMergeIntoNewLPContent() {
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

                // this kind of comment is destroyed at generation
                /**
                 * this this kind of comment is also destroyed at generation *
                 */
                public final static int STATUT_EN_COURS = 0;

                public boolean isEnCours() {
                    return getStatut() == STATUT_EN_COURS;
                }

                @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source field: APPLICATION_SCRIPT_SQL.ASS_COMMENTAIRE")
                private String commentaire;

                @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source field: APPLICATION_SCRIPT_SQL.ASS_FILE_NAME")
                private String fileName;
            }
            """;
    }

    private String expectedMergeIntoOldLPContent() {
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
                // this kind of comment is destroyed at generation
                /** this this kind of comment is also destroyed at generation **/

                public final static int STATUT_EN_COURS = 0;

                public boolean isEnCours() {
                    return getStatut() == STATUT_EN_COURS;
                }
               \s
                @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source field: APPLICATION_SCRIPT_SQL.ASS_COMMENTAIRE")
                private String commentaire;
               \s
                @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "Source field: APPLICATION_SCRIPT_SQL.ASS_FILE_NAME")
                private String fileName;
            }
            """;
    }
}
