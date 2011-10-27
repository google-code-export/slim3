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
        System.out.println(m.getNegativeDoubleValue());
        String fmt = "<p>%s: %s<br>actual: <span id=\"%1$s\"></span></p>";
        System.out.println(String.format(fmt, "maxValue", str(m.getMaxValue())));
        System.out.println(String.format(fmt, "minValue", str(m.getMinValue())));
        System.out.println(String.format(fmt, "minM2Value", str(m.getMinM2Value())));
        System.out.println(String.format(fmt, "minNormalValue", str(m.getMinNormalValue())));
        
        OutputStream os = new FileOutputStream("double.bin");
        try{
            DoubleModelMeta.get().modelToPb(m, os);
        } finally{
            os.close();
        }

        PrintWriter w = new PrintWriter("pbDoubleModelMeta.js", "UTF-8");
        try{
            DoubleModelMeta.get().writePbModelMetaJs(w);
        } finally{
            w.close();
        }
    }

    private String str(double d){
        return String.format("%1.40g", d).replaceAll("([^\\.])0+e", "$1e");
    }
}
