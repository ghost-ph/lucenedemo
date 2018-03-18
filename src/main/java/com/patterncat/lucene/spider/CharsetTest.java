package com.patterncat.lucene.spider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.mozilla.universalchardet.UniversalDetector;

public class CharsetTest {
    public static void main(String[] args) throws IOException {
        File file1=new File("D:\\test\\gb2312.txt");
        File file2=new File("D:\\test\\utf-8.txt");
        BufferedReader br1=new BufferedReader(new FileReader(file1));
        BufferedReader br2=new BufferedReader(new FileReader(file2));
        String context1=br1.readLine();
        String context2=br2.readLine();
        byte[] bytes1=context1.getBytes();
        byte[] bytes2=context2.getBytes();
        UniversalDetector detector = new UniversalDetector(null);
        //给编码检测器提供数据
        detector.handleData(bytes1, 0, bytes1.length);
        //通知编码检测器数据已经结束
        detector.dataEnd();
        //取得检测出的编码名
        System.out.println(detector.getDetectedCharset()+"  D:\\test\\gb2312.txt");

        //在再次使用编码检测器之前，先调用UniversalDetector.reset()
        detector.reset();
        //给编码检测器提供数据
        detector.handleData(bytes2, 0, bytes2.length);
        //通知编码检测器数据已经结束
        detector.dataEnd();
        //取得检测出的编码名
        System.out.println(detector.getDetectedCharset()+"  D:\\test\\utf-8.txt");
    }
}

