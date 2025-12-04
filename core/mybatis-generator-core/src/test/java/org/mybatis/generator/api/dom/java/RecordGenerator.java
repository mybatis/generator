package org.mybatis.generator.api.dom.java;

import java.io.Serializable;
import java.util.List;

/**
 * Allowed:
 *   super interfaces
 *   inner classes
 *   inner interfaces
 *   inner records
 *   inner enums
 *   methods
 *   constructors
 *   static initialization blocks
 *   static fields
 *   type parameters
 *
 * <p>Not allowed:
 *   super classes
 *   abstract
 *   static (even on inner records)
 *   instance fields
 *
 *
 *
 */
public class RecordGenerator {

    public record Record<T>(int id, String description, T theT) implements Serializable {
        private static final int f;

        static {
            f = 22;
        }

        public Record(int id, String description) {
            this(id, description, null);
        }

        public static int ff() {
            return f;
        }

        public int add(int a, int b) {
            return a + b + f;
        }

        public String sayIt() {
            return id + " " + description;
        }

        public static class Fred implements Flintstone {

        }

        public interface Flintstone {

        }

        public enum MyEnum {
            A, B, C
        }

        public record InnerRecord(int id, String description) {}
    }

    public static List<CompilationUnit> generateTestClasses() {

        TopLevelRecord topLevelRecord = new TopLevelRecord("foo.bar.Record");
        topLevelRecord.setVisibility(JavaVisibility.PUBLIC);
        topLevelRecord.addParameter(new Parameter(new FullyQualifiedJavaType("int"), "id"));
        topLevelRecord.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "description"));

        return List.of(topLevelRecord);
    }
}
