package org.slim3.json.test.issue_protobuf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.google.appengine.repackaged.com.google.protobuf.CodedOutputStream;

public class PrimitivesAndStringPerformance {
    public static void main(String[] args) throws Exception{
        speed(1);
        speed(1000);
        speed(10000);
        speed(50000);
        size();
    }

    public static void speed_orig(int n) throws Exception{
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
            System.out.println("pbtime" + n + ": " + d);
        }
        {
            long s = System.currentTimeMillis();
            for(int i = 0; i < n; i++){
                for(int j = 0; j < 100; j++){
                    new OutputStreamWriter(new ByteArrayOutputStream(), "UTF-8")
                        .write(meta.modelToJson(models[j]));
//                    new ByteArrayOutputStream().write(
//                        meta.modelToJson(models[j]).getBytes("UTF-8"));
                }
            }
            long d = System.currentTimeMillis() - s;
            System.out.println("jsontime" + n + ": " + d);
        }
    }
    
    static class NullOutputStream extends OutputStream{
        @Override
        public void write(int arg0) throws IOException {
        }
    }
    
    public static void speed(int n) throws Exception{
        PrimitivesAndStringModelMeta meta = PrimitivesAndStringModelMeta.get();
        PrimitivesAndStringModel[] models = new PrimitivesAndStringModel[100];
        for(int i = 0; i < 100; i++){
            models[i] = PrimitivesAndStringModel.createModel((int)(Math.random() * 32767));
        }
        {
            long d = 0;
            for(int i = 0; i < n; i++){
                for(int j = 0; j < 100; j++){
                    CodedOutputStream cos = CodedOutputStream.newInstance(new NullOutputStream());
                    long s = System.currentTimeMillis();
                    meta.modelToPb(cos, models[j], 1, 0);
                    d += System.currentTimeMillis() - s;
                }
            }
            System.out.println("pbtime" + n + ": " + d);
        }
        {
            long d = 0;
            for(int i = 0; i < n; i++){
                for(int j = 0; j < 100; j++){
                    OutputStreamWriter w = new OutputStreamWriter(new NullOutputStream(), "UTF-8");
                    long s = System.currentTimeMillis();
                    w.write(meta.modelToJson(models[j]));
                    d += System.currentTimeMillis() - s;
                }
            }
            System.out.println("jsontime" + n + ": " + d);
        }
    }
    
    private static void writePb(PrimitivesAndStringModelMeta meta, PrimitivesAndStringModel m)
    throws IOException{
        CodedOutputStream cos = CodedOutputStream.newInstance(new ByteArrayOutputStream());
/*
        cos.writeBool(1, m.isBooleanValue());
        cos.writeDouble(2, m.getDoubleValue());
        cos.writeFloat(3, m.getFloatValue());
        cos.writeInt32(4, m.getIntValue());
        if(m.getKey() != null){
            cos.writeString(5, com.google.appengine.api.datastore.KeyFactory.keyToString(m.getKey()));
        }
        cos.writeInt64(6, m.getLongValue());
        cos.writeInt32(7, m.getShortValue());
        if(m.getStringValue() != null){
            cos.writeString(8, m.getStringValue());
        }
*/
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
