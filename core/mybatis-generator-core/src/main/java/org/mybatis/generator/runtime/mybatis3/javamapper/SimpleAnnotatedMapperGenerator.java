/*
 *    Copyright 2006-2026 the original author or authors.
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
package org.mybatis.generator.runtime.mybatis3.javamapper;

import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedDeleteByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedInsertMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedSelectAllMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedSelectByPrimaryKeyMethodGenerator;
import org.mybatis.generator.runtime.mybatis3.javamapper.elements.annotated.AnnotatedUpdateByPrimaryKeyWithoutBLOBsMethodGenerator;

public class SimpleAnnotatedMapperGenerator extends SimpleJavaMapperGenerator {

    public SimpleAnnotatedMapperGenerator(Builder builder) {
        super(builder);
    }

    @Override
    protected void addDeleteByPrimaryKeyMethod(Interface interfaze) {
        initializeSubBuilder(new AnnotatedDeleteByPrimaryKeyMethodGenerator.Builder())
                .isSimple(true)
                .build()
                .execute(interfaze);
    }

    @Override
    protected void addInsertMethod(Interface interfaze) {
        initializeSubBuilder(new AnnotatedInsertMethodGenerator.Builder())
                .isSimple(true)
                .build()
                .execute(interfaze);
    }

    @Override
    protected void addSelectByPrimaryKeyMethod(Interface interfaze) {
        initializeSubBuilder(new AnnotatedSelectByPrimaryKeyMethodGenerator.Builder())
                .isSimple(true)
                .useResultMapIfAvailable(false)
                .build()
                .execute(interfaze);
    }

    @Override
    protected void addSelectAllMethod(Interface interfaze) {
        initializeSubBuilder(new AnnotatedSelectAllMethodGenerator.Builder())
                .build()
                .execute(interfaze);
    }

    @Override
    protected void addUpdateByPrimaryKeyMethod(Interface interfaze) {
        initializeSubBuilder(new AnnotatedUpdateByPrimaryKeyWithoutBLOBsMethodGenerator.Builder())
                .isSimple(true)
                .build()
                .execute(interfaze);
    }

    public static class Builder extends SimpleJavaMapperGenerator.Builder {
        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public SimpleAnnotatedMapperGenerator build() {
            return new SimpleAnnotatedMapperGenerator(this);
        }
    }
}
