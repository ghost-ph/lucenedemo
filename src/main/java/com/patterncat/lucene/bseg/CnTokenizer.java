package com.patterncat.lucene.bseg;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;

/**
 *
 * 分词的输入，输出类
 *
 */
public class CnTokenizer extends Tokenizer {
    private static TernarySearchTrie dic = new TernarySearchTrie("SDIC.txt");
    private CharTermAttribute termAtt;// 词属性
    private static final int IO_BUFFER_SIZE = 4096;
    private char[] ioBuffer = new char[IO_BUFFER_SIZE];
    private OffsetAttribute offsetAtt;

    private boolean done;
    private int i = 0;// i是用来控制匹配的起始位置的变量
    private int upto = 0;

    public CnTokenizer(Reader reader) {
        super(reader);
        this.offsetAtt = addAttribute(OffsetAttribute.class);
        this.termAtt = addAttribute(CharTermAttribute.class);
        this.done = false;
    }

    public void resizeIOBuffer(int newSize) {
        if (ioBuffer.length < newSize) {
            // Not big enough; create a new array with slight
            // over allocation and preserve content
            final char[] newCharBuffer = new char[newSize];
            System.arraycopy(ioBuffer, 0, newCharBuffer, 0, ioBuffer.length);
            ioBuffer = newCharBuffer;
        }
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (!done) {
            clearAttributes();
            done = true;
            upto = 0;  //文本坐标 从后往前
            i = 0;
            while (true) {
                final int length = input.read(ioBuffer, upto, ioBuffer.length
                        - upto);
                if (length == -1)
                    break;
                upto += length;
                if (upto == ioBuffer.length)
                    resizeIOBuffer(upto * 2);
                upto = upto-1;
            }
        }

        if (i < upto) {
            char[] word = dic.matchLong(ioBuffer, upto);// 逆向最大长度匹配
            int len = upto+1;
            if (word != null)// 已经匹配上
            {
                termAtt.copyBuffer(word, 0, word.length);
                offsetAtt.setOffset(len-word.length, len);
                // 如果这个词是词库中的那么就打印出来
                // System.out.print(word + " ");
                // result.add(word);
                upto -= word.length;
            } else {
                // word = new char[]{sentence[i]};
                // result.add(word);
                termAtt.copyBuffer(ioBuffer, upto, 1);
                offsetAtt.setOffset(len-1, len);
                --upto;// 下次匹配点在这个字符之后
            }
            return true;
        }

        return false;
    }
}