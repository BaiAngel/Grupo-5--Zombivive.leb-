package com.mygdx.game.objects;

import static com.mygdx.game.helpers.AssetManager.frameActual;
import static com.mygdx.game.helpers.AssetManager.tiempoAnim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.helpers.AssetManager;
import com.mygdx.game.utils.Methods;
import com.mygdx.game.utils.Settings;

public class Human extends Actor {
    // Diferents posicions de Human: recta, pujant i baixant
    public static final int HUMAN_IDLE = 0;
    public static final int HUMAN_UP = 1;
    public static final int HUMAN_RIGHT = 2;
    public static final int HUMAN_DOWN = 3;
    public static final int HUMAN_LEFT = 4;

    // Paràmetres de Human
    private Vector2 position;
    private int width, height;
    private int direction;
    private Rectangle collisionRect;



    public Human(float x, float y, int width, int height) {

        // Inicialitzem els arguments segons la crida del constructor
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);

        // Inicialitzem Human a l'estat normal
        direction = HUMAN_IDLE;
        // Creem el rectangle de col·lisions
        collisionRect = new Rectangle();



    }
    public void act(float delta) {

        // Movem l'Spacecraft depenent de la direcció controlant que no surti de la pantalla
        switch (direction) {
            case HUMAN_UP:
                if (this.position.y + Settings.HUMAN_VELOCITY * delta <= Settings.GAME_HEIGHT) {
                    this.position.y += Settings.HUMAN_VELOCITY * delta;
                }
                break;
            case HUMAN_RIGHT:
                if (this.position.x - Settings.HUMAN_VELOCITY * delta <= Settings.GAME_WIDTH) {
                    this.position.x -= Settings.HUMAN_VELOCITY * delta;
                }
                break;
            case HUMAN_DOWN:
                if (this.position.y - height + Settings.HUMAN_VELOCITY * delta >= 0) {
                    this.position.y -= Settings.HUMAN_VELOCITY * delta;
                }
                break;
            case HUMAN_LEFT:
                if (this.position.x + Settings.HUMAN_VELOCITY * delta >= 0) {
                    this.position.x += Settings.HUMAN_VELOCITY * delta;
                }
                break;
            case HUMAN_IDLE:
                break;
        }
        collisionRect.set(position.x+4, position.y + 4, width/2, height/2);
    }

    // Obtenim el TextureRegion depenent de la posició de l'spacecraft
    public Animation getHumanTexture() {

        switch (direction) {

            case HUMAN_UP:
                return AssetManager.aHumanUp;
            case HUMAN_RIGHT:
                return AssetManager.aHumanLeft;
            case HUMAN_DOWN:
                return AssetManager.aHumanDown;
            case HUMAN_LEFT:
                return AssetManager.aHumanRight;
            default:
                return AssetManager.aHumanIdle;
        }
    }

    // Getters dels atributs principals
    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Rectangle getCollisionRect() {
        return collisionRect;
    }

    // Canviem la direcció de l'Spacecraft: Puja
    public void goUp() {
        direction = HUMAN_UP;
    }

    // Canviem la direcció de l'Spacecraft: Dreta
    public void goRight() {
        direction = HUMAN_RIGHT;
    }

    // Canviem la direcció de l'Spacecraft: Baixa
    public void goDown() {
        direction = HUMAN_DOWN;
    }

    // Canviem la direcció de l'Spacecraft: Esquerra
    public void goLeft() {
        direction = HUMAN_LEFT;
    }

    // Posem l'Spacecraft al seu estat original
    public void goStraight() {
        direction = HUMAN_IDLE;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        tiempoAnim += Gdx.graphics.getDeltaTime(); //es el tiempo que paso desde el ultimo render
        Animation frameDir = getHumanTexture();
        frameActual = (TextureRegion) frameDir.getKeyFrame(tiempoAnim,true);
        batch.draw(frameActual,getX(),getY());
    }
}
