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
