package com.patterncat.lucene.bseg;

import java.io.File;

/**
 * 中文分词器
 */
public interface ChineseSpliter {
    /**
     * 对给定的文本进行中文分词
     *
     * @param text
     *            给定的文本
     * @return 分词完毕的词数组
     */
    public String[] split(String text);

    public void split(File srcFile,File destFile); //内部调用Split(String)
}
