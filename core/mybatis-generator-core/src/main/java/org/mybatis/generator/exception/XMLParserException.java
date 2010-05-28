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
package org.mybatis.generator.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeff Butler
 */
public class XMLParserException extends Exception {

    private static final long serialVersionUID = 5172525430401340573L;

    private List<String> errors;

    /**
	 *  
	 */
    public XMLParserException(List<String> errors) {
        super();
        this.errors = errors;
    }

    public XMLParserException(String error) {
        super(error);
        this.errors = new ArrayList<String>();
        errors.add(error);
    }

    public List<String> getErrors() {
        return errors;
    }
}
