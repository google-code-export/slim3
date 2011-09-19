package org.slim3.json.test.issue_protobuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.junit.Assert;
import org.junit.Test;

import com.google.appengine.repackaged.com.google.protobuf.CodedInputStream;

public class PrimitivesTest {
    @Test
    public void test() throws Exception{
        PrimitivesModel m = createModel(1);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivesModelMeta.get().modelToPb(m, bos);

        CodedInputStream cis = CodedInputStream.newInstance(
            new ByteArrayInputStream(bos.toByteArray()));
        assertValues(cis, 1);
    }

    @Test
    public void testArray() throws Exception{
        PrimitivesModel m1 = createModel(1);
        PrimitivesModel m2 = createModel(8);
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrimitivesModelMeta.get().modelsToPb(new Object[]{m1, m2}, bos);
        
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
    
    @Test
    public void modelsToPb() throws Exception{
        PrimitivesModel m1 = createModel(1);
        PrimitivesModel m2 = createModel(8);
        OutputStream os = new FileOutputStream("www/primitives.bin");
        try{
            PrimitivesModelMeta.get().modelsToPb(new Object[]{m1, m2}, os);
        } finally{
            os.close();
        }
        PrintWriter w = new PrintWriter(new FileOutputStream("www/pbPrimitivesModelMeta.js"));
        try{
            PrimitivesModelMeta.get().writePbJs(w);
        } finally{
            w.close();
        }
    }
    
    @Test
    public void d() throws Exception{
        long raw = Double.doubleToRawLongBits(1.0);
        System.out.println(String.format(
            "raw : %64s", Long.toString(raw, 2)));
        System.out.println(String.format(
            "sign: %64s", Long.toString(raw & 0x8000000000000000L, 2)));
        System.out.println(String.format(
            "exp : %11s", Long.toString((raw >> 52) & 0x7ff, 2)));
        System.out.println(String.format(
            "frac: %52s",  Long.toString(raw & 0xfffffffffffffL, 2)));
    }
    
    private PrimitivesModel createModel(int baseValue){
        PrimitivesModel m = new PrimitivesModel();
        m.setBooleanValue(true);
        m.setDoubleValue(baseValue++);
        m.setFloatValue(baseValue++);
        m.setIntValue(baseValue++);
        m.setLongValue(baseValue++);
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
