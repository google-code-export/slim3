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
package org.slim3.controller.router;

import org.junit.Test;
import org.slim3.controller.ControllerConstants;
import org.slim3.tester.ControllerTestCase;
import org.slim3.util.WrapRuntimeException;

/**
 * @author higa
 * 
 */
public class RouterFactoryForBadPathTest extends ControllerTestCase {

    private static final String ROOT_PACKAGE = "org.slim3.controller.bad";

    @Override
    public void setUp() throws Exception {
        tester.servletContext.setInitParameter(
            ControllerConstants.ROOT_PACKAGE_KEY,
            ROOT_PACKAGE);
        super.setUp();
    }

    /**
     * @throws Exception
     */
    @Test(expected = WrapRuntimeException.class)
    public void getRouter() throws Exception {
        RouterFactory.getRouter();
    }
}