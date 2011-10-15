package org.slim3.json.test.issue_protobuf;


public class StringModelTestJsGenerator {
    public static void main(String[] args) throws Exception{
        new TestJsGenerator().generate(new StringModel());
    }
}
