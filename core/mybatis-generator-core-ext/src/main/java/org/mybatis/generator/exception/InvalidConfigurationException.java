/**
 *    Copyright 2006-2017 the original author or authors.
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
package org.mybatis.generator.exception;

import java.util.List;

/**
 * The Class InvalidConfigurationException.
 *
 * @author Jeff Butler
 */
public class InvalidConfigurationException extends Exception {

    /** The Constant serialVersionUID. */
    static final long serialVersionUID = 4902307610148543411L;

    /** The errors. */
    private List<String> errors;

    /**
     * Instantiates a new invalid configuration exception.
     *
     * @param errors
     *            the errors
     */
    public InvalidConfigurationException(List<String> errors) {
        super();
        this.errors = errors;
    }

    /**
     * Gets the errors.
     *
     * @return the errors
     */
    public List<String> getErrors() {
        return errors;
    }

    /* (non-Javadoc)
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public String getMessage() {
        if (errors != null && errors.size() > 0) {
            return errors.get(0);
        }

        return super.getMessage();
    }
}
