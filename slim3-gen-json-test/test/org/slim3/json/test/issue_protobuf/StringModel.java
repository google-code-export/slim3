package org.slim3.json.test.issue_protobuf;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class StringModel {
    public static StringModel createModel(){
        StringModel m = new StringModel();
        m.setHello("hello");
        return m;
    }
    public Key getKey() {
        return key;
    }
    public void setKey(Key key) {
        this.key = key;
    }
    public String getHello() {
        return hello;
    }
    public void setHello(String stringValue) {
        this.hello = stringValue;
    }
    public String getJapanese() {
        return japanese;
    }
    public void setJapanese(String japanese) {
        this.japanese = japanese;
    }
    public String getSurrogate() {
        return surrogate;
    }
    public void setSurrogate(String surrogate) {
        this.surrogate = surrogate;
    }
    @Attribute(primaryKey=true)
    private Key key;
    private String hello;
    private String japanese;
    private String surrogate;
}
