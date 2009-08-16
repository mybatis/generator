/*
 *  Copyright 2006 The Apache Software Foundation
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

package org.apache.ibatis.abator.internal.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class implements an iterator that combines two or three other
 * iterators together.  This iterator does not support the remove function.
 * 
 * @author Jeff Butler
 *
 */
public class AggregatingIterator implements Iterator {
    private Iterator iterator1;
    private Iterator iterator2;
    private Iterator iterator3;

    /**
     * 
     */
    public AggregatingIterator(Iterator iterator1, Iterator iterator2, Iterator iterator3) {
        super();
        
        this.iterator1 = iterator1 == null ? new NullIterator() : iterator1;
        this.iterator2 = iterator2 == null ? new NullIterator() : iterator2;
        this.iterator3 = iterator3 == null ? new NullIterator() : iterator3;
    }

    public AggregatingIterator(Iterator iterator1, Iterator iterator2) {
        this(iterator1, iterator2, null);
    }
    
    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        return iterator1.hasNext()
            || iterator2.hasNext()
            || iterator3.hasNext();
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    public Object next() {
        if (iterator1.hasNext()) {
            return iterator1.next();
        } else if (iterator2.hasNext()) {
            return iterator2.next();
        } else if (iterator3.hasNext()) {
            return iterator3.next();
        } else {
            throw new NoSuchElementException();
        }
    }
    
    public static class NullIterator implements Iterator {
        public boolean hasNext() {
            return false;
        }

        public Object next() {
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
