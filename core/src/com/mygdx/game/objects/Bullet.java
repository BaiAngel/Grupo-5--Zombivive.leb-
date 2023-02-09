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

public class Bullet extends Actor {

    public static final int BULLET_IDLE = Settings.IDLE;
    public static final int BULLET_UP = Settings.UP;
    public static final int BULLET_RIGHT = Settings.RIGHT;
    public static final int BULLET_DOWN = Settings.DOWN;
    public static final int BULLET_LEFT = Settings.LEFT;

    // Paràmetres de Bullet
    private Vector2 position;
    private int width, height;
    private int direction;
    private Rectangle collisionRect;


    public Bullet(float x, float y, int width, int height) {

        // Inicialitzem els arguments segons la crida del constructor
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);
        direction = humanFacing;
        Gdx.app.log("AnimationTime", "Time: "+humanFacing);
        collisionRect = new Rectangle();

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
        collisionRect.set(position.x+11, position.y + 7, (float) (width/1.5), (float) (height/1.5));
    }

    public boolean collides(Skeleton skeleton) {

        if (position.x <= skeleton.getX() + skeleton.getWidth()) {
            // Comprovem si han col·lisionat sempre que l'asteroide es trobi a la mateixa alçada que l'spacecraft
            return (Intersector.overlaps(collisionRect, skeleton.getCollisionRect()));
        }
        return false;
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
        batch.draw(AssetManager.bullet, position.x, position.y, width, height);
    }
}

