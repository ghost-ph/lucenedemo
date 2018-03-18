package com.patterncat.lucene.basic;

public class BinarySearch {
    public static void main(String[] args) {
        int a[] = { 1, 4, 6, 7, 12, 14, 15 };
        System.out.println();
        int fromIndex = 0, toIndex = 6, key = 4;
        int low = fromIndex; // 开始位置
        int high = toIndex - 1; // 结束位置
        while (low <= high) {
            int mid = (low + high) >>> 1; // 相当于mid = (low + high)/2
            int midVal = a[mid]; // 取中间的值
            int cmp = midVal - key; // 中间值和要找的关键字比较

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else {
                System.out.println("return" + mid); // 查找成功，返回找到的位置
                return;
            }
        }
        System.out.println("return" + -(low + 1)); // 没找到，返回负值
        return;
    }

    int binarySearch(String[] a,String key){
        int low = 0; //开始位置
        int high = a.length - 1; //结束位置

        while (low <= high) {
            int mid = (low + high) >>> 1; //相当于mid = (low + high)/2
            String midVal = a[mid]; //取中间的值
            int cmp = midVal.compareTo(key); //中间值和要找的关键字比较

            if (cmp < 0)
                low = mid + 1;
            else if (cmp > 0)
                high = mid - 1;
            else
                return mid; // 查找成功，返回找到的位置
        }
        return -(low + 1);  // 没找到，返回负值
    }
}
