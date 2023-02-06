package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.helpers.AssetManager;
import com.mygdx.game.helpers.InputHandler;
import com.mygdx.game.objects.Background;
import com.mygdx.game.objects.Human;
import com.mygdx.game.objects.Skeleton;
import com.mygdx.game.scenes.Hud;
import com.mygdx.game.utils.Settings;

import java.util.LinkedList;
import java.util.ListIterator;

public class GameScreen implements Screen {

        private LinkedList<Skeleton> zombieList;
        private Stage stage;
        private Human human;
        private OrthographicCamera camera;
        private Background background;
        // Representació de figures geomètriques
        private ShapeRenderer shapeRenderer;
        // Per obtenir el batch de l'stage
        private Batch batch;
        private int timeBetweenEnemySpawns = 500;
        private int enemySpawnTimer = 0;
        private Hud hud;
        private SpriteBatch spriteBatch;




        public GameScreen() {

                // Iniciem la música
                AssetManager.music.play();

                // Creem el ShapeRenderer
                shapeRenderer = new ShapeRenderer();
                spriteBatch = new SpriteBatch();

                // Creem la càmera de les dimensions del joc
                camera = new OrthographicCamera(Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
                // Posant el paràmetre a true configurem la càmera perquè
                // faci servir el sistema de coordenades Y-Down
                camera.setToOrtho(false);


                // Creem el viewport amb les mateixes dimensions que la càmera
                StretchViewport viewport = new StretchViewport(Settings.GAME_WIDTH/2, Settings.GAME_HEIGHT/2 , camera);

                // Creem l'stage i assignem el viewport
                stage = new Stage(viewport);

                batch = stage.getBatch();
                //create our game HUD for scores/timers/level info
                hud = new Hud(spriteBatch);
                // Creem la persona
                human = new Human(Settings.HUMAN_STARTX, Settings.HUMAN_STARTY, Settings.HUMAN_WIDTH, Settings.HUMAN_HEIGHT);
                zombieList = new LinkedList<>();

                // Creem el fons
                background = new Background(0,0, Settings.GAME_WIDTH, Settings.GAME_HEIGHT);

                // Afegim els actors a l'stage
                stage.addActor(background);
                stage.addActor(human);
                ListIterator<Skeleton> zombieListIterator = zombieList.listIterator();
                while (zombieListIterator.hasNext()) {
                        Skeleton zombie = zombieListIterator.next();
                        stage.addActor(zombie);
                }
                // Donem nom a l'Actor
                human.setName("human");
                // Assignem com a gestor d'entrada la classe InputHandler
                Gdx.input.setInputProcessor(new InputHandler(this));
        }

        @Override
        public void show() {

        }

        @Override
        public void render(float delta) {
                Gdx.gl.glClearColor( 0, 0, 0.5f, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                // Dibuixem i actualitzem tots els actors de l'stage
                batch.setProjectionMatrix(hud.stage.getCamera().combined);
                stage.draw();
                camera.position.set(human.getX(), human.getY(), 0);
                camera.update();
                stage.act(delta);
                spawnZombies();
                updateZombie();
                drawElements();
        }



        private void updateZombie() {
                ListIterator<Skeleton> zombieListIterator = zombieList.listIterator();
                while (zombieListIterator.hasNext()) {
                        Skeleton zombie = zombieListIterator.next();
                        checkColision(zombie);
                        checkMovement(zombie);
                }
        }

        private void spawnZombies() {
                enemySpawnTimer = enemySpawnTimer + 1;
                //Gdx.app.log("Timer", "SpawnTimer: " + enemySpawnTimer);

                if (enemySpawnTimer > timeBetweenEnemySpawns){
                        zombieList.add(
                                new Skeleton(Settings.MOB_STARTX, Settings.MOB_STARTY, Settings.MOB_WIDTH, Settings.MOB_HEIGHT)

                        );
                        ListIterator<Skeleton> zombieListIterator = zombieList.listIterator();
                        while (zombieListIterator.hasNext()) {
                                Skeleton zombie = zombieListIterator.next();
                                stage.addActor(zombie);
                        }
                        enemySpawnTimer = 0;
                }
        }

        private void checkColision(Skeleton mob) {
                if (mob.collides(human)) {
                        // La nau explota i desapareix
                        if (mob.attackCooldown() == true) {
                                Gdx.app.log("App", "Ñam");
                                AssetManager.hitSound.play();
                        }
                }
        }

        private void checkMovement(Skeleton mob) {
                float resy = human.getY() - mob.getY();
                float resx = human.getX() - mob.getX();
                if (Math.abs(resx) > Math.abs(resy)) {
                        trakingX(mob);
                }
                else if((Math.abs(resx) < Math.abs(resy))){
                        trakingY(mob);
                }
                else{
                        mob.goStraight();
                }
        }

        private void trakingY(Skeleton mob) {
                float resy = human.getY() - mob.getY();
                if (resy<0){
                        mob.goDown();
                }else if (resy > 0){
                        mob.goUp();
                }
                else {
                        mob.goStraight();
                }
        }

        private void trakingX(Skeleton mob) {
                float resx = human.getX() - mob.getX();
                if (resx<0){
                        mob.goLeft();
                } else if (resx > 0){
                        mob.goRight();
                }
                else {
                        mob.goStraight();
                }
        }

        private void drawElements(){

                /* 1 */
                // Pintem el fons de negre per evitar el "flickering"
                //Gdx.gl20.glClearColor(1.0f, 0.0f, 0.0f, 0.5f);
                //Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

                /* 2 */
                // Recollim les propietats del Batch de l'Stage
                shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
                // Inicialitzem el shaperenderer
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

                /* 3 */
                // Definim el color (verd)
                shapeRenderer.setColor(new Color(0, 1, 0, 1));

                // Pintem la nau
                shapeRenderer.rect(human.getX()+4, human.getY()+4, human.getWidth()/2, human.getHeight()/2);
                shapeRenderer.setColor(new Color(1, 0, 0, 1));
                ListIterator<Skeleton> zombieListIterator = zombieList.listIterator();
                while (zombieListIterator.hasNext()) {
                        Skeleton zombie = zombieListIterator.next();
                        shapeRenderer.rect(zombie.getX()+4, zombie.getY()+4, (float) (zombie.getWidth()/2), (float) (zombie.getHeight()/2));
                }
                /* 4 */
                shapeRenderer.end();
        }

        @Override
        public void resize(int width, int height) {

        }

        @Override
        public void pause() {

        }

        @Override
        public void resume() {

        }

        @Override
        public void hide() {

        }

        @Override
        public void dispose() {

        }

        public Stage getStage() {
                return stage;
        }
        public Human getHuman() {
                return human;
        }
}
