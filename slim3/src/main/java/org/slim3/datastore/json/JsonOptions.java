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
package org.slim3.datastore.json;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slim3.datastore.AbstractAttributeMeta;

import com.google.gwt.dev.util.collect.HashSet;

/**
 * @author nakaguchi
 *
 */
public class JsonOptions {
    private JsonOptions(boolean defaultIncluded){
        this.defaultIncluded = defaultIncluded;
    }

    public static JsonOptions withAll(){
        return new JsonOptions(true);
    }
    
    public static JsonOptions withEmpty(){
        return new JsonOptions(false);
    }

    public <M, A> JsonOptions includes(AbstractAttributeMeta<M, A>... attrs){
        return this;
    }
    
    public <M, A> JsonOptions excludes(AbstractAttributeMeta<M, A>... attrs){
        for(AbstractAttributeMeta<?, ?> s : attrs){
            excludes.add(s.getAttributeName());
        }
        return this;
    }

    public <M, A> JsonOptions alias(AbstractAttributeMeta<M, A> attr, String alias){
        aliases.put(attr.getAttributeName(), alias);
        return this;
    }

    public <M, A> JsonOptions coder(AbstractAttributeMeta<M, A> attr, JsonCoder coder){
        coders.put(attr.getAttributeName(), coder);
        return this;
    }

    public JsonOptions maxDepth(int maxDepth){
        this.maxDepth = maxDepth;
        return this;
    }
    
    public boolean included(String attributeName){
        if(defaultIncluded){
            return !excludes.contains(attributeName);
        }
        return !excludes.contains(attributeName);
    }

    public String propertyName(String attributeName, String annotAlias){
        String alias = aliases.get(attributeName);
        if(alias != null) return alias;
        if(annotAlias.length() > 0) return annotAlias;
        return attributeName;
    }

    public JsonCoder coder(String attributeName){
        return coders.get(attributeName);
    }
    
    public int maxDepth(){
        return maxDepth;
    }

    private int maxDepth;
    private boolean defaultIncluded = true;
    private Set<String> excludes = new HashSet<String>();
    private Map<String, String> aliases = new HashMap<String, String>();
    private Map<String, JsonCoder> coders = new HashMap<String, JsonCoder>();
}
