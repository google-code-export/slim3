package org.slim3.json.test.issue_protobuf;


public class IntModelTestJsGenerator{
    public static void main(String[] args) throws Exception{
        IntModel m = new IntModel();
        m.setPositive(1 << 20);
        m.setNegative(-13924443);
        new TestJsGenerator().generate(m);
    }
}
