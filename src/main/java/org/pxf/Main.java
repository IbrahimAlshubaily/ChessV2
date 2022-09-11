package org.pxf;

import org.pxf.model.Engine;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Engine engine = new Engine();
        System.out.println(engine.getBoardRepr());
        engine.initPieces();
        System.out.println(engine.getBoardRepr());
        while (!engine.isGameOver()){
            engine.minMaxRollout();
            //TimeUnit.SECONDS.sleep(1);
        }
    }
}