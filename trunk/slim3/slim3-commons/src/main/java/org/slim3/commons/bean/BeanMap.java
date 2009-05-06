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
package org.slim3.commons.bean;

import java.util.HashMap;

/**
 * A map that acts as bean.
 * 
 * @author higa
 * @since 3.0
 * 
 */
public class BeanMap extends HashMap<String, Object> {

    private static final long serialVersionUID = 1;

    @Override
    public Object get(Object key) {
        if (!containsKey(key)) {
            throw new IllegalArgumentException(key
                + " is not found in "
                + keySet());
        }
        return super.get(key);
    }
}