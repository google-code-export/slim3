package org.slim3.json.test.issue_protobuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Test;

import com.google.appengine.repackaged.com.google.protobuf.CodedInputStream;

public class WrappersTest {
    @Test
    public void test() throws Exception{
        WrappersModel m = createModel(1);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        WrappersModelMeta.get().modelToPb(m, bos);

        CodedInputStream cis = CodedInputStream.newInstance(
            new ByteArrayInputStream(bos.toByteArray()));
        assertValues(cis, 1);
    }

    @Test
    public void testArray() throws Exception{
        WrappersModel m1 = createModel(1);
        WrappersModel m2 = createModel(8);
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        WrappersModelMeta.get().modelsToPb(new Object[]{m1, m2}, bos);
        
        CodedInputStream cis = CodedInputStream.newInstance(
            new ByteArrayInputStream(bos.toByteArray()));
        int size = cis.readRawVarint32();
        int oldSize = cis.pushLimit(size);
        assertValues(cis, 1);
        cis.popLimit(oldSize);
        size = cis.readRawVarint32();
        oldSize = cis.pushLimit(size);
        assertValues(cis, 8);
    }

    private WrappersModel createModel(int baseValue){
        WrappersModel m = new WrappersModel();
        m.setBooleanValue(true);
        m.setDoubleValue((double)baseValue++);
        m.setFloatValue((float)baseValue++);
        m.setIntegerValue(baseValue++);
        m.setLongValue((long)baseValue++);
        m.setShortValue((short)baseValue++);
        return m;
    }
    
    private void assertValues(CodedInputStream cis, int baseValue) throws Exception{
        Assert.assertEquals(1, cis.readTag() >> 3);
        Assert.assertEquals(true, cis.readBool());
        Assert.assertEquals(2, cis.readTag() >> 3);
        Assert.assertEquals(baseValue++, cis.readDouble(), 0);
        Assert.assertEquals(3, cis.readTag() >> 3);
        Assert.assertEquals(baseValue++, cis.readFloat(), 0);
        Assert.assertEquals(4, cis.readTag() >> 3);
        Assert.assertEquals(baseValue++, cis.readInt32());
        Assert.assertEquals(6, cis.readTag() >> 3);
        Assert.assertEquals(baseValue++, cis.readInt64());
        Assert.assertEquals(7, cis.readTag() >> 3);
        Assert.assertEquals((short)baseValue++, cis.readInt32());
    }
}
