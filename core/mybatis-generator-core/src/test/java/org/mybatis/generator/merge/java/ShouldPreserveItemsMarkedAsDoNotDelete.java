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

/**
 * This test case verifies that generated items with the special text
 * "do_not_delete_during_merge" survive the merge. This is something
 * supported in the legacy example classes.
 */
public class ShouldPreserveItemsMarkedAsDoNotDelete extends JavaMergeTestCase {
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
                   * @mbg.generated
                   */
                  protected List<Criteria> oredCriteria;

                  /**
                   * @mbg.generated
                   */
                  public PkfieldsExample() {
                      oredCriteria = new ArrayList<>();
                  }

                  public void customMethod() {
                  }

                  /**
                   * @mbg.generated do_not_delete_during_merge
                   */
                  public int getId() {
                      // existing
                      return id;
                  }

                  /**
                   * @mbg.generated
                   */
                  public List<Criteria> getOredCriteria() {
                      return oredCriteria;
                  }

                  /**
                   * @mbg.generated
                   */
                  public void clear() {
                      oredCriteria.clear();
                  }

                  /**
                   * @mbg.generated
                   */
                  protected abstract static class GeneratedCriteria {
                      protected List<Criterion> allCriteria;
                  }

                  /**
                   * @mbg.generated do_not_delete_during_merge
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
    public String expectedContentAfterMerge(String parameter, JavaMergerFactory.PrinterConfiguration printerConfiguration) {
        return switch (printerConfiguration) {
            case ECLIPSE -> expectedEclipseContent();
            case LEXICAL_PRESERVING -> expectedLexicalPreservingContent();
        };
    }

    private String expectedEclipseContent() {
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
                   * @mbg.generated do_not_delete_during_merge
                   */
                  public int getId() {
                      // existing
                      return id;
                  }

                  /**
                   * @mbg.generated do_not_delete_during_merge
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

    private String expectedLexicalPreservingContent() {
        // TODO - this is completely wrong. The javadoc should be preserved, but is lost or relocated
        return """
              package mbg.test.mb3.generated.flat.model;

              import java.util.List;

              public class PkfieldsExample {
                  /**
                   * @mbg.generated do_not_delete_during_merge
                   */
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
                  protected int id;
                 \s
                  public int getId() {
                      // existing
                      return id;
                  }
                 \s
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

    @Override
    public List<JavaMergerFactory.PrinterConfiguration> printerConfigurations() {
        return List.of(JavaMergerFactory.PrinterConfiguration.ECLIPSE,
                JavaMergerFactory.PrinterConfiguration.LEXICAL_PRESERVING);
    }
}
