package org.slim3.json.test;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.json.Json;

import com.google.appengine.api.datastore.Key;

@Model
public class LobAttrModel {
    public LobAttrModel() {
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
    
    public byte[] getNotIgnoreNullBytes() {
        return notIgnoreNullBytes;
    }
    public void setNotIgnoreNullBytes(byte[] ignoreNullBytes) {
        this.notIgnoreNullBytes = ignoreNullBytes;
    }

    @Attribute(primaryKey = true)
    private Key key;
    private byte[] bytes;
    @Attribute(json=@Json(ignoreNull=false))
    private byte[] notIgnoreNullBytes;
}
