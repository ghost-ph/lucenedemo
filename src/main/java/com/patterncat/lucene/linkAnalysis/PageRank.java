package com.patterncat.lucene.linkAnalysis;

public class PageRank {

    public static void main(String[] args) {
        int N=7;
        double[][] counts = {
                {0, 1, 1, 1, 1, 0, 1},
                {1, 0, 0, 0, 0, 0, 0},
                {1, 1, 0, 0, 0, 0, 0},
                {0, 1, 1, 0, 1, 0, 0},
                {1, 0, 1, 1, 0, 1, 0},
                {1, 0, 0, 0, 1, 0, 0},
                {0, 0, 0, 0, 1, 0, 0}
        };// counts[i][j] = # links from page i to page j
        int[] outDegree = new int[N];      // outDegree[i] = # links from page i to anywhere

        // Print probability distribution for row i.
        for (int i = 0; i < N; i++)  {
            // Print probability for column j.
            for (int j = 0; j < N; j++) {
                if(counts[i][j]==1)
                {
                    ++outDegree[i];
                }
            }
            System.out.println("outDegree "+i+":"+outDegree[i]);

            for (int j = 0; j < N; j++) {
                counts[i][j]=counts[i][j]/outDegree[i];
                //double p = .90*counts[i][j]/outDegree[i] + .10/N;
            }
        }
        StdArrayIO.print(counts);
        // Transition matrix: p[i][j] = prob. that surfer moves from page i to
        double[][] p = Matrix.transpose(counts);

        // initialize the pagerank vector - any unit vector will do
        double[] rank = new double[N];
        for (int i = 0; i < N; i++)  {
            rank[i] = 1.0/N;
        }
        //rank[0] = 1.0;

        int T=100;
        // perform T iterations of the power method
        for (int t = 0; t < T; t++) {
            //rank = Matrix.multiply(rank, p);
            rank = Matrix.multiply(p,rank);
        }

        // print page ranks
        StdArrayIO.print(rank);
    }
}
