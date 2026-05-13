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
import org.mybatis.generator.config.MergeStrategy;

/**
 * This test case verifies that generated items with the special text
 * "do_not_delete_during_merge" survive the merge. This is something
 * supported in the legacy example classes.
 */
public class ShouldPreserveItemsMarkedAsDoNotDelete extends JavaMergeTestCase {
    public ShouldPreserveItemsMarkedAsDoNotDelete() {
        addMergeConfiguration("MergeIntoNew", new JavaMergeConfiguration.Builder()
                .withMergeStrategy(MergeStrategy.MERGE_INTO_NEW)
                .build());

        // disabled because of an issue with the lexical preserving printer - JavaDoc comments are garbled
        addMergeConfiguration(false, "MergeIntoNewLP", new JavaMergeConfiguration.Builder()
                .isLexicalPreserving(true)
                .withMergeStrategy(MergeStrategy.MERGE_INTO_NEW)
                .build());

        addMergeConfiguration("MergeIntoOld", new JavaMergeConfiguration.Builder()
                .withMergeStrategy(MergeStrategy.MERGE_INTO_EXISTING)
                .build());

        // disabled because of an issue with the lexical preserving printer - JavaDoc comments are lost
        addMergeConfiguration(false, "MergeIntoOldLP", new JavaMergeConfiguration.Builder()
                .isLexicalPreserving(true)
                .withMergeStrategy(MergeStrategy.MERGE_INTO_EXISTING)
                .build());
    }

    @Override
    public String existingContent(String parameter) {
        return """
              package mbg.test.mb3.generated.flat.model;

              import java.util.List;

              public class PkfieldsExample {
                  /**
                   * @mbg.generated do_not_delete_during_merge (existing)
                   */
                  protected int id;

                  /**
                   * @mbg.generated (1)
                   */
                  protected List<Criteria> oredCriteria;

                  /**
                   * @mbg.generated (2)
                   */
                  public PkfieldsExample() {
                      oredCriteria = new ArrayList<>();
                  }

                  public void customMethod() {
                  }

                  /**
                   * @mbg.generated do_not_delete_during_merge (3)
                   */
                  public int getId() {
                      // existing
                      return id;
                  }

                  /**
                   * @mbg.generated (4)
                   */
                  public List<Criteria> getOredCriteria() {
                      return oredCriteria;
                  }

                  /**
                   * @mbg.generated (5)
                   */
                  public void clear() {
                      oredCriteria.clear();
                  }

                  /**
                   * @mbg.generated (6)
                   */
                  protected abstract static class GeneratedCriteria {
                      protected List<Criterion> allCriteria;
                  }

                  /**
                   * @mbg.generated do_not_delete_during_merge (7)
                   */
                  public static class Criteria extends GeneratedCriteria {
                      protected Criteria() {
                          super();
                      }

                      public boolean isValid() {
                          return true;
                      }
                  }

                  /**
                   * @mbg.generated (8)
                   */
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

              import java.util.List;

              public class PkfieldsExample {
                  /**
                   * @mbg.generated do_not_delete_during_merge
                   */
                  protected int id;

                  /**
                   * @mbg.generated
                   */
                  public void reset() {
                  }

                  /**
                   * @mbg.generated
                   */
                  protected abstract static class GeneratedCriteria {
                  }

                  /**
                   * @mbg.generated do_not_delete_during_merge
                   */
                  public int getId() {
                      return id;
                  }

                  /**
                   * @mbg.generated do_not_delete_during_merge
                   */
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
            case "MergeIntoNew" -> expectedMergeIntoNewContent();
            case "MergeIntoNewLP" -> expectedMergeIntoNewLPContent();
            case "MergeIntoOld" -> expectedMergeIntoOldContent();
            case "MergeIntoOldLP" -> expectedMergeIntoOldLPContent();
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    private String expectedMergeIntoNewContent() {
        return """
              package mbg.test.mb3.generated.flat.model;

              import java.util.List;

              public class PkfieldsExample {

                  /**
                   * @mbg.generated
                   */
                  public void reset() {
                  }

                  /**
                   * @mbg.generated
                   */
                  protected abstract static class GeneratedCriteria {
                  }

                  public void customMethod() {
                  }

                  /**
                   * @mbg.generated do_not_delete_during_merge (existing)
                   */
                  protected int id;

                  /**
                   * @mbg.generated do_not_delete_during_merge (3)
                   */
                  public int getId() {
                      // existing
                      return id;
                  }

                  /**
                   * @mbg.generated do_not_delete_during_merge (7)
                   */
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

    private String expectedMergeIntoNewLPContent() {
        return """
              package mbg.test.mb3.generated.flat.model;

              import java.util.List;

              public class PkfieldsExample {
                  /**
                   * @mbg.generated
                   */
                  public void reset() {
                  }

                  /**
                   * @mbg.generated
                   */
                  protected abstract static class GeneratedCriteria {
                  }
                 \s
                  public void customMethod() {
                  }
                 \s
                  /**
                   * @mbg.generated do_not_delete_during_merge (existing)
                   */
                  protected int id;
                 \s
                  /**
                   * @mbg.generated do_not_delete_during_merge (3)
                   */
                  public int getId() {
                      // existing
                      return id;
                  }
                 \s
                  /**
                   * @mbg.generated do_not_delete_during_merge (7)
                   */
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

              public class PkfieldsExample {

                  /**
                   * @mbg.generated do_not_delete_during_merge (existing)
                   */
                  protected int id;

                  public void customMethod() {
                  }

                  /**
                   * @mbg.generated do_not_delete_during_merge (3)
                   */
                  public int getId() {
                      // existing
                      return id;
                  }

                  /**
                   * @mbg.generated do_not_delete_during_merge (7)
                   */
                  public static class Criteria extends GeneratedCriteria {

                      protected Criteria() {
                          super();
                      }

                      public boolean isValid() {
                          return true;
                      }
                  }

                  /**
                   * @mbg.generated
                   */
                  public void reset() {
                  }

                  /**
                   * @mbg.generated
                   */
                  protected abstract static class GeneratedCriteria {
                  }
              }
              """;
    }

    private String expectedMergeIntoOldLPContent() {
        return """
              package mbg.test.mb3.generated.flat.model;

              import java.util.List;

              public class PkfieldsExample {
                  /**
                   * @mbg.generated do_not_delete_during_merge (existing)
                   */
                  protected int id;



                  public void customMethod() {
                  }

                  /**
                   * @mbg.generated do_not_delete_during_merge (3)
                   */
                  public int getId() {
                      // existing
                      return id;
                  }




                  /**
                   * @mbg.generated do_not_delete_during_merge (7)
                   */
                  public static class Criteria extends GeneratedCriteria {
                      protected Criteria() {
                          super();
                      }

                      public boolean isValid() {
                          return true;
                      }
                  }
                 \s
                  public void reset() {
                  }
                 \s
                  /**
                   * @mbg.generated
                   */
                  protected abstract static class GeneratedCriteria {
                  }

              }
              """;
    }
}
