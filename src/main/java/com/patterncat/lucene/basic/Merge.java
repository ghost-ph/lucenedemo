package com.patterncat.lucene.basic;

public class Merge {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int[] a = { 1, 3, 5, 7, 9,11 };
        int[] b = { 2, 3, 4, 6, 8, 10 };

        int aindex = 0;
        int bindex = 0;
        while (aindex < a.length && bindex < b.length) {
            if (a[aindex] == b[bindex]) {
                System.out.println(a[aindex]+"\n"+b[bindex]);
                aindex++;
                bindex++;
            } else if (a[aindex] < b[bindex]) {
                System.out.println(a[aindex]);
                aindex++;
            } else {
                System.out.println(b[bindex]);
                bindex++;
            }
        }
        while (aindex<a.length)
        {
            System.out.println(a[aindex]);
            aindex++;
        }
        while (bindex<b.length)
        {
            System.out.println(b[aindex]);
            bindex++;
        }
    }

}
