package org.slim3.json.test.issue_protobuf;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;
import org.slim3.datastore.pb.Pb;

import com.google.appengine.api.datastore.Key;

@Model
public class AnnotatedStringModel {
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
    public String getIgnored() {
        return ignored;
    }
    public void setIgnored(String ignored) {
        this.ignored = ignored;
    }
    public String getWorld() {
        return world;
    }
    public void setWorld(String world) {
        this.world = world;
    }

    @Attribute(primaryKey=true)
    private Key key;
    private String hello = "Hello, World.";
    @Pb(ignore=true)
    private String ignored = "お早うございます。行ってらっしゃい。";
    @Pb(alias="aliased")
    private String world = "world";
}
