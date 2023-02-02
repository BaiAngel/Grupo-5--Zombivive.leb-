package com.mygdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.helpers.AssetManager;
import com.mygdx.game.utils.Settings;

public class Zombie extends Actor {

    public static final int MOB_IDLE = 0;
    public static final int MOB_UP = 1;
    public static final int MOB_RIGHT = 2;
    public static final int MOB_DOWN = 3;
    public static final int MOB_LEFT = 4;

    // Paràmetres de Human
    private Vector2 position;
    private int width, height;
    private int direction;
    private Rectangle collisionRect;

    public Zombie(float x, float y, int width, int height) {

        // Inicialitzem els arguments segons la crida del constructor
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);

        // Inicialitzem Human a l'estat normal
        direction = MOB_IDLE;
        collisionRect = new Rectangle();

    }
    public void act(float delta) {

        // Movem l'Spacecraft depenent de la direcció controlant que no surti de la pantalla
        switch (direction) {
            case MOB_UP:
                if (this.position.y + Settings.MOB_VELOCITY * delta <= Settings.GAME_HEIGHT) {
                    this.position.y += Settings.MOB_VELOCITY * delta;
                }
                break;
            case MOB_LEFT:
                if (this.position.x - Settings.MOB_VELOCITY * delta <= Settings.GAME_WIDTH) {
                    this.position.x -= Settings.MOB_VELOCITY * delta;
                }
                break;
            case MOB_DOWN:
                if (this.position.y - height + Settings.MOB_VELOCITY * delta >= 0) {
                    this.position.y -= Settings.MOB_VELOCITY * delta;
                }
                break;
            case MOB_RIGHT:
                if (this.position.x + Settings.MOB_VELOCITY * delta >= 0) {
                    this.position.x += Settings.MOB_VELOCITY * delta;
                }
                break;
            case MOB_IDLE:
                break;
        }
        collisionRect.set(position.x+11, position.y + 7, (float) (width/1.5), (float) (height/1.5));

    }

    public boolean collides(Human human) {

        if (position.x <= human.getX() + human.getWidth()) {
            // Comprovem si han col·lisionat sempre que l'asteroide es trobi a la mateixa alçada que l'spacecraft
            return (Intersector.overlaps(collisionRect, human.getCollisionRect()));
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

    // Canviem la direcció de l'Spacecraft: Puja
    public void goUp() {
        direction = MOB_UP;
    }

    // Canviem la direcció de l'Spacecraft: Dreta
    public void goRight() {
        direction = MOB_RIGHT;
    }

    // Canviem la direcció de l'Spacecraft: Baixa
    public void goDown() {
        direction = MOB_DOWN;
    }

    // Canviem la direcció de l'Spacecraft: Esquerra
    public void goLeft() {
        direction = MOB_LEFT;
    }

    // Posem l'Spacecraft al seu estat original
    public void goStraight() {
        direction = MOB_IDLE;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(AssetManager.human, position.x, position.y, width, height);
    }
}

