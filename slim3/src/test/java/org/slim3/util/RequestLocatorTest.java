/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.slim3.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.Test;
import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockServletContext;

/**
 * @author higa
 * 
 */
public class RequestLocatorTest {

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        RequestLocator.set(null);
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void test() throws Exception {
        MockServletContext servletContext = new MockServletContext();
        MockHttpServletRequest request =
            new MockHttpServletRequest(servletContext);
        RequestLocator.set(request);
        assertThat(
            RequestLocator.get(),
            is(sameInstance((HttpServletRequest) request)));
    }
}