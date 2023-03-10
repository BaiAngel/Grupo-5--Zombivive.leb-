package com.mygdx.game.objects;

import static com.mygdx.game.helpers.AssetManager.frameActual;
import static com.mygdx.game.screens.GameScreen.mapZone;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.helpers.AssetManager;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.utils.Methods;
import com.mygdx.game.utils.Settings;

import java.util.ListIterator;

public class Human extends Actor {
    // Diferents posicions de Human: recta, pujant i baixant
    public static final int HUMAN_IDLE = Settings.IDLE;
    public static final int HUMAN_UP = Settings.UP;
    public static final int HUMAN_RIGHT = Settings.RIGHT;
    public static final int HUMAN_DOWN = Settings.DOWN;
    public static final int HUMAN_LEFT = Settings.LEFT;

    // Paràmetres de Human
    private Vector2 position;
    private int width, height;
    private int direction;
    private Rectangle boundingBox, limitUp, limitDown, limitRight, limitLeft;
    private float tiempoAnim = 0f;
    private static int humanFacing = Settings.IDLE;
    private static int MAX_HEALTH = 100;
    private static int health = MAX_HEALTH;
    private static int regeneration = 1;
    private float timer = 0;
    public static int lvl = 1;
    private float TIMER_VELOCITY = 2f;
    private float centreHumanX, centreHumanY;



    public Human(float x, float y, int width, int height) {

        // Inicialitzem els arguments segons la crida del constructor
        this.width = width;
        this.height = height;
        position = new Vector2(x, y);

        // Inicialitzem Human a l'estat normal
        direction = HUMAN_IDLE;
        // Creem el rectangle de col·lisions
        boundingBox = new Rectangle(position.x+4, position.y, width/2, height/2);
        //up
        limitUp = new Rectangle((float) (getX()+5.5), getY() + getHeight()/2, (float) (getWidth()/3), 1);
        //down
        limitDown = new Rectangle((float) (getX()+5.5), getY(), (float) (getWidth()/3), 1);
        //left
        limitLeft = new Rectangle(getX()+1, getY()+5, 1, getHeight()/5);
        //right
        limitRight = new Rectangle(getX()+ getHeight()/2-1, getY()+5, 1, getHeight()/5);
        centreHumanX = x+5;
        centreHumanY = y+5;

    }
    public void act(float delta) {
        Settings.FIREBALL_SPAWN_TIMER += delta;
        checkMovementColision(delta);
        boundingBox.set(position.x+4, position.y, width/2, height/2);
        if(timer > 0) {
            timer -= delta;
        }
        crearLimitHuman();
        movimentHitbox();
        regenerarVida(regeneration);
    }

    private void movimentHitbox() {
        //up
        limitUp = new Rectangle((float) (getX()+5.5), getY() + getHeight()/2, (float) (getWidth()/3), 1);
        //down
        limitDown = new Rectangle((float) (getX()+5.5), getY(), (float) (getWidth()/3), 1);
        //left
        limitLeft = new Rectangle(getX()+1, getY()+5, 1, getHeight()/5);
        //right
        limitRight = new Rectangle(getX()+ getHeight()/2-1, getY()+5, 1, getHeight()/5);
    }

    private void checkMovementColision(float delta) {
        if (GameScreen.mapZone != null) {
            // Movem l'Spacecraft depenent de la direcció controlant que no surti de la pantalla
            boolean colisionUp = false;
            boolean colisionDown = false;
            boolean colisionRight = false;
            boolean colisionLeft = false;

            if (!Methods.getColision(getLimitUp(), GameScreen.mapZone)) {
                colisionUp = true;
            }
            if (!Methods.getColision(getLimitDown(), GameScreen.mapZone)) {
                colisionDown = true;
            }
            if (!Methods.getColision(getLimitLeft(), GameScreen.mapZone)) {
                colisionRight = true;
            }
            if (!Methods.getColision(getLimitRight(), GameScreen.mapZone)) {
               colisionLeft = true;
            }

            ListIterator<Rectangle> mapColisionIterator = GameScreen.mapColision.listIterator();
            while (mapColisionIterator.hasNext()) {
                Rectangle colision = mapColisionIterator.next();

                Rectangle traduccionTemporalColision = new Rectangle(colision.x, colision.y, colision.width, colision.height);

                if (Methods.getColision(getLimitUp(), traduccionTemporalColision)) {
                    colisionUp = true;
                }
                if (Methods.getColision(getLimitDown(), traduccionTemporalColision)) {
                    colisionDown = true;
                }
                if (Methods.getColision(getLimitLeft(), traduccionTemporalColision)) {
                    colisionRight = true;
                }
                if (Methods.getColision(getLimitRight(), traduccionTemporalColision)) {
                    colisionLeft = true;
                }
            }

            //Gdx.app.log("app", "Up "+colisionUp+" Down "+colisionDown+" Left "+colisionRight+" Right "+colisionLeft);
            switch (direction) {
                case HUMAN_UP:
                    if (!colisionUp) {
                        humanFacing = Settings.UP;
                        this.position.y += Settings.HUMAN_VELOCITY * delta;
                        this.centreHumanY += Settings.HUMAN_VELOCITY * delta;
                    }
                    break;
                case HUMAN_RIGHT:
                    if (!colisionRight) {
                        humanFacing = Settings.RIGHT;
                        this.position.x -= Settings.HUMAN_VELOCITY * delta;
                        this.centreHumanX -= Settings.HUMAN_VELOCITY * delta;
                    }
                    break;
                case HUMAN_DOWN:
                    if (!colisionDown) {
                        humanFacing = Settings.DOWN;
                        this.position.y -= Settings.HUMAN_VELOCITY * delta;
                        this.centreHumanY -= Settings.HUMAN_VELOCITY * delta;
                    }
                    break;
                case HUMAN_LEFT:
                    if (!colisionLeft) {
                        humanFacing = Settings.LEFT;
                        this.position.x += Settings.HUMAN_VELOCITY * delta;
                        this.centreHumanX += Settings.HUMAN_VELOCITY * delta;
                    }
                    break;
                case HUMAN_IDLE:
                    break;
            }
        }
    }

    private void crearLimitHuman() {
        //up
        limitUp.set(getX()+1, getY() + getHeight()/2, getWidth()-1, 1);
        //down
        limitDown.set(getX()+1, getY(), getWidth()-1, 1);
        //left
        limitLeft.set(getX()+1, getY(), 1, getHeight()/2);
        //right
        limitRight.set(getX()+ getHeight()/2-1, getY(), 1, getHeight()/2);

    }

    private void regenerarVida(int regeneration) {
        if (health < MAX_HEALTH && timer <= 0) {
            health = health + regeneration;
            timer = TIMER_VELOCITY;
        }
    }


    public void getHit(int damage) {

        health = health-damage;

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
                switch (humanFacing) {
                    case Settings.UP:
                        return AssetManager.aHumanIdleUp;
                    case Settings.DOWN:
                        return AssetManager.aHumanIdleDown;
                    case Settings.LEFT:
                        return AssetManager.aHumanIdleRight;
                    case Settings.RIGHT:
                        return AssetManager.aHumanIdleLeft;
                }
                return AssetManager.aHumanIdleDown;
        }
    }

    public Rectangle getLimitLeft() {
        return limitLeft;
    }

    public Rectangle getLimitDown() {
        return limitDown;
    }

    public Rectangle getLimitRight() {
        return limitRight;
    }

    public Rectangle getLimitUp() {
        return limitUp;
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
        return boundingBox;
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

    public static int getHealth() {
        return health;
    }

    public static void setHealth(int vida) {
        health = vida;
    }

    public static void resetLvl() {
        lvl = 1;
    }

    public static int getMaxHealth() {
        return MAX_HEALTH;
    }

    public float getCentreX() {
        return centreHumanX;
    }

    public float getCentreY() {
        return centreHumanY;
    }

    public int getLvl() {
        return lvl;
    }


    public static int getHumanFacing() {
        return humanFacing;
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
