package org.slim3.json.test.issue_protobuf;

import java.util.List;

import java.util.Arrays;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class WrappersListModel {
    public List<Boolean> getBooleanList() {
        return booleanList;
    }
    public void setBooleanList(List<Boolean> booleanList) {
        this.booleanList = booleanList;
    }
    public List<Double> getDoubleList() {
        return doubleList;
    }
    public void setDoubleList(List<Double> doubleList) {
        this.doubleList = doubleList;
    }
    public List<Float> getFloatList() {
        return floatList;
    }
    public void setFloatList(List<Float> floatList) {
        this.floatList = floatList;
    }
    public List<Integer> getIntegerList() {
        return integerList;
    }
    public void setIntegerList(List<Integer> integerList) {
        this.integerList = integerList;
    }
    public Key getKey() {
        return key;
    }
    public void setKey(Key key) {
        this.key = key;
    }
    public List<Long> getLongList() {
        return longList;
    }
    public void setLongList(List<Long> longList) {
        this.longList = longList;
    }
    public List<Short> getShortList() {
        return shortList;
    }
    public void setShortList(List<Short> shortList) {
        this.shortList = shortList;
    }
    
    @Attribute(primaryKey=true)
    private Key key;
    private List<Boolean> booleanList = Arrays.asList(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
    private List<Short> shortList = Arrays.asList((short)1, (short)2, (short)3);
    private List<Integer> integerList = Arrays.asList(1, 2, 3);
    private List<Long> longList = Arrays.asList(1L, 2L, 3L);
    private List<Float> floatList = Arrays.asList(1.1f, 2.2f, 3.3f);
    private List<Double> doubleList = Arrays.asList(1.1, 2.2, 3.3);
}
