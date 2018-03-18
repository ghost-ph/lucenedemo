package com.patterncat.lucene.statemachine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 电话号码格式
 * 在有限状态机中定义有效的状态转换
 * (111) 222-1111
 * @author lietu
 *
 */
public class FSMTel {
    private State startState; //开始状态
    private State currentState; //状态机的当前状态
    private LinkedList<State> states = new LinkedList<State>();//状态集合
    private LinkedList<Guard> guards = new LinkedList<Guard>();//条件集合
    ArrayList<HashMap<InputEvent,Transition>> state2Transition;

    private static FSMTel ruleFSM = new FSMTel();
    private FSMTel()
    {
        State startState = createState();//创建开始状态
        startState.setLabel("start");
        setStartState(startState);
        reset();//重置当前状态
        addRule();//定义有限状态机
    }

    /**
     * 定义有限状态机
     */
    private void addRule()
    {
        //定义有限状态机开始 如：(202) 522-2230------------------------------------------------------
        //（
        Guard startcodeGuard = createGuard();//创建条件
        startcodeGuard.setEvent(InputEvent.codeStart);

        State codeNumState = createState();//创建开始状态
        codeNumState.setLabel("codeStart");
        createTransition(startState, codeNumState, startcodeGuard);//创建状态之间的转移


        //202创建代码状态
        Guard CodeNumGuard= createGuard();
        CodeNumGuard.setEvent(InputEvent.code);

        State codeState = createState();
        codeState.setLabel("code");
        createTransition(codeNumState, codeState, CodeNumGuard);//状态 的转移
        //)代码结束状态
        Guard codeEndGuard =createGuard();
        codeEndGuard.setEvent(InputEvent.codeEnd);

        State codeEndState=createState();
        codeEndState.setLabel("codeEnd");
        createTransition(codeState, codeEndState, codeEndGuard);

        //空格状态
        Guard blankGuard =createGuard();
        blankGuard.setEvent(InputEvent.blankEvent);

        State blankState=createState();
        blankState.setLabel("blankEvent");
        createTransition(codeEndState, blankState, blankGuard);

        //522创建区号状态
        Guard zipCodeNumGuard=createGuard();
        zipCodeNumGuard.setEvent(InputEvent.code);
        State zipCodeNumState=createState();

        zipCodeNumState.setLabel("zipcode");
        createTransition(blankState,zipCodeNumState, zipCodeNumGuard);


        //-分隔符
        Guard  zipCodeSplitGuard=createGuard();
        zipCodeSplitGuard.setEvent(InputEvent.splitEvent);

        State zipSplitSate =createState();
        zipSplitSate.setLabel("splitEvent");
        createTransition(zipCodeNumState,zipSplitSate, zipCodeSplitGuard);

        //2230电话号码
        Guard phonenumberGuard=createGuard();
        phonenumberGuard.setEvent(InputEvent.phoneNumber);

        State phoneNumSate =createState();
        phoneNumSate.setLabel("phoneNumber");
        phoneNumSate.setFinal(true);//结束状态
        createTransition(zipSplitSate, phoneNumSate, phonenumberGuard);

        // 分支二：
        //94数字2位
        Guard numGuard =createGuard();
        numGuard.setEvent(InputEvent.phoneNum2);
        State numState=createState();
        numState.setLabel("phoneNumber2");
        createTransition(codeNumState, numState, numGuard);

        //分支二的分支 1 。格式(94-1)866854----------------------------------------------------------
        //-分割符
        Guard  zipCodeSplitGuard2=createGuard();
        zipCodeSplitGuard2.setEvent(InputEvent.splitEvent);

        State zipSplitSate2 =createState();
        zipSplitSate2.setLabel("splitEvent");
        createTransition(numState,zipSplitSate2, zipCodeSplitGuard2);

        //1电话号码1位
        Guard phonenumberGuard11=createGuard();
        phonenumberGuard11.setEvent(InputEvent.codeOne);
        State phoneNumSate11 =createState();
        phoneNumSate11.setLabel("phoneNumber");
        createTransition(zipSplitSate2, phoneNumSate11, phonenumberGuard11);

        //)代码结束状态
        Guard codeEndGuard11 =createGuard();
        codeEndGuard11.setEvent(InputEvent.codeEnd);
        State codeEndState11=createState();
        codeEndState11.setLabel("codeEnd");
        createTransition(phoneNumSate11, codeEndState11, codeEndGuard11);

        //866854电话号码6位
        Guard phonenumberGuard6=createGuard();
        phonenumberGuard6.setEvent(InputEvent.phoneNum6);
        State phoneNumSate6 =createState();
        phoneNumSate6.setLabel("phoneNumber6");
        phoneNumSate6.setFinal(true);//结束状态
        createTransition(codeEndState11, phoneNumSate6, phonenumberGuard6);


        //分支二的分支2。 UK（英国）的电话号码格式  (44.171) 830 1007----------------------------------
        //.点
        Guard midSplitGuard2 =createGuard();
        midSplitGuard2.setEvent(InputEvent.splitEventD);

        State midSpliatState2=createState();
        midSpliatState2.setLabel("splitEventD");
        createTransition(numState,midSpliatState2 , midSplitGuard2);

        //171创建代码状态
        Guard CodeNumGuard4= createGuard();
        CodeNumGuard4.setEvent(InputEvent.code);
        State codeState4 = createState();
        codeState4.setLabel("code");
        createTransition(midSpliatState2, codeState4, CodeNumGuard4);//状态 的转移

        //)代码结束状态
        Guard codeEndGuard4 =createGuard();
        codeEndGuard4.setEvent(InputEvent.codeEnd);
        State codeEndState4=createState();
        codeEndState4.setLabel("codeEnd");
        createTransition(codeState4, codeEndState4, codeEndGuard4);

        //空格状态
        Guard blankGuard15 =createGuard();
        blankGuard15.setEvent(InputEvent.blankEvent);

        State blankState15=createState();
        blankState15.setLabel("blankEvent");
        createTransition(codeEndState4, blankState15, blankGuard15);

        //830创建区号状态
        Guard zipCodeNumGuard6=createGuard();
        zipCodeNumGuard6.setEvent(InputEvent.code);
        State zipCodeNumState6=createState();
        zipCodeNumState6.setLabel("code");
        createTransition(blankState15,zipCodeNumState6, zipCodeNumGuard6);

        //空格状态
        Guard blankGuard5 =createGuard();
        blankGuard5.setEvent(InputEvent.blankEvent);

        State blankState5=createState();
        blankState5.setLabel("blankEvent");
        createTransition(zipCodeNumState6, blankState5, blankGuard5);

        //1007电话号码
        Guard phonenumberGuard1=createGuard();
        phonenumberGuard1.setEvent(InputEvent.phoneNumber);

        State phoneNumSate1 =createState();
        phoneNumSate1.setLabel("phoneNumber");
        phoneNumSate1.setFinal(true);//结束状态
        createTransition(blankState5, phoneNumSate1, phonenumberGuard1);

        //定义有限状态机开始 如：212.995.5402--------------------------------------------------------
        //212
        Guard  numStartGuard= createGuard();//创建开始条件
        numStartGuard.setEvent(InputEvent.code);

        State numStartState = createState();//创建开始状态
        codeNumState.setLabel("code");
        createTransition(startState, numStartState,numStartGuard );//创建状态之间的转移

        //.创建分隔符条件
        Guard startSplitGuard=createGuard();
        startSplitGuard.setEvent(InputEvent.splitEventD);

        State startSplitState=createState();
        startSplitState.setLabel("splitEventD");
        createTransition(numStartState, startSplitState, startSplitGuard);

        //995创建中间代码状态
        Guard midNumGuard= createGuard();
        midNumGuard.setEvent(InputEvent.code);
        State midNumState = createState();
        midNumState.setLabel("code");
        createTransition(startSplitState, midNumState, midNumGuard);//状态 的转移

        //.中间分隔符状态
        Guard midSplitGuard =createGuard();
        midSplitGuard.setEvent(InputEvent.splitEventD);
        State midSpliatState=createState();
        midSpliatState.setLabel("splitEventD");
        createTransition(midNumState,midSpliatState , midSplitGuard);

        //5402结束电话号码
        Guard numEndGuard=createGuard();
        numEndGuard.setEvent(InputEvent.phoneNumber);

        State numEndState =createState();
        numEndState.setLabel("phoneNumber");
        numEndState.setFinal(true);//结束状态
        createTransition(midSpliatState, numEndState, numEndGuard);

        //定义有限状态机 如：1111 111 1111-----------------------------------------------------------

        //1111定义开始状态
        Guard phonenumberGuard2=createGuard();
        phonenumberGuard2.setEvent(InputEvent.phoneNumber);

        State phoneNumSate2 =createState();
        phoneNumSate2.setLabel("phoneNumber2");
        createTransition(startState, phoneNumSate2, phonenumberGuard2);

        //空格状态
        Guard blankGuard2 =createGuard();
        blankGuard2.setEvent(InputEvent.blankEvent);

        State blankState2=createState();
        blankState2.setLabel("blankEvent");
        createTransition(phoneNumSate2, blankState2, blankGuard2);

        //111创建区号状态
        Guard zipCodeNumGuard2=createGuard();
        zipCodeNumGuard2.setEvent(InputEvent.code);

        State zipCodeNumState2=createState();
        zipCodeNumState2.setLabel("code");
        createTransition(blankState2,zipCodeNumState2, zipCodeNumGuard2);

        //空格状态
        Guard blankGuard3 =createGuard();
        blankGuard3.setEvent(InputEvent.blankEvent);

        State blankState3=createState();
        blankState3.setLabel("blankEvent");
        createTransition(zipCodeNumState2, blankState3, blankGuard3);

        //1111电话号码
        Guard phonenumberGuard3=createGuard();
        phonenumberGuard3.setEvent(InputEvent.phoneNumber);

        State phoneNumSate3 =createState();
        phoneNumSate3.setLabel("phoneNumber");
        phoneNumSate3.setFinal(true);//结束状态
        createTransition(blankState3, phoneNumSate3, phonenumberGuard3);

        //定义有限状态机开始 如：1-925-225-3000------------------------------------------------------
        //1
        Guard  threeStartGuard= createGuard();//创建开始条件
        threeStartGuard.setEvent(InputEvent.codeOne);

        State threeStartState = createState();//创建开始状态
        threeStartState.setLabel("code1");
        createTransition(startState, threeStartState,threeStartGuard );//创建状态之间的转移

        //-创建分隔符条件
        Guard threeStartSplitGuard=createGuard();
        threeStartSplitGuard.setEvent(InputEvent.splitEvent);

        State threeStartSplitState=createState();
        threeStartSplitState.setLabel("splitEvent");
        createTransition(threeStartState, threeStartSplitState, threeStartSplitGuard);

        //925创建中间代码状态
        Guard threeMidNumGuard= createGuard();
        threeMidNumGuard.setEvent(InputEvent.code);

        State threeMidNumState = createState();
        threeMidNumState.setLabel("code2");
        createTransition(threeStartSplitState, threeMidNumState, threeMidNumGuard);//状态 的转移

        //-中间分隔符状态
        Guard threeMidSplitGuard =createGuard();
        threeMidSplitGuard.setEvent(InputEvent.splitEvent);

        State threeMidSpliatState=createState();
        threeMidSpliatState.setLabel("splitEvent");
        createTransition(threeMidNumState,threeMidSpliatState , threeMidSplitGuard);

        //225号码
        Guard threeNumGuard=createGuard();
        threeNumGuard.setEvent(InputEvent.code);

        State threeNumState =createState();
        threeNumState.setLabel("code22");
        createTransition(threeMidSpliatState,threeNumState,threeNumGuard);

        //-结束分隔符状态
        Guard threeEndSplitGuard =createGuard();
        threeEndSplitGuard.setEvent(InputEvent.splitEvent);

        State threeEndSplitState=createState();
        threeEndSplitState.setLabel("splitEvent");
        createTransition(threeNumState,threeEndSplitState , threeEndSplitGuard);

        //3000结束号码
        Guard threeEndcodeGuard=createGuard();
        threeEndcodeGuard.setEvent(InputEvent.phoneNumber);

        State threeEndCodeState =createState();
        threeEndCodeState.setLabel("phoneNumber");
        threeEndCodeState.setFinal(true);//结束状态
        createTransition(threeEndSplitState, threeEndCodeState, threeEndcodeGuard);

        //定义有限状态机开始 如：++31-20-5200161-------------------------------------------------------
        //+号
        Guard  StartGuard4= createGuard();//创建开始条件
        StartGuard4.setEvent(InputEvent.splitEventJ);

        State StartState4 = createState();//创建开始状态
        StartState4.setLabel("jiahao");
        createTransition(startState, StartState4,StartGuard4 );//创建状态之间的转移

        //+号
        Guard  splitGuard4= createGuard();
        splitGuard4.setEvent(InputEvent.splitEventJ);

        State splitState4 = createState();
        splitState4.setLabel("plus");
        createTransition(StartState4, splitState4,splitGuard4 );//创建状态之间的转移

        //31
        Guard secondGuard4 =createGuard();
        secondGuard4.setEvent(InputEvent.phoneNum2);

        State secondState4=createState();
        secondState4.setLabel("num2");
        createTransition(splitState4,secondState4,secondGuard4);
        //-分隔符
        Guard midSplitGuard4=createGuard();
        midSplitGuard4.setEvent(InputEvent.splitEvent);

        State midSplitState4=createState();
        midSplitState4.setLabel("splitEvent");
        createTransition(secondState4,midSplitState4,midSplitGuard4);
        //20
        Guard codeGuard4 =createGuard();
        codeGuard4.setEvent(InputEvent.phoneNum2);

        State codeNumState4=createState();
        codeNumState4.setLabel("num22");
        createTransition(midSplitState4,codeNumState4,codeGuard4);

        //-分隔符
        Guard endSplitGuard4=createGuard();
        endSplitGuard4.setEvent(InputEvent.splitEvent);

        State endSplitState4=createState();
        endSplitState4.setLabel("splitEvent");
        createTransition(codeNumState4,endSplitState4,endSplitGuard4);
        //5200161
        Guard phoneGuard4=createGuard();
        phoneGuard4.setEvent(InputEvent.phoneNum7);
        //结束状态
        State phoneState4=createState();
        phoneState4.setLabel("num7");
        phoneState4.setFinal(true);//结束状态
        createTransition(endSplitState4,phoneState4,phoneGuard4);

        //创建 有限状态机 如：33 1 34 43 32 26--------------------------------------------------------
        //33
        Guard startGuard =createGuard();
        startGuard.setEvent(InputEvent.phoneNum2);

        State startStateOne=createState();
        startStateOne.setLabel("phone");
        createTransition(startState,startStateOne,startGuard);

        // blank
        Guard blankGuardOne =createGuard();
        blankGuardOne.setEvent(InputEvent.blankEvent);

        State blankStateOne=createState();
        blankStateOne.setLabel("blank");
        createTransition(startStateOne,blankStateOne,blankGuardOne);

        //1
        Guard codeGuardOne=createGuard();
        codeGuardOne.setEvent(InputEvent.codeOne);

        State codeStateOne=createState();
        codeStateOne.setLabel("code1");
        createTransition(blankStateOne,codeStateOne,codeGuardOne);

        //blank
        Guard blankGuardSecond =createGuard();
        blankGuardSecond.setEvent(InputEvent.blankEvent);

        State blankStateSecond=createState();
        blankStateSecond.setLabel("blank");
        createTransition(codeStateOne,blankStateSecond,blankGuardSecond);

        //34
        Guard startGuardSecond =createGuard();
        startGuardSecond.setEvent(InputEvent.phoneNum2);

        State startStateSecond=createState();
        startStateSecond.setLabel("phone");
        createTransition(blankStateSecond,startStateSecond,startGuardSecond);

        //blank
        Guard blankGuardThree =createGuard();
        blankGuardThree.setEvent(InputEvent.blankEvent);

        State blankStateThree=createState();
        blankStateThree.setLabel("blank");
        createTransition(startStateSecond,blankStateThree,blankGuardThree);

        //43
        Guard startGuardThree =createGuard();
        startGuardThree.setEvent(InputEvent.phoneNum2);

        State startStateThree=createState();
        startStateThree.setLabel("phone");
        createTransition(blankStateThree,startStateThree,startGuardThree);

        //blank
        Guard blankGuardFour =createGuard();
        blankGuardFour.setEvent(InputEvent.blankEvent);

        State blankStateFour=createState();
        blankStateFour.setLabel("blank");
        createTransition(startStateThree,blankStateFour,blankGuardFour);

        //32
        Guard startGuardFour =createGuard();
        startGuardFour.setEvent(InputEvent.phoneNum2);

        State startStateFour=createState();
        startStateFour.setLabel("phone");
        createTransition(blankStateFour,startStateFour,startGuardFour);

        //blank
        Guard blankGuardFive =createGuard();
        blankGuardFive.setEvent(InputEvent.blankEvent);

        State blankStateFive=createState();
        blankStateFive.setLabel("blank");
        createTransition(startStateFour,blankStateFive,blankGuardFive);

        //26
        Guard startGuardFive =createGuard();
        startGuardFive.setEvent(InputEvent.phoneNum2);

        State startStateFive=createState();
        startStateFive.setLabel("phone");
        startStateFive.setFinal(true);//结束状态
        createTransition(blankStateFive,startStateFive,startGuardFive);


        //创建有限状态机如：+49 69 136-2 98 05---------------------------------------------------
        //+引用
        //49
        Guard GermanyGuardOne =createGuard();
        GermanyGuardOne.setEvent(InputEvent.phoneNum2);

        State GermanyStateOne=createState();
        GermanyStateOne.setLabel("phone");
        createTransition(StartState4,GermanyStateOne,GermanyGuardOne);

        //blank
        Guard GermanyBlankGuard =createGuard();
        GermanyBlankGuard.setEvent(InputEvent.blankEvent);

        State GermanyBlankState=createState();
        GermanyBlankState.setLabel("blank");
        createTransition(GermanyStateOne,GermanyBlankState,GermanyBlankGuard);

        //69
        Guard GermanyGuardSecond =createGuard();
        GermanyGuardSecond.setEvent(InputEvent.phoneNum2);

        State GermanyStateSecond=createState();
        GermanyStateSecond.setLabel("phone");
        createTransition(GermanyBlankState,GermanyStateSecond,GermanyGuardSecond);

        //blank
        Guard GermanyBlankGuardSecond =createGuard();
        GermanyBlankGuardSecond.setEvent(InputEvent.blankEvent);

        State GermanyBlankStateSecond=createState();
        GermanyBlankStateSecond.setLabel("blank");
        createTransition(GermanyStateSecond,GermanyBlankStateSecond,GermanyBlankGuardSecond);

        //136
        Guard GermanyGuardThree =createGuard();
        GermanyGuardThree.setEvent(InputEvent.code);

        State GermanyStateThree=createState();
        GermanyStateThree.setLabel("code");
        createTransition(GermanyBlankStateSecond,GermanyStateThree,GermanyGuardThree);
        //-
        Guard GermanySplitGuardFour=createGuard();
        GermanySplitGuardFour.setEvent(InputEvent.splitEvent);

        State GermanySplitStateFour=createState();
        GermanySplitStateFour.setLabel("split");
        createTransition(GermanyStateThree,GermanySplitStateFour,GermanySplitGuardFour);

        //2
        Guard GermanyNumGuardFour =createGuard();
        GermanyNumGuardFour.setEvent(InputEvent.codeOne);

        State GermanyNumStateFour=createState();
        GermanyNumStateFour.setLabel("code1");
        createTransition(GermanySplitStateFour,GermanyNumStateFour,GermanyNumGuardFour);

        //blank
        Guard GermanyBlankGuardThree =createGuard();
        GermanyBlankGuardThree.setEvent(InputEvent.blankEvent);

        State GermanyBlankStateThree=createState();
        GermanyBlankStateThree.setLabel("blank");
        createTransition(GermanyNumStateFour,GermanyBlankStateThree,GermanyBlankGuardThree);

        //98
        Guard GermanyNumGuardFive =createGuard();
        GermanyNumGuardFive.setEvent(InputEvent.phoneNum2);

        State GermanyNumStateFive=createState();
        GermanyNumStateFive.setLabel("phone");
        createTransition(GermanyBlankStateThree,GermanyNumStateFive,GermanyNumGuardFive);

        //blank
        Guard GermanyBlankGuardFour =createGuard();
        GermanyBlankGuardFour.setEvent(InputEvent.blankEvent);

        State GermanyBlankStateFour=createState();
        GermanyBlankStateFour.setLabel("blank");
        createTransition(GermanyNumStateFive,GermanyBlankStateFour,GermanyBlankGuardFour);

        //05
        Guard GermanyNumGuardEnd =createGuard();
        GermanyNumGuardEnd.setEvent(InputEvent.phoneNum2);

        State GermanyNumStateEnd=createState();
        GermanyNumStateEnd.setLabel("phone");
        GermanyNumStateEnd.setFinal(true);//结束状态
        createTransition(GermanyBlankStateFour,GermanyNumStateEnd,GermanyNumGuardEnd);


        //创建有限状态机 如：95-51-279648----------------------------------------------------------
        //引用 95
        //-
        Guard splitOneGuard=createGuard();
        splitOneGuard.setEvent(InputEvent.splitEvent);

        State splitOneState=createState();
        splitOneState.setLabel("split");
        createTransition(startStateOne, splitOneState, splitOneGuard);

        //51
        Guard NumOneGuard=createGuard();
        NumOneGuard.setEvent(InputEvent.phoneNum2);

        State NumOneState=createState();
        NumOneState.setLabel("num2");
        createTransition(splitOneState, NumOneState, NumOneGuard);
        //-
        Guard splitSecondGuard=createGuard();
        splitSecondGuard.setEvent(InputEvent.splitEvent);

        State splitSecondState=createState();
        splitSecondState.setLabel("split");
        createTransition(NumOneState, splitSecondState, splitSecondGuard);
        //279648
        Guard NumEndGuard=createGuard();
        NumEndGuard.setEvent(InputEvent.phoneNum6);


        State NumEndState=createState();
        NumEndState.setLabel("num2");
        NumEndState.setFinal(true);
        createTransition(splitSecondState, NumEndState, NumEndGuard);


        //创建有限状态机 如：+45 43 48 60 60-----------------------------------------------
        //引用 +45 43
        //48
        Guard denMarkNumThreeGuard =createGuard();
        denMarkNumThreeGuard.setEvent(InputEvent.phoneNum2);

        State denMarkNumThreeState=createState();
        denMarkNumThreeState.setLabel("phone");
        createTransition(GermanyBlankStateSecond,denMarkNumThreeState,denMarkNumThreeGuard);
        //blank
        Guard denMarkBlankThreeGuard =createGuard();
        denMarkBlankThreeGuard.setEvent(InputEvent.blankEvent);

        State denMarkBlankthreeState=createState();
        denMarkBlankthreeState.setLabel("blank");
        createTransition(denMarkNumThreeState,denMarkBlankthreeState,denMarkBlankThreeGuard);
        //60
        Guard denMarkNumFourGuard =createGuard();
        denMarkNumFourGuard.setEvent(InputEvent.phoneNum2);

        State denMarkNumFourState=createState();
        denMarkNumFourState.setLabel("phone");
        createTransition(denMarkBlankthreeState,denMarkNumFourState,denMarkNumFourGuard);
        //blank
        Guard denMarkBlankFiveGuard =createGuard();
        denMarkBlankFiveGuard.setEvent(InputEvent.blankEvent);

        State denMarkBlankFiveState=createState();
        denMarkBlankthreeState.setLabel("blank");
        createTransition(denMarkNumFourState,denMarkBlankFiveState,denMarkBlankFiveGuard);
        //60
        Guard denMarkNumFiveGuard =createGuard();
        denMarkNumFiveGuard.setEvent(InputEvent.phoneNum2);

        State denMarkNumFiveState=createState();
        denMarkNumFiveState.setLabel("phone");
        denMarkBlankFiveState.setFinal(true);//结束状态
        createTransition(denMarkBlankFiveState,denMarkNumFiveState,denMarkNumFiveGuard);

        //创建有限状态机 如：+44(0) 1225 753678 -----------------------------------------------------
        //引用+44GermanyStateOne
        Guard ukStartCodeGuard = createGuard();//创建条件
        ukStartCodeGuard.setEvent(InputEvent.codeStart);

        State ukStartCodeState = createState();//创建开始状态
        ukStartCodeState.setLabel("codeStart");
        createTransition(GermanyStateOne, ukStartCodeState, ukStartCodeGuard);//创建状态之间的转移


        //创建代码状态
        Guard ukNumGuard= createGuard();
        ukNumGuard.setEvent(InputEvent.codeOne);

        State ukNumState = createState();
        ukNumState.setLabel("code1");
        createTransition(ukStartCodeState, ukNumState, ukNumGuard);//状态 的转移
        //代码结束状态
        Guard ukCodEndGuard =createGuard();
        ukCodEndGuard.setEvent(InputEvent.codeEnd);

        State ukCodEndState=createState();
        ukCodEndState.setLabel("codeEnd");
        createTransition(ukNumState, ukCodEndState, ukCodEndGuard);
        //blank
        Guard ukBlankThreeGuard =createGuard();
        ukBlankThreeGuard.setEvent(InputEvent.blankEvent);

        State ukBlankthreeState=createState();
        ukBlankthreeState.setLabel("blank");
        createTransition(ukCodEndState,ukBlankthreeState,ukBlankThreeGuard);
        //1225
        Guard ukNumFourGuard =createGuard();
        ukNumFourGuard.setEvent(InputEvent.phoneNumber);

        State ukNumFourState=createState();
        ukNumFourState.setLabel("num");
        createTransition(ukBlankthreeState,ukNumFourState,ukNumFourGuard);

        //blank
        Guard ukBlankFourGuard =createGuard();
        ukBlankFourGuard.setEvent(InputEvent.blankEvent);

        State ukBlankfourState=createState();
        ukBlankthreeState.setLabel("blank");
        createTransition(ukNumFourState,ukBlankfourState,ukBlankFourGuard);
        //1225
        Guard ukNumEndGuard =createGuard();
        ukNumEndGuard.setEvent(InputEvent.phoneNum6);

        State ukNumEndState=createState();
        ukNumEndState.setLabel("num");
        ukNumEndState.setFinal(true);//结束状态
        createTransition(ukBlankfourState,ukNumEndState,ukNumEndGuard);
        //创建有限状态机如：+441/284 3797---------------------------------------------------------
        //+引用
        //441
        Guard landGuardOne =createGuard();
        landGuardOne.setEvent(InputEvent.code);

        State landStateOne=createState();
        landStateOne.setLabel("code");
        createTransition(StartState4,landStateOne,landGuardOne);
        // /
        Guard landSplitGuard =createGuard();
        landSplitGuard.setEvent(InputEvent.splitEventx);

        State landSplitState=createState();
        landSplitState.setLabel("slash");
        createTransition(landStateOne,landSplitState,landSplitGuard);
        //284
        Guard landNumGuardSecond =createGuard();
        landNumGuardSecond.setEvent(InputEvent.code);

        State landNumStateSecond=createState();
        landNumStateSecond.setLabel("phone");
        createTransition(landSplitState,landNumStateSecond,landNumGuardSecond);
        //blank
        Guard landSplitEndGuard =createGuard();
        landSplitEndGuard.setEvent(InputEvent.blankEvent);

        State landSplitEndState=createState();
        landSplitEndState.setLabel("blank");
        createTransition(landNumStateSecond,landSplitEndState,landSplitEndGuard);
        //3797
        Guard landNumGuardEnd =createGuard();
        landNumGuardEnd.setEvent(InputEvent.phoneNumber);

        State landNumStateEnd=createState();
        landNumStateEnd.setLabel("phone");
        landNumStateEnd.setFinal(true);//结束状态
        createTransition(landSplitEndState,landNumStateEnd,landNumGuardEnd);

        //创建有限状态机 如：01256 468551------------------------------------------------------------
        //01256
        //电话号码5位
        Guard phonenumberGuard5=createGuard();
        phonenumberGuard5.setEvent(InputEvent.phoneNum5);
        State phoneNumSate5 =createState();
        phoneNumSate5.setLabel("phoneNumber5");
        createTransition(startState, phoneNumSate5, phonenumberGuard5);

        //空格状态
        Guard blankGuard55 =createGuard();
        blankGuard55.setEvent(InputEvent.blankEvent);
        State blankState55=createState();
        blankState55.setLabel("blankEvent");
        createTransition(phoneNumSate5, blankState55, blankGuard55);

        //468551结束电话号码 6位
        Guard numEndGuard6=createGuard();
        numEndGuard6.setEvent(InputEvent.phoneNum6);

        State numEndState6 =createState();
        numEndState6.setLabel("phoneNumber6");
        numEndState6.setFinal(true);//结束状态
        createTransition(blankState55, numEndState6, numEndGuard6);

        //定义有限状态机如：8-906-703-0987
        Guard startcodeGuard1 = createGuard();//创建条件
        startcodeGuard1.setEvent(InputEvent.codeOne);

        State codeNumState1 = createState();//创建开始状态
        codeNumState1.setLabel("codeOne");
        createTransition(startState, codeNumState1, startcodeGuard1);//创建状态之间的转移


        //-创建代码状态
        Guard splitNumGuard1= createGuard();
        splitNumGuard1.setEvent(InputEvent.splitEvent);

        State splitcodeState1 = createState();
        splitcodeState1.setLabel("split");
        createTransition(codeNumState1, splitcodeState1, splitNumGuard1);//状态 的转移
//		906创建代码状态
        Guard CodeNumGuard1= createGuard();
        CodeNumGuard1.setEvent(InputEvent.code);

        State codeState1 = createState();
        codeState1.setLabel("code");
        createTransition(splitcodeState1, codeState1, CodeNumGuard1);//状态 的转移

        //-创建代码状态
        Guard splitNumGuard2= createGuard();
        splitNumGuard2.setEvent(InputEvent.splitEvent);

        State splitcodeState2 = createState();
        splitcodeState2.setLabel("split");
        createTransition(codeState1, splitcodeState2, splitNumGuard2);//状态 的转移

//		703创建代码状态
        Guard CodeNumGuard2= createGuard();
        CodeNumGuard2.setEvent(InputEvent.code);

        State codeState2 = createState();
        codeState2.setLabel("code");
        createTransition(splitcodeState2, codeState2, CodeNumGuard2);//状态 的转移

        //-创建代码状态
        Guard splitNumGuard3= createGuard();
        splitNumGuard3.setEvent(InputEvent.splitEvent);

        State splitcodeState3 = createState();
        splitcodeState3.setLabel("split");
        createTransition(codeState2, splitcodeState3, splitNumGuard3);//状态 的转移

//		0987创建代码状态
        Guard CodeNumGuard3= createGuard();
        CodeNumGuard3.setEvent(InputEvent.phoneNumber);

        State codeState3 = createState();
        codeState3.setLabel("phoneNumber");
        numEndState6.setFinal(true);
        createTransition(splitcodeState3, codeState3, CodeNumGuard3);//状态 的转移

//		定义有限状态机如：995-90-00

        Guard splitNumGuard4= createGuard();
        splitNumGuard4.setEvent(InputEvent.splitEvent);

        State splitcodeState4 = createState();
        splitcodeState4.setLabel("splitEvent");
        createTransition(numStartState, splitcodeState4, splitNumGuard4);//状态 的转移

        Guard CodeNumGuard6= createGuard();
        CodeNumGuard6.setEvent(InputEvent.phoneNum2);

        State codeState6 = createState();
        codeState6.setLabel("phoneNum2");
        createTransition(splitcodeState4, codeState6, CodeNumGuard6);


        Guard splitNumGuard5= createGuard();
        splitNumGuard5.setEvent(InputEvent.splitEvent);

        State splitcodeState5 = createState();
        splitcodeState5.setLabel("splitEvent");
        createTransition(codeState6, splitcodeState5, splitNumGuard5);//状态 的转移

        Guard CodeNumGuard7= createGuard();
        CodeNumGuard7.setEvent(InputEvent.phoneNum2);

        State codeState7= createState();
        codeState7.setLabel("phoneNum2");
        codeState7.setFinal(true);
        createTransition(splitcodeState5, codeState7, CodeNumGuard7);

//		定义有限状态机如007-812-1147670
        Guard CodeNumGuard9= createGuard();
        CodeNumGuard9.setEvent(InputEvent.code);

        State codeState9 = createState();
        codeState9.setLabel("code");
        createTransition(splitcodeState4, codeState9, CodeNumGuard9);

        Guard splitNumGuard7= createGuard();
        splitNumGuard7.setEvent(InputEvent.splitEvent);

        State splitcodeState7 = createState();
        splitcodeState7.setLabel("splitEvent");
        createTransition(codeState9, splitcodeState7, splitNumGuard7);//状态 的转移

        Guard CodeNumGuard10= createGuard();
        CodeNumGuard10.setEvent(InputEvent.phoneNum7);

        State codeState10 = createState();
        codeState10.setLabel("phoneNum7");
        codeState10.setFinal(true);
        createTransition(splitcodeState7, codeState10, CodeNumGuard10);


//		有限状态机格式如490-2009 / 2106

        Guard CodeNumGuard12= createGuard();
        CodeNumGuard12.setEvent(InputEvent.phoneNumber);

        State codeState12 = createState();
        codeState12.setLabel("phoneNumber");
        codeState12.setFinal(true);
        createTransition(splitcodeState4, codeState12, CodeNumGuard12);

        Guard blankGuard1=createGuard();
        blankGuard1.setEvent(InputEvent.blankEvent);

        State blankState1 = createState();
        blankState1.setLabel("blank");
        createTransition(codeState12, blankState1, blankGuard1);

        Guard splitNumGuard9= createGuard();
        splitNumGuard9.setEvent(InputEvent.splitEventx);

        State splitcodeState9 = createState();
        splitcodeState9.setLabel("splitx");
        createTransition(blankState1, splitcodeState9, splitNumGuard9);//状态 的转移

        Guard blankGuard11=createGuard();
        blankGuard11.setEvent(InputEvent.blankEvent);

        State blankState11 = createState();
        blankState11.setLabel("blank");
        createTransition(splitcodeState9, blankState11, blankGuard11);

        Guard CodeNumGuard13= createGuard();
        CodeNumGuard13.setEvent(InputEvent.phoneNumber);

        State codeState13 = createState();
        codeState13.setLabel("phoneNumber");
        codeState13.setFinal(true);
        createTransition(blankState11, codeState13, CodeNumGuard13);



//		有限状态机格式如：8（8634）313-988
        Guard blankGuard18=createGuard();
        blankGuard18.setEvent(InputEvent.blankEvent);

        State blankState18 = createState();
        blankState18.setLabel("blank");
        createTransition(threeStartState, blankState18, blankGuard18);

        Guard splitNumGuard22= createGuard();
        splitNumGuard22.setEvent(InputEvent.codeStart);

        State splitcodeState22 = createState();
        splitcodeState22.setLabel("codeStart");
        createTransition(blankState18, splitcodeState22, splitNumGuard22);//状态 的转移

        Guard phonenumberGuard24=createGuard();
        phonenumberGuard24.setEvent(InputEvent.phoneNumber);

        State phoneNumSate24 =createState();
        phoneNumSate24.setLabel("phoneNumber");
        createTransition(splitcodeState22, phoneNumSate24, phonenumberGuard24);

        Guard splitNumGuard23= createGuard();
        splitNumGuard23.setEvent(InputEvent.codeEnd);

        State splitcodeState23 = createState();
        splitcodeState23.setLabel("codeEnd");
        createTransition(phoneNumSate24, splitcodeState23, splitNumGuard23);//状态 的转移

        Guard blankGuard19=createGuard();
        blankGuard19.setEvent(InputEvent.blankEvent);

        State blankState19 = createState();
        blankState19.setLabel("blank");
        createTransition(splitcodeState23, blankState19, blankGuard19);

        Guard phonenumberGuard25=createGuard();
        phonenumberGuard25.setEvent(InputEvent.code);

        State phoneNumSate25 =createState();
        phoneNumSate25.setLabel("code");
        createTransition(blankState19, phoneNumSate25, phonenumberGuard25);

        Guard splitNumGuard24= createGuard();
        splitNumGuard24.setEvent(InputEvent.splitEvent);

        State splitcodeState24 = createState();
        splitcodeState24.setLabel("split");
        createTransition(phoneNumSate25, splitcodeState24, splitNumGuard24);//状态 的转移

        Guard phonenumberGuard26=createGuard();
        phonenumberGuard26.setEvent(InputEvent.code);

        State phoneNumSate26 =createState();
        phoneNumSate26.setLabel("code");
        phoneNumSate26.setFinal(true);
        createTransition(splitcodeState24, phoneNumSate26, phonenumberGuard26);
//		有限状态机如(495) 514-01-64
//		(495)514-引用
        Guard phonenumberGuard14=createGuard();
        phonenumberGuard14.setEvent(InputEvent.phoneNum2);

        State phoneNumSate14 =createState();
        phoneNumSate14.setLabel("phoneNum2");
        createTransition(zipSplitSate, phoneNumSate14, phonenumberGuard14);


        Guard splitNumGuard10= createGuard();
        splitNumGuard10.setEvent(InputEvent.splitEvent);

        State splitcodeState10 = createState();
        splitcodeState10.setLabel("split");
        createTransition(phoneNumSate14, splitcodeState10, splitNumGuard10);//状态 的转移

        Guard phonenumberGuard15=createGuard();
        phonenumberGuard15.setEvent(InputEvent.phoneNum2);

        State phoneNumSate15 =createState();
        phoneNumSate15.setLabel("phoneNum2");
        phoneNumSate15.setFinal(true);
        createTransition(splitcodeState10, phoneNumSate15, phonenumberGuard15);

//		有限状态机格式如+7 (495) 514-01-64 / 788-90-68

        Guard CodeNumGuarda6= createGuard();
        CodeNumGuarda6.setEvent(InputEvent.codeOne);

        State codeStatea6 = createState();
        codeStatea6.setLabel("codeOne");
        createTransition(StartState4, codeStatea6, CodeNumGuarda6);

        Guard blankGuarda2=createGuard();
        blankGuarda2.setEvent(InputEvent.blankEvent);

        State blankStatea2 = createState();
        blankStatea2.setLabel("blank");
        createTransition(codeStatea6, blankStatea2, blankGuarda2);

        Guard splitNumGuarda2= createGuard();
        splitNumGuarda2.setEvent(InputEvent.codeStart);

        State splitcodeStatea2 = createState();
        splitcodeStatea2.setLabel("codeStart");
        createTransition(blankStatea2, splitcodeStatea2, splitNumGuarda2);

        Guard CodeNumGuarda7= createGuard();
        CodeNumGuarda7.setEvent(InputEvent.code);

        State codeStatea7 = createState();
        codeStatea7.setLabel("code");
        createTransition(splitcodeStatea2, codeStatea7, CodeNumGuarda7);

        Guard splitNumGuarda3= createGuard();
        splitNumGuarda3.setEvent(InputEvent.codeEnd);

        State splitcodeStatea3 = createState();
        splitcodeStatea3.setLabel("codeEnd");
        createTransition(codeStatea7, splitcodeStatea3, splitNumGuarda3);

        Guard blankGuarda3=createGuard();
        blankGuarda3.setEvent(InputEvent.blankEvent);

        State blankStatea3 = createState();
        blankStatea3.setLabel("blank");
        createTransition(splitcodeStatea3, blankStatea3, blankGuarda3);

        Guard CodeNumGuarda8= createGuard();
        CodeNumGuarda8.setEvent(InputEvent.code);

        State codeStatea8 = createState();
        codeStatea8.setLabel("code");
        createTransition(blankStatea3, codeStatea8, CodeNumGuarda8);
//
        Guard splitNumGuarda4= createGuard();
        splitNumGuarda4.setEvent(InputEvent.splitEvent);

        State splitcodeStatea4 = createState();
        splitcodeStatea4.setLabel("splitEvent");
        createTransition(codeStatea8, splitcodeStatea4, splitNumGuarda4);
//
        Guard CodeNumGuarda9= createGuard();
        CodeNumGuarda9.setEvent(InputEvent.phoneNum2);

        State codeStatea9 = createState();
        codeStatea9.setLabel("phoneNum2");
        createTransition(splitcodeStatea4, codeStatea9, CodeNumGuarda9);
//
        Guard splitNumGuarda5= createGuard();
        splitNumGuarda5.setEvent(InputEvent.splitEvent);

        State splitcodeStatea5 = createState();
        splitcodeStatea5.setLabel("splitEvent");
        createTransition(codeStatea9, splitcodeStatea5, splitNumGuarda5);

        Guard CodeNumGuardb1= createGuard();
        CodeNumGuardb1.setEvent(InputEvent.phoneNum2);

        State codeStateb1 = createState();
        codeStateb1.setLabel("phoneNum2");
        codeStateb1.setFinal(true);
        createTransition(splitcodeStatea5, codeStateb1, CodeNumGuardb1);
//
        Guard blankGuard14=createGuard();
        blankGuard14.setEvent(InputEvent.blankEvent);

        State blankState14 = createState();
        blankState14.setLabel("blank");
        createTransition(codeStateb1, blankState14, blankGuard14);

        Guard splitNumGuard16= createGuard();
        splitNumGuard16.setEvent(InputEvent.splitEventx);

        State splitcodeState16 = createState();
        splitcodeState16.setLabel("splitEventx");
        createTransition(blankState14, splitcodeState16, splitNumGuard16);

        Guard blankGuard16=createGuard();
        blankGuard16.setEvent(InputEvent.blankEvent);

        State blankState16 = createState();
        blankState16.setLabel("blank");
        createTransition(splitcodeState16, blankState16, blankGuard16);

        Guard CodeNumGuard21= createGuard();
        CodeNumGuard21.setEvent(InputEvent.code);

        State codeState21 = createState();
        codeState21.setLabel("code");
        createTransition(blankState16, codeState21, CodeNumGuard21);

        Guard splitNumGuard17= createGuard();
        splitNumGuard17.setEvent(InputEvent.splitEvent);

        State splitcodeState17 = createState();
        splitcodeState17.setLabel("splitEvent");
        createTransition(codeState21, splitcodeState17, splitNumGuard17);

        Guard CodeNumGuard22= createGuard();
        CodeNumGuard22.setEvent(InputEvent.phoneNum2);

        State codeState22 = createState();
        codeState22.setLabel("phoneNum2");
        createTransition(splitcodeState17, codeState22, CodeNumGuard22);

        Guard splitNumGuard18= createGuard();
        splitNumGuard18.setEvent(InputEvent.splitEvent);

        State splitcodeState18= createState();
        splitcodeState18.setLabel("splitEvent");
        createTransition(codeState22, splitcodeState18, splitNumGuard18);

        Guard CodeNumGuard23= createGuard();
        CodeNumGuard23.setEvent(InputEvent.phoneNum2);

        State codeState23 = createState();
        codeState23.setLabel("phoneNum2");
        codeState23.setFinal(true);
        createTransition(splitcodeState18, codeState23, CodeNumGuard23);

//		有限状态机如(495)  514-01-64
//		(495) 引用

        Guard blankGuard17=createGuard();
        blankGuard17.setEvent(InputEvent.blankEvent);

        State blankState17 = createState();
        blankState17.setLabel("blank");
        createTransition(blankState, blankState17, blankGuard17);

        Guard CodeNumGuard24= createGuard();
        CodeNumGuard24.setEvent(InputEvent.code);

        State codeState24 = createState();
        codeState24.setLabel("code");
        createTransition(blankState17, codeState24, CodeNumGuard24);

        Guard splitNumGuard19= createGuard();
        splitNumGuard19.setEvent(InputEvent.splitEvent);

        State splitcodeState19= createState();
        splitcodeState19.setLabel("splitEvent");
        createTransition(codeState24, splitcodeState19, splitNumGuard19);

        Guard phonenumberGuard21=createGuard();
        phonenumberGuard21.setEvent(InputEvent.phoneNum2);

        State phoneNumSate21 =createState();
        phoneNumSate21.setLabel("phoneNum2");
        createTransition(splitcodeState19, phoneNumSate21, phonenumberGuard21);


        Guard splitNumGuard21= createGuard();
        splitNumGuard21.setEvent(InputEvent.splitEvent);

        State splitcodeState21 = createState();
        splitcodeState21.setLabel("split");
        createTransition(phoneNumSate21, splitcodeState21, splitNumGuard21);//状态 的转移

        Guard phonenumberGuard22=createGuard();
        phonenumberGuard22.setEvent(InputEvent.phoneNum2);

        State phoneNumSate22 =createState();
        phoneNumSate22.setLabel("phoneNum2");
        phoneNumSate22.setFinal(true);
        createTransition(splitcodeState21, phoneNumSate22, phonenumberGuard22);


        Guard phonenumberGuard23=createGuard();
        phonenumberGuard23.setEvent(InputEvent.phoneNum2);

        State phoneNumSate23 =createState();
        phoneNumSate23.setLabel("phoneNum2");
        createTransition(zipSplitSate, phoneNumSate23, phonenumberGuard23);

        Guard splitNumGuard25= createGuard();
        splitNumGuard25.setEvent(InputEvent.splitEvent);

        State splitcodeState25 = createState();
        splitcodeState25.setLabel("split");
        createTransition(phoneNumSate23, splitcodeState25, splitNumGuard25);//状态 的转移

        Guard phonenumberGuard27=createGuard();
        phonenumberGuard27.setEvent(InputEvent.phoneNum2);

        State phoneNumSate27 =createState();
        phoneNumSate27.setLabel("phoneNum2");
        phoneNumSate27.setFinal(true);
        createTransition(splitcodeState25, phoneNumSate27, phonenumberGuard27);

        Guard splitNumGuard26= createGuard();
        splitNumGuard26.setEvent(InputEvent.splitEventx);

        State splitcodeState26 = createState();
        splitcodeState26.setLabel("splitEventx");
        createTransition(phoneNumSate27, splitcodeState26, splitNumGuard26);//状态 的转移

        Guard phonenumberGuard28=createGuard();
        phonenumberGuard28.setEvent(InputEvent.phoneNum2);

        State phoneNumSate28 =createState();
        phoneNumSate28.setLabel("phoneNum2");
        phoneNumSate28.setFinal(true);
        createTransition(splitcodeState26, phoneNumSate28, phonenumberGuard28);

//		有限状态机格式为8-495-1632200
        Guard phonenumberGuard29=createGuard();
        phonenumberGuard29.setEvent(InputEvent.phoneNum7);

        State phoneNumSate29 =createState();
        phoneNumSate29.setLabel("phoneNum7");
        phoneNumSate29.setFinal(true);
        createTransition(threeMidSpliatState, phoneNumSate29, phonenumberGuard29);

//		有限状态机格式如（8452）29-54-28, 58-11-28

        Guard phonenumberGuard30=createGuard();
        phonenumberGuard30.setEvent(InputEvent.phoneNumber);

        State phoneNumSate30 =createState();
        phoneNumSate30.setLabel("phoneNumber");
        createTransition(codeNumState, phoneNumSate30, phonenumberGuard30);

        Guard splitNumGuard27= createGuard();
        splitNumGuard27.setEvent(InputEvent.codeEnd);

        State splitcodeState27 = createState();
        splitcodeState27.setLabel("codeEnd");
        createTransition(phoneNumSate30, splitcodeState27, splitNumGuard27);//状态 的转移

        Guard splitNumGuard28= createGuard();
        splitNumGuard28.setEvent(InputEvent.blankEvent);

        State splitcodeState28 = createState();
        splitcodeState28.setLabel("blankEvent");
        createTransition(splitcodeState27, splitcodeState28, splitNumGuard28);//状态 的转移

        Guard phonenumberGuard31=createGuard();
        phonenumberGuard31.setEvent(InputEvent.phoneNum2);

        State phoneNumSate31 =createState();
        phoneNumSate31.setLabel("phoneNum2");
        createTransition(splitcodeState28, phoneNumSate31, phonenumberGuard31);

        Guard splitNumGuard29= createGuard();
        splitNumGuard29.setEvent(InputEvent.splitEvent);

        State splitcodeState29 = createState();
        splitcodeState29.setLabel("splitEvent");
        createTransition(phoneNumSate31, splitcodeState29, splitNumGuard29);//状态 的转移

        Guard phonenumberGuard32=createGuard();
        phonenumberGuard32.setEvent(InputEvent.phoneNum2);

        State phoneNumSate32 =createState();
        phoneNumSate32.setLabel("phoneNum2");
        createTransition(splitcodeState29, phoneNumSate32, phonenumberGuard32);

        Guard splitNumGuard30= createGuard();
        splitNumGuard30.setEvent(InputEvent.splitEvent);

        State splitcodeState30 = createState();
        splitcodeState30.setLabel("splitEvent");
        createTransition(phoneNumSate32, splitcodeState30, splitNumGuard30);//状态 的转移

        Guard phonenumberGuard33=createGuard();
        phonenumberGuard33.setEvent(InputEvent.phoneNum2);

        State phoneNumSate33 =createState();
        phoneNumSate33.setLabel("phoneNum2");
        phoneNumSate33.setFinal(true);
        createTransition(splitcodeState30, phoneNumSate33, phonenumberGuard33);

        Guard NumEndGuard33=createGuard();
        NumEndGuard33.setEvent(InputEvent.phoneNum2);


        State NumEndState33=createState();
        NumEndState33.setLabel("phoneNum2");
        NumEndState33.setFinal(true);
        createTransition(splitSecondState, NumEndState33, NumEndGuard33);
    }

    /**
     *
     * @return the singleton of basic dictionary
     */
    public static FSMTel getInstance()
    {
        ruleFSM.reset();
        return ruleFSM;
    }

    public void setStartState(State s) {
        this.startState = s;
    }

    /**
     * a transition strategy
     * implements first match strategy (sufficient for dfa)
     * @param e
     * @return
     */
    protected Transition selectTransition(InputEvent e) {
        for (Transition t : currentState.getTransitions()) {
            Guard g = t.getGuard();
            if (g.checkEvent(e)) {
                return t;
            }
        }
        return null;
    }

    public MatchType consumeEvent(InputEvent e) {
        //System.out.println("consumeEvent");
        Transition t = selectTransition(e);
        if (t == null) {
            //System.err.println("no matching transition found");
            reset();
            return MatchType.MisMatch;
        }
        currentState = t.getNextState();

        //ArrayList<TokenSpan> entryAction = currentState.getEntryAction();
        if(currentState.isFinal())
        {
            //reset();
            return MatchType.Match;
        }
        return MatchType.MatchPrefix;
    }

    public void reset() {
        currentState = startState;
    }

    /**
     * factory method
     *
     */
    public Guard createGuard() {
        Guard g = new Guard();
        guards.add(g);
        return g;
    }

    public void removeGuard(Guard g) {
        guards.remove(g);
    }

    /**
     * factory method
     */
    public void createTransition(State from, State to, Guard g) {
        Transition t = new Transition();
        t.setState(from);
        t.setNextState(to);
        from.getTransitions().add(t);
        //to.getIncomingTransitions().add(t);
        t.setGuard(g);
        //transitions.add(t);
    }

    /** creates a new Default state */
    public State createState() {
        State s = new State();
        states.add(s);
        return s;
    }

    public Collection<State> getStates() {
        return states;
    }

    public Collection<Guard> getGuards() {
        return guards;
    }

    public State getCurrentState() {
        return this.currentState;
    }
}
