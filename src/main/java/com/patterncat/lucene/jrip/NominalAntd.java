package com.patterncat.lucene.jrip;

/**
 * The antecedent with nominal attribute
 */
public class NominalAntd
        extends Antd {

    /** for serialization */
    static final long serialVersionUID = -9102297038837585135L;

    /* The parameters of infoGain calculated for each attribute value
     * in the growing data */
    private double[] accurate;
    private double[] coverage;

    /**
     * Constructor
     */
    public NominalAntd(Attribute a){
        super(a);
        int bag = att.numValues();
        accurate = new double[bag];
        coverage = new double[bag];
    }

    /**
     * Implements Copyable
     *
     * @return a copy of this object
     */
    public Object copy(){
        Antd antec = new NominalAntd(getAttr());
        antec.value = this.value;
        return antec;
    }

    /**
     * Implements the splitData function.
     * This procedure is to split the data into bags according
     * to the nominal attribute value
     * The infoGain for each bag is also calculated.
     *
     * @param data the data to be split
     * @param defAcRt the default accuracy rate for data
     * @param cl the class label to be predicted
     * @return the array of data after split
     * @throws Exception
     */
    public Instances[] splitData(Instances data, double defAcRt,
                                 double cl) throws Exception{
        int bag = att.numValues();
        Instances[] splitData = new Instances[bag];

        for(int x=0; x<bag; x++){
            splitData[x] = new Instances(data, data.numInstances());
            accurate[x] = 0;
            coverage[x] = 0;
        }

        for(int x=0; x<data.numInstances(); x++){
            Instance inst=data.instance(x);
            if(!inst.isMissing(att)){
                int v = (int)inst.value(att);
                splitData[v].add(inst);
                coverage[v] += inst.weight();
                if((int)inst.classValue() == (int)cl)
                    accurate[v] += inst.weight();
            }
        }

        for(int x=0; x<bag; x++){
            double t = coverage[x]+1.0;
            double p = accurate[x] + 1.0;
            double infoGain =
                    //Utils.eq(defAcRt, 1.0) ?
                    //accurate[x]/(double)numConds :
                    accurate[x]*(Utils.log2(p/t)-Utils.log2(defAcRt));

            if(infoGain > maxInfoGain){
                maxInfoGain = infoGain;
                cover = coverage[x];
                accu = accurate[x];
                accuRate = p/t;
                value = (double)x;
            }
        }

        return splitData;
    }

    /**
     * Whether the instance is covered by this antecedent
     *
     * @param inst the instance in question
     * @return the boolean value indicating whether the instance is
     *         covered by this antecedent
     */
    public boolean covers(Instance inst){
        boolean isCover=false;
        if(!inst.isMissing(att)){
            if((int)inst.value(att) == (int)value)
                isCover=true;
        }
        return isCover;
    }

    /**
     * Prints this antecedent
     *
     * @return a textual description of this antecedent
     */
    public String toString() {
        return (att.name() + " = " +att.value((int)value));
    }
}
