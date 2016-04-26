package mbg.domtest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Add this annotation to a generator class to ignore the class.  This aids in
 * developing tests for new features so we don't break the build when working on
 * the test first.
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface IgnoreDomTest {
    /**
     * The reason why the test is ignored (optional)
     */
    String value() default "";
}
