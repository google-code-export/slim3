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

import java.util.Locale;
import java.util.Map;

import junit.framework.TestCase;

import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockServletContext;
import org.slim3.util.ApplicationMessage;
import org.slim3.util.RequestMap;

/**
 * @author higa
 * 
 */
public class LongRangeValidatorTest extends TestCase {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    private Map<String, Object> parameters = new RequestMap(request);

    private LongRangeValidator validator = new LongRangeValidator(3, 5);

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ApplicationMessage.setBundle("test", Locale.ENGLISH);
    }

    @Override
    protected void tearDown() throws Exception {
        ApplicationMessage.clearBundle();
        super.tearDown();
    }

    /**
     * @throws Exception
     */
    public void testValidateForNull() throws Exception {
        assertNull(validator.validate(parameters, "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testValidateForEmptyString() throws Exception {
        parameters.put("aaa", "");
        assertNull(validator.validate(parameters, "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testValidateForValid() throws Exception {
        parameters.put("aaa", "3");
        assertNull(validator.validate(parameters, "aaa"));
        parameters.put("aaa", "4");
        assertNull(validator.validate(parameters, "aaa"));
        parameters.put("aaa", "5");
        assertNull(validator.validate(parameters, "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testValidateForInvalid() throws Exception {
        parameters.put("aaa", "2");
        assertEquals("Aaa is not in the 3 to 5 range.", validator.validate(
            parameters,
            "aaa"));
        parameters.put("aaa", "6");
        assertEquals("Aaa is not in the 3 to 5 range.", validator.validate(
            parameters,
            "aaa"));
        parameters.put("aaa", "xxx");
        assertEquals("Aaa is not in the 3 to 5 range.", validator.validate(
            parameters,
            "aaa"));
    }

    /**
     * @throws Exception
     */
    public void testValidateForInvalidAndMessage() throws Exception {
        parameters.put("aaa", "xxx");
        assertEquals("hoge", new LongRangeValidator(3, 5, "hoge").validate(
            parameters,
            "aaa"));
    }
}