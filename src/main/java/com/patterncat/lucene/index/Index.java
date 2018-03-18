package com.patterncat.lucene.index;

import java.io.*;

/** 一个索引应该实现的功能模版 */
public interface Index {
    /** 使用数据库统计类构建索引 */
    public void build(DatabaseConsumer s);

    /** 从文件加载倒排索引 */
    public void read(String filename) throws IOException;

    /** 把内存中建好的倒排索引存入文件 */
    public void flush(String filename) throws IOException;
}
