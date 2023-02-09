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

public class Skeleton extends Actor {

    public static final int SKELETON_IDLE = Settings.IDLE;
    public static final int SKELETON_UP = Settings.UP;
    public static final int SKELETON_RIGHT = Settings.RIGHT;
    public static final int SKELETON_DOWN = Settings.DOWN;
    public static final int SKELETON_LEFT = Settings.LEFT;

    // Paràmetres de Human
    private Vector2 position;
    private int width, height;
    private int direction;
    private Rectangle collisionRect;
    private float COOLDOWN_TIME = 2f;
    private float tiempoAnim = 0f;
    private float cooldown = 0;


    public Skeleton(float x, float y, int width, int height) {

        // Inicialitzem els arguments segons la crida del constructor
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);

        // Inicialitzem Human a l'estat normal
        direction = SKELETON_IDLE;
        collisionRect = new Rectangle();

    }
    public void act(float delta) {

        // Movem l'Spacecraft depenent de la direcció controlant que no surti de la pantalla
        switch (direction) {
            case SKELETON_UP:
                if (this.position.y + Settings.MOB_VELOCITY * delta <= Settings.GAME_HEIGHT) {
                    this.position.y += Settings.MOB_VELOCITY * delta;
                }
                break;
            case SKELETON_LEFT:
                if (this.position.x - Settings.MOB_VELOCITY * delta <= Settings.GAME_WIDTH) {
                    this.position.x -= Settings.MOB_VELOCITY * delta;
                }
                break;
            case SKELETON_DOWN:
                if (this.position.y - height + Settings.MOB_VELOCITY * delta >= 0) {
                    this.position.y -= Settings.MOB_VELOCITY * delta;
                }
                break;
            case SKELETON_RIGHT:
                if (this.position.x + Settings.MOB_VELOCITY * delta >= 0) {
                    this.position.x += Settings.MOB_VELOCITY * delta;
                }
                break;
            case SKELETON_IDLE:
                break;
        }
        collisionRect.set(position.x+11, position.y + 7, (float) (width/1.5), (float) (height/1.5));
        if(cooldown > 0) {
            cooldown -= delta;
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

    // Obtenim el TextureRegion depenent de la posició de l'spacecraft
    public Animation getSkeletonTexture() {

        switch (direction) {

            case SKELETON_UP:
                return AssetManager.aSkeletonUp;
            case SKELETON_RIGHT:
                return AssetManager.aSkeletonRight;
            case SKELETON_DOWN:
                return AssetManager.aSkeletonDown;
            case SKELETON_LEFT:
                return AssetManager.aSkeletonLeft;
            default:
                return AssetManager.aSkeletonIdle;
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
        direction = SKELETON_UP;
    }

    // Canviem la direcció de l'Spacecraft: Dreta
    public void goRight() {
        direction = SKELETON_RIGHT;
    }

    // Canviem la direcció de l'Spacecraft: Baixa
    public void goDown() {
        direction = SKELETON_DOWN;
    }

    // Canviem la direcció de l'Spacecraft: Esquerra
    public void goLeft() {
        direction = SKELETON_LEFT;
    }

    // Posem l'Spacecraft al seu estat original
    public void goStraight() {
        direction = SKELETON_IDLE;
    }
    public Rectangle getCollisionRect() {
        return collisionRect;
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

