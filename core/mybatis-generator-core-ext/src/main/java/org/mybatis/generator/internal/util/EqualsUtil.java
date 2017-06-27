/**
 *    Copyright 2006-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.internal.util;

/**
 * This class is from javapractices.com:
 * 
 * <p>http://www.javapractices.com/Topic17.cjp
 * 
 * <p>Collected methods which allow easy implementation of <code>equals</code>.
 * 
 * <p>Example use case in a class called Car:
 * 
 * <pre>
 * public boolean equals(Object that) {
 *     if (this == that)
 *         return true;
 *     if (!(that instanceof Car))
 *         return false;
 *     Car thatCar = (Car) that;
 *     return EqualsUtil.areEqual(this.fName, that.fName)
 *             &amp;&amp; EqualsUtil.areEqual(this.fNumDoors, that.fNumDoors)
 *             &amp;&amp; EqualsUtil.areEqual(this.fGasMileage, that.fGasMileage)
 *             &amp;&amp; EqualsUtil.areEqual(this.fColor, that.fColor)
 *             &amp;&amp; Arrays.equals(this.fMaintenanceChecks, that.fMaintenanceChecks); //array!
 * }
 * </pre>
 * 
 * <em>Arrays are not handled by this class</em>. This is because the
 * <code>Arrays.equals</code> methods should be used for array fields.
 */
public final class EqualsUtil {

    public static boolean areEqual(boolean b1, boolean b2) {
        return b1 == b2;
    }

    public static boolean areEqual(char c1, char c2) {
        return c1 == c2;
    }

    public static boolean areEqual(long l1, long l2) {
        /*
         * Implementation Note Note that byte, short, and int are handled by
         * this method, through implicit conversion.
         */
        return l1 == l2;
    }

    public static boolean areEqual(float f1, float f2) {
        return Float.floatToIntBits(f1) == Float.floatToIntBits(f2);
    }

    public static boolean areEqual(double d1, double d2) {
        return Double.doubleToLongBits(d1) == Double.doubleToLongBits(d2);
    }

    /**
     * Possibly-null object field.
     * 
     * <p>Includes type-safe enumerations and collections, but does not include arrays. See class comment.
     *
     * @param o1
     *            the first object
     * @param o2
     *            the second object
     * @return true, if the objects are equals (meaning that the equals() method returns true)
     */
    public static boolean areEqual(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }
}
