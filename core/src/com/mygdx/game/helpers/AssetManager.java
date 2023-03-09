package com.mygdx.game.helpers;

import static com.mygdx.game.screens.TitleScreen.selectCharacter;

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
    public static Animation aHumanIdleDown,aHumanIdleUp,aHumanIdleLeft,aHumanIdleRight, aHumanUp, aHumanRight, aHumanLeft, aHumanDown;
    public static Animation aSkeletonIdle, aSkeletonUp, aSkeletonRight, aSkeletonLeft, aSkeletonDown;
    public static Animation aFireballRUp, aFireballRRight, aFireballRLeft, aFireballRDown;
    public static Animation aFireballBUp, aFireballBRight, aFireballBLeft, aFireballBDown;
    public static Animation aBossIdle, aBossHit;
    // Try
    public static TextureRegion [] regionsMovimiento;
    private static Texture imagen;
    public static TextureRegion frameActual;
    // Necesito
    private static String path;
    private static int divideIn;
    private final static int MAGE = 0;
    private final static int WARRIOR = 1;
    // Sons
    public static Sound hitSound;
    public static Music music;
    public static Music musicatac;
    public static Music musica;
    public static void load() {
        //Crear skeleton
        createSkeletonTexture();
        //Crear boss
        createBossTexture();
        //Crear skeleton
        createFireballBlueTexture();
        createFireballRedTexture();
        //Sounds
        //Hit
        hitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bite.mp3"));
        //Música del joc
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setVolume(0.2f);
        musica= Gdx.audio.newMusic(Gdx.files.internal("sounds/kingdom.mp3"));
        musicatac =Gdx.audio.newMusic(Gdx.files.internal("sounds/Tranzit.mp3"));
        music.setLooping(true);
        musica.setLooping(true);

    }



    public static void createHumanTexture() {
        switch (selectCharacter) {
            case WARRIOR:
                createHumanWarriorTexture();
                break;
            case MAGE:
                createHumanMageTexture();
                break;
        }
    }

    private static void createBossTexture() {
        //Boss Animation
        divideIn = 4;
        path = "bossAnimation/bossIdle.png";
        aBossIdle = crearAnimacion(path, divideIn);
        //Boss hit
        divideIn = 1;
        path = "bossAnimation/bossHit.png";
        aBossHit = crearAnimacion(path, divideIn);
    }

    private static void createFireballRedTexture() {
        divideIn = 4;
        //fireballUp
        path = "bullets/fireballR/up.png";
        aFireballRUp = crearAnimacion(path, divideIn);
        //fireballRight
        path = "bullets/fireballR/right.png";
        aFireballRRight = crearAnimacion(path, divideIn);
        //fireballDown
        path = "bullets/fireballR/down.png";
        aFireballRDown = crearAnimacion(path, divideIn);
        //fireballLeft
        path = "bullets/fireballR/left.png";
        aFireballRLeft = crearAnimacion(path, divideIn);
    }

    private static void createFireballBlueTexture() {
        divideIn = 4;
        //fireballUp
        path = "bullets/fireballB/up.png";
        aFireballBUp = crearAnimacion(path, divideIn);
        //fireballRight
        path = "bullets/fireballB/right.png";
        aFireballBRight = crearAnimacion(path, divideIn);
        //fireballDown
        path = "bullets/fireballB/down.png";
        aFireballBDown = crearAnimacion(path, divideIn);
        //fireballLeft
        path = "bullets/fireballB/left.png";
        aFireballBLeft = crearAnimacion(path, divideIn);
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
        path = "humanAnimation/warrior/humanIdleDown.png";
        divideIn = 1;
        aHumanIdleDown = crearAnimacion(path, divideIn);
        path = "humanAnimation/warrior/humanIdleUp.png";
        divideIn = 1;
        aHumanIdleUp = crearAnimacion(path, divideIn);
        path = "humanAnimation/warrior/humanIdleRight.png";
        divideIn = 1;
        aHumanIdleRight = crearAnimacion(path, divideIn);
        path = "humanAnimation/warrior/humanIdleLeft.png";
        divideIn = 1;
        aHumanIdleLeft = crearAnimacion(path, divideIn);
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
        path = "humanAnimation/mage/humanIdleDown.png";
        divideIn = 1;
        aHumanIdleDown = crearAnimacion(path, divideIn);
        path = "humanAnimation/mage/humanIdleUp.png";
        divideIn = 1;
        aHumanIdleUp = crearAnimacion(path, divideIn);
        path = "humanAnimation/mage/humanIdleRight.png";
        divideIn = 1;
        aHumanIdleRight = crearAnimacion(path, divideIn);
        path = "humanAnimation/mage/humanIdleLeft.png";
        divideIn = 1;
        aHumanIdleLeft = crearAnimacion(path, divideIn);
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

    public static TiledMap crearMapDesertTmx() {
        TiledMap map;
        TmxMapLoader loader = new TmxMapLoader();
        map = loader.load("maps/mapDesert/desert.tmx");
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