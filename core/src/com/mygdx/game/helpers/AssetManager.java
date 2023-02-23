package com.mygdx.game.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class AssetManager {

    // Animacions
    public static Animation aHumanIdle, aHumanUp, aHumanRight, aHumanLeft, aHumanDown;
    public static Animation aSkeletonIdle, aSkeletonUp, aSkeletonRight, aSkeletonLeft, aSkeletonDown;
    public static Animation aFireballUp, aFireballRight, aFireballLeft, aFireballDown;
    public static Texture background;
    // Try
    public static TextureRegion [] regionsMovimiento;
    private static Texture imagen;
    public static TextureRegion frameActual;
    // Necesito
    private static String path;
    private static int divideIn;
    // Sons
    public static Sound hitSound;
    public static Music music;

    public static void load() {
        //Crear human
        createHumanMageTexture();
        //Crear skeleton
        createSkeletonTexture();
        //Crear skeleton
        createFireballBlueTexture();
        //Sounds
        //Hit
        hitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bite.mp3"));
        //Música del joc
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setVolume(0.2f);
        music.setLooping(true);
    }

    private static void createFireballRedTexture() {
        divideIn = 4;
        //fireballUp
        path = "bullets/fireballR/up.png";
        aFireballUp = crearAnimacion(path, divideIn);
        //fireballRight
        path = "bullets/fireballR/right.png";
        aFireballRight = crearAnimacion(path, divideIn);
        //fireballDown
        path = "bullets/fireballR/down.png";
        aFireballDown = crearAnimacion(path, divideIn);
        //fireballLeft
        path = "bullets/fireballR/left.png";
        aFireballLeft = crearAnimacion(path, divideIn);
    }

    private static void createFireballBlueTexture() {
        divideIn = 4;
        //fireballUp
        path = "bullets/fireballB/up.png";
        aFireballUp = crearAnimacion(path, divideIn);
        //fireballRight
        path = "bullets/fireballB/right.png";
        aFireballRight = crearAnimacion(path, divideIn);
        //fireballDown
        path = "bullets/fireballB/down.png";
        aFireballDown = crearAnimacion(path, divideIn);
        //fireballLeft
        path = "bullets/fireballB/left.png";
        aFireballLeft = crearAnimacion(path, divideIn);
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

    private static void createHumanWarriorTexture() {
        //humanIdle
        path = "humanAnimation/warrior/humanIdle.png";
        divideIn = 1;
        aHumanIdle = crearAnimacion(path, divideIn);
        //HumanUp
        path = "humanAnimation/warrior/humanUp.png";
        divideIn = 3;
        aHumanUp = crearAnimacion(path, divideIn);
        //HumanRight
        path = "humanAnimation/warrior/humanRight.png";
        divideIn = 3;
        aHumanRight = crearAnimacion(path, divideIn);
        //HumanDown
        path = "humanAnimation/warrior/humanDown.png";
        divideIn = 2;
        aHumanDown = crearAnimacion(path, divideIn);
        //HumanLeft
        path = "humanAnimation/warrior/humanLeft.png";
        divideIn = 3;
        aHumanLeft = crearAnimacion(path, divideIn);
    }

    private static void createHumanMageTexture() {
        //humanIdle
        path = "humanAnimation/mage/humanIdle.png";
        divideIn = 1;
        aHumanIdle = crearAnimacion(path, divideIn);
        //HumanUp
        path = "humanAnimation/mage/humanUp.png";
        divideIn = 3;
        aHumanUp = crearAnimacion(path, divideIn);
        //HumanRight
        path = "humanAnimation/mage/humanRight.png";
        divideIn = 3;
        aHumanRight = crearAnimacion(path, divideIn);
        //HumanDown
        path = "humanAnimation/mage/humanDown.png";
        divideIn = 2;
        aHumanDown = crearAnimacion(path, divideIn);
        //HumanLeft
        path = "humanAnimation/mage/humanLeft.png";
        divideIn = 3;
        aHumanLeft = crearAnimacion(path, divideIn);
    }

    private static Animation<TextureRegion> crearAnimacion(String path, int divideIn) {
        imagen = new Texture(Gdx.files.internal(path));
        TextureRegion [][] tmp = TextureRegion.split(imagen,
                imagen.getWidth()/divideIn,imagen.getHeight());

        regionsMovimiento = new TextureRegion[divideIn];
        for (int i = 0; i < divideIn; i++){
            regionsMovimiento[i] = tmp[0][i];
        }


        Animation createAnimation = new Animation(0.3f, regionsMovimiento);
        return createAnimation;
    }

    public static TiledMap crearMapForestTmx() {
        TiledMap map;
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("maps/mapForest/forest.tmx");
        return map;
    }

    public static JsonValue getJson(String path) {
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.internal(path));
        return base;
    }

    public static void dispose() {
    }

}