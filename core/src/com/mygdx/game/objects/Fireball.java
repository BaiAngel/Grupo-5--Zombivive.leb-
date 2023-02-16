package com.mygdx.game.objects;

import static com.mygdx.game.helpers.AssetManager.frameActual;
import static com.mygdx.game.objects.Human.humanFacing;

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

public class Fireball extends Actor {

    public static final int BULLET_IDLE = Settings.IDLE;
    public static final int BULLET_UP = Settings.UP;
    public static final int BULLET_RIGHT = Settings.RIGHT;
    public static final int BULLET_DOWN = Settings.DOWN;
    public static final int BULLET_LEFT = Settings.LEFT;

    private float tiempoAnim = 0f;
    // Paràmetres de Bullet
    private Vector2 position;
    private int width, height;
    private int direction;
    public Rectangle boundingBox;


    public Fireball(float x, float y, int width, int height) {
        // Inicialitzem els arguments segons la crida del constructor
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);
        direction = humanFacing;
        this.boundingBox =  new Rectangle(position.x+2, position.y + 1, (float) (width/2), (float) (height/2));


    }
    public void act(float delta) {

        // Movem l'Spacecraft depenent de la direcció controlant que no surti de la pantalla
        switch (direction) {
            case BULLET_UP:
                if (this.position.y + Settings.BULLET_VELOCITY * delta <= Settings.GAME_HEIGHT) {
                    this.position.y += Settings.BULLET_VELOCITY * delta;
                }
                break;
            case BULLET_RIGHT:
                if (this.position.x - Settings.BULLET_VELOCITY * delta <= Settings.GAME_WIDTH) {
                    this.position.x -= Settings.BULLET_VELOCITY * delta;
                }
                break;
            case BULLET_DOWN:
                if (this.position.y - height + Settings.BULLET_VELOCITY * delta >= 0) {
                    this.position.y -= Settings.BULLET_VELOCITY * delta;
                }
                break;
            case BULLET_LEFT:
                if (this.position.x + Settings.BULLET_VELOCITY * delta >= 0) {
                    this.position.x += Settings.BULLET_VELOCITY * delta;
                }
                break;
            case BULLET_IDLE:
                break;
        }
                boundingBox.set(position.x+2, position.y + 1, (float) (width/2), (float) (height/2));
    }


    public boolean collides(Skeleton skeleton) {
        if (getX() <= skeleton.getX() + skeleton.getWidth()) {
            Gdx.app.log("Timer", "namc ");
            // Comprovem si han col·lisionat sempre que l'asteroide es trobi a la mateixa alçada que l'spacecraft
            return (Intersector.overlaps(boundingBox, skeleton.getCollisionRect()));
        }
        return false;
    }

    // Obtenim el TextureRegion depenent de la posició de l'spacecraft
    public Animation getBulletTexture() {

        switch (direction) {

            case BULLET_UP:
                return AssetManager.aFireballUp;
            case BULLET_RIGHT:
                return AssetManager.aFireballLeft;
            case BULLET_DOWN:
                return AssetManager.aFireballDown;
            case BULLET_LEFT:
                return AssetManager.aFireballRight;
            default:
                return AssetManager.aFireballDown;
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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        tiempoAnim += Gdx.graphics.getDeltaTime(); //es el tiempo que paso desde el ultimo render
        Animation frameDir = getBulletTexture();
        frameActual = (TextureRegion) frameDir.getKeyFrame(tiempoAnim,true);
        batch.draw(frameActual,getX(),getY(),boundingBox.width, boundingBox.height);
    }
}

