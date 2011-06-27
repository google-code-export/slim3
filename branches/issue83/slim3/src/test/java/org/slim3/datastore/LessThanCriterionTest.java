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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

/**
 * @author higa
 * 
 */
public class LessThanCriterionTest extends AppEngineTestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getFilters() throws Exception {
        LessThanCriterion c = new LessThanCriterion(meta.myString, "aaa");
        Filter[] filters = c.getFilters();
        assertThat(filters.length, is(1));
        assertThat(filters[0].getPropertyName(), is("myString"));
        assertThat(filters[0].getOperator(), is(FilterOperator.LESS_THAN));
        assertThat((String) filters[0].getValue(), is("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getFiltersForEnum() throws Exception {
        LessThanCriterion c =
            new LessThanCriterion(meta.myEnum, SortDirection.ASCENDING);
        Filter[] filters = c.getFilters();
        assertThat(filters.length, is(1));
        assertThat(filters[0].getPropertyName(), is("myEnum"));
        assertThat(filters[0].getOperator(), is(FilterOperator.LESS_THAN));
        assertThat((String) filters[0].getValue(), is("ASCENDING"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void getFiltersForNull() throws Exception {
        LessThanCriterion c = new LessThanCriterion(meta.myString, null);
        Filter[] filters = c.getFilters();
        assertThat(filters.length, is(1));
        assertThat(filters[0].getPropertyName(), is("myString"));
        assertThat(filters[0].getOperator(), is(FilterOperator.LESS_THAN));
        assertThat(filters[0].getValue(), is(nullValue()));
    }
}