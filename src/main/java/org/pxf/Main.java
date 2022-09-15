package org.pxf;

import org.pxf.model.Engine;

public class Main {
    public static void main(String[] args) {
        int sampleSize = 3;
        experiment(3, 1_000, sampleSize);
        //experiment(3, 5_00, sampleSize);
        //experiment(3, 10_00, sampleSize);

    }
    private static void experiment(int MinMaxDepth, int MCTS_nSamples, int sampleSize){
        Engine engine = new Engine();
        int playerOneScore = 0;
        int currScore;
        for (int i = 0; i < sampleSize; i++){
            engine.initPieces();
            currScore = engine.rollOutScore(MinMaxDepth, MCTS_nSamples);
            playerOneScore += currScore;
            if (currScore == 0)
                System.out.println("MCTS won!");
        }
        System.out.println(
                "Experiment ( MinMaxDepth = " + MinMaxDepth +
                           ", MCTS nSamples = " + MCTS_nSamples +
                           ", nSamples = " + sampleSize);
        System.out.println("Score =  " + playerOneScore + " Vs. " + (sampleSize - playerOneScore));
        System.out.println();
    }
}