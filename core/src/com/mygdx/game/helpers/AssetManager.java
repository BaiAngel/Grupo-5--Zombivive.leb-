package com.mygdx.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetManager {

    // Sprite Sheet
    public static Texture human, background;

    public static void load() {
        // Carreguem les textures i li apliquem el mètode d'escalat 'nearest'
        human = new Texture(Gdx.files.internal("human.png"));
        human.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        // Carreguem les textures i li apliquem el mètode d'escalat 'nearest'
        background = new Texture(Gdx.files.internal("fons.jpeg"));
        background.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public static void dispose() {
        human.dispose();
        background.dispose();
    }

}
