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
package org.slim3.datastore;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;

/**
 * @author higa
 * 
 */
public class StringCollectionAttributeMetaTest {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     * 
     */
    @Test
    public void startsWith() throws Exception {
        assertThat(
            meta.myStringList.startsWith("a"),
            is(StartsWithCriterion.class));
        assertThat(meta.myStringList.startsWith(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void endsWith() throws Exception {
        assertThat(meta.myStringList.endsWith("a"), is(InMemoryEndsWithCriterion.class));
        assertThat(meta.myStringList.endsWith(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void contains() throws Exception {
        assertThat(meta.myStringList.contains("a"), is(InMemoryContainsCriterion.class));
        assertThat(meta.myStringList.contains(null), is(notNullValue()));
    }
}