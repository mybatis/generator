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
package org.mybatis.generator.eclipse.tests.harness.summary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractSummary extends MatcherSupport {

    private Map<String, AnnotationSummary> annotationSummaries = new HashMap<String, AnnotationSummary>();
    private Map<String, ClassSummary> classSummaries = new HashMap<String, ClassSummary>();
    private Map<String, EnumSummary> enumSummaries = new HashMap<String, EnumSummary>();
    private Map<String, InterfaceSummary> interfaceSummaries = new HashMap<String, InterfaceSummary>();

    @Override
    public ClassSummary getClassSummary(String name) {
        return classSummaries.get(name);
    }

    @Override
    public int getClassCount() {
        return classSummaries.size();
    }

    @Override
    public InterfaceSummary getInterfaceSummary(String name) {
        return interfaceSummaries.get(name);
    }

    @Override
    public int getInterfaceCount() {
        return interfaceSummaries.size();
    }

    @Override
    public EnumSummary getEnumSummary(String name) {
        return enumSummaries.get(name);
    }

    @Override
    public int getEnumCount() {
        return enumSummaries.size();
    }

    @Override
    public AnnotationSummary getAnnotationSummary(String name) {
        return annotationSummaries.get(name);
    }

    @Override
    public int getAnnotationCount() {
        return annotationSummaries.size();
    }
    
    protected static abstract class AbstractSummaryBuilder<T extends AbstractSummaryBuilder<T>> {
        
        protected T withAnnotationSummaries(List<AnnotationSummary> annotationSummaries) {
            fillMap(annotationSummaries, summary().annotationSummaries);
            return getThis();
        }

        protected T withClassSummaries(List<ClassSummary> classSummaries) {
            fillMap(classSummaries, summary().classSummaries);
            return getThis();
        }

        protected T withEnumSummaries(List<EnumSummary> enumSummaries) {
            fillMap(enumSummaries, summary().enumSummaries);
            return getThis();
        }

        protected T withInterfaceSummaries(List<InterfaceSummary> interfaceSummaries) {
            fillMap(interfaceSummaries, summary().interfaceSummaries);
            return getThis();
        }
        
        private <S extends AbstractBodyElementSummary> void fillMap(List<S> items, Map<String, S> map) {
            for (S item : items) {
                map.put(item.getName(), item);
            }
        }
        
        protected abstract AbstractSummary summary();
        protected abstract T getThis();
    }
}
