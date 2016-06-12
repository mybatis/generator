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
package org.mybatis.generator.api.dom.java;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * The Class FullyQualifiedJavaType.
 *
 * @author Jeff Butler
 */
public class FullyQualifiedJavaType implements
        Comparable<FullyQualifiedJavaType> {
    
    /** The Constant JAVA_LANG. */
    private static final String JAVA_LANG = "java.lang"; //$NON-NLS-1$
    
    /** The int instance. */
    private static FullyQualifiedJavaType intInstance = null;
    
    /** The string instance. */
    private static FullyQualifiedJavaType stringInstance = null;
    
    /** The boolean primitive instance. */
    private static FullyQualifiedJavaType booleanPrimitiveInstance = null;
    
    /** The object instance. */
    private static FullyQualifiedJavaType objectInstance = null;
    
    /** The date instance. */
    private static FullyQualifiedJavaType dateInstance = null;
    
    /** The criteria instance. */
    private static FullyQualifiedJavaType criteriaInstance = null;
    
    /** The generated criteria instance. */
    private static FullyQualifiedJavaType generatedCriteriaInstance = null;

    /** The short name without any generic arguments. */
    private String baseShortName;

    /** The fully qualified name without any generic arguments. */
    private String baseQualifiedName;

    /** The explicitly imported. */
    private boolean explicitlyImported;
    
    /** The package name. */
    private String packageName;
    
    /** The primitive. */
    private boolean primitive;
    
    /** The is array. */
    private boolean isArray;
    
    /** The primitive type wrapper. */
    private PrimitiveTypeWrapper primitiveTypeWrapper;
    
    /** The type arguments. */
    private List<FullyQualifiedJavaType> typeArguments;

    // the following three values are used for dealing with wildcard types
    /** The wildcard type. */
    private boolean wildcardType;
    
    /** The bounded wildcard. */
    private boolean boundedWildcard;
    
    /** The extends bounded wildcard. */
    private boolean extendsBoundedWildcard;

    /**
     * Use this constructor to construct a generic type with the specified type parameters.
     *
     * @param fullTypeSpecification
     *            the full type specification
     */
    public FullyQualifiedJavaType(String fullTypeSpecification) {
        super();
        typeArguments = new ArrayList<FullyQualifiedJavaType>();
        parse(fullTypeSpecification);
    }

    /**
     * Checks if is explicitly imported.
     *
     * @return Returns the explicitlyImported.
     */
    public boolean isExplicitlyImported() {
        return explicitlyImported;
    }

    /**
     * This method returns the fully qualified name - including any generic type parameters.
     *
     * @return Returns the fullyQualifiedName.
     */
    public String getFullyQualifiedName() {
        StringBuilder sb = new StringBuilder();
        if (wildcardType) {
            sb.append('?');
            if (boundedWildcard) {
                if (extendsBoundedWildcard) {
                    sb.append(" extends "); //$NON-NLS-1$
                } else {
                    sb.append(" super "); //$NON-NLS-1$
                }

                sb.append(baseQualifiedName);
            }
        } else {
            sb.append(baseQualifiedName);
        }

        if (typeArguments.size() > 0) {
            boolean first = true;
            sb.append('<');
            for (FullyQualifiedJavaType fqjt : typeArguments) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", "); //$NON-NLS-1$
                }
                sb.append(fqjt.getFullyQualifiedName());

            }
            sb.append('>');
        }

        return sb.toString();
    }

    public String getFullyQualifiedNameWithoutTypeParameters() {
        return baseQualifiedName;
    }
    
    /**
     * Returns a list of Strings that are the fully qualified names of this type, and any generic type argument
     * associated with this type.
     *
     * @return the import list
     */
    public List<String> getImportList() {
        List<String> answer = new ArrayList<String>();
        if (isExplicitlyImported()) {
            int index = baseShortName.indexOf('.');
            if (index == -1) {
                answer.add(baseQualifiedName);
            } else {
                // an inner class is specified, only import the top
                // level class
                StringBuilder sb = new StringBuilder();
                sb.append(packageName);
                sb.append('.');
                sb.append(baseShortName.substring(0, index));
                answer.add(sb.toString());
            }
        }

        for (FullyQualifiedJavaType fqjt : typeArguments) {
            answer.addAll(fqjt.getImportList());
        }

        return answer;
    }

    /**
     * Gets the package name.
     *
     * @return Returns the packageName.
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Gets the short name.
     *
     * @return Returns the shortName - including any type arguments.
     */
    public String getShortName() {
        StringBuilder sb = new StringBuilder();
        if (wildcardType) {
            sb.append('?');
            if (boundedWildcard) {
                if (extendsBoundedWildcard) {
                    sb.append(" extends "); //$NON-NLS-1$
                } else {
                    sb.append(" super "); //$NON-NLS-1$
                }

                sb.append(baseShortName);
            }
        } else {
            sb.append(baseShortName);
        }

        if (typeArguments.size() > 0) {
            boolean first = true;
            sb.append('<');
            for (FullyQualifiedJavaType fqjt : typeArguments) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", "); //$NON-NLS-1$
                }
                sb.append(fqjt.getShortName());

            }
            sb.append('>');
        }

        return sb.toString();
    }
    
    public String getShortNameWithoutTypeArguments() {
        return baseShortName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof FullyQualifiedJavaType)) {
            return false;
        }

        FullyQualifiedJavaType other = (FullyQualifiedJavaType) obj;

        return getFullyQualifiedName().equals(other.getFullyQualifiedName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return getFullyQualifiedName().hashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getFullyQualifiedName();
    }

    /**
     * Checks if is primitive.
     *
     * @return Returns the primitive.
     */
    public boolean isPrimitive() {
        return primitive;
    }

    /**
     * Gets the primitive type wrapper.
     *
     * @return Returns the wrapperClass.
     */
    public PrimitiveTypeWrapper getPrimitiveTypeWrapper() {
        return primitiveTypeWrapper;
    }

    /**
     * Gets the int instance.
     *
     * @return the int instance
     */
    public static final FullyQualifiedJavaType getIntInstance() {
        if (intInstance == null) {
            intInstance = new FullyQualifiedJavaType("int"); //$NON-NLS-1$
        }

        return intInstance;
    }

    /**
     * Gets the new map instance.
     *
     * @return the new map instance
     */
    public static final FullyQualifiedJavaType getNewMapInstance() {
        // always return a new instance because the type may be parameterized
        return new FullyQualifiedJavaType("java.util.Map"); //$NON-NLS-1$
    }

    /**
     * Gets the new list instance.
     *
     * @return the new list instance
     */
    public static final FullyQualifiedJavaType getNewListInstance() {
        // always return a new instance because the type may be parameterized
        return new FullyQualifiedJavaType("java.util.List"); //$NON-NLS-1$
    }

    /**
     * Gets the new hash map instance.
     *
     * @return the new hash map instance
     */
    public static final FullyQualifiedJavaType getNewHashMapInstance() {
        // always return a new instance because the type may be parameterized
        return new FullyQualifiedJavaType("java.util.HashMap"); //$NON-NLS-1$
    }

    /**
     * Gets the new array list instance.
     *
     * @return the new array list instance
     */
    public static final FullyQualifiedJavaType getNewArrayListInstance() {
        // always return a new instance because the type may be parameterized
        return new FullyQualifiedJavaType("java.util.ArrayList"); //$NON-NLS-1$
    }

    /**
     * Gets the new iterator instance.
     *
     * @return the new iterator instance
     */
    public static final FullyQualifiedJavaType getNewIteratorInstance() {
        // always return a new instance because the type may be parameterized
        return new FullyQualifiedJavaType("java.util.Iterator"); //$NON-NLS-1$
    }

    /**
     * Gets the string instance.
     *
     * @return the string instance
     */
    public static final FullyQualifiedJavaType getStringInstance() {
        if (stringInstance == null) {
            stringInstance = new FullyQualifiedJavaType("java.lang.String"); //$NON-NLS-1$
        }

        return stringInstance;
    }

    /**
     * Gets the boolean primitive instance.
     *
     * @return the boolean primitive instance
     */
    public static final FullyQualifiedJavaType getBooleanPrimitiveInstance() {
        if (booleanPrimitiveInstance == null) {
            booleanPrimitiveInstance = new FullyQualifiedJavaType("boolean"); //$NON-NLS-1$
        }

        return booleanPrimitiveInstance;
    }

    /**
     * Gets the object instance.
     *
     * @return the object instance
     */
    public static final FullyQualifiedJavaType getObjectInstance() {
        if (objectInstance == null) {
            objectInstance = new FullyQualifiedJavaType("java.lang.Object"); //$NON-NLS-1$
        }

        return objectInstance;
    }

    /**
     * Gets the date instance.
     *
     * @return the date instance
     */
    public static final FullyQualifiedJavaType getDateInstance() {
        if (dateInstance == null) {
            dateInstance = new FullyQualifiedJavaType("java.util.Date"); //$NON-NLS-1$
        }

        return dateInstance;
    }

    /**
     * Gets the criteria instance.
     *
     * @return the criteria instance
     */
    public static final FullyQualifiedJavaType getCriteriaInstance() {
        if (criteriaInstance == null) {
            criteriaInstance = new FullyQualifiedJavaType("Criteria"); //$NON-NLS-1$
        }

        return criteriaInstance;
    }

    /**
     * Gets the generated criteria instance.
     *
     * @return the generated criteria instance
     */
    public static final FullyQualifiedJavaType getGeneratedCriteriaInstance() {
        if (generatedCriteriaInstance == null) {
            generatedCriteriaInstance = new FullyQualifiedJavaType(
                    "GeneratedCriteria"); //$NON-NLS-1$
        }

        return generatedCriteriaInstance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(FullyQualifiedJavaType other) {
        return getFullyQualifiedName().compareTo(other.getFullyQualifiedName());
    }

    /**
     * Adds the type argument.
     *
     * @param type
     *            the type
     */
    public void addTypeArgument(FullyQualifiedJavaType type) {
        typeArguments.add(type);
    }

    /**
     * Parses the.
     *
     * @param fullTypeSpecification
     *            the full type specification
     */
    private void parse(String fullTypeSpecification) {
        String spec = fullTypeSpecification.trim();

        if (spec.startsWith("?")) { //$NON-NLS-1$
            wildcardType = true;
            spec = spec.substring(1).trim();
            if (spec.startsWith("extends ")) { //$NON-NLS-1$
                boundedWildcard = true;
                extendsBoundedWildcard = true;
                spec = spec.substring(8);  // "extends ".length()
            } else if (spec.startsWith("super ")) { //$NON-NLS-1$
                boundedWildcard = true;
                extendsBoundedWildcard = false;
                spec = spec.substring(6);  // "super ".length()
            } else {
                boundedWildcard = false;
            }
            parse(spec);
        } else {
            int index = fullTypeSpecification.indexOf('<');
            if (index == -1) {
                simpleParse(fullTypeSpecification);
            } else {
                simpleParse(fullTypeSpecification.substring(0, index));
                int endIndex = fullTypeSpecification.lastIndexOf('>');
                if (endIndex == -1) {
                    throw new RuntimeException(getString(
                            "RuntimeError.22", fullTypeSpecification)); //$NON-NLS-1$
                }
                genericParse(fullTypeSpecification.substring(index, endIndex + 1));
            }
            
            // this is far from a perfect test for detecting arrays, but is close
            // enough for most cases.  It will not detect an improperly specified
            // array type like byte], but it will detect byte[] and byte[   ]
            // which are both valid
            isArray = fullTypeSpecification.endsWith("]"); //$NON-NLS-1$
        }
    }

    /**
     * Simple parse.
     *
     * @param typeSpecification
     *            the type specification
     */
    private void simpleParse(String typeSpecification) {
        baseQualifiedName = typeSpecification.trim();
        if (baseQualifiedName.contains(".")) { //$NON-NLS-1$
            packageName = getPackage(baseQualifiedName);
            baseShortName = baseQualifiedName
                    .substring(packageName.length() + 1);
            int index = baseShortName.lastIndexOf('.');
            if (index != -1) {
                baseShortName = baseShortName.substring(index + 1);
            }
            
            if (JAVA_LANG.equals(packageName)) { //$NON-NLS-1$
                explicitlyImported = false;
            } else {
                explicitlyImported = true;
            }
        } else {
            baseShortName = baseQualifiedName;
            explicitlyImported = false;
            packageName = ""; //$NON-NLS-1$

            if ("byte".equals(baseQualifiedName)) { //$NON-NLS-1$
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getByteInstance();
            } else if ("short".equals(baseQualifiedName)) { //$NON-NLS-1$
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getShortInstance();
            } else if ("int".equals(baseQualifiedName)) { //$NON-NLS-1$
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper
                        .getIntegerInstance();
            } else if ("long".equals(baseQualifiedName)) { //$NON-NLS-1$
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getLongInstance();
            } else if ("char".equals(baseQualifiedName)) { //$NON-NLS-1$
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper
                        .getCharacterInstance();
            } else if ("float".equals(baseQualifiedName)) { //$NON-NLS-1$
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getFloatInstance();
            } else if ("double".equals(baseQualifiedName)) { //$NON-NLS-1$
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getDoubleInstance();
            } else if ("boolean".equals(baseQualifiedName)) { //$NON-NLS-1$
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper
                        .getBooleanInstance();
            } else {
                primitive = false;
                primitiveTypeWrapper = null;
            }
        }
    }

    /**
     * Generic parse.
     *
     * @param genericSpecification
     *            the generic specification
     */
    private void genericParse(String genericSpecification) {
        int lastIndex = genericSpecification.lastIndexOf('>');
        if (lastIndex == -1) {
            // shouldn't happen - should be caught already, but just in case...
            throw new RuntimeException(getString(
                    "RuntimeError.22", genericSpecification)); //$NON-NLS-1$
        }
        String argumentString = genericSpecification.substring(1, lastIndex);
        // need to find "," outside of a <> bounds
        StringTokenizer st = new StringTokenizer(argumentString, ",<>", true); //$NON-NLS-1$
        int openCount = 0;
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if ("<".equals(token)) { //$NON-NLS-1$
                sb.append(token);
                openCount++;
            } else if (">".equals(token)) { //$NON-NLS-1$
                sb.append(token);
                openCount--;
            } else if (",".equals(token)) { //$NON-NLS-1$
                if (openCount == 0) {
                    typeArguments
                            .add(new FullyQualifiedJavaType(sb.toString()));
                    sb.setLength(0);
                } else {
                    sb.append(token);
                }
            } else {
                sb.append(token);
            }
        }

        if (openCount != 0) {
            throw new RuntimeException(getString(
                    "RuntimeError.22", genericSpecification)); //$NON-NLS-1$
        }

        String finalType = sb.toString();
        if (stringHasValue(finalType)) {
            typeArguments.add(new FullyQualifiedJavaType(finalType));
        }
    }

    /**
     * Returns the package name of a fully qualified type.
     * 
     * This method calculates the package as the part of the fully qualified name up to, but not including, the last
     * element. Therefore, it does not support fully qualified inner classes. Not totally fool proof, but correct in
     * most instances.
     *
     * @param baseQualifiedName
     *            the base qualified name
     * @return the package
     */
    private static String getPackage(String baseQualifiedName) {
        int index = baseQualifiedName.lastIndexOf('.');
        return baseQualifiedName.substring(0, index);
    }

    /**
     * Checks if is array.
     *
     * @return true, if is array
     */
    public boolean isArray() {
        return isArray;
    }
}
