package org.slim3.json.test.issue_protobuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.repackaged.com.google.protobuf.CodedInputStream;

public class IntModelTest extends AppEngineTestCase {
    @Test
    public void test2() throws Exception{
        IntModelMeta meta = IntModelMeta.get();
        IntModel m = new IntModel();
        m.setPositive(1 << 20);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        meta.modelToPb(m, bos);
        CodedInputStream cis = CodedInputStream.newInstance(new ByteArrayInputStream(bos.toByteArray()));
        int n = cis.readTag() >> 3;
        while(n != 0){
            System.out.print(n + ": ");
            if(n == 2) System.out.println(cis.readString());
            else System.out.println(cis.readInt32());
            n = cis.readTag() >> 3;
        }
    }
}
