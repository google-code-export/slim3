package org.slim3.json.test.issue_protobuf;


public class StringModelTestJsGenerator {
    public static void main(String[] args) throws Exception{
        StringModel m = new StringModel();
        m.setHello("hello");
        m.setJapanese("こんにちは");
        m.setSurrogate("叱");
        new TestJsGenerator().generate(m);
    }
}
