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
    public double getMaxValue() {
        return maxValue;
    }
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }
    public double getMinNormalValue() {
        return minNormalValue;
    }
    public void setMinNormalValue(double minNormalValue) {
        this.minNormalValue = minNormalValue;
    }
    public double getMinValue() {
        return minValue;
    }
    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }
    public double getMinM2Value() {
        return minM2Value;
    }
    public void setMinM2Value(double minM2Value) {
        this.minM2Value = minM2Value;
    }
    @Attribute(primaryKey=true)
    private Key key;
    private double doubleValue = 0x00ffffffffffffffL;
    private double negativeDoubleValue = -233323.4333584;
    private double maxValue = Double.MAX_VALUE;
    private double minValue = Double.MIN_VALUE;
    private double minM2Value = Double.MIN_VALUE + Double.MIN_VALUE;
    private double minNormalValue = Double.MIN_NORMAL;
    private double nanValue = Double.NaN;
    private double infinityValue = Double.POSITIVE_INFINITY;
    private double negativeInfinityValue = Double.NEGATIVE_INFINITY;
}
