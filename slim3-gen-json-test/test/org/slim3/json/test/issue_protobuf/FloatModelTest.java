package org.slim3.json.test.issue_protobuf;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

public class FloatModelTest extends AppEngineTestCase {
    @Test
    public void test() throws Exception{
        FloatModelMeta meta = FloatModelMeta.get();
        FloatModel m = new FloatModel();
        float d = 0;
        for(int i = 0; i < 52; i++){
            d += d * 2 + 1;
        }
        m.setFloatValue(d);
        m.setNegativeFloatValue(-233323.4333584f);
        System.out.println(String.format("%1.16g", m.getFloatValue()));
        System.out.println(String.format("%f", m.getNegativeFloatValue()));
        String fmt = "<p>%s: %s<br>actual: <span id=\"%1$s\"></span></p>";
        System.out.println(String.format(fmt, "maxValue", str(m.getMaxValue())));
        System.out.println(String.format(fmt, "minValue", str(m.getMinValue())));
        System.out.println(String.format(fmt, "minP1Value", str(m.getMinP1Value())));
        System.out.println(String.format(fmt, "minNormalValue", str(m.getMinNormalValue())));

        OutputStream os = new FileOutputStream("float.bin");
        try{
            meta.modelToPb(m, os);
        } finally{
            os.close();
        }

        PrintWriter w = new PrintWriter("pbFloatModelMeta.js", "UTF-8");
        try{
            meta.writePbJs(w);
        } finally{
            w.close();
        }
    }

    private String str(float d){
        return String.format("%1.20g", d).replaceAll("0+e", "e");
    }
}
