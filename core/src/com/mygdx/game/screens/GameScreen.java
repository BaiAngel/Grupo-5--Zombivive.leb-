package com.mygdx.game.screens;

import static com.mygdx.game.helpers.AssetManager.getJson;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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
import com.mygdx.game.objects.Boss;
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
        private final Texture red, black, purple;
        private LinkedList<Skeleton> skeletonList;
        private LinkedList<Boss> bossList;
        private LinkedList<Fireball> bulletList;
        private Stage stage;
        private Human human;
        private OrthographicCamera camera;
        // Representació de figures geomètriques
        private ShapeRenderer shapeRenderer;
        // Per obtenir el batch de l'stage
        private Batch batch;
        private int timeBetweenEnemySpawns = 50;//50
        private int timeBetweenBossSpawns = 2500;//2500
        private int enemySpawnTimer = 0;
        private int bossSpawnTimer = 0;
        private int timeBetweenBulletSpawns = 50;
        private int bulletSpawnTimer = 0;
        private Hud hud;
        private SpriteBatch spriteBatch;
        private float width, totalBarWidth, totalBossBarWidth, currentHealth, totalHealth;
        private NinePatch health, backgroundHealth, bossHealth;
        //Mapa
        public static LinkedList<Rectangle> mapColision;
        public static LinkedList<Rectangle> mapSpawns;
        private TiledMap map;
        private String path;
        public static Rectangle mapZone;
        private OrthogonalTiledMapRenderer renderer;
        private int MapProperties, mapWidth, mapHeight, tilePixelWidth, tilePixelHeight, mapPixelWidth, mapPixelHeight;
        private final int MAP_FOREST = 0;
        private final int MAP_DESERT = 1;
        int numBullets = 1;
        int attackDamage = 10;
        int attackBossDamage = 35;

        public GameScreen(Zombivive game) {
                this.game = game;
                // Iniciem la música
                AssetManager.musicatac.play();
                // Creem el ShapeRenderer
                shapeRenderer = new ShapeRenderer();
                spriteBatch = new SpriteBatch();
                // Creem la càmera de les dimensions del joc
                camera = new OrthographicCamera(Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
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
                bossList = new LinkedList<>();
                bulletList = new LinkedList<>();
                mapColision = new LinkedList<>();
                mapSpawns = new LinkedList<>();
                // Afegim els actors a l'stage
                stage.addActor(human);
                // Donem nom a l'Actor
                human.setName("human");
                //Health
                totalHealth = human.getMaxHealth();
                currentHealth = human.getHealth();
                totalBarWidth = 31;
                totalBossBarWidth = 64;
                red = new Texture(Gdx.files.internal("fons/red.png"));
                black = new Texture(Gdx.files.internal("fons/black.png"));
                purple = new Texture(Gdx.files.internal("fons/purple.png"));
                // Assignem com a gestor d'entrada la classe InputHandler
                Gdx.input.setInputProcessor(new InputHandler(this));
        }

        @Override
        public void show() {
                switch (TitleScreen.selectMap) {
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
                        if (layers.getString("name").equals("Hitbox entorno")) {
                                for (int c = 0; c < layers.get("objects").size; c++) {
                                        mapColision.add(new Rectangle(
                                                layers.get("objects").get(c).getFloat("x"), layers.get("objects").get(c).getFloat("y"), layers.get("objects").get(c).getFloat("width"), layers.get("objects").get(c).getFloat("height")
                                        ));
                                }
                        }
                        if (layers.getString("name").equals("Spawns")) {
                                for (int c = 0; c < layers.get("objects").size; c++) {
                                        mapSpawns.add(new Rectangle(
                                                layers.get("objects").get(c).getFloat("x"), layers.get("objects").get(c).getFloat("y"), layers.get("objects").get(c).getFloat("width"), layers.get("objects").get(c).getFloat("height")
                                        ));
                                }
                        }
                }
        }

        @Override
        public void render(float delta) {
                switch (TitleScreen.selectMap) {
                        case MAP_FOREST:
                                Gdx.gl.glClearColor((float) (129 / 255.0), (float) (185 / 255.0), (float) (11 / 255.0), 1);
                                break;
                        case MAP_DESERT:
                                Gdx.gl.glClearColor((float) (226 / 255.0), (float) (168 / 255.0), (float) (23 / 255.0), 1);
                                break;
                }
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
                        game.setScreen(new PuntuacioScreen(game));
                        Human.setHealth(100);
                        Human.resetLvl();
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
                spawnBoss();
                spawnBullet();
                if (skeletonList != null)
                        updateSkeleton();
                if (bossList != null)
                        updateBoss();
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
                createHumanHealth();
                if (bossList != null) {
                        ListIterator<Boss> bossListIterator = bossList.listIterator();
                        int i =0;
                        while (bossListIterator.hasNext()) {
                                Boss boss = bossListIterator.next();
                                if (!boss.isDead()) {
                                        createBossHealth(boss, i);
                                        i++;
                                }
                        }
                }
                batch.end();
        }

        private void createHumanHealth() {
                health = crearBarraColor(red);
                backgroundHealth = crearBarraColor(black);
                currentHealth = human.getHealth();
                width = currentHealth / totalHealth * totalBarWidth;
                backgroundHealth.draw(batch, Settings.GAME_WIDTH/2, Settings.GAME_HEIGHT/2+35, totalBarWidth,10);
                health.draw(batch, Settings.GAME_WIDTH/2, Settings.GAME_HEIGHT/2+35, width,10);
        }

        private void createBossHealth(Boss boss, int i) {
                        bossHealth = crearBarraColor(purple);
                        backgroundHealth = crearBarraColor(black);
                        currentHealth = boss.getHealth();
                        width = currentHealth / boss.getMaxHealth() * totalBossBarWidth;
                        backgroundHealth.draw(batch, 5, Settings.GAME_HEIGHT / 3 + i * 13, totalBossBarWidth, 10);
                        bossHealth.draw(batch, 5, Settings.GAME_HEIGHT / 3 + i * 13, width, 10);
        }

        public NinePatch crearBarraColor (Texture texture) {
                return new NinePatch(texture, 0, 0, 0, 0);
        }


        private void updateSkeleton() {
                ListIterator<Skeleton> skeletonListIterator = skeletonList.listIterator();
                while (skeletonListIterator.hasNext()) {
                        Skeleton skeleton = skeletonListIterator.next();
                        checkColision(skeleton);
                        checkMovement(skeleton);
                }
        }

        private void updateBoss() {
                ListIterator<Boss> bossListIterator = bossList.listIterator();
                while (bossListIterator.hasNext()) {
                        Boss boss = bossListIterator.next();
                        checkColision(boss);
                        checkMovement(boss);
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
                int timerDifficulty = timeBetweenEnemySpawns / numBullets;
                if (enemySpawnTimer > timerDifficulty){
                        float mobSpawnX, mobSpawnY;
                        int numero = (int)(Math.random()*mapSpawns.size());
                        mobSpawnX = mapSpawns.get(numero).x;
                        mobSpawnY = mapSpawns.get(numero).y;
                        skeletonList.add(
                                new Skeleton(mobSpawnX, mobSpawnY, Settings.MOB_WIDTH, Settings.MOB_HEIGHT)

                        );
                        ListIterator<Skeleton> skeletonListIterator = skeletonList.listIterator();
                        while (skeletonListIterator.hasNext()) {
                                Skeleton skeleton = skeletonListIterator.next();
                                stage.addActor(skeleton);
                        }
                        enemySpawnTimer = 0;
                }
        }

        private void spawnBoss() {
                bossSpawnTimer = bossSpawnTimer + 1;
                if (bossSpawnTimer > timeBetweenBossSpawns){
                        float mobSpawnX, mobSpawnY;
                        int numero = (int)(Math.random()*mapSpawns.size());
                        mobSpawnX = mapSpawns.get(numero).x;
                        mobSpawnY = mapSpawns.get(numero).y;
                        bossList.add(
                                new Boss(mobSpawnX, mobSpawnY, Settings.BOSS_WIDTH, Settings.BOSS_HEIGHT)

                        );
                        ListIterator<Boss> bossListIterator = bossList.listIterator();
                        while (bossListIterator.hasNext()) {
                                Boss boss = bossListIterator.next();
                                stage.addActor(boss);
                        }
                        bossSpawnTimer = 0;
                }
        }

        private void spawnBullet() {
                bulletSpawnTimer = bulletSpawnTimer + 1;
                if (bulletSpawnTimer > timeBetweenBulletSpawns){
                        if (Human.lvl == 1) {
                                numBullets = 1;
                        }
                        if (Human.lvl == 3) {
                                numBullets = 2;
                        }
                        if (Human.lvl == 4) {
                                numBullets = 4;
                        }
                        for (int b = 1; b <= numBullets; b++) {
                                bulletList.add(
                                        new Fireball(human.getCentreX(), human.getCentreY(), b)
                                );
                        }
                        ListIterator<Fireball> bulletListIterator = bulletList.listIterator();
                        while (bulletListIterator.hasNext()) {
                                Fireball bullet = bulletListIterator.next();
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
                                human.getHit(attackDamage);
                                AssetManager.hitSound.play();
                        }
                }
        }

        private void checkColision(Boss mob) {
                if (mob.collides(human)) {
                        // La nau explota i desapareix
                        if (mob.attackCooldown() == true) {
                                Gdx.app.log("App", "Bong");
                                human.getHit(attackBossDamage);
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
                                bullet.reduceBulletHealth(1);
                                hud.addScore(1);
                        }
                }
                ListIterator<Boss> bossListIterator = bossList.listIterator();
                while (bossListIterator.hasNext()) {
                        Boss boss = bossListIterator.next();
                        if (bullet.collides(boss)) {
                                if (boss.getHealth() <= 0) {
                                        boss.killed();
                                        hud.addScore(10);
                                }else {
                                        if (human.getLvl() < 5) {
                                                boss.reduceHealth(1);
                                        } else {
                                                boss.reduceHealth(3);
                                        }
                                }
                                bullet.reduceBulletHealth(5);
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

        private void checkMovement(Boss mob) {
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

        private void trakingX(Boss mob) {
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

        private void trakingY(Boss mob) {
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
                // Recollim les propietats del Batch de l'Stage
                shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
                // Inicialitzem el shaperenderer
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
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
                shapeRenderer.setColor(new Color(0.5F, 0, 0, 1));
                ListIterator<Boss> bossListIterator = bossList.listIterator();
                while (bossListIterator.hasNext()) {
                        Boss boss = bossListIterator.next();
                        shapeRenderer.rect(boss.getX()+40, boss.getY()+5, (float) (boss.getWidth()/2), (float) (boss.getHeight()/2+20));
                }
                shapeRenderer.setColor(new Color(1, 1, 0, 1));
                ListIterator<Fireball> bulletListIterator = bulletList.listIterator();
                while (bulletListIterator.hasNext()) {
                        Fireball bullet = bulletListIterator.next();
                        shapeRenderer.rect(bullet.getX()+2, bullet.getY()+1, (float) (bullet.getWidth()/2), (float) (bullet.getHeight()/2));
                }
                shapeRenderer.setColor(new Color(1, 1, 1, 1));
                shapeRenderer.rect(20, 20, mapPixelWidth-40,mapPixelHeight-40);
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
                shapeRenderer.setColor(new Color(0, 0, (float) 0.26, 1));
                ListIterator<Rectangle> mapSpawnsIterator = mapSpawns.listIterator();
                while (mapSpawnsIterator.hasNext()) {
                        Rectangle colision = mapSpawnsIterator.next();
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
