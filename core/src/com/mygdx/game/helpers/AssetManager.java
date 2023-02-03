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
     public static Animation animation;
    public static float tiempoAnim;
    public static TextureRegion [] regionsMovimiento;
    private static Texture imagen;
    public static TextureRegion frameActual;

    public static void load() {
        // Carreguem les textures i li apliquem el mètode d'escalat 'nearest'
        human = new Texture(Gdx.files.internal("human.png"));
        human.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        actor_malo = new Texture(Gdx.files.internal("actor malo.png"));
        actor_malo.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);


        personajes = new Texture(Gdx.files.internal("personajes.png"));
        personajes.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        //humanIdle
        //humanIdle = new TextureRegion(personajes, 80, 142, 16, 18);
        humanIdle = new TextureRegion[2];
        x = 80;
        for (int i = 0; i < humanIdle.length; i++) {

            humanIdle[i] = new TextureRegion(personajes, x + 16, 142, 16, 18);
        }
        aHumanIdle = new Animation(0.05f, humanIdle);
        aHumanIdle.setPlayMode(Animation.PlayMode.LOOP);
        //HumanUp
        //humanUp = new TextureRegion(personajes, 176, 142, 16, 18);
        humanUp = new TextureRegion[3];
        x = 80;
        for (int i = 0; i < humanUp.length; i++) {

            humanUp[i] = new TextureRegion(personajes, x + 16, 142, 16, 18);
        }
        aHumanUp = new Animation<TextureRegion>(0.05f, humanUp);
        aHumanUp.setPlayMode(Animation.PlayMode.LOOP);
        //HumanRight
        //humanRight = new TextureRegion(personajes, 128, 142, 16, 18);
        humanRight = new TextureRegion[3];
        x = 128;
        for (int i = 0; i < humanRight.length; i++) {

            humanRight[i] = new TextureRegion(personajes, x + 16, 142, 16, 18);
        }
        aHumanRight = new Animation(0.05f, humanRight);
        aHumanRight.setPlayMode(Animation.PlayMode.LOOP);
        //HumanDown
        //humanDown = new TextureRegion(personajes, 96, 142, 16, 18);
        humanDown = new TextureRegion[2];
        x = 96;
        for (int i = 0; i < humanDown.length; i++) {

            humanDown[i] = new TextureRegion(personajes, x + 16, 142, 16, 18);
        }
        aHumanDown = new Animation(0.05f, humanDown);
        aHumanDown.setPlayMode(Animation.PlayMode.LOOP);
        //HumanLeft
        String path = "humanLeft.png";
        int divideIn = 6;
        aHumanLeft = crearAnimacion(path, divideIn);
        /*
        humanLeft = new TextureRegion(personajes, 224, 142, 16, 18);
        x = 224;
        humanLeft = new TextureRegion[3];
        for (int i = 0; i < humanLeft.length; i++) {
            humanLeft[i] = new TextureRegion(personajes, x + 16, 142, 16, 18);
        }
        aHumanLeft = new Animation(0.05f, humanLeft);
        aHumanLeft.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
         */
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
        Animation createAnimation = new Animation(1 / 10f, regionsMovimiento);
        tiempoAnim = 0f;
        return createAnimation;
    }

    public static void dispose() {
        human.dispose();
        background.dispose();
        personajes.dispose();
    }

}