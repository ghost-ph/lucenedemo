package com.patterncat.lucene.spider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class RetrieveMp3 {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String mp3URL = "http://cn.voicedic.com/voicefi1e/mandarin/mandarinyi1.mp3";
        URLConnection conn = new URL(mp3URL).openConnection();
        InputStream is = conn.getInputStream();

        OutputStream outstream = new FileOutputStream(new File("d:/file.mp3"));
        byte[] buffer = new byte[4096];
        int len;
        while ((len = is.read(buffer)) > 0) {
            outstream.write(buffer, 0, len);
        }
        outstream.close();
    }


}
