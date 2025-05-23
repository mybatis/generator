package org.mybatis.generator.api.dom.java;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mybatis.generator.api.dom.java.render.TopLevelRecordRenderer;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.Arrays;
import java.util.Objects;

/**
 * Allowed:
 *   super interfaces
 *   inner classes
 *   inner interfaces
 *   inner records
 *   inner enums
 *   methods
 *   constructors
 *   initialization blocks
 *   type parameters
 *
 * Not allowed:
 *   super classes
 *   abstract
 *   static (even on inner records)
 *   instance fields
 *
 *
 *
 */
public class RecordTest {

    public static void ff() {

    }

    public int add(int a, int b) {
        return a + b;
    }

    public enum MyEnum {
        A, B, C
    }

    public class Fred {

    }

    public interface Barney {

    }

    public record Verge() {

    }

    @Test
    void testBasicRecord() throws IOException {

        TopLevelRecord topLevelRecord = new TopLevelRecord("foo.bar.Record");
        topLevelRecord.setVisibility(JavaVisibility.PUBLIC);
        topLevelRecord.addParameter(new Parameter(new FullyQualifiedJavaType("int"), "id"));
        topLevelRecord.addParameter(new Parameter(new FullyQualifiedJavaType("String"), "description"));

        String rendered = new TopLevelRecordRenderer().render(topLevelRecord);
//        assertThat(rendered).isEqualTo("for.bar.Record");

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        JavaFileObject file = new JavaSourceFromString("foo.bar.Record", rendered);
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
        JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, compilationUnits);
        boolean success = task.call();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            System.out.println(diagnostic.getCode());
            System.out.println(diagnostic.getKind());
            System.out.println(diagnostic.getPosition());
            System.out.println(diagnostic.getStartPosition());
            System.out.println(diagnostic.getEndPosition());
            System.out.println(diagnostic.getSource());
            System.out.println(diagnostic.getMessage(null));

        }
        System.out.println("Success: " + success);
    }

    static class JavaSourceFromString extends SimpleJavaFileObject {
        final String code;

        public JavaSourceFromString(String name, String code) {
            super(URI.create("string:///" + name.replace('.','/') + Kind.SOURCE.extension),Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }
}
