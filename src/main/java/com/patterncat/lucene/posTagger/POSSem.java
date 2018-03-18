package com.patterncat.lucene.posTagger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class POSSem {
    byte pos; //词性编码
    String[] codes; //语义编码集合

    public POSSem(DataInputStream inStream) throws IOException {
        pos = inStream.readByte();
        byte codesCount = inStream.readByte(); //类型数量
        codes = new String[codesCount];

        for (int i = 0; i < codesCount; i++) {
            int length = inStream.readInt();
            byte[] bytebuff = new byte[length];
            inStream.read(bytebuff);
            String code = new String(bytebuff, "UTF-8");
            codes[i] = code;
        }
    }

    public POSSem(byte n, String... semCodes) {
        pos = n;
        codes = semCodes;
    }

    public void save(DataOutputStream outStream) throws IOException{
        outStream.writeByte(pos);
		/* 写入key的数量 */
        outStream.writeByte(codes.length);

        Charset charset = Charset.forName("utf-8"); //得到字符集

		/* 写入前缀词编号 */
        for (int i = 0; i < codes.length; i++) {
            CharBuffer data = CharBuffer.wrap(codes[i].toCharArray());
            ByteBuffer bb = charset.encode(data);

			/* 写入词的长度 */
            outStream.writeInt(bb.limit());
			/* 写入词的内容 */
            for (int k = 0; k < bb.limit(); ++k)
                outStream.write(bb.get(k));
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(PartOfSpeech.names[pos]);
        sb.append(" ");
        for(String code : codes){
            sb.append(code);
            sb.append(" ");
        }
        return sb.toString();
    }
}
