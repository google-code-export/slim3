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

import java.util.Collection;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * An implementation class for "contains null" filter criterion.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class ContainsNullCriterion extends AbstractCriterion implements
        FilterCriterion {

    /**
     * Constructor.
     * 
     * @param attributeMeta
     *            the meta data of attribute
     */
    public ContainsNullCriterion(CollectionAttributeMeta<?, ?, ?> attributeMeta) {
        super(attributeMeta);
    }

    public void apply(Query query) {
        query.addFilter(attributeMeta.getName(), FilterOperator.EQUAL, null);
    }

    public boolean accept(Object model) {
        Object v = attributeMeta.getValue(model);
        if (v == null) {
            return false;
        }
        return Collection.class.cast(v).contains(null);
    }
}