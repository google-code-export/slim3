package org.slim3.json.test.issue_protobuf;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

public class FloatModelTest extends AppEngineTestCase {
    @Test
    public void test2() throws Exception{
        float d = 0;
        for(int i = 0; i < 52; i++){
            d += d * 2 + 1;
        }
        System.out.println(String.format("%1.16g", d));
        System.out.println(String.format("%f", -233323.4333584f));
    }
}
