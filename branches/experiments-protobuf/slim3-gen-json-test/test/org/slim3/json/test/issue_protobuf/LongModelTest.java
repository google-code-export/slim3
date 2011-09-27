package org.slim3.json.test.issue_protobuf;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

public class LongModelTest extends AppEngineTestCase {
    @Test
    public void test() throws Exception{
        LongModel m = new LongModel();
        m.setLongValue(1L << 53);
        
        System.out.println(String.format("%x", m.getLongValue()));
        
        OutputStream os = new FileOutputStream("long.bin");
        try{
            LongModelMeta.get().modelToPb(m, os);
        } finally{
            os.close();
        }
        
        PrintWriter w = new PrintWriter("pbLongModelMeta.js", "UTF-8");
        try{
            LongModelMeta.get().writePbJs(w);
        } finally{
            w.close();
        }
    }

    @Test
    public void test2() throws Exception{
        long v = 1;
        for(int i = 0; i <= 64; i++){
            System.out.println(String.format("%02d: %d", i + 1, v));
            v <<= 1;
        }
    }
}
