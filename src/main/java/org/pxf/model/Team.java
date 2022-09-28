package org.pxf.model;

public enum Team{
    WHITE,
    BLACK,
    ;
    public Team getOpponent() {
        return this == Team.WHITE ? Team.BLACK : Team.WHITE;
    }

}

