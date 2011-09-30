package org.slim3.json.test.issue_protobuf;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class FloatModel {
    public static FloatModel createModel(){
        FloatModel m = new FloatModel();
        m.setFloatValue(10.0f);
        m.setNegativeFloatValue(-10.0f);
        return m;
    }
    public Key getKey() {
        return key;
    }
    public void setKey(Key key) {
        this.key = key;
    }
    public float getFloatValue() {
        return floatValue;
    }
    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }
    public float getInfinityValue() {
        return infinityValue;
    }
    public void setInfinityValue(float infinityValue) {
        this.infinityValue = infinityValue;
    }
    public float getMaxValue() {
        return maxValue;
    }
    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }
    public float getMinNormalValue() {
        return minNormalValue;
    }
    public void setMinNormalValue(float minNormalValue) {
        this.minNormalValue = minNormalValue;
    }
    public float getMinValue() {
        return minValue;
    }
    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }
    public float getNanValue() {
        return nanValue;
    }
    public void setNanValue(float nanValue) {
        this.nanValue = nanValue;
    }
    public float getNegativeFloatValue() {
        return negativeFloatValue;
    }
    public void setNegativeFloatValue(float negativeFloatValue) {
        this.negativeFloatValue = negativeFloatValue;
    }
    public float getNegativeInfinityValue() {
        return negativeInfinityValue;
    }
    public void setNegativeInfinityValue(float negativeInfinityValue) {
        this.negativeInfinityValue = negativeInfinityValue;
    }
    public float getMinP1Value() {
        return minP1Value;
    }
    public void setMinP1Value(float minP1Value) {
        this.minP1Value = minP1Value;
    }
    @Attribute(primaryKey=true)
    private Key key;
    private float floatValue;
    private float negativeFloatValue;
    private float maxValue = Float.MAX_VALUE;
    private float minValue = Float.MIN_VALUE;
    private float minP1Value = Float.MIN_VALUE + Float.MIN_VALUE;
    private float minNormalValue = Float.MIN_NORMAL;
    private float nanValue = Float.NaN;
    private float infinityValue = Float.POSITIVE_INFINITY;
    private float negativeInfinityValue = Float.NEGATIVE_INFINITY;
}
