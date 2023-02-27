package com.mygdx.game.objects;

import static com.mygdx.game.helpers.AssetManager.frameActual;
import static com.mygdx.game.objects.Human.getHumanFacing;

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
    private int width, height = 1;
    private int direction;
    public Rectangle boundingBox;
    private boolean outBullet = false;
    private float timer = 2f;


    public Fireball(float x, float y, int lvl) {
        // Inicialitzem els arguments segons la crida del constructor
        if (getHumanFacing() == BULLET_UP || getHumanFacing() == BULLET_DOWN) {
            this.width = Settings.BULLET_WIDTH_Y;
            this.height = Settings.BULLET_HEIGHT_Y;
        }
        else if (getHumanFacing() == BULLET_RIGHT || getHumanFacing() == BULLET_LEFT) {
            this.width = Settings.BULLET_WIDTH_X;
            this.height = Settings.BULLET_HEIGHT_X;
        }
        position = new Vector2(x, y);
        direction = getHumanFacing();
        this.boundingBox =  new Rectangle(position.x+2, position.y + 1, (float) (width/2), (float) (height/2));


    }
    public void act(float delta) {
        if (!outBullet) {
            // Movem l'Spacecraft depenent de la direcció controlant que no surti de la pantalla
            switch (direction) {
                case BULLET_UP:
                    this.position.y += Settings.BULLET_VELOCITY * delta;
                    break;
                case BULLET_RIGHT:
                    this.position.x -= Settings.BULLET_VELOCITY * delta;
                    break;
                case BULLET_DOWN:
                    this.position.y -= Settings.BULLET_VELOCITY * delta;
                    break;
                case BULLET_LEFT:
                    this.position.x += Settings.BULLET_VELOCITY * delta;
                    break;
                case BULLET_IDLE:
                    break;
            }
            boundingBox.set(position.x + 2, position.y + 1, (float) (width / 2), (float) (height / 2));
            if (timer > 0) {
                timer -= delta;
            }
            else {
                fireballOut();
            }
        }
        else {
            boundingBox.set(position.x + 2, position.y + 1, (float) (width / 2), (float) (height / 2));
            position.x = 5000;
            position.y = 5000;
        }
    }


    public boolean collides(Skeleton skeleton) {
        if (getX() <= skeleton.getX() + skeleton.getWidth()) {
            // Comprovem si han col·lisionat sempre que l'asteroide es trobi a la mateixa alçada que l'spacecraft
            return (Intersector.overlaps(boundingBox, skeleton.getCollisionRect()));
        }
        return false;
    }

    public void fireballOut () {
        outBullet = true;
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

