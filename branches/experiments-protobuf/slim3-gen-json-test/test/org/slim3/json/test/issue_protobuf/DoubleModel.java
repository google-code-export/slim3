package org.slim3.json.test.issue_protobuf;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class DoubleModel {
    public static DoubleModel createModel(){
        DoubleModel m = new DoubleModel();
        m.setDoubleValue(10.0);
        return m;
    }
    public Key getKey() {
        return key;
    }
    public void setKey(Key key) {
        this.key = key;
    }
    public double getDoubleValue() {
        return doubleValue;
    }
    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }
    @Attribute(primaryKey=true)
    private Key key;
    private double doubleValue;
}
