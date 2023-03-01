package com.mygdx.game.objects;

import static com.mygdx.game.helpers.AssetManager.frameActual;
import static com.mygdx.game.objects.Human.getHumanFacing;
import static com.mygdx.game.objects.Human.lvl;

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
    private float timer = 0.5f;
    private int bulletHealth = 1;

    public Fireball(float x, float y, int lvl) {
        position = new Vector2(x, y);
        if (lvl == 1) {
            direction = getHumanFacing();
        }
        if (lvl == 2) {
            direction = getBulletDirection(2, getHumanFacing());
        }
        if (lvl == 3) {
            direction = getBulletDirection(1, getHumanFacing());
        }
        if (lvl == 4) {
            direction = getBulletDirection(3, getHumanFacing());
        }
        if (Human.lvl == 5) {
            bulletHealth = 5;
        }
        if (Human.lvl >= 2) {
            timer = 2f;
        }
        // Inicialitzem els arguments segons la crida del constructor
        if (direction == BULLET_UP || direction == BULLET_DOWN) {
            this.width = Settings.BULLET_WIDTH_Y;
            this.height = Settings.BULLET_HEIGHT_Y;
        }
        else if (direction == BULLET_RIGHT || direction == BULLET_LEFT) {
            this.width = Settings.BULLET_WIDTH_X;
            this.height = Settings.BULLET_HEIGHT_X;
        }
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
            if (bulletHealth == 0) {
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

    public void reduceBulletHealth () {
        bulletHealth--;
    }

    // Obtenim el TextureRegion depenent de la posició de l'spacecraft
    public Animation getBulletTexture() {
        if (lvl < 5) {
            return getBulletTextureRed();
        }
        else {
            return getBulletTextureBlue();
        }

    }

    private Animation getBulletTextureBlue() {
        switch (direction) {

            case BULLET_UP:
                return AssetManager.aFireballBUp;
            case BULLET_RIGHT:
                return AssetManager.aFireballBLeft;
            case BULLET_DOWN:
                return AssetManager.aFireballBDown;
            case BULLET_LEFT:
                return AssetManager.aFireballBRight;
            default:
                return AssetManager.aFireballBDown;
        }
    }

    private Animation getBulletTextureRed() {
        switch (direction) {

            case BULLET_UP:
                return AssetManager.aFireballRUp;
            case BULLET_RIGHT:
                return AssetManager.aFireballRLeft;
            case BULLET_DOWN:
                return AssetManager.aFireballRDown;
            case BULLET_LEFT:
                return AssetManager.aFireballRRight;
            default:
                return AssetManager.aFireballRDown;
        }
    }

    public int getBulletDirection (int site, int dir) {
        //site 0 mateix
        //site 1 seguent
        //site 2 reverse
        //site 3 penultim
        for (int i = 0; i < site; i++) {
            if (dir == 4)
                dir = 0;
            dir++;
        }
        return dir;
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
        if (frameDir != null)
        frameActual = (TextureRegion) frameDir.getKeyFrame(tiempoAnim,true);
        batch.draw(frameActual,getX(),getY(),boundingBox.width, boundingBox.height);
    }
}

