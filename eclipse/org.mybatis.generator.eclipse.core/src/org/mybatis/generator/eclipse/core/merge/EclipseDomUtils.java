/*
 *  Copyright 2012 The MyBatis.org Team
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
package org.mybatis.generator.eclipse.core.merge;

import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.WildcardType;

/**
 * Convenience methods for working with Eclipse DOM
 * 
 * @author Jeff Butler
 * 
 */
public class EclipseDomUtils {

    public static boolean importDeclarationsMatch(ImportDeclaration import1,
            ImportDeclaration import2) {
        if (import1 == null || import2 == null) {
            return import1 == null && import2 == null;
        }

        boolean rc = import1.isStatic() == import2.isStatic();

        if (rc) {
            rc = import1.getName().getFullyQualifiedName()
                    .equals(import2.getName().getFullyQualifiedName());
        }

        return rc;
    }

    public static boolean typesMatch(Type type1, Type type2) {

        if (type1 == null || type2 == null) {
            return type1 == null && type2 == null;
        }

        boolean rc = false;

        if (type1.isSimpleType() && type2.isSimpleType()) {
            return simpleTypesMatch((SimpleType) type1, (SimpleType) type2);
        } else if (type1.isParameterizedType() && type2.isParameterizedType()) {
            return parameterizedTypesMatch((ParameterizedType) type1,
                    (ParameterizedType) type2);
        } else if (type1.isPrimitiveType() && type2.isPrimitiveType()) {
            return primitiveTypesMatch((PrimitiveType) type1,
                    (PrimitiveType) type2);
        } else if (type1.isArrayType() && type2.isArrayType()) {
            return arrayTypesMatch((ArrayType) type1, (ArrayType) type2);
        } else if (type1.isUnionType() && type2.isUnionType()) {
            return unionTypesMatch((UnionType) type1, (UnionType) type2);
        } else if (type1.isQualifiedType() && type2.isQualifiedType()) {
            return qualifiedTypesMatch((QualifiedType) type1,
                    (QualifiedType) type2);
        } else if (type1.isWildcardType() && type2.isWildcardType()) {
            return wildcardTypesMatch((WildcardType) type1,
                    (WildcardType) type2);
        }

        return rc;
    }

    public static boolean wildcardTypesMatch(WildcardType type1,
            WildcardType type2) {
        boolean rc = type1.isUpperBound() == type2.isUpperBound();

        if (rc) {
            rc = typesMatch(type1.getBound(), type2.getBound());
        }

        return rc;
    }

    public static boolean simpleTypesMatch(SimpleType type1, SimpleType type2) {
        return type1.getName().getFullyQualifiedName()
                .equals(type2.getName().getFullyQualifiedName());
    }

    public static boolean primitiveTypesMatch(PrimitiveType type1,
            PrimitiveType type2) {
        return type1.getPrimitiveTypeCode().toString()
                .equals(type2.getPrimitiveTypeCode().toString());
    }

    public static boolean arrayTypesMatch(ArrayType type1, ArrayType type2) {
        boolean rc = type1.getDimensions() == type2.getDimensions();

        if (rc) {
            rc = typesMatch(type1.getComponentType(), type2.getComponentType());
        }

        return rc;
    }

    public static boolean unionTypesMatch(UnionType type1, UnionType type2) {

        boolean rc = type1.types().size() == type2.types().size();

        if (rc) {
            for (int i = 0; i < type1.types().size(); i++) {
                rc = typesMatch((Type) type1.types().get(i), (Type) type2
                        .types().get(i));
                if (!rc) {
                    break;
                }
            }
        }

        return rc;
    }

    public static boolean qualifiedTypesMatch(QualifiedType type1,
            QualifiedType type2) {

        boolean rc = type1.getName().getFullyQualifiedName()
                .equals(type2.getName().getFullyQualifiedName());

        if (rc) {
            rc = typesMatch(type1.getQualifier(), type2.getQualifier());
        }

        return rc;
    }

    public static boolean parameterizedTypesMatch(ParameterizedType type1,
            ParameterizedType type2) {
        // check base type first
        boolean rc = typesMatch(type1.getType(), type2.getType());

        if (rc) {
            // base types match, check type argument list length
            rc = type1.typeArguments().size() == type2.typeArguments().size();

            // length matches, check each type argument
            if (rc) {
                for (int i = 0; i < type1.typeArguments().size(); i++) {
                    rc = typesMatch((Type) type1.typeArguments().get(i),
                            (Type) type2.typeArguments().get(i));
                    if (!rc) {
                        break;
                    }
                }
            }
        }

        return rc;
    }
}
