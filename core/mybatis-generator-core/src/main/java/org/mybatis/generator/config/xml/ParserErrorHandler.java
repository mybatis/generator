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
package org.mybatis.generator.config.xml;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.util.List;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class ParserErrorHandler implements ErrorHandler {

    private List<String> warnings;

    private List<String> errors;

    public ParserErrorHandler(List<String> warnings, List<String> errors) {
        super();
        this.warnings = warnings;
        this.errors = errors;
    }

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        warnings.add(getString("Warning.7", //$NON-NLS-1$
                Integer.toString(exception.getLineNumber()), exception
                        .getMessage()));
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        errors.add(getString("RuntimeError.4", //$NON-NLS-1$
                Integer.toString(exception.getLineNumber()), exception
                        .getMessage()));
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        errors.add(getString("RuntimeError.4", //$NON-NLS-1$
                Integer.toString(exception.getLineNumber()), exception
                        .getMessage()));
    }
}
