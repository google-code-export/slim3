package org.slim3.json.test.issue_protobuf;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import testproto.TestProtos.Person;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.com.google.protobuf.CodedOutputStream;

public class ProtobufTest extends AppEngineTestCase{
    @Test
    public void test() throws Exception{
        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
        ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
        CodedOutputStream cos = CodedOutputStream.newInstance(bos1);
        String key = KeyFactory.createKeyString("test", 1);
        cos.writeString(1, key);
        cos.writeString(2, "suzuki");
        cos.writeInt32(3, 30);
        cos.flush();

        Person p = Person.newBuilder()
            .setKey(key)
            .setName("suzuki")
            .setAge(30)
            .build();
        p.writeTo(bos2);
        Assert.assertArrayEquals(bos2.toByteArray(), bos1.toByteArray());
    }

    @Test
    public void test2() throws Exception{
        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
        ByteArrayOutputStream bos2 = new ByteArrayOutputStream();

        TestModel m = new TestModel();
        m.setName("hello");
        TestModelMeta.get().modelToPb(m, bos1);
        
        CodedOutputStream cos = CodedOutputStream.newInstance(bos2);
        cos.writeString(2, "hello");
        cos.flush();

        Assert.assertArrayEquals(bos2.toByteArray(), bos1.toByteArray());
        OutputStream os = new FileOutputStream("test.bin");
        os.write(bos1.toByteArray());
        os.close();
        byte[] buff = new byte[100];
        int n = new FileInputStream("test.bin").read(buff);
        Assert.assertArrayEquals(bos2.toByteArray(), Arrays.copyOf(buff,  n));
    }

    @Test
    public void modelToPb() throws Exception{
        TestModel m = new TestModel();
        m.setName("hello");
        OutputStream os = new FileOutputStream("test.bin");
        try{
            TestModelMeta.get().modelToPb(m, os);
        } finally{
            os.close();
        }
    }

    @Test
    public void modelsToPb() throws Exception{
        TestModel m1 = new TestModel();
        m1.setName("hello");
        TestModel m2 = new TestModel();
        m2.setName("world");
        OutputStream os = new FileOutputStream("tests.bin");
        try{
            TestModelMeta.get().modelsToPb(new Object[]{m1, m2}, os);
        } finally{
            os.close();
        }
    }

    @Test
    public void writePbJs() throws Exception{
        PrintWriter w = new PrintWriter(new FileOutputStream("pbTestModelMeta.js"));
        try{
            TestModelMeta.get().writePbJs(w);
        } finally{
            w.close();
        }
    }

    @Test
    public void sizeComparison() throws Exception{
        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
        ByteArrayOutputStream bos2 = new ByteArrayOutputStream();

        TestModelMeta meta = TestModelMeta.get();
        TestModel m1 = new TestModel();
        m1.setName("hello");
        TestModel m2 = new TestModel();
        m2.setName("world");

        OutputStreamWriter w = new OutputStreamWriter(bos1, "UTF-8");
        w.write(meta.modelsToJson(new Object[]{m1, m2}));
        w.close();
        meta.modelsToPb(new Object[]{m1, m2}, bos2);
        
        System.out.println(bos1.size());
        System.out.println(bos2.size());
    }
}
