package org.slim3.json.test.issue_protobuf;


public class GenerateJsTests {
    public static void main(String[] args) throws Exception{
        TestJsGenerator gen = new TestJsGenerator();
        gen.generate(new StringModel());
        gen.generate(new IntModel());
        gen.generate(new LongModel());
        gen.generate(new FloatModel());
        gen.generate(new DoubleModel());
    }
}
