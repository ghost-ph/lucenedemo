package com.patterncat.lucene.jrip;

/**
 * The antecedent with numeric attribute
 */
public class
        NumericAntd extends Antd {

    /** for serialization */
    static final long serialVersionUID = 5699457269983735442L;

    /** The split point for this numeric antecedent */
    private double splitPoint;

    /**
     * Constructor
     */
    public NumericAntd(Attribute a){
        super(a);
        splitPoint = Double.NaN;
    }

    /**
     * Get split point of this numeric antecedent
     *
     * @return the split point of this numeric antecedent
     */
    public double getSplitPoint(){
        return splitPoint;
    }

    /**
     * Implements Copyable
     *
     * @return a copy of this object
     */
    public Object copy(){
        NumericAntd na = new NumericAntd(getAttr());
        na.value = this.value;
        na.splitPoint = this.splitPoint;
        return na;
    }

    /**
     * Implements the splitData function.
     * This procedure is to split the data into two bags according
     * to the information gain of the numeric attribute value
     * The maximum infoGain is also calculated.
     *
     * @param insts the data to be split
     * @param defAcRt the default accuracy rate for data
     * @param cl the class label to be predicted
     * @return the array of data after split
     * @throws Exception
     */
    public Instances[] splitData(Instances insts, double defAcRt,
                                 double cl) throws Exception{
        Instances data = insts;
        int total=data.numInstances();// Total number of instances without
        // missing value for att

        int split=1;                  // Current split position
        int prev=0;                   // Previous split position
        int finalSplit=split;         // Final split position
        maxInfoGain = 0;
        value = 0;

        double fstCover=0, sndCover=0, fstAccu=0, sndAccu=0;

        data.sort(att);
        // Find the las instance without missing value
        for(int x=0; x<data.numInstances(); x++){
            Instance inst = data.instance(x);
            if(inst.isMissing(att)){
                total = x;
                break;
            }

            sndCover += inst.weight();
            if(Utils.eq(inst.classValue(), cl))
                sndAccu += inst.weight();
        }

        if(total == 0) return null; // Data all missing for the attribute
        splitPoint = data.instance(total-1).value(att);

        for(; split <= total; split++){
            if((split == total) ||
                    (data.instance(split).value(att) > // Can't split within
                            data.instance(prev).value(att))){ // same value

                for(int y=prev; y<split; y++){
                    Instance inst = data.instance(y);
                    fstCover += inst.weight();
                    if(Utils.eq(data.instance(y).classValue(), cl)){
                        fstAccu += inst.weight();  // First bag positive# ++
                    }
                }

                double fstAccuRate = (fstAccu+1.0)/(fstCover+1.0),
                        sndAccuRate = (sndAccu+1.0)/(sndCover+1.0);

	  /* Which bag has higher information gain? */
                boolean isFirst;
                double fstInfoGain, sndInfoGain;
                double accRate, infoGain, coverage, accurate;

                fstInfoGain =
                        //Utils.eq(defAcRt, 1.0) ?
                        //fstAccu/(double)numConds :
                        fstAccu*(Utils.log2(fstAccuRate)-Utils.log2(defAcRt));

                sndInfoGain =
                        //Utils.eq(defAcRt, 1.0) ?
                        //sndAccu/(double)numConds :
                        sndAccu*(Utils.log2(sndAccuRate)-Utils.log2(defAcRt));

                if(fstInfoGain > sndInfoGain){
                    isFirst = true;
                    infoGain = fstInfoGain;
                    accRate = fstAccuRate;
                    accurate = fstAccu;
                    coverage = fstCover;
                }
                else{
                    isFirst = false;
                    infoGain = sndInfoGain;
                    accRate = sndAccuRate;
                    accurate = sndAccu;
                    coverage = sndCover;
                }

	  /* Check whether so far the max infoGain */
                if(infoGain > maxInfoGain){
                    splitPoint = data.instance(prev).value(att);
                    value = (isFirst) ? 0 : 1;
                    accuRate = accRate;
                    accu = accurate;
                    cover = coverage;
                    maxInfoGain = infoGain;
                    finalSplit = (isFirst) ? split : prev;
                }

                for(int y=prev; y<split; y++){
                    Instance inst = data.instance(y);
                    sndCover -= inst.weight();
                    if(Utils.eq(data.instance(y).classValue(), cl)){
                        sndAccu -= inst.weight();  // Second bag positive# --
                    }
                }
                prev=split;
            }
        }

    /* Split the data */
        Instances[] splitData = new Instances[2];
        splitData[0] = new Instances(data, 0, finalSplit);
        splitData[1] = new Instances(data, finalSplit, total-finalSplit);

        return splitData;
    }

    /**
     * Whether the instance is covered by this antecedent
     *
     * @param inst the instance in question
     * @return the boolean value indicating whether the instance is covered
     *         by this antecedent
     */
    public boolean covers(Instance inst){
        boolean isCover=true;
        if(!inst.isMissing(att)){
            if((int)value == 0){ // First bag
                if(inst.value(att) > splitPoint)
                    isCover=false;
            }
            else if(inst.value(att) < splitPoint) // Second bag
                isCover=false;
        }
        else
            isCover = false;

        return isCover;
    }

    /**
     * Prints this antecedent
     *
     * @return a textual description of this antecedent
     */
    public String toString() {
        String symbol = ((int)value == 0) ? " <= " : " >= ";
        return (att.name() + symbol + Utils.doubleToString(splitPoint, 6));
    }
}

