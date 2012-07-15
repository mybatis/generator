package org.mybatis.generator.internal.types;

public class Jdbc4Types {
    // these are added manually until we move to JDK 6
    // TODO - remove after JDK 6 and use the java.sql.Types constants instead
    public static final int LONGNVARCHAR = -16;
    public static final int NVARCHAR = -9;
    public static final int NCHAR = -15;
    public static final int NCLOB = 2011;
}
