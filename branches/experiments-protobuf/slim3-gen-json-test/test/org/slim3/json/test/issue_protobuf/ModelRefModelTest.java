package org.slim3.json.test.issue_protobuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Stack;

import org.junit.Assert;
import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.com.google.protobuf.CodedInputStream;

public class ModelRefModelTest extends AppEngineTestCase {
    @Test
    public void test() throws Exception{
        ModelRefModelMeta meta = ModelRefModelMeta.get();
        ModelRefModel m = new ModelRefModel();
        m.setIntValue(100);
        ModelRefModel child = new ModelRefModel();
        child.setIntValue(1000);
        m.getModelRefValue().setModel(child);
        Datastore.put(m, child);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        meta.modelToPb(m, bos, 2);

        CodedInputStream cis = CodedInputStream.newInstance(
            new ByteArrayInputStream(bos.toByteArray()));
        int fieldNum = cis.readTag() >> 3;
        int nest = 0;
        int intValues = 0, keyValues = 0;
        String[] keys = new String[2];
        Stack<Integer> limits = new Stack<Integer>();
        while(fieldNum > 0 || nest > 0){
            if(fieldNum == 1){
                Assert.assertEquals(100 * (int)Math.pow(10, nest), cis.readRawVarint32());
                intValues++;
            } else if(fieldNum == 2){
                Key key = m.getKey();
                if(nest == 1){
                    key = child.getKey();
                }
                Assert.assertEquals(Datastore.keyToString(key), cis.readString());
                keys[keyValues] = Datastore.keyToString(key);
                keyValues++;
            } else if(fieldNum == 3){
                nest++;
                int size = cis.readRawVarint32();
                limits.push(cis.pushLimit(size));
            } else if(fieldNum == 0 && nest > 0){
                nest--;
                cis.popLimit(limits.pop());
            }
            fieldNum = cis.readTag() >> 3;
        }
        Assert.assertEquals(2, intValues);
        Assert.assertEquals(2, keyValues);
        System.out.println(keys[0]);
        System.out.println(keys[1]);
    }
    
    @Test
    public void genJs() throws Exception{
        String name = "ModelRefModel";
        ModelRefModelMeta meta = ModelRefModelMeta.get();
        PrintWriter w = new PrintWriter("www/js/pb" + name + "Meta.js", "UTF-8");
        try{
            meta.writePbJs(w);
        } finally{
            w.close();
        }        
    }

    @Test
    public void genPb() throws Exception{
        ModelRefModelMeta meta = ModelRefModelMeta.get();
        ModelRefModel m = new ModelRefModel();
        m.setIntValue(100);
        ModelRefModel child = new ModelRefModel();
        child.setIntValue(1000);
        m.getModelRefValue().setModel(child);
        Datastore.put(m, child);

        FileOutputStream fos = new FileOutputStream("www/data/ModelRefModel.bin");
        try{
            meta.modelToPb(m, fos, 2);
        } finally{
            fos.close();
        }
    }
}
