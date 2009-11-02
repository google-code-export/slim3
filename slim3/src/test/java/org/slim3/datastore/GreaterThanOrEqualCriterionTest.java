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
package org.slim3.datastore;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.datastore.model.Hoge;
import org.slim3.tester.LocalServiceTestCase;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

/**
 * @author higa
 * 
 */
public class GreaterThanOrEqualCriterionTest extends LocalServiceTestCase {

    private HogeMeta meta = new HogeMeta();

    /**
     * @throws Exception
     * 
     */
    @Test
    public void constructor() throws Exception {
        GreaterThanOrEqualCriterion c =
            new GreaterThanOrEqualCriterion(meta.myString, "aaa");
        assertThat((String) c.value, is("aaa"));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void apply() throws Exception {
        Query query = new Query();
        GreaterThanOrEqualCriterion c =
            new GreaterThanOrEqualCriterion(meta.myString, "aaa");
        c.apply(query);
        List<FilterPredicate> predicates = query.getFilterPredicates();
        assertThat(predicates.get(0).getPropertyName(), is("myString"));
        assertThat(
            predicates.get(0).getOperator(),
            is(FilterOperator.GREATER_THAN_OR_EQUAL));
        assertThat((String) predicates.get(0).getValue(), is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void accept() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyString("c");
        FilterCriterion c = new GreaterThanOrEqualCriterion(meta.myString, "b");
        assertThat(c.accept(hoge), is(true));
        hoge.setMyString("b");
        assertThat(c.accept(hoge), is(true));
        hoge.setMyString("a");
        assertThat(c.accept(hoge), is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void acceptForNull() throws Exception {
        Hoge hoge = new Hoge();
        hoge.setMyString("c");
        FilterCriterion c =
            new GreaterThanOrEqualCriterion(meta.myString, null);
        assertThat(c.accept(hoge), is(true));
        hoge.setMyString(null);
        assertThat(c.accept(hoge), is(true));
    }
}