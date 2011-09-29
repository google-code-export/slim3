package org.slim3.json.test.issue_protobuf;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class LongModel {
    public static LongModel createModel(){
        LongModel m = new LongModel();
        m.setLongValue(10);
        return m;
    }
    public Key getKey() {
        return key;
    }
    public void setKey(Key key) {
        this.key = key;
    }
    public long getLongValue() {
        return longValue;
    }
    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }
    public long getMaxValue() {
        return maxValue;
    }
    public void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
    }
    public long getMinValue() {
        return minValue;
    }
    public void setMinValue(long minValue) {
        this.minValue = minValue;
    }
    @Attribute(primaryKey=true)
    private Key key;
    private long longValue;
    private long maxValue = Long.MAX_VALUE;
    private long minValue = Long.MIN_VALUE;
}
