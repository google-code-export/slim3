package org.slim3.json.test.issue_protobuf;


public class FloatModelTestJsGenerator{
    public static void main(String[] args) throws Exception{
        FloatModel m = new FloatModel();
        float d = 0;
        for(int i = 0; i < 52; i++){
            d += d * 2 + 1;
        }
        m.setPositive(d);
        m.setNegativeFloatValue(-233323.4333584f);
        new TestJsGenerator().generate(m);
    }
}
