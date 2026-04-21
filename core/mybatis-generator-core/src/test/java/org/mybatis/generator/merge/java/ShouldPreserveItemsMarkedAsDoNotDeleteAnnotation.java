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

/**
 * This test case verifies that generated items with the special text
 * "do_not_delete_during_merge" survive the merge. This is something
 * supported in the legacy example classes.
 */
public class ShouldPreserveItemsMarkedAsDoNotDeleteAnnotation extends JavaMergeTestCase {
    public ShouldPreserveItemsMarkedAsDoNotDeleteAnnotation() {
        addMergeConfiguration("Eclipse", new MergeConfiguration.Builder()
                .withImportSortType(MergeConfiguration.ImportSortType.ECLIPSE)
                .build());

        addMergeConfiguration("LexicalPreserving", new MergeConfiguration.Builder()
                .isLexicalPreserving(true)
                .build());

        addMergeConfiguration("MergeIntoOld", new MergeConfiguration.Builder()
                .withMergeStrategy(MergeConfiguration.MergeStrategy.MERGE_INTO_EXISTING)
                .build());
    }

    @Override
    public String existingContent(String parameter) {
        return """
              package mbg.test.mb3.generated.flat.model;

              import jakarta.annotation.Generated;
              import java.util.List;

              public class PkfieldsExample {
                  @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="do_not_delete_during_merge (existing)")
                  protected int id;

                  @Generated("org.mybatis.generator.api.MyBatisGenerator")
                  protected List<Criteria> oredCriteria;

                  @Generated("org.mybatis.generator.api.MyBatisGenerator")
                  public PkfieldsExample() {
                      oredCriteria = new ArrayList<>();
                  }

                  public void customMethod() {
                  }

                  @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="do_not_delete_during_merge")
                  public int getId() {
                      // existing
                      return id;
                  }

                  @Generated("org.mybatis.generator.api.MyBatisGenerator")
                  public List<Criteria> getOredCriteria() {
                      return oredCriteria;
                  }

                  @Generated("org.mybatis.generator.api.MyBatisGenerator")
                  public void clear() {
                      oredCriteria.clear();
                  }

                  @Generated("org.mybatis.generator.api.MyBatisGenerator")
                  protected abstract static class GeneratedCriteria {
                      protected List<Criterion> allCriteria;
                  }

                  @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="do_not_delete_during_merge")
                  public static class Criteria extends GeneratedCriteria {
                      protected Criteria() {
                          super();
                      }

                      public boolean isValid() {
                          return true;
                      }
                  }

                  @Generated("org.mybatis.generator.api.MyBatisGenerator")
                  public static class Criterion {
                      private String condition;
                  }
              }
              """;
    }

    @Override
    public String newContent(String parameter) {
        return """
              package mbg.test.mb3.generated.flat.model;

              import jakarta.annotation.Generated;
              import java.util.List;

              public class PkfieldsExample {
                  @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="do_not_delete_during_merge")
                  protected int id;

                  @Generated("org.mybatis.generator.api.MyBatisGenerator")
                  public void reset() {
                  }

                  @Generated("org.mybatis.generator.api.MyBatisGenerator")
                  protected abstract static class GeneratedCriteria {
                  }

                  @Generated("org.mybatis.generator.api.MyBatisGenerator")
                  public int getId() {
                      return id;
                  }

                  @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="do_not_delete_during_merge")
                  public static class Criteria extends GeneratedCriteria {
                      protected Criteria() {
                          super();
                      }
                  }
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
        return """
              package mbg.test.mb3.generated.flat.model;

              import java.util.List;

              import jakarta.annotation.Generated;

              public class PkfieldsExample {

                  @Generated("org.mybatis.generator.api.MyBatisGenerator")
                  public void reset() {
                  }

                  @Generated("org.mybatis.generator.api.MyBatisGenerator")
                  protected abstract static class GeneratedCriteria {
                  }

                  public void customMethod() {
                  }

                  @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "do_not_delete_during_merge (existing)")
                  protected int id;

                  @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "do_not_delete_during_merge")
                  public int getId() {
                      // existing
                      return id;
                  }

                  @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "do_not_delete_during_merge")
                  public static class Criteria extends GeneratedCriteria {

                      protected Criteria() {
                          super();
                      }

                      public boolean isValid() {
                          return true;
                      }
                  }
              }
              """;
    }

    private String expectedLexicalPreservingContent() {
        return """
              package mbg.test.mb3.generated.flat.model;

              import jakarta.annotation.Generated;
              import java.util.List;

              public class PkfieldsExample {
                  @Generated("org.mybatis.generator.api.MyBatisGenerator")
                  public void reset() {
                  }

                  @Generated("org.mybatis.generator.api.MyBatisGenerator")
                  protected abstract static class GeneratedCriteria {
                  }
                 \s
                  public void customMethod() {
                  }
                 \s
                  @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="do_not_delete_during_merge (existing)")
                  protected int id;
                 \s
                  @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="do_not_delete_during_merge")
                  public int getId() {
                      // existing
                      return id;
                  }
                 \s
                  @Generated(value="org.mybatis.generator.api.MyBatisGenerator", comments="do_not_delete_during_merge")
                  public static class Criteria extends GeneratedCriteria {
                      protected Criteria() {
                          super();
                      }

                      public boolean isValid() {
                          return true;
                      }
                  }
              }
              """;
    }

    private String expectedMergeIntoOldContent() {
        return """
              package mbg.test.mb3.generated.flat.model;

              import java.util.List;

              import jakarta.annotation.Generated;

              public class PkfieldsExample {

                  @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "do_not_delete_during_merge (existing)")
                  protected int id;

                  public void customMethod() {
                  }

                  @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "do_not_delete_during_merge")
                  public int getId() {
                      // existing
                      return id;
                  }

                  @Generated(value = "org.mybatis.generator.api.MyBatisGenerator", comments = "do_not_delete_during_merge")
                  public static class Criteria extends GeneratedCriteria {

                      protected Criteria() {
                          super();
                      }

                      public boolean isValid() {
                          return true;
                      }
                  }

                  @Generated("org.mybatis.generator.api.MyBatisGenerator")
                  public void reset() {
                  }

                  @Generated("org.mybatis.generator.api.MyBatisGenerator")
                  protected abstract static class GeneratedCriteria {
                  }
              }
              """;
    }
}
