package mbg.test.mb3.dsql.miscellaneous;

import java.util.function.Supplier;

import org.mybatis.dynamic.sql.AbstractSingleValueCondition;

// TODO - remove.  This should be in the base library
public class IsLikeGeneric<T> extends AbstractSingleValueCondition<T> {

    protected IsLikeGeneric(Supplier<T> valueSupplier) {
        super(valueSupplier);
    }

    @Override
    public String renderCondition(String columnName, String placeholder) {
        return columnName + " like " + placeholder; //$NON-NLS-1$
    }

    public static <T> IsLikeGeneric<T> of(Supplier<T> valueSupplier) {
        return new IsLikeGeneric<>(valueSupplier);
    }

    public static <T> IsLikeGeneric<T> of(T value) {
        return of(() -> value);
    }
}
