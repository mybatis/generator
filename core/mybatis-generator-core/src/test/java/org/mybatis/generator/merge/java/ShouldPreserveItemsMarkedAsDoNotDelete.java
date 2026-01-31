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

import org.mybatis.generator.merge.MergeTestCase;

/**
 * This test case verifies that generated items with the special text
 * "do_not_delete_during_merge" survive the merge. This is something that is
 * supported in the legacy model classes.
 */
public class ShouldPreserveItemsMarkedAsDoNotDelete extends MergeTestCase {
    @Override
    public String existingContent(String parameter) {
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
                  protected List<Criteria> oredCriteria;

                  /**
                   * @mbg.generated
                   */
                  public PkfieldsExample() {
                      oredCriteria = new ArrayList<>();
                  }

                  /**
                   * @mbg.generated do_not_delete_during_merge
                   */
                  public int getId() {
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

                  public void customMethod() {
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
    public String expectedContentAfterMerge(String parameter) {
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

                  /**
                   * @mbg.generated do_not_delete_during_merge
                   */
                  protected int id;

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

                      public boolean isValid() {
                          return true;
                      }
                  }

                  public void customMethod() {
                  }
              }
              """;
    }
}
