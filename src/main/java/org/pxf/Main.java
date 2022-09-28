package org.pxf;

import org.pxf.model.Engine;

public class Main {

    public static void main(String[] args) {
        Integer[] experimentSizes = new Integer[] {10, 100, 1000};
        for (int n : experimentSizes)
            experiment(n);
    }
    private static void experiment(int sampleSize){
        int playerOneScore = 0;
        Engine engine = new Engine();
        for (int i = 0; i < sampleSize; i++){
            playerOneScore += engine.rollOutScore();
            engine.reset();
        }
        System.out.println("Sample Size = " + sampleSize + " Score =  " + playerOneScore + " Vs. " + (sampleSize - playerOneScore));
        System.out.println();
    }
}