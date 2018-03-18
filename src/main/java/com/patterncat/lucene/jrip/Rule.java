package com.patterncat.lucene.jrip;

import java.io.Serializable;

/**
 * Abstract class of generic rule
 *
 * @author Xin Xu (xx5@cs.waikato.ac.nz)
 * @version $Revision: 1.8 $
 */
public abstract class Rule
        implements Serializable{

    /** for serialization */
    private static final long serialVersionUID = 8815687740470471229L;

    /**
     * Get a shallow copy of this rule
     *
     * @return the copy
     */
    public Object copy(){ return this;}

    /**
     * Whether the instance covered by this rule
     *
     * @param datum the instance in question
     * @return the boolean value indicating whether the instance
     *         is covered by this rule
     */
    public abstract boolean covers(Instance datum);

    /**
     * Build this rule
     *
     * @param data the data used to build the rule
     * @exception Exception if rule cannot be built
     */
    public abstract void grow(Instances data) throws Exception;

    /**
     * Whether this rule has antecedents, i.e. whether it is a default rule
     *
     * @return the boolean value indicating whether the rule has antecedents
     */
    public abstract boolean hasAntds();

    /**
     * Get the consequent of this rule, i.e. the predicted class
     *
     * @return the consequent
     */
    public abstract double getConsequent();

    /**
     * The size of the rule.  Could be number of antecedents in the case
     * of conjunctive rule
     *
     * @return the size of the rule
     */
    public abstract double size();
}
