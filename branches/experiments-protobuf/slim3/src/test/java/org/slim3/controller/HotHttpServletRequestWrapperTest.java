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
package org.slim3.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.slim3.tester.MockHttpServletRequest;
import org.slim3.tester.MockServletContext;

/**
 * @author higa
 * 
 */
public class HotHttpServletRequestWrapperTest {

    private MockServletContext servletContext = new MockServletContext();

    private MockHttpServletRequest request =
        new MockHttpServletRequest(servletContext);

    /**
     * @throws Exception
     * 
     */
    @Test
    public void constructor() throws Exception {
        HotHttpServletRequestWrapper requestWrapper =
            new HotHttpServletRequestWrapper(request);
        assertThat(requestWrapper.sessionWrapper, is(nullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void constructorWithSession() throws Exception {
        request.getSession();
        HotHttpServletRequestWrapper requestWrapper =
            new HotHttpServletRequestWrapper(request);
        assertThat(requestWrapper.sessionWrapper, is(not(nullValue())));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getSession() throws Exception {
        HotHttpServletRequestWrapper requestWrapper =
            new HotHttpServletRequestWrapper(request);
        HttpSession session = requestWrapper.getSession();
        assertThat(session, is(not(nullValue())));
        assertThat(requestWrapper.getSession(), is(sameInstance(session)));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void invalidateSession() throws Exception {
        HotHttpServletRequestWrapper requestWrapper =
            new HotHttpServletRequestWrapper(request);
        requestWrapper.getSession().invalidate();
        assertThat(requestWrapper.sessionWrapper, is(nullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getRequestDispatcher() throws Exception {
        HotHttpServletRequestWrapper requestWrapper =
            new HotHttpServletRequestWrapper(request);
        assertThat(
            requestWrapper.getRequestDispatcher("/index.jsp"),
            is(HotRequestDispatcherWrapper.class));
    }
}