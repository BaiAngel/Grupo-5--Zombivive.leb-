package com.mygdx.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetManager {

    // Sprite Sheet
    public static Texture personajes;

    // Nau i fons
    public static TextureRegion[] humanIdle, humanUp, humanRight, humanLeft, humanDown;
    public static Animation aHumanIdle, aHumanUp, aHumanRight, aHumanLeft, aHumanDown;
    public static Texture human, background, actor_malo;
    public static int x;
     //try
    public static float tiempoAnim;
    public static TextureRegion [] regionsMovimiento;
    private static Texture imagen;
    public static TextureRegion frameActual;
    //Necesito
    private static String path;
    private static int divideIn;

    public static void load() {
        // Carreguem les textures i li apliquem el mètode d'escalat 'nearest'
        human = new Texture(Gdx.files.internal("human.png"));
        human.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        actor_malo = new Texture(Gdx.files.internal("actor malo.png"));
        actor_malo.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);


        personajes = new Texture(Gdx.files.internal("personajes.png"));
        personajes.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        //humanIdle
        path = "humanIdle.png";
        divideIn = 1;
        aHumanIdle = crearAnimacion(path, divideIn);
        //HumanUp
        path = "humanUp.png";
        divideIn = 3;
        aHumanUp = crearAnimacion(path, divideIn);
        //HumanRight
        path = "humanRight.png";
        divideIn = 3;
        aHumanRight = crearAnimacion(path, divideIn);
        //HumanDown
        path = "humanDown.png";
        divideIn = 2;
        aHumanDown = crearAnimacion(path, divideIn);
        //HumanLeft
        path = "humanLeft.png";
        divideIn = 3;
        aHumanLeft = crearAnimacion(path, divideIn);
        // Carreguem les textures i li apliquem el mètode d'escalat 'nearest'
        background = new Texture(Gdx.files.internal("fons.jpeg"));
        background.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    private static Animation crearAnimacion(String path, int divideIn) {
        imagen = new Texture(Gdx.files.internal(path));
        TextureRegion [][] tmp = TextureRegion.split(imagen,
                imagen.getWidth()/divideIn,imagen.getHeight());

        regionsMovimiento = new TextureRegion[divideIn];
        for (int i = 0; i < divideIn; i++) regionsMovimiento[i] = tmp[0][i];
        Animation createAnimation = new Animation(1 / 3f, regionsMovimiento);
        tiempoAnim = 0f;
        return createAnimation;
    }

    public static void dispose() {
        human.dispose();
        background.dispose();
        personajes.dispose();
    }

}