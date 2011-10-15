package org.slim3.json.test.issue_protobuf;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

@Model
public class LongModel {
    public static LongModel createModel(){
        LongModel m = new LongModel();
        m.setPositive(10);
        return m;
    }
    public Key getKey() {
        return key;
    }
    public void setKey(Key key) {
        this.key = key;
    }
    public long getPositive() {
        return positive;
    }
    public void setPositive(long positive) {
        this.positive = positive;
    }
    public long getNegative() {
        return negative;
    }
    public void setNegative(long negative) {
        this.negative = negative;
    }
    public long getMax() {
        return max;
    }
    public void setMax(long max) {
        this.max = max;
    }
    public long getMin() {
        return min;
    }
    public void setMin(long min) {
        this.min = min;
    }
    public long getMinus1() {
        return minus1;
    }
    public void setMinus1(long minus1) {
        this.minus1 = minus1;
    }
    public long getV2p52() {
        return v2p52;
    }
    public void setV2p52(long v2p52) {
        this.v2p52 = v2p52;
    }
    public long getV2p53() {
        return v2p53;
    }
    public void setV2p53(long v2p53) {
        this.v2p53 = v2p53;
    }
    public long getV2p54() {
        return v2p54;
    }
    public void setV2p54(long v2p54) {
        this.v2p54 = v2p54;
    }
    public long getMinusB20() {
        return minusB20;
    }
    public void setMinusB20(long smallBitMinus) {
        this.minusB20 = smallBitMinus;
    }
    public long getMinusB36() {
        return minusB36;
    }
    public void setMinusB36(long smallBitMinus2) {
        this.minusB36 = smallBitMinus2;
    }
    public long getAlmostMin1() {
        return almostMin1;
    }
    public void setAlmostMin1(long almostMin1) {
        this.almostMin1 = almostMin1;
    }
    public long getAlmostMin2() {
        return almostMin2;
    }
    public void setAlmostMin2(long almostMin2) {
        this.almostMin2 = almostMin2;
    }
    @Attribute(primaryKey=true)
    private Key key;
    private long positive = 32342423444L;
    private long negative = -23948821142L;
    private long minusB20 = 0xfffffffffff03466L; // -9223372036853727233
    private long minusB36 = 0xfffffffe2f67a44dL; // -9223371968135299073
    private long v2p52 = 1L << 52;
    private long v2p53 = 1L << 53;
    private long v2p54 = 1L << 54;
    private long minus1 = -1;
    private long max = Long.MAX_VALUE;
    private long min = Long.MIN_VALUE;
    private long almostMin1 = 0xfffffffffffff000L;
    private long almostMin2 = 0x8007ffffffffffffL;
}
