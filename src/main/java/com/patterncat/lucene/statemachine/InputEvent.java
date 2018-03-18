package com.patterncat.lucene.statemachine;

public class InputEvent {
    public static InputEvent codeStart = new InputEvent("(");//代码开始
    public static InputEvent codeOne=new InputEvent("codeOne");//一位数字
    public static InputEvent code = new InputEvent("code");//代码3位
    public static InputEvent codeEnd = new InputEvent(")");//代码结束
    public static InputEvent phoneNumber = new InputEvent("number4");//号码4位
    public static InputEvent phoneNum7=new InputEvent("number7");//号码7位
    public static InputEvent phoneNum6=new InputEvent("number6");//号码6位
    public static InputEvent phoneNum5=new InputEvent("number5");//号码6位
    public static InputEvent phoneNum2=new InputEvent("number2");//2位
    public static InputEvent splitEventD=new InputEvent(".");//点分隔符
    public static InputEvent splitEventJ=new InputEvent("+"); //+符号
    public static InputEvent splitEventx=new InputEvent("/"); ///符号

    public static InputEvent yearNum = new InputEvent("Y");
    public static InputEvent digital2 = new InputEvent("D2");
    public static InputEvent digital1 = new InputEvent("D1");
    public static InputEvent yearUnitEvent = new InputEvent("年");
    public static InputEvent monthUnitEvent = new InputEvent("月");
    public static InputEvent dayUnitEvent = new InputEvent("日");
    public static InputEvent splitEvent = new InputEvent("-");

    public static InputEvent timeUnitEvent = new InputEvent("时");
    public static InputEvent minuteUnitEvent = new InputEvent("分");
    public static InputEvent secondUnitEvent = new InputEvent("秒");
    public static InputEvent colonEvent = new InputEvent(":");
    public static InputEvent blankEvent = new InputEvent(" ");

    public String word;

    public InputEvent(String w)
    {
        this.word = w;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof InputEvent)) {
            return false;
        }
        InputEvent other = (InputEvent) obj;

        if(this.word!=null)
        {
            if(!this.word.equals(other.word))
            {
                return false;
            }
            return true;
        }

        return true;
    }

    public String toString()
    {
        return word;
    }
}

