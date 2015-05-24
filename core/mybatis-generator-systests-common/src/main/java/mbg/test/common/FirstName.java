/**
 *    Copyright 2006-2015 the original author or authors.
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
package mbg.test.common;

/**
 * @author Jeff Butler
 *
 */
public class FirstName {

    private String value;
    
    /**
     * 
     */
    public FirstName() {
        super();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean equals(Object arg0) {
        if (arg0 == null) {
            return false;
        }
        
        return value.equals(((FirstName)arg0).getValue());
    }

    public int hashCode() {
        return value == null ? 0 : value.hashCode();
    }

}
