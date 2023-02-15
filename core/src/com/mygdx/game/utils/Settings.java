package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;

public class Settings {
    // Mida del joc, s'escalarà segons la necessitat
    public static final int GAME_WIDTH = Gdx.graphics.getWidth();
    public static final int GAME_HEIGHT = Gdx.graphics.getHeight();

    // Propietats de la nau
    public static final float HUMAN_VELOCITY = 50;
    public static final float MOB_VELOCITY = 10;
    public static final float BULLET_VELOCITY = 70;
    public static final int HUMAN_WIDTH = 15;
    public static final int HUMAN_HEIGHT = 30;
    public static final float HUMAN_STARTX = GAME_WIDTH/2 - HUMAN_WIDTH/2;
    public static final float HUMAN_STARTY = GAME_HEIGHT/2 - HUMAN_HEIGHT/2;
    public static final float MOB_STARTX = GAME_WIDTH/3;
    public static final float MOB_STARTY = GAME_HEIGHT/3;
    public static final int MOB_WIDTH = 15;
    public static final int MOB_HEIGHT = 30;
    public static final int BULLET_WIDTH = 15;
    public static final int BULLET_HEIGHT = 10;
    public static final int IDLE = 0;
    public static final int UP = 1;
    public static final int RIGHT = 2;
    public static final int DOWN = 3;
    public static final int LEFT = 4;
    public static int TIME_BETWEEN_FIREBALL_SPAWNS = 50;
    public static int FIREBALL_SPAWN_TIMER = 0;

}
