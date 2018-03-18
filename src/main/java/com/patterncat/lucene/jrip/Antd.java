package com.patterncat.lucene.jrip;

import java.io.Serializable;


/**
 * The single antecedent in the rule, which is composed of an attribute and
 * the corresponding value.  There are two inherited classes, namely NumericAntd
 * and NominalAntd in which the attributes are numeric and nominal respectively.
 */
public abstract class Antd implements Serializable{

    /** for serialization */
    private static final long serialVersionUID = -8929754772994154334L;

    /** The attribute of the antecedent */
    protected Attribute att;

    /** The attribute value of the antecedent.
     For numeric attribute, value is either 0(1st bag) or 1(2nd bag) */
    protected double value;

    /** The maximum infoGain achieved by this antecedent test
     * in the growing data */
    protected double maxInfoGain;

    /** The accurate rate of this antecedent test on the growing data */
    protected double accuRate;

    /** The coverage of this antecedent in the growing data */
    protected double cover;

    /** The accurate data for this antecedent in the growing data */
    protected double accu;

    /**
     * Constructor
     */
    public Antd(Attribute a){
        att=a;
        value=Double.NaN;
        maxInfoGain = 0;
        accuRate = Double.NaN;
        cover = Double.NaN;
        accu = Double.NaN;
    }

    /* The abstract members for inheritance */
    public abstract Instances[] splitData(Instances data, double defAcRt,
                                          double cla) throws Exception;
    public abstract boolean covers(Instance inst);
    public abstract String toString();

    /**
     * Implements Copyable
     *
     * @return a copy of this object
     */
    public abstract Object copy();

    /* Get functions of this antecedent */
    public Attribute getAttr(){ return att; }
    public double getAttrValue(){ return value; }
    public double getMaxInfoGain(){ return maxInfoGain; }
    public double getAccuRate(){ return accuRate; }
    public double getAccu(){ return accu; }
    public double getCover(){ return cover; }

}
