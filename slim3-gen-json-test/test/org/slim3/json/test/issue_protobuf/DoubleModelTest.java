package org.slim3.json.test.issue_protobuf;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

public class DoubleModelTest extends AppEngineTestCase {
    @Test
    public void test() throws Exception{
        DoubleModel m = new DoubleModel();
        double d = 0;
        for(int i = 0; i < 52; i++){
            d += d * 2 + 1;
        }
        m.setDoubleValue(d);
        m.setNegativeDoubleValue(-233323.4333584);
        
        System.out.println(Long.toString(Double.doubleToRawLongBits(d), 2));
        
        System.out.println(m.getDoubleValue());
        
        OutputStream os = new FileOutputStream("double.bin");
        try{
            DoubleModelMeta.get().modelToPb(m, os);
        } finally{
            os.close();
        }

        PrintWriter w = new PrintWriter("pbDoubleModelMeta.js", "UTF-8");
        try{
            DoubleModelMeta.get().writePbJs(w);
        } finally{
            w.close();
        }
    }
}
