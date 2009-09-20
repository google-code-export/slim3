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
package org.slim3.jdo;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class DescCriterionTest extends TestCase {

    /**
     * @throws Exception
     */
    public void testGetQueryString() throws Exception {
        SampleMeta m = new SampleMeta();
        DescCriterion criterion = m.id.desc();
        assertEquals("id desc", criterion.getQueryString());
    }

    /**
     * @throws Exception
     */
    public void testCompare() throws Exception {
        SampleMeta m = new SampleMeta();
        OrderCriterion criterion = m.name.desc();
        assertEquals(0, criterion.compare(new Sample(), new Sample()));
        Sample sample = new Sample();
        sample.setName("aaa");
        assertEquals(-1, criterion.compare(new Sample(), sample));
        assertEquals(1, criterion.compare(sample, new Sample()));
        Sample sample2 = new Sample();
        sample2.setName("bbb");
        assertEquals(1, criterion.compare(sample, sample2));
        assertEquals(-1, criterion.compare(sample2, sample));
    }
}