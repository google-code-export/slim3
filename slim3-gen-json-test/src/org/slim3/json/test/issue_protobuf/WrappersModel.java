package org.slim3.json.test.issue_protobuf;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class WrappersModel {
    public Boolean getBooleanValue() {
        return booleanValue;
    }
    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }
/*
    public Character getCharacterValue() {
        return characterValue;
    }
    public void setCharacterValue(Character characterValue) {
        this.characterValue = characterValue;
    }
*/
    public Double getDoubleValue() {
        return doubleValue;
    }
    public void setDoubleValue(Double doubleValue) {
        this.doubleValue = doubleValue;
    }
    public Float getFloatValue() {
        return floatValue;
    }
    public void setFloatValue(Float floatValue) {
        this.floatValue = floatValue;
    }
    public Integer getIntegerValue() {
        return integerValue;
    }
    public void setIntegerValue(Integer integerValue) {
        this.integerValue = integerValue;
    }
    public Key getKey() {
        return key;
    }
    public void setKey(Key key) {
        this.key = key;
    }
    public Long getLongValue() {
        return longValue;
    }
    public void setLongValue(Long longValue) {
        this.longValue = longValue;
    }
    public Short getShortValue() {
        return shortValue;
    }
    public void setShortValue(Short shortValue) {
        this.shortValue = shortValue;
    }
    @Attribute(primaryKey=true)
    private Key key;
    private Boolean booleanValue;
//    private Character characterValue;
    private Short shortValue;
    private Integer integerValue;
    private Long longValue;
    private Float floatValue;
    private Double doubleValue;
}
