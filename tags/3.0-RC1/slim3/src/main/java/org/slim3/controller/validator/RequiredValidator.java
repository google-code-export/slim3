/*
 * Copyright 2004-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.slim3.controller.validator;

import java.util.Map;

import org.slim3.util.ApplicationMessage;

/**
 * A validator for a required value.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class RequiredValidator extends AbstractValidator {

    /**
     * The instance.
     */
    public static RequiredValidator INSTANCE = new RequiredValidator();

    /**
     * Constructor.
     */
    public RequiredValidator() {
        super();
    }

    /**
     * Constructor.
     * 
     * @param message
     *            the error message
     */
    public RequiredValidator(String message) {
        super(message);
    }

    public String validate(Map<String, Object> parameters, String name) {
        Object value = parameters.get(name);
        if (value == null || "".equals(value)) {
            if (message != null) {
                return message;
            }
            return ApplicationMessage.get(getMessageKey(), getLabel(name));
        }
        return null;
    }

    @Override
    protected String getMessageKey() {
        return "validator.required";
    }
}