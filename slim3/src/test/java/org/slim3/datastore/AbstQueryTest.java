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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;
import org.slim3.util.ByteUtil;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.QueryResultIterable;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.repackaged.com.google.common.util.Base64;

/**
 * @author higa
 * 
 */
public class AbstQueryTest extends AppEngineTestCase {

    private DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

    /**
     * @throws Exception
     */
    @Test
    public void constructor() throws Exception {
        MyQuery q = new MyQuery();
        assertThat(q.query, is(notNullValue()));
        assertThat(q.query.getKind(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void constructorUsingKind() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertThat(q.query.getKind(), is("Hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void constructorUsingKindAndAncestorKey() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        MyQuery q = new MyQuery("Hoge", key);
        assertThat(q.query.getKind(), is("Hoge"));
        assertThat(q.query.getAncestor(), is(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void constructorUsingAncestorKey() throws Exception {
        Key key = KeyFactory.createKey("Hoge", 1);
        MyQuery q = new MyQuery(key);
        assertThat(q.query.getAncestor(), is(key));
    }

    /**
     * @throws Exception
     */
    @Test
    public void setTx() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        q.setTx(ds.beginTransaction());
        assertNotNull(q.tx);
        assertTrue(q.txSet);
    }

    /**
     * @throws Exception
     */
    @Test
    public void setTxForNothing() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertThat(q.tx, is(nullValue()));
        assertThat(q.txSet, is(false));
    }

    /**
     * @throws Exception
     */
    @Test
    public void setTxForNull() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        q.setTx(null);
        assertThat(q.tx, is(nullValue()));
        assertThat(q.txSet, is(true));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void setIllegalTx() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        Transaction tx = ds.beginTransaction();
        tx.rollback();
        q.setTx(tx);
    }

    /**
     * @throws Exception
     */
    @Test
    public void asEntityList() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        List<Entity> list = query.asEntityList();
        assertThat(list.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asEntityListForKindlessQuery() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery();
        List<Entity> list = query.asEntityList();
        assertThat(list.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asEntityListForKindlessAncestorQuery() throws Exception {
        Key parentKey = ds.put(new Entity("Parent"));
        ds.put(new Entity(KeyFactory.createKey(parentKey, "Child", 1)));
        MyQuery query = new MyQuery(parentKey);
        List<Entity> list = query.asEntityList();
        assertThat(list.size(), is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asEntityListInternally() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        List<Entity> list = query.asEntityList(ds, query.query);
        assertThat(list.size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asSingleEntity() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        assertThat(query.asSingleEntity(), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asSingleEntityForKindlessQuery() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery();
        assertThat(query.asSingleEntity(), is(not(nullValue())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asKeyList() throws Exception {
        Key key = ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        assertThat(query.asKeyList(), is(Arrays.asList(key)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asIterableEntities() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        boolean found = false;
        for (Entity entity : query.asIterableEntities()) {
            found = true;
            assertThat(entity.getKind(), is("Hoge"));
        }
        assertThat(found, is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asQueryResultEntityList() throws Exception {
        ds.put(new Entity("Hoge"));
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        QueryResultList<Entity> list = query.limit(1).asQueryResultEntityList();
        assertThat(list, is(notNullValue()));
        assertThat(list.size(), is(1));
        assertThat(list.getCursor(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asQueryResultEntityIterator() throws Exception {
        ds.put(new Entity("Hoge"));
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        QueryResultIterator<Entity> ite =
            query.limit(1).asQueryResultEntityIterator();
        assertThat(ite, is(notNullValue()));
        assertThat(ite.getCursor(), is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void asQueryResultEntityIterable() throws Exception {
        ds.put(new Entity("Hoge"));
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        QueryResultIterable<Entity> iterable =
            query.limit(1).asQueryResultEntityIterable();
        assertThat(iterable, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void filterWithPropertyNameAndOperatorAndValue() throws Exception {
        MyQuery query = new MyQuery("Hoge");
        assertThat(
            query.filter("myString", FilterOperator.EQUAL, "aaa"),
            is(sameInstance(query)));
        assertThat(query.query.getFilterPredicates().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void filterWithFilter() throws Exception {
        MyQuery query = new MyQuery("Hoge");
        assertThat(query.filter(new Filter(
            "myString",
            FilterOperator.EQUAL,
            "aaa")), is(sameInstance(query)));
        assertThat(query.query.getFilterPredicates().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void encodedFilters() throws Exception {
        MyQuery query = new MyQuery("Hoge");
        query.filter(new Filter("myString", FilterOperator.EQUAL, "aaa"));
        MyQuery query2 = new MyQuery("Hoge");
        assertThat(
            query2.encodedFilters(query.getEncodedFilters()),
            is(sameInstance(query2)));
        assertThat(query2.query.getFilterPredicates().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void sortForSort() throws Exception {
        MyQuery query = new MyQuery("Hoge");
        assertThat(
            query.sort(new Sort("myString", SortDirection.ASCENDING)),
            is(sameInstance(query)));
        assertThat(query.query.getSortPredicates().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void sortWithPropertyName() throws Exception {
        MyQuery query = new MyQuery("Hoge");
        assertThat(query.sort("myString"), is(sameInstance(query)));
        assertThat(query.query.getSortPredicates().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void sortForWithPropertyNameAndDirection() throws Exception {
        MyQuery query = new MyQuery("Hoge");
        assertThat(
            query.sort("myString", SortDirection.ASCENDING),
            is(sameInstance(query)));
        assertThat(query.query.getSortPredicates().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void encodedSorts() throws Exception {
        MyQuery query = new MyQuery("Hoge");
        query.sort("myString");
        MyQuery query2 = new MyQuery("Hoge");
        assertThat(
            query2.encodedSorts(query.getEncodedSorts()),
            is(sameInstance(query2)));
        assertThat(query2.query.getSortPredicates().size(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void count() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        assertThat(query.count(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void countQuickly() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        assertThat(query.countQuickly(), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void min() throws Exception {
        Entity entity = new Entity("Hoge");
        entity.setProperty("age", 10);
        ds.put(entity);
        Entity entity2 = new Entity("Hoge");
        entity2.setProperty("age", 20);
        ds.put(entity2);
        Entity entity3 = new Entity("Hoge");
        entity3.setProperty("age", null);
        ds.put(entity3);
        MyQuery query = new MyQuery("Hoge");
        assertThat((Long) query.min("age"), is(10L));
        assertThat(query.max("name"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void max() throws Exception {
        Entity entity = new Entity("Hoge");
        entity.setProperty("age", 10);
        ds.put(entity);
        Entity entity2 = new Entity("Hoge");
        entity2.setProperty("age", 20);
        ds.put(entity2);
        MyQuery query = new MyQuery("Hoge");
        assertThat((Long) query.max("age"), is(20L));
        assertThat(query.max("name"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void offset() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertThat(q.offset(10), is(sameInstance(q)));
        assertThat(q.fetchOptions.getOffset(), is(10));
    }

    /**
     * @throws Exception
     */
    @Test
    public void limit() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertThat(q.limit(100), is(sameInstance(q)));
        assertThat(q.fetchOptions.getLimit(), is(100));
    }

    /**
     * @throws Exception
     */
    @Test
    public void prefetchSize() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertThat(q.prefetchSize(15), is(sameInstance(q)));
        assertThat(q.fetchOptions.getPrefetchSize(), is(15));
    }

    /**
     * @throws Exception
     */
    @Test
    public void chunkSize() throws Exception {
        MyQuery q = new MyQuery("Hoge");
        assertThat(q.chunkSize(20), is(sameInstance(q)));
        assertThat(q.fetchOptions.getChunkSize(), is(20));
    }

    /**
     * @throws Exception
     */
    @Test
    public void cursor() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        QueryResultList<Entity> list = query.limit(1).asQueryResultEntityList();
        Cursor cursor = list.getCursor();
        query = new MyQuery("Hoge");
        assertThat(query.cursor(cursor), is(sameInstance(query)));
        assertThat(query.fetchOptions.getCursor(), is(cursor));
    }

    /**
     * @throws Exception
     */
    @Test
    public void encodedCursor() throws Exception {
        ds.put(new Entity("Hoge"));
        MyQuery query = new MyQuery("Hoge");
        QueryResultList<Entity> list = query.limit(1).asQueryResultEntityList();
        Cursor cursor = list.getCursor();
        String encodedCursor = cursor.toWebSafeString();
        query = new MyQuery("Hoge");
        assertThat(query.encodedCursor(encodedCursor), is(sameInstance(query)));
        assertThat(query.fetchOptions.getCursor(), is(cursor));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getFilters() throws Exception {
        MyQuery query = new MyQuery("Hoge");
        query.query.addFilter("aaa", FilterOperator.EQUAL, "111");
        query.query.addFilter("bbb", FilterOperator.EQUAL, "222");
        Filter[] filters = query.getFilters();
        assertThat(filters.length, is(2));
        assertThat(filters[0].getPropertyName(), is("aaa"));
        assertThat(filters[0].getOperator(), is(FilterOperator.EQUAL));
        assertThat((String) filters[0].getValue(), is("111"));
        assertThat(filters[1].getPropertyName(), is("bbb"));
        assertThat(filters[1].getOperator(), is(FilterOperator.EQUAL));
        assertThat((String) filters[1].getValue(), is("222"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEncodedFilters() throws Exception {
        MyQuery query = new MyQuery("Hoge");
        query.query.addFilter("aaa", FilterOperator.EQUAL, "111");
        query.query.addFilter("bbb", FilterOperator.EQUAL, "222");
        String encodedFilters = query.getEncodedFilters();
        Filter[] filters = ByteUtil.toObject(Base64.decode(encodedFilters));
        assertThat(filters.length, is(2));
        assertThat(filters[0].getPropertyName(), is("aaa"));
        assertThat(filters[0].getOperator(), is(FilterOperator.EQUAL));
        assertThat((String) filters[0].getValue(), is("111"));
        assertThat(filters[1].getPropertyName(), is("bbb"));
        assertThat(filters[1].getOperator(), is(FilterOperator.EQUAL));
        assertThat((String) filters[1].getValue(), is("222"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSorts() throws Exception {
        MyQuery query = new MyQuery("Hoge");
        query.query.addSort("aaa", SortDirection.ASCENDING);
        query.query.addSort("bbb", SortDirection.DESCENDING);
        Sort[] sorts = query.getSorts();
        assertThat(sorts.length, is(2));
        assertThat(sorts[0].getPropertyName(), is("aaa"));
        assertThat(sorts[0].getDirection(), is(SortDirection.ASCENDING));
        assertThat(sorts[1].getPropertyName(), is("bbb"));
        assertThat(sorts[1].getDirection(), is(SortDirection.DESCENDING));
    }

    /**
     * @throws Exception
     */
    @Test
    public void getEncodedSorts() throws Exception {
        MyQuery query = new MyQuery("Hoge");
        query.query.addSort("aaa", SortDirection.ASCENDING);
        query.query.addSort("bbb", SortDirection.DESCENDING);
        String encodedSorts = query.getEncodedSorts();
        Sort[] sorts = ByteUtil.toObject(Base64.decode(encodedSorts));
        assertThat(sorts.length, is(2));
        assertThat(sorts[0].getPropertyName(), is("aaa"));
        assertThat(sorts[0].getDirection(), is(SortDirection.ASCENDING));
        assertThat(sorts[1].getPropertyName(), is("bbb"));
        assertThat(sorts[1].getDirection(), is(SortDirection.DESCENDING));
    }

    private static class MyQuery extends AbstractQuery<MyQuery> {

        /**
         * 
         */
        public MyQuery() {
            super();
        }

        /**
         * @param ancestorKey
         * @throws NullPointerException
         */
        public MyQuery(Key ancestorKey) throws NullPointerException {
            super(ancestorKey);
        }

        /**
         * @param kind
         * @param ancestorKey
         * @throws NullPointerException
         */
        public MyQuery(String kind, Key ancestorKey)
                throws NullPointerException {
            super(kind, ancestorKey);
        }

        /**
         * @param kind
         * @throws NullPointerException
         */
        public MyQuery(String kind) throws NullPointerException {
            super(kind);
        }
    }
}