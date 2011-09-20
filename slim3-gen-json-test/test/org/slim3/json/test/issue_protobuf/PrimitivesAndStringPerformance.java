package org.slim3.json.test.issue_protobuf;

import java.io.ByteArrayOutputStream;

public class PrimitivesAndStringPerformance {
    public static void main(String[] args) throws Exception{
        System.out.println(PrimitivesAndStringModelMeta.get()
            .modelToJson(PrimitivesAndStringModel.createModel(1)));
        speed(1);
        speed(1000);
        speed(10000);
        speed(50000);
        size();
    }

    public static void speed(int n) throws Exception{
        PrimitivesAndStringModelMeta meta = PrimitivesAndStringModelMeta.get();
        PrimitivesAndStringModel[] models = new PrimitivesAndStringModel[100];
        for(int i = 0; i < 100; i++){
            models[i] = PrimitivesAndStringModel.createModel((int)(Math.random() * 32767));
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
                for(int j = 0; j < 100; j++){
                    meta.modelToJson(models[j]);
                }
            }
            long d = System.currentTimeMillis() - s;
            System.out.println("jsontime: " + d);
        }
    }
    
    public static void size() throws Exception{
        PrimitivesAndStringModelMeta meta = PrimitivesAndStringModelMeta.get();
        PrimitivesAndStringModel[] models = new PrimitivesAndStringModel[100];
        for(int i = 0; i < 100; i++){
            models[i] = PrimitivesAndStringModel.createModel((int)(Math.random() * 32767));
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
