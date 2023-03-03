package com.mygdx.game.objects;

import static com.mygdx.game.helpers.AssetManager.frameActual;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.helpers.AssetManager;
import com.mygdx.game.utils.Settings;

public class Boss extends Actor {

    public static final int BOSS_IDLE = Settings.IDLE;
    public static final int BOSS_UP = Settings.UP;
    public static final int BOSS_RIGHT = Settings.RIGHT;
    public static final int BOSS_DOWN = Settings.DOWN;
    public static final int BOSS_LEFT = Settings.LEFT;
    public static final int BOSS_HIT = Settings.HIT;

    // Paràmetres de Human
    private Vector2 position;
    private int width, height;
    private int direction;
    private Rectangle collisionRect;
    private float COOLDOWN_TIME = 2f;
    private float tiempoAnim = 0f;
    private float cooldown = 0;
    private int health = 20;
    private boolean isDead = false;

    public Boss(float x, float y, int width, int height) {

        // Inicialitzem els arguments segons la crida del constructor
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);

        // Inicialitzem Human a l'estat normal
        direction = BOSS_IDLE;
        collisionRect = new Rectangle();

    }
    public void act(float delta) {
        if (!isDead) {
            // Movem l'Spacecraft depenent de la direcció controlant que no surti de la pantalla
            switch (direction) {
                case BOSS_UP:
                    this.position.y += Settings.MOB_VELOCITY * delta;
                    break;
                case BOSS_LEFT:
                    this.position.x -= Settings.MOB_VELOCITY * delta;
                    break;
                case BOSS_DOWN:
                    this.position.y -= Settings.MOB_VELOCITY * delta;
                    break;
                case BOSS_RIGHT:
                    this.position.x += Settings.MOB_VELOCITY * delta;
                    break;
                case BOSS_IDLE:
                    break;
                case BOSS_HIT:
                    break;
            }
            collisionRect.set(position.x + 4, position.y, width / 2, height / 2);
            if (cooldown > 0) {
                cooldown -= delta;
            }
        }
        else{
            position.x = 500;
            position.y = 500;
            collisionRect.set(position.x + 4, position.y, width / 2, height / 2);
        }
    }

    public boolean collides(Human human) {

        if (position.x <= human.getX() + human.getWidth()) {
            // Comprovem si han col·lisionat sempre que l'asteroide es trobi a la mateixa alçada que l'spacecraft
            return (Intersector.overlaps(collisionRect, human.getCollisionRect()));
        }
        return false;
    }

    public boolean attackCooldown()
    {
        if(cooldown <= 0)
        {
            cooldown = COOLDOWN_TIME;
            return true;
        }else{
            return false;
        }

    }

    public void reduceHealth(int damage) {
        health = health - damage;
    }

    public void killed(){
        isDead = true;
    }

    // Obtenim el TextureRegion depenent de la posició de l'spacecraft
    public Animation getSkeletonTexture() {

        switch (direction) {

            case BOSS_HIT:
                return AssetManager.aBossHit;
            default:
                return AssetManager.aBossIdle;
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

    // Canviem la direcció de l'Spacecraft: Puja
    public void goUp() {
        direction = BOSS_UP;
    }

    // Canviem la direcció de l'Spacecraft: Dreta
    public void goRight() {
        direction = BOSS_RIGHT;
    }

    // Canviem la direcció de l'Spacecraft: Baixa
    public void goDown() {
        direction = BOSS_DOWN;
    }

    // Canviem la direcció de l'Spacecraft: Esquerra
    public void goLeft() {
        direction = BOSS_LEFT;
    }

    // Posem l'Spacecraft al seu estat original
    public void goStraight() {
        direction = BOSS_IDLE;
    }
    public Rectangle getCollisionRect() {
        return collisionRect;
    }

    public int getHealth() {
        return health;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        tiempoAnim += Gdx.graphics.getDeltaTime(); //es el tiempo que paso desde el ultimo render
        Animation frameDir = getSkeletonTexture();
        frameActual = (TextureRegion) frameDir.getKeyFrame(tiempoAnim,true);
        batch.draw(frameActual,getX(),getY());
    }
}

