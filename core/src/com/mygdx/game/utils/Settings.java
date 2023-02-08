package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;

public class Settings {
    // Mida del joc, s'escalarà segons la necessitat
    public static final int GAME_WIDTH = Gdx.graphics.getWidth();//1024
    public static final int GAME_HEIGHT = Gdx.graphics.getHeight();//1024

    // Propietats de la nau
    public static final float HUMAN_VELOCITY = 50;
    public static final float MOB_VELOCITY = 10;
    public static final int HUMAN_WIDTH = 15;
    public static final int HUMAN_HEIGHT = 30;
    public static final float HUMAN_STARTX = GAME_WIDTH/2 - HUMAN_WIDTH/2;
    public static final float HUMAN_STARTY = GAME_HEIGHT/2 - HUMAN_HEIGHT/2;
    public static final float MOB_STARTX = 300;
    public static final float MOB_STARTY = 500;
    public static final int MOB_WIDTH = 15;
    public static final int MOB_HEIGHT = 30;

}
