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
package org.slim3.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.Timestamp;

import org.junit.Test;

/**
 * @author higa
 * 
 */
public class NumberConverterTest {

    /**
     * @throws Exception
     */
    @Test
    public void getAsObject() throws Exception {
        NumberConverter converter = new NumberConverter("##0");
        assertThat((Long) converter.getAsObject("100"), is(100L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getAsString() throws Exception {
        NumberConverter converter = new NumberConverter("##0");
        assertThat(converter.getAsString(new Integer("100")), is("100"));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void getAsStringForCastException() throws Exception {
        NumberConverter converter = new NumberConverter("##0");
        converter.getAsString("aaa");
    }

    /**
     * @throws Exception
     */
    @Test(expected = NullPointerException.class)
    public void constructorForNull() throws Exception {
        new NumberConverter(null);
    }

    /**
     * @throws Exception
     */
    @Test
    public void isTarget() throws Exception {
        NumberConverter converter = new NumberConverter("##0");
        assertThat(converter.isTarget(Integer.class), is(true));
        assertThat(converter.isTarget(Timestamp.class), is(false));
    }
}