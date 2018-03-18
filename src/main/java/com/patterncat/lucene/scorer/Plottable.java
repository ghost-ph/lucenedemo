package com.patterncat.lucene.scorer;

/**
 * The Plottable interface is used to pass methods as arguments to the
 * <CODE>functionPlot</CODE> commands.
 *
 * <P>To pass a function to be graphed as an argument to <CODE>functionPlot
 * </CODE>, we define a class to contain the function which implements the
 * <CODE>Plottable</CODE> interface and defines the method <CODE>f</CODE>. This
 * will look something like:
 *
 * <PRE>
 * public class FunctionClass extends Plottable
 * {
 *   public double f(double x)
 *   {
 *     ...
 *   }
 * }
 * </PRE>
 *
 * We can then pass this method as an argument to <CODE>functionPlot</CODE> by
 * creating an object of this class and passing that object:
 *
 * <PRE>
 * FunctionClass o = new FunctionClass();
 * SciGraph.functionPlot(o);
 * </PRE>
 *
 * @author Richard J. Davies
 * @version 1.0
 * @see SciGraph#functionPlot
 */
public interface Plottable
{
    /**
     * The method <CODE>f</CODE> should be overridden in your class to calculate
     * whatever function you wish to graph.
     *
     * @param x The x-coordinate at which the function is to be evaluated.
     * @return The y-coordinate at that point.
     * @see Plottable
     */
    public double f(double x);
}

