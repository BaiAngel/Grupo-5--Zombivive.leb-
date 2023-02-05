package com.mygdx.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetManager {

    // Sprite Sheet
    public static Texture personajes;

    // Nau i fons
    public static Animation aHumanIdle, aHumanUp, aHumanRight, aHumanLeft, aHumanDown;
    public static Animation aSkeletonIdle, aSkeletonUp, aSkeletonRight, aSkeletonLeft, aSkeletonDown;
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
        //Crear human
        createHumanTexture();
        //Crear skeleton
        createSkeletonTexture();
        // Carreguem les textures i li apliquem el mètode d'escalat 'nearest'
        background = new Texture(Gdx.files.internal("fons.jpeg"));
        background.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    private static void createSkeletonTexture() {
        //skeletonIdle
        path = "skeletonAnimation/skeletonIdle.png";
        divideIn = 1;
        aSkeletonIdle = crearAnimacion(path, divideIn);
        //skeletonUp
        path = "skeletonAnimation/skeletonUp.png";
        divideIn = 3;
        aSkeletonUp = crearAnimacion(path, divideIn);
        //skeletonRight
        path = "skeletonAnimation/skeletonRight.png";
        divideIn = 3;
        aSkeletonRight = crearAnimacion(path, divideIn);
        //skeletonDown
        path = "skeletonAnimation/skeletonDown.png";
        divideIn = 2;
        aSkeletonDown = crearAnimacion(path, divideIn);
        //skeletonLeft
        path = "skeletonAnimation/skeletonLeft.png";
        divideIn = 3;
        aSkeletonLeft = crearAnimacion(path, divideIn);
    }

    private static void createHumanTexture() {
        //humanIdle
        path = "humanAnimation/humanIdle.png";
        divideIn = 1;
        aHumanIdle = crearAnimacion(path, divideIn);
        //HumanUp
        path = "humanAnimation/humanUp.png";
        divideIn = 3;
        aHumanUp = crearAnimacion(path, divideIn);
        //HumanRight
        path = "humanAnimation/humanRight.png";
        divideIn = 3;
        aHumanRight = crearAnimacion(path, divideIn);
        //HumanDown
        path = "humanAnimation/humanDown.png";
        divideIn = 2;
        aHumanDown = crearAnimacion(path, divideIn);
        //HumanLeft
        path = "humanAnimation/humanLeft.png";
        divideIn = 3;
        aHumanLeft = crearAnimacion(path, divideIn);
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