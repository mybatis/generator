/**
 *    Copyright 2006-2018 the original author or authors.
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

import java.util.StringJoiner;
import java.util.stream.Collector;

public interface CustomCollectors {

    /**
     * Returns a {@code Collector} similar to the standard JDK joining collector, except that
     * this collector returns an empty string if there are no elements to collect.
     *
     * @param delimiter the delimiter to be used between each element
     * @param  prefix the sequence of characters to be used at the beginning
     *                of the joined result
     * @param  suffix the sequence of characters to be used at the end
     *                of the joined result
     * @return A {@code Collector} which concatenates CharSequence elements,
     *     separated by the specified delimiter, in encounter order
     */
    static Collector<CharSequence, StringJoiner, String> joining(CharSequence delimiter, CharSequence prefix,
            CharSequence suffix) {
        return Collector.of(() -> {
            StringJoiner sj = new StringJoiner(delimiter, prefix, suffix);
            sj.setEmptyValue(""); //$NON-NLS-1$
            return sj;
        }, StringJoiner::add, StringJoiner::merge, StringJoiner::toString);
    }
}
