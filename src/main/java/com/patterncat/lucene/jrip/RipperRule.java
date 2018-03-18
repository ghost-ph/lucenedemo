package com.patterncat.lucene.jrip;

import java.util.Enumeration;

/**
 * This class implements a single rule that predicts specified class.
 *
 * A rule consists of antecedents "AND"ed together and the consequent
 * (class value) for the classification.
 * In this class, the Information Gain (p*[log(p/t) - log(P/T)]) is used to
 * select an antecedent and Reduced Error Prunning (REP) with the metric
 * of accuracy rate p/(p+n) or (TP+TN)/(P+N) is used to prune the rule.
 */
public class RipperRule
        extends Rule {
    private double m_MinNo = 2.0;
    private boolean m_Debug = true;

    /** for serialization */
    static final long serialVersionUID = -2410020717305262952L;

    /** The internal representation of the class label to be predicted */
    private double m_Consequent = -1;

    /** The vector of antecedents of this rule*/
    protected FastVector m_Antds = null;

    /** Constructor */
    public RipperRule(){
        m_Antds = new FastVector();
    }

    /**
     * Sets the internal representation of the class label to be predicted
     *
     * @param cl the internal representation of the class label to be predicted
     */
    public void setConsequent(double cl) {
        m_Consequent = cl;
    }

    /**
     * Gets the internal representation of the class label to be predicted
     *
     * @return the internal representation of the class label to be predicted
     */
    public double getConsequent() {
        return m_Consequent;
    }

    /**
     * Whether the instance covered by this rule
     *
     * @param datum the instance in question
     * @return the boolean value indicating whether the instance
     *         is covered by this rule
     */
    public boolean covers(Instance datum){
        boolean isCover=true;

        for(int i=0; i<m_Antds.size(); i++){
            Antd antd = (Antd)m_Antds.elementAt(i);
            if(!antd.covers(datum)){
                isCover = false;
                break;
            }
        }

        return isCover;
    }

    /**
     * Whether this rule has antecedents, i.e. whether it is a default rule
     *
     * @return the boolean value indicating whether the rule has antecedents
     */
    public boolean hasAntds(){
        if (m_Antds == null)
            return false;
        else
            return (m_Antds.size() > 0);
    }

    /**
     * the number of antecedents of the rule
     *
     * @return the size of this rule
     */
    public double size(){ return (double)m_Antds.size(); }


    /**
     * Private function to compute default number of accurate instances
     * in the specified data for the consequent of the rule
     *
     * @param data the data in question
     * @return the default accuracy number
     * @throws Exception
     */
    private double computeDefAccu(Instances data) throws Exception{
        double defAccu=0;
        for(int i=0; i<data.numInstances(); i++){
            Instance inst = data.instance(i);
            if((int)inst.classValue() == (int)m_Consequent)
                defAccu += inst.weight();
        }
        return defAccu;
    }


    /**
     * Build one rule using the growing data
     *
     * @param data the growing data used to build the rule
     * @throws Exception if the consequent is not set yet
     */
    public void grow(Instances data) throws Exception {
        if(m_Consequent == -1)
            throw new Exception(" Consequent not set yet.");

        Instances growData = data;
        double sumOfWeights = growData.sumOfWeights();
        //System.err.println("\nsumOfWeights "+sumOfWeights);
        if(!Utils.gr(sumOfWeights, 0.0))
            return;
        //System.err.println("\nsumOfWeights2 "+sumOfWeights);
    /* Compute the default accurate rate of the growing data */
        double defAccu = computeDefAccu(growData);
        double defAcRt = (defAccu+1.0)/(sumOfWeights+1.0);

    /* Keep the record of which attributes have already been used*/
        boolean[] used=new boolean [growData.numAttributes()];
        for (int k=0; k<used.length; k++)
            used[k]=false;
        int numUnused=used.length;

        // If there are already antecedents existing
        for(int j=0; j < m_Antds.size(); j++){
            Antd antdj = (Antd)m_Antds.elementAt(j);
            if(!antdj.getAttr().isNumeric()){
                used[antdj.getAttr().index()]=true;
                numUnused--;
            }
        }

        //System.err.println("\nnumUnused "+numUnused);
        //System.err.println("\ndefAcRt "+defAcRt);
        double maxInfoGain;
        while (Utils.gr(growData.numInstances(), 0.0) &&
                (numUnused > 0)
                && Utils.sm(defAcRt, 1.0)
                ){

            // We require that infoGain be positive
	/*if(numAntds == originalSize)
	  maxInfoGain = 0.0; // At least one condition allowed
	  else
	  maxInfoGain = Utils.eq(defAcRt, 1.0) ?
	  defAccu/(double)numAntds : 0.0; */
            maxInfoGain = 0.0;

	/* Build a list of antecedents */
            Antd oneAntd=null;
            Instances coverData = null;
            Enumeration enumAttr=growData.enumerateAttributes();

	/* Build one condition based on all attributes not used yet*/
            while (enumAttr.hasMoreElements()){
                Attribute att= (Attribute)(enumAttr.nextElement());

                if(m_Debug)
                    System.err.println("\nOne condition: size = "
                            + growData.sumOfWeights());

                Antd antd =null;
                if(att.isNumeric())
                    antd = new NumericAntd(att);
                else
                    antd = new NominalAntd(att);

                if(!used[att.index()]){
	    /* Compute the best information gain for each attribute,
	       it's stored in the antecedent formed by this attribute.
	       This procedure returns the data covered by the antecedent*/
                    Instances coveredData = computeInfoGain(growData, defAcRt,
                            antd);
                    if(coveredData != null){
                        double infoGain = antd.getMaxInfoGain();
                        if(m_Debug)
                            System.err.println("Test of \'"+antd.toString()+
                                    "\': infoGain = "+
                                    infoGain + " | Accuracy = " +
                                    antd.getAccuRate()+
                                    "="+antd.getAccu()
                                    +"/"+antd.getCover()+
                                    " def. accuracy: "+defAcRt);

                        if(infoGain > maxInfoGain){
                            oneAntd=antd;
                            coverData = coveredData;
                            maxInfoGain = infoGain;
                        }
                    }
                }
            }

            if(oneAntd == null) break; // Cannot find antds
            if(Utils.sm(oneAntd.getAccu(), m_MinNo)) break;// Too low coverage

            //Numeric attributes can be used more than once
            if(!oneAntd.getAttr().isNumeric()){
                used[oneAntd.getAttr().index()]=true;
                numUnused--;
            }

            m_Antds.addElement(oneAntd);
            growData = coverData;// Grow data size is shrinking
            defAcRt = oneAntd.getAccuRate();
        }
    }


    /**
     * Compute the best information gain for the specified antecedent
     *
     * @param instances the data based on which the infoGain is computed
     * @param defAcRt the default accuracy rate of data
     * @param antd the specific antecedent
     * @param numConds the number of antecedents in the rule so far
     * @return the data covered by the antecedent
     * @throws Exception
     */
    private Instances computeInfoGain(Instances instances, double defAcRt,
                                      Antd antd) throws Exception{
        Instances data = instances;

    /* Split the data into bags.
	 The information gain of each bag is also calculated in this procedure */
        Instances[] splitData = antd.splitData(data, defAcRt,
                m_Consequent);

    /* Get the bag of data to be used for next antecedents */
        if(splitData != null)
            return splitData[(int)antd.getAttrValue()];
        else return null;
    }

    /**
     * Prune all the possible final sequences of the rule using the
     * pruning data.  The measure used to prune the rule is based on
     * flag given.
     *
     * @param pruneData the pruning data used to prune the rule
     * @param useWhole flag to indicate whether use the error rate of
     *                 the whole pruning data instead of the data covered
     * @throws Exception
     */
    public void prune(Instances pruneData, boolean useWhole) throws Exception{
        Instances data = pruneData;

        double total = data.sumOfWeights();
        if(!Utils.gr(total, 0.0))
            return;

    /* The default accurate # and rate on pruning data */
        double defAccu=computeDefAccu(data);

        if(m_Debug)
            System.err.println("Pruning with " + defAccu +
                    " positive data out of " + total +
                    " instances");

        int size=m_Antds.size();
        if(size == 0) return; // Default rule before pruning

        double[] worthRt = new double[size];
        double[] coverage = new double[size];
        double[] worthValue = new double[size];
        for(int w=0; w<size; w++){
            worthRt[w]=coverage[w]=worthValue[w]=0.0;
        }

    /* Calculate accuracy parameters for all the antecedents in this rule */
        double tn = 0.0; // True negative if useWhole
        for(int x=0; x<size; x++){
            Antd antd=(Antd)m_Antds.elementAt(x);
            Instances newData = data;
            data = new Instances(newData, 0); // Make data empty

            for(int y=0; y<newData.numInstances(); y++){
                Instance ins=newData.instance(y);

                if(antd.covers(ins)){   // Covered by this antecedent
                    coverage[x] += ins.weight();
                    data.add(ins);                 // Add to data for further pruning
                    if((int)ins.classValue() == (int)m_Consequent) // Accurate prediction
                        worthValue[x] += ins.weight();
                }
                else if(useWhole){ // Not covered
                    if((int)ins.classValue() != (int)m_Consequent)
                        tn += ins.weight();
                }
            }

            if(useWhole){
                worthValue[x] += tn;
                worthRt[x] = worthValue[x] / total;
            }
            else // Note if coverage is 0, accuracy is 0.5
                worthRt[x] = (worthValue[x]+1.0)/(coverage[x]+2.0);
        }

        double maxValue = (defAccu+1.0)/(total+2.0);
        int maxIndex = -1;
        for(int i=0; i<worthValue.length; i++){
            if(m_Debug){
                double denom = useWhole ? total : coverage[i];
                System.err.println(i+"(useAccuray? "+!useWhole+"): "
                        + worthRt[i] +
                        "="+worthValue[i]+
                        "/"+denom);
            }
            if(worthRt[i] > maxValue){ // Prefer to the
                maxValue = worthRt[i]; // shorter rule
                maxIndex = i;
            }
        }

    /* Prune the antecedents according to the accuracy parameters */
        for(int z=size-1;z>maxIndex;z--)
            m_Antds.removeElementAt(z);
    }

    /**
     * Prints this rule
     *
     * @param classAttr the class attribute in the data
     * @return a textual description of this rule
     */
    public String toString(Attribute classAttr) {
        StringBuffer text =  new StringBuffer();
        if(m_Antds.size() > 0){
            for(int j=0; j< (m_Antds.size()-1); j++)
                text.append("(" + ((Antd)(m_Antds.elementAt(j))).toString()+ ") and ");
            text.append("("+((Antd)(m_Antds.lastElement())).toString() + ")");
        }
        text.append(" => " + classAttr.name() +
                "=" + classAttr.value((int)m_Consequent));

        return text.toString();
    }
}
