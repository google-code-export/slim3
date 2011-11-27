package org.slim3.json.test.issue_protobuf;

import org.slim3.datastore.Datastore;
import org.slim3.tester.AppEngineTester;


public class GenerateJsTests {
    public static void main(String[] args) throws Exception{
        AppEngineTester tester = new AppEngineTester();
        tester.setUp();
        try{
            TestJsGenerator gen = new TestJsGenerator();
            gen.generate(new StringModel());
            gen.generate(new IntModel());
            gen.generate(new LongModel());
            gen.generate(new FloatModel());
            gen.generate(new DoubleModel());
            gen.generate(newModelRefModel());
            gen.generate(newInverseModelListRefModel());
            gen.generate(new AnnotatedStringModel());
            gen.generate(new WrappersListModel());
        } finally{
            tester.tearDown();
        }
    }

    private static ModelRefModel newModelRefModel() throws Exception{
        ModelRefModel m = new ModelRefModel();
        m.setIntValue(100);
        ModelRefModel child = new ModelRefModel();
        child.setIntValue(1000);
        m.getModelRefValue().setModel(child);
        Datastore.put(m, child);
        return m;
    }

    private static InverseModelListRefModel newInverseModelListRefModel() throws Exception{
        InverseModelListRefModel m = new InverseModelListRefModel();
        m.setIntValue(100);
        InverseModelListRefModel child1 = new InverseModelListRefModel();
        child1.setIntValue(1000);
        child1.getParent().setModel(m);
        InverseModelListRefModel child2 = new InverseModelListRefModel();
        child2.setIntValue(2000);
        child2.getParent().setModel(m);
        Datastore.put(m, child1, child2);
        return m;
    }
}
