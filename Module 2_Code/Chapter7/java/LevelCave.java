package com.gamecodeschool.c7platformgame;

import java.util.ArrayList;

public class LevelCave extends LevelData{
    LevelCave() {
        tiles = new ArrayList<String>();
        this.tiles.add("p.............................................");
        this.tiles.add(".....................................d........");
        this.tiles.add(".........................g....................");
        this.tiles.add("..............................................");
        this.tiles.add(".....111111111111111111111111111111111111111..");
        this.tiles.add("....................1........u.........d......");
        this.tiles.add(".................c..........u1................");
        this.tiles.add("......d..........1.........u1.........d.......");
        this.tiles.add("..............c...........u1..................");
        this.tiles.add(".d............1..........u1...................");
        this.tiles.add("......................e..1....e.....e.........");
        this.tiles.add("....11111111111111111111111111111111111111....");
    }
}


