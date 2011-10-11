package org.slim3.json.test.issue_protobuf;


public class LongModelTestJsGenerator{
    public static void main(String[] args) throws Exception{
        LongModel m = new LongModel();
        m.setPositive(32342423444L);
        m.setNegative(-23948821142L);
        m.setV2p52(1L << 52);
        m.setV2p53(1L << 53);
        m.setV2p54(1L << 54);
        new TestJsGenerator().generate(m);
    }
}
