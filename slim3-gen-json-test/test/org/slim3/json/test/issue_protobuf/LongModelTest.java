package org.slim3.json.test.issue_protobuf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.repackaged.com.google.protobuf.CodedOutputStream;

public class LongModelTest extends AppEngineTestCase {
    @Test
    public void test() throws Exception{
        long v = 1;
        for(int i = 0; i <= 64; i++){
            System.out.println(String.format("%02d: %d", i + 1, v));
            v <<= 1;
        }
    }
    
    @Test
    public void test2() throws Exception{
        long value = (2147483647L << 32) + 9223372036854775807L;
        System.out.println(value);
        System.out.println(Long.MAX_VALUE);
        System.out.println(Long.MAX_VALUE & 0xffffffffL);
        System.out.println(Long.MAX_VALUE & 0xffff);
        System.out.println((long)Math.pow(2, 32));
        for(int i = 53; i < 63; i++){
            StringBuilder b = new StringBuilder();
            b.append("this.positiveBignums[")
                .append(i)
                .append("] = new Array(");
            boolean first = true;
            for(char c : Long.toString(1L << i).toCharArray()){
                if(first) first = false;
                else b.append(", ");
                b.append(c);
            }
            b.append(");");
            System.out.println(b);
        }
        for(int i = 53; i < 64; i++){
            StringBuilder b = new StringBuilder();
            b.append("this.negativeBignums[")
                .append(i)
                .append("] = new Array(");
            boolean first = true;
            for(char c : Long.toString((1L << i) | 0x8000000000000000L).substring(1).toCharArray()){
                if(first) first = false;
                else b.append(", ");
                b.append(c);
            }
            b.append(");");
            System.out.println(b);
        }
        System.out.println(0x0000000000000001L);
        System.out.println(0x8000000000000001L);
        System.out.println(0x8000000000000000L);
        System.out.println(0x0100000000000000L);
        System.out.println(0x8100000000000000L);
    }

    @Test
    public void test3() throws Exception{
        System.out.println(encode(-1).length);
        System.out.println(encode(0x8000000000000000L).length);
        System.out.println(Arrays.toString(encode(0x8000000000000000L)));
        System.out.println(Arrays.toString(encode(-1)));
    }

    @Test
    public void test4() throws Exception{
        testBytes(1234);
        testBytes(123456789);
        testBytes(1234567890);
        testBytes(12345678901L);
        testBytes(123456789012345678L);
        testBytes(1234567890123456789L);
        testBytes(-10);
        testBytes(-23948821142L);
        testBytes(0x8000000000000001L);
    }
    
    @Test
    public void test5() throws Exception{
        System.out.print("rawbytes: ");
        printBytes(new byte[]{
            (byte)((-10 & 0xff00000000000000L) >> 56)
            , (byte)((-10 & 0x00ff000000000000L) >> 48)
            , (byte)((-10 & 0x0000ff0000000000L) >> 40)
            , (byte)((-10 & 0x000000ff00000000L) >> 32)
            , (byte)((-10 & 0x00000000ff000000L) >> 24)
            , (byte)((-10 & 0x0000000000ff0000L) >> 16)
            , (byte)((-10 & 0x000000000000ff00L) >> 8)
            , (byte)((-10 & 0x00000000000000ffL))
        });
        System.out.print("rawbits: ");
        printBits(-10);
        byte[] bytes = encode(-10);
        System.out.print("pbbytes: ");
        printBytes(bytes);
        System.out.print("pbbytes(posi): ");
        printBytes(encode(Long.MAX_VALUE));
        printBytes(encode(decode(bytes)));
        System.out.println(0x8000000000000001L);
        System.out.println(0x7fffffffL);
//        printBytes(encode(~decode(bytes)));
//        System.out.println(-10 + ":" + decode(bytes));
//        printBits(-10);
//        printBits(decode(encode(-10)));
    }
   
    @Test
    public void test6() throws Exception{
        printBits(-10);
        printBits(2147483647);
        printBits(~2147483647);
        printBits(Double.doubleToRawLongBits(2147483647));
        printBits(Double.doubleToRawLongBits(-2147483647));
        System.out.println(0x80000001);
    }
   
    @Test
    public void test7() throws Exception{
        System.out.println(0x80000001);
        System.out.println(0x8000000000000001L);
        System.out.println(0x8000000080000000L);
        printBits(-4611685984067649500L);
        System.out.println((1 | 0x80000000) * Math.pow(2, 1));
        System.out.println((long)(1 | 0x80000000) << 31);
        printBits((long)(1 | 0x80000000) << 31);
        printBits(1 | 0x80000000);
        printBits((long)((0x80000000 * 2L) * Math.pow(2, 31)));
        System.out.println((long)((0x80000000 * 2L) * Math.pow(2, 31)));
        System.out.println((long)((0x80000000 * 2L) * Math.pow(2, 20)));
        System.out.println((long)((0x80000000 * 2L) * Math.pow(2, 21)));

        printBits(Double.doubleToLongBits((long)((0x80000000))));
        printBits(Double.doubleToLongBits((long)((0x80000000 * 2L) * Math.pow(2, 20))));
        printBits(Double.doubleToLongBits((long)((0x80000000 * 2L) * Math.pow(2, 21))));
        printBits((long)((long)(0x80000000 * 1L) * Math.pow(2, -1)));
    }
    
    @Test
    public void test8() throws Exception{
        printBits(0x80000000000fffffL);
        printBits(Double.doubleToLongBits(0x80000000000fffffL));
        System.out.println(0x80000000000fffffL);
        printBits(0x8000000fffffffffL);
        printBits(Double.doubleToLongBits(0x8000000fffffffffL));
        System.out.println(0x8000000fffffffffL);
        System.out.println(0xffffffff00000000L);
        printBits(Double.doubleToLongBits(-13763000465539203000.0));
    }
    
    @Test
    public void test9() throws Exception{
        printBits(1);
        printBits(2113929216);
        printBits(2147483647);
    }
    
    @Test
    public void test10() throws Exception{
        System.out.println(0x800000007fffffffL);
        printBits(0x800000007fffffffL);
        printBits(-1L);
        printBits(-1.0);
        printBits("~0x7fffffff*1.0: ", ~0x7fffffff * 1.0);
        printBits("(0x7fffffff | 0x80000000): ", 0x7fffffff | 0x80000000);
        printBits(~0x7fffffff * Math.pow(2, 31));
        printBits(~1 * Math.pow(2, 62));
        printBits(
            ~1 * Math.pow(2, 62)
            + ~0x7fffffff * Math.pow(2, 31)
            + ~0x7fffffff * 1.0
                    );
        printBits(-7794613248L * 1.0);
        printBits(-7794613171L * 1.0);
    }
    
    @Test
    public void test11() throws Exception{
        printBits(0x8000000100000001L);
        printBits((double)((long)(0x1 | 0x80000000) << 32) + -1);
        printBits((0x00000001 | 0x80000000) * Math.pow(2, 32) + 0xffffffff);
    }

    @Test
    public void test12() throws Exception{
        // 0, 0, 0
        int ex = 0;
        int hi31 = 0;
        int low31 = 0;
        double d = (ex | 0xfffffffe) * Math.pow(2, 62);
        d += (hi31 | 0x80000000) * Math.pow(2, 31);
        d += low31 | 0x80000000;
        System.out.println(d);
    }

    @Test
    public void test13() throws Exception{
        System.out.println(0xc000000000000000L);
        System.out.println(String.format("%.25f", (double)0xc000000000000000L));
        System.out.println((0x1 | 0x80000000) * Math.pow(2, 62));
        System.out.println();
        printBits(0xc000000000000000L);
        printBits((double)(0xc000000000000000L));
        printBits(0x8000000000000001L << 62);
        printBits(0x8000000000000001L * Math.pow(2, 62));
        printBits(0x80000001L * Math.pow(2, 62));
        System.out.println(
            0xc000000000000000L - 0x8000000000000000L);
    }

    @Test
    public void test14() throws Exception{
        printBits(0x8000000000000001L);
        printBits(~1);
        printBits((long)~1);
        printBits(0x8000000000000000L + ~1);
        printBits(0x8000000000000000L + -2);
        printBits(0x8000000000000000L + -1);
        printBits(0x8000000000000000L + 1);
        System.out.println(Long.MIN_VALUE);
    }

    @Test
    public void test15() throws Exception{
        System.out.println(Long.MIN_VALUE);
        System.out.println(String.format("%.20f", (double)Long.MIN_VALUE));
        System.out.println(Long.MAX_VALUE);
        System.out.println(String.format("%.20f", (double)Long.MAX_VALUE));
        System.out.println(Long.MIN_VALUE + 0x7fffffffffffffffL);
        System.out.println(Integer.MIN_VALUE + 0x7fffffff);
        System.out.println((double)Long.MIN_VALUE + 0x7fffffffffffffffL);
        System.out.println((double)Integer.MIN_VALUE + 0x7fffffff);
        System.out.println(-9223372036854770000L + 0x7fffffffffffffffL
            -5808);
        System.out.println(-0x7fffffff00000000L);
        System.out.println(0xffffffffL);
        printBits(-4294967296L);
        printBits((double)-4294967296L);
        printBits((double)-4294967295L);
        printBits(-9223372032559808512L);
        printBits((double)-9223372032559808512L);
        printBits(Double.MAX_VALUE);
        System.out.println(String.format("%20.20f", Math.pow(2, 63)));
    }

    @Test
    public void test16(){
        printBits(Long.MIN_VALUE);
        System.out.println("-9223372036854776000");
        printBits(-9223372032559809000L);
        printBits(-4294967296L);
        printBits(-9223372032559809000L + -4294967296L);
        System.out.println("-- min --");
        System.out.println("expected: -9223372036854775808: 1000000000000000000000000000000000000000000000000000000000000000");
        System.out.println("expected_hi32: -9223372036854775808: 1000000000000000000000000000000000000000000000000000000000000000");
        System.out.println("expected_low32: 0: 0000000000000000000000000000000000000000000000000000000000000000");
        System.out.println("expected_sum: -9223372036854775808: 1000000000000000000000000000000000000000000000000000000000000000");
        System.out.println("true");
    }
    
    @Test
    public void test17(){
        long v = Long.MIN_VALUE;
        int vl = (int)(v & 0xffffffffL);
        long vh = v & 0x7fffffff00000000L;
        long vl2 = vl - 4294967296L; // 296
        long vh2 = vh - 9223372032559808512L; // 12
        double vl3 = vl2;
        double vh3 = vh2;
        printBits(4294967296L);
        printBits(-4294967296L);
        printBits(9223372032559808512L);
        printBits(-9223372032559808512L);
        printBits(" v: ", v);
        printBits("vl: ", vl);
        printBits("vh: ", vh);
        printBits("vl2: ", vl2);
        printBits("vh2: ", vh2);
        printBits("vl3: ", vl3);
        printBits("vh3: ", vh3);
        printBits("vh + vl: ", vh + vl);
        printBits("vh2 + vl2: ", vh2 + vl2);
        printBits("vh3 + vl3: ", vh3 + vl3);
        printBits("~vl: ", ~vl);
    }
    
    @Test
    public void test18(){
        printBits(-9223372032559808500d);
        for(int i = 0; i < 20; i++){
            printBits(-9223372032559808500d - i);
        }
        printBits(-12d);
        printBits(-223372032559808500d - 12d);
        printBits(Double.longBitsToDouble(
            0xc3dfffffffc00000L
//          0b1_100 0011 1101 _1111 1111 1111 1111 1111 1111 1111 1100 00000000000000000000L
            ));
        printBits(Double.longBitsToDouble(
            0xc3dfffffffe00000L
//          0b1_100 0011 1101 _1111 1111 1111 1111 1111 1111 1111 1110 00000000000000000000L
            ));
        printBits(Double.longBitsToDouble(
            0xc3dfffffffc00001L
//          0b1_100 0011 1101 _1111 1111 1111 1111 1111 1111 1111 1100 00000000000000000001L
            ));
        printBits(-9223372032559808512L);
        printBits(-4294967296L);
    }
    
    @Test
    public void test19(){
        printBits(9223372032559808512L);
        printBits(9223372032559808512L * 1.0);
        printBits(0x0088000000000000L);
        printBits(38280596832649216.0-9223372032559808512.0);
        printBits(9223372032559808512.0 + 4294967296.0);
        printBits((long)(9223372032559808512L * 1.0));
        printBits((9223372032559808512L * 1.0));
        printBits(0x0000000100000000L);
        printBits((9223372032559808512L));
        printBits((9223372032559808512L * 1.0) * 8);
    }
    
    @Test
    public void test20(){
        printBits(-9223372032559809000L + -4294967296L);
        printBits(0x8101010100000000L + 0xffffffff00000000L);
        printBits(0x8000000000000000L + 0xffffffff00000000L);
        printBits(808512 + 967296);
        printBits(9223372032559808512L);
        printBits(4294967296L);
        printBits(9223372036854775808.0 * 2);
        printBits(36854775808.0 * 2);
    }

    private void printBytes(byte... bytes){
        for(byte b : bytes){
            System.out.print(String.format("%x,", b));
        }
        System.out.println();
    }

    private void printBits(int bits){
        System.out.print(bits + ": ");
        System.out.print(bits < 0 ? 1 : 0);
        for(int i = 0; i < 31; i++){
            System.out.print(((bits << i) & 0x40000000) != 0 ? 1 : 0);
        }
        System.out.println();
    }
    
    private void printBits(String msg, long v){
        System.out.print(msg);
        printBits(v);
    }
    private void printBits(long bits){
        System.out.print(String.format("%20d: ", bits));
        System.out.print(bits < 0 ? 1 : 0);
        for(int i = 0; i < 63; i++){
            System.out.print(((bits << i) & 0x4000000000000000L) != 0 ? 1 : 0);
        }
        System.out.println();
    }

    private void printBits(String msg, double v){
        System.out.print(msg);
        printBits(v);
    }

    private void printBits(double v){
        System.out.print(String.format("%24.24f: ", v));
        long bits = Double.doubleToRawLongBits(v);
        System.out.print(bits < 0 ? 1 : 0);
        System.out.print(" ");
        for(int i = 0; i < 63; i++){
            System.out.print(((bits << i) & 0x4000000000000000L) != 0 ? 1 : 0);
            if(i == 10) System.out.print(" ");
        }
        System.out.println();
    }

    private void testBytes(long expected) throws IOException{
        System.out.println(expected + ":" + decode(encode(expected)));
        System.out.println(Long.toString(expected, 2)
            + ":" + Long.toString(decode(encode(expected)), 2));
    }
    
    private long decode(byte[] bytes){
        int index = 0;
        byte b = bytes[index++];
        long ret = b & 0x7f;
        System.out.print("b1: ");
        printBits(ret);
        if((b & 0x80) == 0){
            System.out.println(ret);
            return ret;
        }
        int shift = 7;
        while(shift <= 21){
            b = bytes[index++];
            ret += (b & 0x7f) << shift;
            if((b & 0x80) == 0) return ret;
            shift += 7;
        }
        while(shift <= 42){
            b = bytes[index++];
            ret += (b & 0x7f) * Math.pow(2, shift);
            if((b & 0x80) == 0) return ret;
            shift += 7;
        }
        System.out.print("b7: ");
        printBits(ret);
        
        b = bytes[index++];
        ret += (long)(b & 0x1f) * (long)Math.pow(2, shift);
        if((b & 0xe0) == 0) return ret;
        ret += (long)(b & 0x60) * (long)Math.pow(2, shift);

        System.out.print("b8: ");
        printBits(ret);
        
        byte b9 = bytes[index++];
        ret += (long)(b9 & 0x7f) * (long)Math.pow(2, shift + 7);
        System.out.print("b9: ");
        printBits(ret);
        byte b10 = 0;
        if((b9 & 0x80) != 0){
            b10 = bytes[index++];
            if((b10 & 0x80) != 0){
                throw new RuntimeException("malformedVarint");                     
            }
        }
        if(b10 != 0){
            ret |= 0x8000000000000000L;
//            ret = ~ret * -1;
//            ret = ret * -1 + 1;
//            ret = ~ret + 1;
            System.out.print("b10: ");
            printBits(ret);
        }
        return ret;
    }

    private byte[] encode(long v)
    throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CodedOutputStream cos = CodedOutputStream.newInstance(bos);
        cos.writeInt64NoTag(v);
        cos.flush();
        return bos.toByteArray();
    }

    private byte[] encode(int v)
    throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        CodedOutputStream cos = CodedOutputStream.newInstance(bos);
        cos.writeInt32NoTag(v);
        cos.flush();
        return bos.toByteArray();
    }
}
