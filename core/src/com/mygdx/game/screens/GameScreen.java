package com.mygdx.game.screens;

import static com.mygdx.game.helpers.AssetManager.getJson;

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
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.game.Zombivive;
import com.mygdx.game.helpers.AssetManager;
import com.mygdx.game.helpers.InputHandler;
import com.mygdx.game.objects.Fireball;
import com.mygdx.game.objects.Human;
import com.mygdx.game.objects.Skeleton;
import com.mygdx.game.scenes.Hud;
import com.mygdx.game.utils.Methods;
import com.mygdx.game.utils.Settings;

import java.util.LinkedList;
import java.util.ListIterator;

public class GameScreen implements Screen {
        // Per controlar el gameover
        Boolean gameOver = false;
        Zombivive game;
        private final Texture red, black;
        private LinkedList<Skeleton> skeletonList;
        private LinkedList<Fireball> bulletList;
        private Stage stage;
        private Human human;
        private OrthographicCamera camera;
        // Representació de figures geomètriques
        private ShapeRenderer shapeRenderer;
        // Per obtenir el batch de l'stage
        private Batch batch;
        private int timeBetweenEnemySpawns = 50;
        private int enemySpawnTimer = 0;
        private int timeBetweenBulletSpawns = 50;
        private int bulletSpawnTimer = 0;
        private Hud hud;
        private SpriteBatch spriteBatch;
        private float width, totalBarWidth, currentHealth, totalHealth;
        private NinePatch health, backgroundHealth;
        //Mapa
        public static LinkedList<Rectangle> mapColision;
        private TiledMap map;
        private String path;
        public static Rectangle mapZone;
        private OrthogonalTiledMapRenderer renderer;
        private int MapProperties, mapWidth, mapHeight, tilePixelWidth, tilePixelHeight, mapPixelWidth, mapPixelHeight;
        private int selectMap = 0;
        private final int MAP_FOREST = 0;
        private final int MAP_DESERT = 1;

        public GameScreen(Zombivive game) {
                this.game = game;
                // Iniciem la música
                AssetManager.music.play();

                // Creem el ShapeRenderer
                shapeRenderer = new ShapeRenderer();
                spriteBatch = new SpriteBatch();

                // Creem la càmera de les dimensions del joc
                camera = new OrthographicCamera(Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
                Gdx.app.log("Dimen", "game width" + Settings.GAME_WIDTH + "Game height " + Settings.GAME_HEIGHT);
                // Posant el paràmetre a true configurem la càmera perquè
                // faci servir el sistema de coordenades Y-Down
                camera.setToOrtho(false);


                // Creem el viewport amb les mateixes dimensions que la càmera
                StretchViewport viewport = new StretchViewport(Settings.GAME_WIDTH / 2, Settings.GAME_HEIGHT / 2, camera);

                // Creem l'stage i assignem el viewport
                stage = new Stage(viewport);

                batch = stage.getBatch();
                //create our game HUD for scores/timers/level info
                hud = new Hud(spriteBatch);
                // Creem la persona
                human = new Human(Settings.HUMAN_STARTX, Settings.HUMAN_STARTY, Settings.HUMAN_WIDTH, Settings.HUMAN_HEIGHT);
                skeletonList = new LinkedList<>();
                bulletList = new LinkedList<>();
                mapColision = new LinkedList<>();
                // Afegim els actors a l'stage
                stage.addActor(human);
                // Donem nom a l'Actor
                human.setName("human");
                //Health
                totalHealth = human.getMaxHealth();
                currentHealth = human.getHealth();
                totalBarWidth = 31;
                red = new Texture(Gdx.files.internal("fons/red.png"));
                black = new Texture(Gdx.files.internal("fons/black.png"));
                // Assignem com a gestor d'entrada la classe InputHandler
                Gdx.input.setInputProcessor(new InputHandler(this));
        }

        @Override
        public void show() {
                switch (selectMap) {
                        case MAP_FOREST:
                                map = AssetManager.crearMapForestTmx();
                                path = "maps/mapForest/forest.json";
                                break;
                        case MAP_DESERT:
                                map = AssetManager.crearMapDesertTmx();
                                path = "maps/mapDesert/desert.json";
                                break;
                }
                renderer = new OrthogonalTiledMapRenderer(map);
                crearMapProperties();
                //Try capes
                //JSON
                JsonValue base = getJson(path);
                for (JsonValue layers : base.get("layers"))
                {
                        System.out.println(layers.getString("name"));
                        if (layers.getString("name").equals("Hitbox entorno")) {
                                for (int c = 0; c < layers.get("objects").size; c++) {
                                        mapColision.add(new Rectangle(
                                                layers.get("objects").get(c).getFloat("x"), layers.get("objects").get(c).getFloat("y"), layers.get("objects").get(c).getFloat("width"), layers.get("objects").get(c).getFloat("height")
                                        ));
                                }
                        }
                }
        }

        @Override
        public void render(float delta) {
                Gdx.gl.glClearColor((float) (129/255.0), (float) (185/255.0), (float) (11/255.0), 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
                renderer.setView(camera);
                renderer.render();
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
                        game.setScreen(new GameOverScreen(game));
                        batch.begin();
                        // Si hi ha hagut col·lisió: reproduïm l'explosió
                        BitmapFont font = new BitmapFont(false);
                        Human.setHealth(100);
                        font.draw(batch, "GameOver", Settings.GAME_WIDTH/3, Settings.GAME_HEIGHT/3);
                        batch.end();
                }

        }

        private void crearMapProperties() {
                //Mapdata
                MapProperties prop = map.getProperties();
                mapWidth = prop.get("width", Integer.class);
                mapHeight = prop.get("height", Integer.class);
                tilePixelWidth = prop.get("tilewidth", Integer.class);
                tilePixelHeight = prop.get("tileheight", Integer.class);
                mapPixelWidth = mapWidth * tilePixelWidth;
                mapPixelHeight = mapHeight * tilePixelHeight;
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
                mapZone = new Rectangle(20, 20, mapPixelWidth-40,mapPixelHeight-40);
                Methods.getColision(mapZone,human.getCollisionRect());
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
                ListIterator<Fireball> bulletListIterator = bulletList.listIterator();
                while (bulletListIterator.hasNext()) {
                        Fireball bullet = bulletListIterator.next();
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


                if (bulletSpawnTimer > timeBetweenBulletSpawns){
                        switch (human.getLvl()) {
                                case (1):
                                        bulletList.add(
                                                new Fireball(human.getCentreX(), human.getCentreY(), human.getLvl())

                                        );
                                        ListIterator<Fireball> bulletListIterator = bulletList.listIterator();
                                        while (bulletListIterator.hasNext()) {
                                                Fireball bullet = bulletListIterator.next();
                                                stage.addActor(bullet);
                                        }
                                        bulletSpawnTimer = 0;
                                        break;
                        }
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

        private void checkColision(Fireball bullet) {
                ListIterator<Skeleton> skeletonListIterator = skeletonList.listIterator();
                while (skeletonListIterator.hasNext()) {
                        Skeleton skeleton = skeletonListIterator.next();
                        if (bullet.collides(skeleton)) {
                                skeleton.killed();
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
                /*
                Gdx.gl20.glClearColor(1.0f, 0.0f, 0.0f, 0.5f);
                Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
*/
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
                ListIterator<Fireball> bulletListIterator = bulletList.listIterator();
                while (bulletListIterator.hasNext()) {
                        Fireball bullet = bulletListIterator.next();
                        shapeRenderer.rect(bullet.getX()+2, bullet.getY()+1, (float) (bullet.getWidth()/2), (float) (bullet.getHeight()/2));
                }
                shapeRenderer.setColor(new Color(1, 1, 1, 1));
                shapeRenderer.rect(20, 20, mapPixelWidth-40,mapPixelHeight-40);
                /* 4 */
                shapeRenderer.end();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(new Color(1, 1, 1, 1));
                //up
                shapeRenderer.rect((float) (human.getX()+5.5), human.getY() + human.getHeight()/2, (float) (human.getWidth()/3), 1);
                //down
                shapeRenderer.rect((float) (human.getX()+5.5), human.getY(), (float) (human.getWidth()/3), 1);
                //left
                shapeRenderer.rect(human.getX()+1, human.getY()+5, 1, human.getHeight()/5);
                //right
                shapeRenderer.rect(human.getX()+ human.getHeight()/2-1, human.getY()+5, 1, human.getHeight()/5);
                //up
                Rectangle limitUp = new Rectangle((float) (human.getX()+5.5), human.getY() + human.getHeight()/2, (float) (human.getWidth()/3), 1);
                //down
                Rectangle limitDown = new Rectangle((float) (human.getX()+5.5), human.getY(), (float) (human.getWidth()/3), 1);
                //left
                Rectangle limitLeft = new Rectangle(human.getX()+1, human.getY()+5, 1, human.getHeight()/5);
                //right
                Rectangle limitRight = new Rectangle(human.getX()+ human.getHeight()/2-1, human.getY()+5, 1, human.getHeight()/5);

                shapeRenderer.end();
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                shapeRenderer.setColor(new Color(1, 0, 0, 1));
                ListIterator<Rectangle> mapColisionIterator = mapColision.listIterator();
                while (mapColisionIterator.hasNext()) {
                        Rectangle colision = mapColisionIterator.next();
                        shapeRenderer.rect(colision.getX(), colision.getY(), colision.getWidth(), colision.getHeight());
                }
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
                dispose();
        }

        @Override
        public void dispose() {
                map.dispose();
        }

        public Stage getStage() {
                return stage;
        }
        public Human getHuman() {
                return human;
        }
}
