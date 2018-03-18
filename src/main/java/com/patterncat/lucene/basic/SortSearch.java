package com.patterncat.lucene.basic;

public class SortSearch{
    public static void main(String[] args){
        int[] array={1,4,6,7,12,14,15};
        System.out.println();
        int ret =binarySearch( array,4,0,6);
        System.out.println("ret"+ret);
    }

    static int binarySearch(int[] array,int value,int left,int right){
        if(left>right)
            return-1;
        int middle=(left+right)/2;
        if (array[middle]==value)
            return middle;
        else if(array[middle]>value)
            return binarySearch(array,value,left,middle-1);
        else
            return binarySearch(array,value,middle+1,right);
    }
}
