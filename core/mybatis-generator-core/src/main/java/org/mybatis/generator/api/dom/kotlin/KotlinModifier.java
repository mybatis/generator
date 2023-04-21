/*
 *    Copyright 2006-2023 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.api.dom.kotlin;

/**
 * Nearly complete list of Kotlin modifiers. Modifiers are in the order suggested by Kotlin
 * coding conventions for reference, but this order is not enforced by the library. Users must add modifiers
 * in the correct order.
 */
public enum KotlinModifier {

    PUBLIC("public"), //$NON-NLS-1$
    PROTECTED("protected"), //$NON-NLS-1$
    PRIVATE("private"), //$NON-NLS-1$
    INTERNAL("internal"), //$NON-NLS-1$
    EXPECT("expect"), //$NON-NLS-1$
    ACTUAL("actual"), //$NON-NLS-1$
    FINAL("final"), //$NON-NLS-1$
    OPEN("open"), //$NON-NLS-1$
    ABSTRACT("abstract"), //$NON-NLS-1$
    SEALED("sealed"), //$NON-NLS-1$
    CONST("const"), //$NON-NLS-1$
    EXTERNAL("external"), //$NON-NLS-1$
    OVERRIDE("override"), //$NON-NLS-1$
    LATE_INIT("lateinit"), //$NON-NLS-1$
    TAILREC("tailrec"), //$NON-NLS-1$
    VARARG("vararg"), //$NON-NLS-1$
    SUSPEND("suspend"), //$NON-NLS-1$
    INNER("inner"), //$NON-NLS-1$
    INFIX("infix"), //$NON-NLS-1$
    OPERATOR("operator"), //$NON-NLS-1$
    DATA("data"); //$NON-NLS-1$

    private final String value;

    KotlinModifier(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
