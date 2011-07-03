/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package slim3.demo.cool.model;

import javax.persistence.Id;

/**
 * @author higa
 * 
 */
@com.googlecode.objectify.annotation.Entity(name = "Bar")
public class BarObjectify {

    @Id
    private Long key;

    private String sortValue;

    /**
     * @return the key
     */
    public Long getKey() {
        return key;
    }

    /**
     * @param key
     *            the key to set
     */
    public void setKey(Long key) {
        this.key = key;
    }

    /**
     * @return the sortValue
     */
    public String getSortValue() {
        return sortValue;
    }

    /**
     * @param sortValue
     *            the sortValue to set
     */
    public void setSortValue(String sortValue) {
        this.sortValue = sortValue;
    }

}