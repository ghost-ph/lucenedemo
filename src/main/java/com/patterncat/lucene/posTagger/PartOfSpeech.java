package com.patterncat.lucene.posTagger;

import java.util.HashMap;

/**
 * 词的标注类型
 * @author luogang
 *
 */
public class PartOfSpeech {

    public final static byte start = 0;//开始
    public final static byte end = 1;//结束
    public final static byte a = 2;//形容词
    public final static byte ad = 3;//副形词
    public final static byte ag = 4;//形语素
    public final static byte an = 5;//名形词
    public final static byte b = 6;//区别词
    public final static byte c = 7;//连词
    public final static byte d = 8;//副词
    public final static byte dg = 9;//副语素
    public final static byte e = 10;//叹词
    public final static byte f = 11;//方位词
    public final static byte g = 12;//语素
    public final static byte h = 13;//前接成分
    public final static byte i = 14;//成语
    public final static byte j = 15;//简称略语
    public final static byte k = 16;//后接成分
    public final static byte l = 17;//习用语
    public final static byte m = 18;//数词
    public final static byte n = 19;//名词
    public final static byte ng = 20;//名语素
    public final static byte nr = 21;//人名
    public final static byte ns = 22;//地名
    public final static byte nt = 23;//机构团体
    public final static byte nx = 24;//字母专名
    public final static byte nz = 25;//其他专名
    public final static byte o = 26;//拟声词
    public final static byte p = 27;//介词
    public final static byte q = 28;//量词
    public final static byte r = 29;//代词
    public final static byte s = 30;//处所词
    public final static byte t = 31;//时间词
    public final static byte tg = 32;//时语素
    public final static byte u = 33;//助词
    public final static byte ud = 34;//结构助词
    public final static byte ug = 35;//时态助词
    public final static byte uj = 36;//结构助词的
    public final static byte ul = 37;//时态助词了
    public final static byte uv = 38;//结构助词地
    public final static byte uz = 39;//时态助词着
    public final static byte v = 40;//动词
    public final static byte vd = 41;//副动词
    public final static byte vg = 42;//动语素
    public final static byte vn = 43;//名动词
    public final static byte w = 44;//标点符号
    public final static byte x = 45;//非语素字
    public final static byte y = 46;//语气词
    public final static byte z = 47;//状态词
    public final static byte unknow = 48; //未知

    public static String[] names = {"start","end","a","ad","ag","an","b","c","d","dg","e","f","g","h","i","j","k",
            "l","m","n","ng","nr","ns","nt","nx","nz","o","p","q","r","s","t","tg","u","ud",
            "ug","uj","ul","uv","uz","v","vd","vg","vn","w","x","y","z","unknow"};

    public static HashMap<String,Byte> values;

    static{
        values = new HashMap<String,Byte>();
        values.put("a",a);
        values.put("start",start);//开始
        values.put("end",end);//结束
        values.put("a",a);//形容词
        values.put("ad",ad);//副形词
        values.put("ag",ag);//形语素
        values.put("an",an);//名形词
        values.put("b",b);//区别词
        values.put("c",c);//连词
        values.put("d",d);//副词
        values.put("dg",dg);//副语素
        values.put("e",e);//叹词
        values.put("f",f);//方位词
        values.put("g",g);//语素
        values.put("h",h);//前接成分
        values.put("i",i);//成语
        values.put("j",j);//简称略语
        values.put("k",k);//后接成分
        values.put("l",l);//习用语
        values.put("m",m);//数词
        values.put("n",n);//名词
        values.put("ng",ng);//名语素
        values.put("nr",nr);//人名
        values.put("ns",ns);//地名
        values.put("nt",nt);//机构团体
        values.put("nx",nx);//字母专名
        values.put("nz",nz);//其他专名
        values.put("o",o);//拟声词
        values.put("p",p);//介词
        values.put("q",q);//量词
        values.put("r",r);//代词
        values.put("s",s);//处所词
        values.put("t",t);//时间词
        values.put("tg",tg);//时语素
        values.put("u",u);//助词
        values.put("ud",ud);//结构助词
        values.put("ug",ug);//时态助词
        values.put("uj",uj);//结构助词的
        values.put("ul",ul);//时态助词了
        values.put("uv",uv);//结构助词地
        values.put("uz",uz);//时态助词着
        values.put("v",v);//动词
        values.put("vd",vd);//副动词
        values.put("vg",vg);//动语素
        values.put("vn",vn);//名动词
        values.put("w",w);//标点符号
        values.put("x",x);//非语素字
        values.put("y",y);//语气词
        values.put("z",z);//状态词
        values.put("unknow",unknow); //未知
    }
    String getName(byte b){
        return names[b];
    }
}
