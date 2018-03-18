package com.patterncat.lucene.poiExtract;


public enum DocType {
    Start,    //开始状态
    End,    //结束状态
    PrefixAuthor,    //作者前缀
    SuffixAuthor,    //作者后缀
    PrefixTitle,    //标题前缀
    SuffixTitle,    //标题后缀
    Title,          //标题
    PrefixText,    //正文前缀
    SuffixText,    //正文结尾
    Text,          //正文
    PrefixInfoSource,    //消息来源前缀
    InfoSource,    //消息来源
    PrefixPublishDate,    //发布时间前缀
    PublishDate,    //发布时间
    Time,    // 表示时分秒的时间
    Date,     // 表示年月日的时间
    Author,     //作者
    Unknow,     //未知
    Other, Link,    // 其他
}
