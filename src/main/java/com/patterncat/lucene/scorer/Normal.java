package com.patterncat.lucene.scorer;

import java.awt.Color;


// The `Normal' class is a wrapper to contain the function f,
// the density of the normal distribution, which must be passed
// as an argument to functionPlot.
class Normal implements Plottable
{
    // f is the density of the normal distribution to which the
    // binomial distribution should tend.
    public double f(double x)
    {
        // Convert the point on the real normal distribution
        // into a point on a N(0,1) normal distribution
        double normPt = (x - Binomial.getThrowNo()/2.0)
                / Math.sqrt(Binomial.getThrowNo()/4.0);

        // Calculate the density at that point
        // on the N(0,1) distribution
        double normDensity = Math.exp(-normPt*normPt/2)
                / Math.sqrt(2 * Math.PI);

        // Convert back to the density
        // of the real normal distribution
        return normDensity / Math.sqrt(Binomial.getThrowNo()/4.0);
    }
}


// This class implements the main body of the program.
// It extends SciGraph so that the program can be an applet.
class Binomial extends SciGraph
{
    // The number of throws per experiment
    private static int throwNo;

    // An accessor method for throwNo
    public static int getThrowNo()
    {
        return throwNo;
    }

    // The number of experiments
    private static int experimentNo;


    // A utility function to compute factorials.
    public static double fact(int x)
    {
        double ans = 1;

        for (int i=2; i<=x; i++)
            ans *= i;

        return ans;
    }


    // The `main' method for use as an application
    public static void main(String[] argv)
    {
        // A few useful variables
        String answer;
        int i, j;

        // Create the text window
        SciGraph.showText();

        // Prompt the user for a number of throws per experiment
        SciGraph.println("This program simulates "
                +"repeated tossing of a coin.");
        SciGraph.println();
        SciGraph.print("Enter a number of throws per experiment: ");

        // And read their answer
        answer = SciGraph.readln();
        throwNo = Integer.parseInt(answer);

        // Create a graph window with the right scale for that
        // kind of experiment
        SciGraph.showGraph(0, throwNo, 0, 1.5 * fact(throwNo)
                / (fact(throwNo/2) * fact(throwNo - throwNo/2)
                * Math.pow(2, throwNo)));

        do
        {
            // Clear the graph and prompt for a number of experiments
            SciGraph.clear();
            SciGraph.print("Enter a number of experiments "
                    +"to perform: ");

            // And read the answer
            answer = SciGraph.readln();
            experimentNo = Integer.parseInt(answer);

            // EXPERIMENTAL CALCULATION

            // Create an array to store the experimental results
            double[] experimentValues = new double[throwNo+1];

            // Perform the appropriate number of experiments
            for (i=0; i<experimentNo; i++)
            {
                int count = 0;

                // In each of which we count heads over the throws
                for (j=0; j<throwNo; j++)
                    if (Math.random() > 0.5)
                        count++;

                // And record our result
                experimentValues[count] += 1;
            }

            // Divide all frequencies by the number of experiments
            // to get estimated probabilities for each number of heads.
            for (i=0; i<=throwNo; i++)
                experimentValues[i] /= experimentNo;

            // Plot a graph of the estimated probabilities
            SciGraph.stepPlot(experimentValues, Color.red);

            // BINOMIAL DISTRIBUTION

            // Create an array to store the binomial distribution
            double[] binomValues = new double[throwNo+1];

            // Step through it, filling in the probabilities
            for (i=0; i<=throwNo; i++)
                binomValues[i] = fact(throwNo)
                        / (fact(throwNo-i) * fact(i)
                        * Math.pow(2, throwNo));

            // Plot a graph of the binomial distribution
            SciGraph.stepPlot(binomValues, Color.blue);

            // NORMAL DISTRIBUTION

            // Plot the normal distribution's density
            SciGraph.functionPlot(new Normal(), Color.green);

            // Ask if the user wants to repeat the experiment
            SciGraph.println();
            SciGraph.print("Do you want to have another go (y/n) ? ");
            answer = SciGraph.readln();
        }
        while (answer.equals("y"));
    }


    // The `main' method for use as an applet
    public void main()
    {
        // Simply call the other `main' method.
        main(new String[1]);
    }
}

