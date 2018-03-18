package com.patterncat.lucene.simHash;

public class TestSimHash {

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(MatterType.valueOf("Open"));
        //Enum.valueOf(MatterType, "");
        long simHashOpen = getSimHash(MatterType.Open);
        System.out.println(Long.toBinaryString(simHashOpen));

        long simHashLimit = getSimHash(MatterType.Limit);
        System.out.println(Long.toBinaryString(simHashLimit));

        long simHashCompletion = getSimHash(MatterType.Completion);
        System.out.println(Long.toBinaryString(simHashCompletion));
    }

    public static long getSimHash(MatterType matter)
    {
        int b=1;
        int x=2;
        while(x<MatterType.values().length)
        {
            b++;
            x = x<<1;
        }
        //System.out.println("b:"+b);

        long simHash = matter.ordinal();
        int end = 64/b;
        for(int i=0;i<end;++i)
        {
            simHash = simHash << b;//枚举值按枚举类型总个数向左移位
            simHash += matter.ordinal();
        }
        return simHash;
    }
}

