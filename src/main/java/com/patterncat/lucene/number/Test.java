package com.patterncat.lucene.number;

public class Test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        //System.out.println(Long.MIN_VALUE);
        String zore = (long2sortableStr(Long.MIN_VALUE));
        String one = (long2sortableStr(0L));
        String two = (long2sortableStr(1L));
        String three = (long2sortableStr(2L));
        System.out.println( three);
        //System.out.println( (two.compareToIgnoreCase(one) ));
    }

    public static String long2sortableStr(long val) {
        char[] arr = new char[5];
        long2sortableStr(val,arr,0);
        return new String(arr,0,5);
    }

    public static int long2sortableStr(long val, char[] out, int offset) {
        System.out.println("1: "+Long.toBinaryString(val));
        val += Long.MIN_VALUE;
        System.out.println("2: "+val);
        System.out.println("3: "+Long.toBinaryString(val));
        out[offset++] = (char)(val >>>60);
        out[offset++] = (char)(val >>>45 & 0x7fff); //取17位  val从45 – 60
        out[offset++] = (char)(val >>>30 & 0x7fff);
        out[offset++] = (char)(val >>>15 & 0x7fff);
        out[offset] = (char)(val & 0x7fff);
        return 5;
    }


}

