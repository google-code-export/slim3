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
package org.slim3.gen.util;

import junit.framework.TestCase;

/**
 * @author taedium
 * 
 */
public class StringUtilTest extends TestCase {

    /**
     * 
     * @throws Exception
     */
    public void testIsEmpty() throws Exception {
        assertTrue(StringUtil.isEmpty(""));
        assertTrue(StringUtil.isEmpty(null));
        assertFalse(StringUtil.isEmpty("a"));
    }

    /**
     * 
     * @throws Exception
     */
    public void testCapitalize() throws Exception {
        assertEquals("Aaa", StringUtil.capitalize("aaa"));
        assertEquals("", StringUtil.capitalize(""));
        assertNull(StringUtil.capitalize(null));
    }
}
