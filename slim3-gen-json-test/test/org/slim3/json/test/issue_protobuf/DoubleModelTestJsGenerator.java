package org.slim3.json.test.issue_protobuf;


public class DoubleModelTestJsGenerator{
    public static void main(String[] args) throws Exception{
        DoubleModel m = new DoubleModel();
        double d = 0;
        for(int i = 0; i < 52; i++){
            d += d * 2 + 1;
        }
        m.setDoubleValue(d);
        m.setNegativeDoubleValue(-233323.4333584);
        new TestJsGenerator().generate(m);
    }
}
