package com.patterncat.lucene.extract;

import java.io.File;

//处理文档
public interface IFilter {
    String getTitle(File file);//返回标题
    String getBody(File file);//返回内容
    IDocument getDocument(File file);//返回全部索引信息
}
