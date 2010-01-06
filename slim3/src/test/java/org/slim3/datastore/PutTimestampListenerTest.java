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

import org.junit.Test;
import org.slim3.datastore.meta.HogeMeta;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.Entity;

/**
 * @author higa
 * 
 */
public class PutTimestampListenerTest extends AppEngineTestCase {

    private PutTimestampListener listener = new PutTimestampListener();

    /**
     * @throws Exception
     * 
     */
    @Test
    public void prePost() throws Exception {
        Entity entity = new Entity("Hoge");
        listener.prePut(entity, HogeMeta.get().myDate);
        assertThat(entity.getProperty("myDate"), is(not(nullValue())));
    }
}