package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.ColorAction;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.helpers.AssetManager;
import com.mygdx.game.helpers.InputHandler;
import com.mygdx.game.objects.Background;
import com.mygdx.game.objects.Bullet;
import com.mygdx.game.objects.Human;
import com.mygdx.game.objects.Skeleton;
import com.mygdx.game.scenes.Hud;
import com.mygdx.game.utils.Settings;

import java.util.LinkedList;
import java.util.ListIterator;

public class GameScreen implements Screen {
        // Per controlar el gameover
        Boolean gameOver = false;

        private final Texture red, black;
        private LinkedList<Skeleton> skeletonList;
        private LinkedList<Bullet> bulletList;
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
        private int timeBetweenBulletSpawns = 10;
        private int bulletSpawnTimer = 0;
        private Hud hud;
        private SpriteBatch spriteBatch;
        private float width, totalBarWidth, currentHealth, totalHealth;
        private NinePatch health, backgroundHealth;

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
                skeletonList = new LinkedList<>();
                bulletList = new LinkedList<>();

                // Creem el fons
                background = new Background(0,0, Settings.GAME_WIDTH, Settings.GAME_HEIGHT);

                // Afegim els actors a l'stage
                stage.addActor(background);
                stage.addActor(human);
                // Donem nom a l'Actor
                human.setName("human");
                //Health
                totalHealth = human.getMaxHealth();
                currentHealth = human.getHealth();
                totalBarWidth=31;
                red = new Texture(Gdx.files.internal("fons/red.png"));
                black = new Texture(Gdx.files.internal("fons/black.png"));
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
                stage.draw();
                drawElements();
                drawHud(delta);
                if (!gameOver) {
                        camera.position.set(human.getX(), human.getY(), 0);
                        camera.update();
                        stage.act(delta);
                        updateGame();
                        calcularGameOver();
                }else {
                        batch.begin();
                        // Si hi ha hagut col·lisió: reproduïm l'explosió
                        BitmapFont font = new BitmapFont(false);
                        font.draw(batch, "GameOver", Settings.GAME_WIDTH/3, Settings.GAME_HEIGHT/3);
                        batch.end();
                }

        }

        private void calcularGameOver() {
                if (hud.isTimeUp()) {
                       gameOver = true;
                }
                else if (human.getHealth() <= 0) {
                        gameOver = true;
                }
        }

        @SuppressWarnings("SuspiciousIndentation")
        private void updateGame() {
                spawnSkeleton();
                spawnBullet();
                if (skeletonList != null)
                updateSkeleton();
                if (bulletList != null)
                updateBullet();
        }

        private void drawHud(float delta) {
                //Set our batch to now draw what the Hud camera sees.
                batch.setProjectionMatrix(hud.stage.getCamera().combined);
                hud.stage.draw();
                hud.act(delta);
                //drawVida
                batch.begin();
                health = new NinePatch(red, 0, 0, 0, 0);
                backgroundHealth = new NinePatch(black, 0, 0, 0, 0);
                currentHealth = human.getHealth();
                width = currentHealth / totalHealth * totalBarWidth;
                backgroundHealth.draw(batch, Settings.GAME_WIDTH/2, Settings.GAME_HEIGHT/2+35, totalBarWidth,10);
                health.draw(batch, Settings.GAME_WIDTH/2, Settings.GAME_HEIGHT/2+35, width,10);
                batch.end();
        }


        private void updateSkeleton() {
                ListIterator<Skeleton> skeletonListIterator = skeletonList.listIterator();
                while (skeletonListIterator.hasNext()) {
                        Skeleton skeleton = skeletonListIterator.next();
                        checkColision(skeleton);
                        checkMovement(skeleton);
                }
        }

        private void updateBullet() {
                ListIterator<Bullet> bulletListIterator = bulletList.listIterator();
                while (bulletListIterator.hasNext()) {
                        Bullet bullet = bulletListIterator.next();
                        checkColision(bullet);
                }
        }

        private void spawnSkeleton() {
                enemySpawnTimer = enemySpawnTimer + 1;
                //Gdx.app.log("Timer", "SpawnTimer: " + enemySpawnTimer);

                if (enemySpawnTimer > timeBetweenEnemySpawns){
                        skeletonList.add(
                                new Skeleton(Settings.MOB_STARTX, Settings.MOB_STARTY, Settings.MOB_WIDTH, Settings.MOB_HEIGHT)

                        );
                        ListIterator<Skeleton> skeletonListIterator = skeletonList.listIterator();
                        while (skeletonListIterator.hasNext()) {
                                Skeleton skeleton = skeletonListIterator.next();
                                stage.addActor(skeleton);
                        }
                        enemySpawnTimer = 0;
                }
        }

        private void spawnBullet() {
                bulletSpawnTimer = bulletSpawnTimer + 1;
                //Gdx.app.log("Timer", "SpawnTimer: " + bulletSpawnTimer);

                if (bulletSpawnTimer > timeBetweenBulletSpawns){
                        bulletList.add(
                                new Bullet(human.getCentreX(), human.getCentreY(), Settings.BULLET_WIDTH, Settings.BULLET_HEIGHT)

                        );
                        ListIterator<Bullet> bulletListIterator = bulletList.listIterator();
                        while (bulletListIterator.hasNext()) {
                                Bullet bullet = bulletListIterator.next();
                                stage.addActor(bullet);
                        }
                        bulletSpawnTimer = 0;
                }
        }

        private void checkColision(Skeleton mob) {
                if (mob.collides(human)) {
                        // La nau explota i desapareix
                        if (mob.attackCooldown() == true) {
                                Gdx.app.log("App", "Ñam");
                                human.getHit(10);
                                AssetManager.hitSound.play();
                        }
                }
        }

        private void checkColision(Bullet bullet) {
                ListIterator<Skeleton> skeletonListIterator = skeletonList.listIterator();
                while (skeletonListIterator.hasNext()) {
                        Skeleton skeleton = skeletonListIterator.next();
                        if (bullet.collides(skeleton)) {
                                skeleton.remove();
                                skeletonList.remove(skeleton);
                                hud.addScore(1);
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
                shapeRenderer.rect(human.getX()+4, human.getY(), human.getWidth()/2, human.getHeight()/2);
                shapeRenderer.setColor(new Color(1, 0, 0, 1));
                ListIterator<Skeleton> skeletonListIterator = skeletonList.listIterator();
                while (skeletonListIterator.hasNext()) {
                        Skeleton skeleton = skeletonListIterator.next();
                        shapeRenderer.rect(skeleton.getX()+4, skeleton.getY(), (float) (skeleton.getWidth()/2), (float) (skeleton.getHeight()/2));
                }
                shapeRenderer.setColor(new Color(1, 1, 0, 1));
                ListIterator<Bullet> bulletListIterator = bulletList.listIterator();
                while (bulletListIterator.hasNext()) {
                        Bullet bullet = bulletListIterator.next();
                        shapeRenderer.rect(bullet.getX()+2, bullet.getY()+1, (float) (bullet.getWidth()/2), (float) (bullet.getHeight()/2));
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
