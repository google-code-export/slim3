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
    public double getNegativeDoubleValue() {
        return negativeDoubleValue;
    }
    public void setNegativeDoubleValue(double negativeDoubleValue) {
        this.negativeDoubleValue = negativeDoubleValue;
    }
    public double getNanValue() {
        return nanValue;
    }
    public void setNanValue(double nanValue) {
        this.nanValue = nanValue;
    }
    public double getInfinityValue() {
        return infinityValue;
    }
    public void setInfinityValue(double infinityValue) {
        this.infinityValue = infinityValue;
    }
    public double getNegativeInfinityValue() {
        return negativeInfinityValue;
    }
    public void setNegativeInfinityValue(double negativeInfinityValue) {
        this.negativeInfinityValue = negativeInfinityValue;
    }
    @Attribute(primaryKey=true)
    private Key key;
    private double doubleValue;
    private double negativeDoubleValue;
    private double nanValue = Double.NaN;
    private double infinityValue = Double.POSITIVE_INFINITY;
    private double negativeInfinityValue = Double.NEGATIVE_INFINITY;
}
