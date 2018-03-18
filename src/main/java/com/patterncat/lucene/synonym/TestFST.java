package com.patterncat.lucene.synonym;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.IntsRef;
import org.apache.lucene.util.fst.Builder;
import org.apache.lucene.util.fst.PositiveIntOutputs;
import org.apache.lucene.util.fst.FST;
import org.apache.lucene.util.fst.Util;

public class TestFST {
    public static void main(String[] args) throws Exception {
        testInputChar();
    }

    public static void testFST() throws Exception {
        // 输入键必须按Unicode顺序排序
        String inputValues[] = { "cat", "dog", "dogs" }; // 词
        long outputValues[] = { 5, 7, 12 }; // 词对应的值

        // FST 会共享输出
        final PositiveIntOutputs outputs = PositiveIntOutputs.getSingleton();
        // 构建一个FST映射BytesRef -> Long
        final Builder<Long> builder = new Builder<Long>(FST.INPUT_TYPE.BYTE1,
                outputs);
        // 输入
        BytesRef scratchBytes = new BytesRef();
        IntsRef scratchInts = new IntsRef();
        for (int i = 0; i < inputValues.length; i++) {
            scratchBytes.copyChars(inputValues[i]);
            // 从输入到输出
            builder.add(Util.toIntsRef(scratchBytes, scratchInts),
                    outputValues[i]);
        }

        // 得到fst
        final FST<Long> fst = builder.finish();

        IntsRef key = Util.getByOutput(fst, 12); // 通过输出得到输入
        System.out.println(Util.toBytesRef(key, scratchBytes).utf8ToString()); // dogs
    }

    public static void testInputChar() {
        String inputValue = "cat"; // 词
        BytesRef scratchBytes = new BytesRef();
        scratchBytes.copyChars(inputValue);
        IntsRef scratchInts = new IntsRef();
        Util.toIntsRef(scratchBytes, scratchInts);
        System.out.println(scratchInts);
    }
}

