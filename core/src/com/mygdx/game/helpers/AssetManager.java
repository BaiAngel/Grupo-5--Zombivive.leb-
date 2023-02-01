package com.mygdx.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetManager {

    // Sprite Sheet
    public static Texture personajes;

    // Nau i fons
    public static TextureRegion humanIdle;
    public static TextureRegion humanUp, humanRight, humanLeft, humanDown;
    public static Animation aHumanUp, aHumanRight, aHumanLeft, aHumanDown;
    public static Texture human, background, actor_malo;

    public static void load() {
        // Carreguem les textures i li apliquem el mètode d'escalat 'nearest'
        human = new Texture(Gdx.files.internal("human.png"));
        human.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        actor_malo = new Texture(Gdx.files.internal("actor malo.png"));
        actor_malo.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);


        personajes = new Texture(Gdx.files.internal("personajes.png"));
        personajes.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        //Carreguem idle
        humanIdle = new TextureRegion(personajes, 80, 142, 16, 18);
        humanUp = new TextureRegion(personajes, 176, 142, 16, 18);
        humanRight = new TextureRegion(personajes, 128, 142, 16, 18);
        humanDown = new TextureRegion(personajes, 96, 142, 16, 18);
        humanLeft = new TextureRegion(personajes, 224, 142, 16, 18);

        // Carreguem les textures i li apliquem el mètode d'escalat 'nearest'
        background = new Texture(Gdx.files.internal("fons.jpeg"));
        background.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public static void dispose() {
        human.dispose();
        background.dispose();
    }

}