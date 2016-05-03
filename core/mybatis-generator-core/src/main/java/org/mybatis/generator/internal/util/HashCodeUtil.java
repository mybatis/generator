/**
 *    Copyright 2006-2016 the original author or authors.
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

import java.lang.reflect.Array;

/**
 * This class is from javapractices.com:
 * 
 * http://www.javapractices.com/Topic28.cjp
 * 
 * Collected methods which allow easy implementation of <code>hashCode</code>.
 * 
 * Example use case:
 * 
 * <pre>
 * public int hashCode() {
 *     int result = HashCodeUtil.SEED;
 *     //collect the contributions of various fields
 *     result = HashCodeUtil.hash(result, fPrimitive);
 *     result = HashCodeUtil.hash(result, fObject);
 *     result = HashCodeUtil.hash(result, fArray);
 *     return result;
 * }
 * </pre>
 */
public final class HashCodeUtil {

    /**
     * An initial value for a <code>hashCode</code>, to which is added
     * contributions from fields. Using a non-zero value decreases collisons of
     * <code>hashCode</code> values.
     */
    public static final int SEED = 23;

    /**
     * booleans.
     *
     * @param aSeed
     *            the a seed
     * @param aBoolean
     *            the a boolean
     * @return the int
     */
    public static int hash(int aSeed, boolean aBoolean) {
        return firstTerm(aSeed) + (aBoolean ? 1 : 0);
    }

    /**
     * chars.
     *
     * @param aSeed
     *            the a seed
     * @param aChar
     *            the a char
     * @return the int
     */
    public static int hash(int aSeed, char aChar) {
        return firstTerm(aSeed) + aChar;
    }

    /**
     * ints.
     *
     * @param aSeed
     *            the a seed
     * @param aInt
     *            the a int
     * @return the int
     */
    public static int hash(int aSeed, int aInt) {
        /*
         * Implementation Note Note that byte and short are handled by this
         * method, through implicit conversion.
         */
        return firstTerm(aSeed) + aInt;
    }

    /**
     * longs.
     *
     * @param aSeed
     *            the a seed
     * @param aLong
     *            the a long
     * @return the int
     */
    public static int hash(int aSeed, long aLong) {
        return firstTerm(aSeed) + (int) (aLong ^ (aLong >>> 32));
    }

    /**
     * floats.
     *
     * @param aSeed
     *            the a seed
     * @param aFloat
     *            the a float
     * @return the int
     */
    public static int hash(int aSeed, float aFloat) {
        return hash(aSeed, Float.floatToIntBits(aFloat));
    }

    /**
     * doubles.
     *
     * @param aSeed
     *            the a seed
     * @param aDouble
     *            the a double
     * @return the int
     */
    public static int hash(int aSeed, double aDouble) {
        return hash(aSeed, Double.doubleToLongBits(aDouble));
    }

    /**
     * <code>aObject</code> is a possibly-null object field, and possibly an array.
     * 
     * If <code>aObject</code> is an array, then each element may be a primitive or a possibly-null object.
     *
     * @param aSeed
     *            the a seed
     * @param aObject
     *            the a object
     * @return the int
     */
    public static int hash(int aSeed, Object aObject) {
        int result = aSeed;
        if (aObject == null) {
            result = hash(result, 0);
        } else if (!isArray(aObject)) {
            result = hash(result, aObject.hashCode());
        } else {
            int length = Array.getLength(aObject);
            for (int idx = 0; idx < length; ++idx) {
                Object item = Array.get(aObject, idx);
                // recursive call!
                result = hash(result, item);
            }
        }
        return result;
    }

    // / PRIVATE ///
    /** The odd prime number. */
    private static final int fODD_PRIME_NUMBER = 37;

    /**
     * First term.
     *
     * @param aSeed
     *            the a seed
     * @return the int
     */
    private static int firstTerm(int aSeed) {
        return fODD_PRIME_NUMBER * aSeed;
    }

    /**
     * Checks if is array.
     *
     * @param aObject
     *            the a object
     * @return true, if is array
     */
    private static boolean isArray(Object aObject) {
        return aObject.getClass().isArray();
    }
}
