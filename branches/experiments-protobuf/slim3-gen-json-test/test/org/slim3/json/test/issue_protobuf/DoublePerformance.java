package org.slim3.json.test.issue_protobuf;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import sun.nio.cs.StreamEncoder;

public class DoublePerformance {
    public static void main(String[] args) throws Exception{
        System.out.println(DoubleModelMeta.get()
            .modelToJson(DoubleModel.createModel()));
        speed(1);
        speed(1000);
        speed(10000);
        speed(50000);
        size();
    }

    public static void speed(int n) throws Exception{
        DoubleModelMeta meta = DoubleModelMeta.get();
        DoubleModel[] models = new DoubleModel[100];
        for(int i = 0; i < 100; i++){
            models[i] = DoubleModel.createModel();
        }
        {
            long s = System.currentTimeMillis();
            for(int i = 0; i < n; i++){
                for(int j = 0; j < 100; j++){
                    meta.modelToPb(models[j], new ByteArrayOutputStream());
                }
            }
            long d = System.currentTimeMillis() - s;
            System.out.println("pbtime: " + d);
        }
        {
            long s = System.currentTimeMillis();
            for(int i = 0; i < n; i++){
                ByteArrayOutputStream bos = null;
                for(int j = 0; j < 100; j++){
                    bos = new ByteArrayOutputStream();
//                    new OutputStreamWriter(new ByteArrayOutputStream(), "UTF-8")
//                        .write(meta.modelToJson(models[j]));
//                    bos.write(Charset.forName("UTF-8").newEncoder().encode(
//                            CharBuffer.wrap(meta.modelToJson(models[j]))
//                            ).array());
                    new ByteArrayOutputStream().write(
                        meta.modelToJson(models[j]).getBytes("UTF-8"));
                }
                bos.toByteArray();
            }
            long d = System.currentTimeMillis() - s;
            System.out.println("jsontime: " + d);
        }
    }
    
    public static void size() throws Exception{
        DoubleModelMeta meta = DoubleModelMeta.get();
        DoubleModel[] models = new DoubleModel[100];
        for(int i = 0; i < 100; i++){
            models[i] = DoubleModel.createModel();
        }
        {
            long sum = 0;
            for(int i = 0; i < 1000; i++){
                for(int j = 0; j < 100; j++){
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    meta.modelToPb(models[j], bos);
                    sum += bos.size();
                }
            }
            System.out.println("pbsize: " + (sum / (1000 * 100)));
        }
        {
            long sum = 0;
            for(int i = 0; i < 1000; i++){
                for(int j = 0; j < 100; j++){
                    String json = meta.modelToJson(models[j]);
                    sum += json.getBytes("UTF-8").length;
                }
            }
            System.out.println("jsonsize: " + (sum / (1000 * 100)));
        }
    }
}
