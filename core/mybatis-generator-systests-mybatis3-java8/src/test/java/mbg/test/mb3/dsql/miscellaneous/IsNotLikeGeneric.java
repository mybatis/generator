package mbg.test.mb3.dsql.miscellaneous;

import java.util.function.Supplier;

import org.mybatis.dynamic.sql.AbstractSingleValueCondition;

// TODO - remove.  This should be in the base library
public class IsNotLikeGeneric<T> extends AbstractSingleValueCondition<T> {

    protected IsNotLikeGeneric(Supplier<T> valueSupplier) {
        super(valueSupplier);
    }

    @Override
    public String renderCondition(String columnName, String placeholder) {
        return columnName + " not like " + placeholder; //$NON-NLS-1$
    }

    public static <T> IsNotLikeGeneric<T> of(Supplier<T> valueSupplier) {
        return new IsNotLikeGeneric<>(valueSupplier);
    }

    public static <T> IsNotLikeGeneric<T> of(T value) {
        return of(() -> value);
    }
}
