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
package com.google.appengine.api.datastore;

public class Key {

    private Key parentKey;

    private String kind;

    private String appId;

    private long id;

    private String name;

    private AppIdNamespace appIdNamespace;

    Key(String kind, Key parentKey, long id, String name,
        AppIdNamespace appIdNamespace) {
        this.name = name;
        this.id = id;
        this.parentKey = parentKey;
        this.kind = kind.intern();
        this.appIdNamespace = appIdNamespace;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer(30);
        appendToString(buffer);
        return buffer.toString();
    }

    private void appendToString(StringBuffer buffer) {
        if (parentKey != null) {
            parentKey.appendToString(buffer);
            buffer.append("/");
        }
        buffer.append(kind);
        buffer.append("(");
        if(name != null) {
            buffer.append("\"").append(name).append("\"");
        } else {
            buffer.append(String.valueOf(id));
        }
        buffer.append(")");
    }
  
    public String getKind() {
        return kind;
    }

    public Key getParent() {
        return parentKey;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AppIdNamespace getAppIdNamespace() {
        return appIdNamespace;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((kind == null) ? 0 : kind.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result =
            prime * result + ((parentKey == null) ? 0 : parentKey.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Key other = (Key) obj;
        if (id != other.id)
            return false;
        if (kind == null) {
            if (other.kind != null)
                return false;
        } else if (!kind.equals(other.kind))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (parentKey == null) {
            if (other.parentKey != null)
                return false;
        } else if (!parentKey.equals(other.parentKey))
            return false;
        return true;
    }
}