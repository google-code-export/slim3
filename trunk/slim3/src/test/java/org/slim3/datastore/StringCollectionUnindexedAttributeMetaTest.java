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

import java.util.List;

import org.junit.Test;
import org.slim3.datastore.model.Hoge;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

/**
 * @author higa
 * 
 */
public class StringCollectionUnindexedAttributeMetaTest {

    private ModelMeta<Hoge> meta = new ModelMeta<Hoge>("Hoge", Hoge.class) {

        @Override
        protected void setKey(Object model, Key key) {
        }

        @Override
        public Entity modelToEntity(Object model) {
            return null;
        }

        @Override
        protected void incrementVersion(Object model) {
        }

        @Override
        protected void prePut(Object model) {
        }

        @Override
        protected long getVersion(Object model) {
            return 0;
        }

        @Override
        protected Key getKey(Object model) {
            return null;
        }

        @Override
        public Hoge entityToModel(Entity entity) {
            return null;
        }

        @Override
        public String getClassHierarchyListName() {
            return null;
        }

        @Override
        public String getSchemaVersionName() {
            return null;
        }

        @Override
        protected void assignKeyToModelRefIfNecessary(DatastoreService ds,
                Object model) throws NullPointerException {
        }
    };

    private StringCollectionUnindexedAttributeMeta<Hoge, List<String>> myStringList =
        new StringCollectionUnindexedAttributeMeta<Hoge, List<String>>(
            meta,
            "myStringList",
            "myStringList",
            List.class);

    /**
     * @throws Exception
     * 
     */
    @Test
    public void startsWith() throws Exception {
        assertThat(
            myStringList.startsWith("a"),
            is(InMemoryStartsWithCriterion.class));
        assertThat(myStringList.startsWith(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void endsWith() throws Exception {
        assertThat(
            myStringList.endsWith("a"),
            is(InMemoryEndsWithCriterion.class));
        assertThat(myStringList.endsWith(null), is(notNullValue()));
    }

    /**
     * @throws Exception
     * 
     */
    @Test
    public void contains() throws Exception {
        assertThat(
            myStringList.contains("a"),
            is(InMemoryContainsCriterion.class));
        assertThat(myStringList.contains(null), is(notNullValue()));
    }
}