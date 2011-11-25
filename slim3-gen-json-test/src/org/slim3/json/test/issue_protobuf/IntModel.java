package org.slim3.json.test.issue_protobuf;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class IntModel {
    public static IntModel createModel(){
        IntModel m = new IntModel();
        m.setPositive(10);
        return m;
    }
    public Key getKey() {
        return key;
    }
    public void setKey(Key key) {
        this.key = key;
    }
    public int getPositive() {
        return positive;
    }
    public void setPositive(int longValue) {
        this.positive = longValue;
    }
    public int getNegative() {
        return negative;
    }
    public void setNegative(int negative) {
        this.negative = negative;
    }
    public int getMax() {
        return max;
    }
    public void setMax(int maxValue) {
        this.max = maxValue;
    }
    public int getMin() {
        return min;
    }
    public void setMin(int minValue) {
        this.min = minValue;
    }
    @Attribute(primaryKey=true)
    private Key key;
    private int positive = 1 << 20;
    private int negative = -12924443;
    private int max = Integer.MAX_VALUE;
    private int min = Integer.MIN_VALUE;
}
