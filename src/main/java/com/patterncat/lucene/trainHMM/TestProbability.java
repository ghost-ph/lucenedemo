package com.patterncat.lucene.trainHMM;

public class TestProbability {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        testProbs();
    }

    public static void testProbs(){
        System.out.print("gen\t");
        for(PartOfSpeech s : PartOfSpeech.values()){
            System.out.print(s.toString()+s.ordinal()+"\t");
        }
        System.out.println();
        int i=0;
        for(double[] gs : Probability.PROBS){
            System.out.print(PartOfSpeech.values()[i++]+"\t");
            for(double g : gs){
                System.out.print(g+"\t");
            }
            System.out.println();
        }

        System.out.println("...........................");
        for(PartOfSpeech s : PartOfSpeech.values()){
            System.out.print(s.toString()+s.ordinal()+"\t");
        }
        System.out.println();
        System.out.println("...........................");
        for(int c : Probability.COUNTS){
            System.out.print(c+"\t");
        }
    }
}

