package com.patterncat.lucene.spider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;

public class GetAllMp3 {

    public static void main(String[] args) throws IOException {
        InputStream file = new FileInputStream(new File(
                "D:/lg/work/navinfo/dic/hanziPinyin.txt"));

        // 为了防止乱码，在读文件的地方指定文件的字符编码集
        BufferedReader in = new BufferedReader(new InputStreamReader(file,
                "GBK"));
        String word;
        HashSet<String> yin = new HashSet<String>();
        while ((word = in.readLine()) != null) {
            word = word.substring(1);
            yin.add(word);
        }
        in.close();

        for(String e:yin){
            System.out.println(e);
            getFile(e);
        }
    }

    public static void getFile(String yin) throws IOException {
        String mp3URL = "http://cn.voicedic.com/voicefi1e/mandarin/mandarin"+yin+".mp3";
        URLConnection conn = new URL(mp3URL).openConnection();
        InputStream is = null;
        try{
            is = conn.getInputStream();
        }
        catch(java.io.FileNotFoundException e){
            if(is!=null)
                is.close();
            return;
        }

        OutputStream outstream = new FileOutputStream(new File("d:/lg/nlp/yin/"+yin+".mp3"));
        byte[] buffer = new byte[4096];
        int len;
        while ((len = is.read(buffer)) > 0) {
            outstream.write(buffer, 0, len);
        }
        outstream.close();
    }
}

