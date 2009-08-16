/*
 *  Copyright 2005 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.ibatis.ibator.api.dom.java;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeff Butler
 */
public class FullyQualifiedJavaType implements Comparable<FullyQualifiedJavaType> {
    private static FullyQualifiedJavaType intInstance = null;
    private static FullyQualifiedJavaType stringInstance = null;
    private static FullyQualifiedJavaType booleanPrimitiveInstance = null;
    private static FullyQualifiedJavaType objectInstance = null;
    private static FullyQualifiedJavaType dateInstance = null;
    private static FullyQualifiedJavaType criteriaInstance = null;
    
    private String baseShortName;
    
    //  this is the short name including the parameterized types and wildcards
    private String calculatedShortName;
    
    private String fullyQualifiedName;
    private boolean explicitlyImported;
    private String packageName;
    private boolean primitive;
    private PrimitiveTypeWrapper primitiveTypeWrapper;
    private List<FullyQualifiedJavaType> typeArguments;
    
    /**
     * Use this constructor to construct a generic type with the specified
     * type parameters
     * 
     * @param fullyQualifiedName
     */
    public FullyQualifiedJavaType(String fullyQualifiedName) {
        super();
        typeArguments = new ArrayList<FullyQualifiedJavaType>();
        this.fullyQualifiedName = fullyQualifiedName;
        
        int lastIndex = fullyQualifiedName.lastIndexOf('.');
        if (lastIndex == -1) {
            baseShortName = fullyQualifiedName;
            explicitlyImported = false;
            packageName = ""; //$NON-NLS-1$
            
            if ("byte".equals(fullyQualifiedName)) { //$NON-NLS-1$
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getByteInstance();
            } else if ("short".equals(fullyQualifiedName)) { //$NON-NLS-1$
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getShortInstance();
            } else if ("int".equals(fullyQualifiedName)) { //$NON-NLS-1$
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getIntegerInstance();
            } else if ("long".equals(fullyQualifiedName)) { //$NON-NLS-1$
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getLongInstance();
            } else if ("char".equals(fullyQualifiedName)) { //$NON-NLS-1$
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getCharacterInstance();
            } else if ("float".equals(fullyQualifiedName)) { //$NON-NLS-1$
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getFloatInstance();
            } else if ("double".equals(fullyQualifiedName)) { //$NON-NLS-1$
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getDoubleInstance();
            } else if ("boolean".equals(fullyQualifiedName)) { //$NON-NLS-1$
                primitive = true;
                primitiveTypeWrapper = PrimitiveTypeWrapper.getBooleanInstance();
            } else {
                primitive = false;
                primitiveTypeWrapper = null;
            }
        } else {
            baseShortName = fullyQualifiedName.substring(lastIndex + 1);
            packageName = fullyQualifiedName.substring(0, lastIndex);
            if ("java.lang".equals(packageName)) { //$NON-NLS-1$
                explicitlyImported = false;
            } else {
                explicitlyImported = true;
            }
        }
        
        calculatedShortName = baseShortName;
    }
    
    /**
     * @return Returns the explicitlyImported.
     */
    public boolean isExplicitlyImported() {
        return explicitlyImported;
    }
    /**
     * This method returns the fully qualified name that is suitable
     * for an import statement (i.e. - without the generics specified)
     * 
     * @return Returns the fullyQualifiedName.
     */
    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }
    /**
     * @return Returns the packageName.
     */
    public String getPackageName() {
        return packageName;
    }
    /**
     * @return Returns the shortName.
     */
    public String getShortName() {
        return calculatedShortName;
    }
    
    /* (non-Javadoc)
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
		
        return fullyQualifiedName.equals(other.fullyQualifiedName);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
	@Override
    public int hashCode() {
        return fullyQualifiedName.hashCode();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
	@Override
    public String toString() {
        return fullyQualifiedName;
    }
    
    /**
     * @return Returns the primitive.
     */
    public boolean isPrimitive() {
        return primitive;
    }
    
    /**
     * @return Returns the wrapperClass.
     */
    public PrimitiveTypeWrapper getPrimitiveTypeWrapper() {
        return primitiveTypeWrapper;
    }
    
    public static final FullyQualifiedJavaType getIntInstance() {
        if (intInstance == null) {
            intInstance = new FullyQualifiedJavaType("int"); //$NON-NLS-1$
        }
        
        return intInstance;
    }

    public static final FullyQualifiedJavaType getNewMapInstance() {
        // always return a new instance because the type may be parameterized
        return new FullyQualifiedJavaType("java.util.Map"); //$NON-NLS-1$
    }

    public static final FullyQualifiedJavaType getNewListInstance() {
        // always return a new instance because the type may be parameterized
        return new FullyQualifiedJavaType("java.util.List"); //$NON-NLS-1$
    }

    public static final FullyQualifiedJavaType getNewHashMapInstance() {
        // always return a new instance because the type may be parameterized
        return new FullyQualifiedJavaType("java.util.HashMap"); //$NON-NLS-1$
    }

    public static final FullyQualifiedJavaType getNewArrayListInstance() {
        // always return a new instance because the type may be parameterized
        return new FullyQualifiedJavaType("java.util.ArrayList"); //$NON-NLS-1$
    }

    public static final FullyQualifiedJavaType getNewIteratorInstance() {
        // always return a new instance because the type may be parameterized
        return new FullyQualifiedJavaType("java.util.Iterator"); //$NON-NLS-1$
    }

    public static final FullyQualifiedJavaType getStringInstance() {
        if (stringInstance == null) {
            stringInstance = new FullyQualifiedJavaType("java.lang.String"); //$NON-NLS-1$
        }
        
        return stringInstance;
    }
    
    public static final FullyQualifiedJavaType getBooleanPrimitiveInstance() {
        if (booleanPrimitiveInstance == null) {
            booleanPrimitiveInstance = new FullyQualifiedJavaType("boolean"); //$NON-NLS-1$
        }
        
        return booleanPrimitiveInstance;
    }
    
    public static final FullyQualifiedJavaType getObjectInstance() {
        if (objectInstance == null) {
            objectInstance = new FullyQualifiedJavaType("java.lang.Object"); //$NON-NLS-1$
        }
        
        return objectInstance;
    }

    public static final FullyQualifiedJavaType getDateInstance() {
        if (dateInstance == null) {
            dateInstance = new FullyQualifiedJavaType("java.util.Date"); //$NON-NLS-1$
        }
        
        return dateInstance;
    }

    public static final FullyQualifiedJavaType getCriteriaInstance() {
        if (criteriaInstance == null) {
            criteriaInstance = new FullyQualifiedJavaType("Criteria"); //$NON-NLS-1$
        }
        
        return criteriaInstance;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(FullyQualifiedJavaType other) {
        return fullyQualifiedName.compareTo(other.fullyQualifiedName);
    }
    
    public void addTypeArgument(FullyQualifiedJavaType type) {
        typeArguments.add(type);
        
        StringBuilder sb = new StringBuilder();
        sb.append(baseShortName);
        sb.append('<');
        
        boolean comma = false;
        for (FullyQualifiedJavaType fqjt : typeArguments) {
            if (comma) {
                sb.append(", "); //$NON-NLS-1$
            } else {
                comma = true;
            }
            sb.append(fqjt.getShortName());
        }
        sb.append('>');
        calculatedShortName = sb.toString();
    }
}
