package com.mygdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Zombivive;
import com.mygdx.game.helpers.AssetManager;


public class EscoMapScreen implements Screen {
    private Game game;
    private Stage stage;
    private TextureRegionDrawable background;
    private TextButton btnMap1, btnMap2;

    public EscoMapScreen(Game game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("fons/black.png"))));
        createUI();
        Label title = new Label("Escull mapa", Zombivive.gameSkin,"big-black");
        title.setAlignment(Align.center);
        title.setY(Gdx.graphics.getHeight()*2/3);
        title.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);
    }

    private void createUI() {
        // Set background image
        Image imgBackground = new Image(background);
        imgBackground.setFillParent(true);
        stage.addActor(imgBackground);

        // Create buttons for each map
        btnMap1 = new TextButton("Mapa bosque", Zombivive.gameSkin);

        btnMap1.setPosition(Gdx.graphics.getWidth() / 2f - btnMap1.getWidth() / 2f, Gdx.graphics.getHeight() / 2f + 20);
        btnMap1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                TitleScreen.selectMap = 0;
                game.setScreen(new GameScreen((Zombivive) game));
            }
        });
        stage.addActor(btnMap1);

        btnMap2 = new TextButton("Mapa desierto", Zombivive.gameSkin);

        btnMap2.setPosition(Gdx.graphics.getWidth() / 2f - btnMap2.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - 100);
        btnMap2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                TitleScreen.selectMap = 1;
                game.setScreen(new GameScreen((Zombivive) game));
            }
        });
        stage.addActor(btnMap2);

        // Add stage input processor
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }
}
