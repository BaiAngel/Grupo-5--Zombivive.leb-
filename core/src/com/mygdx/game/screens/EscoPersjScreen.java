package com.mygdx.game.screens;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Zombivive;

public class EscoPersjScreen implements Screen  {
        private Game game;
        private Stage stage;
        private TextureRegionDrawable background;
        private TextButton btnMap1, btnMap2;

        public EscoPersjScreen(Game game) {
            this.game = game;
            stage = new Stage(new ScreenViewport());
            background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("fons/Personajes.png"))));
            createUI();
            Label title = new Label("Escoge Personaje", Zombivive.gameSkin,"big");
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
            btnMap1 = new TextButton("Caballero", Zombivive.gameSkin,"small");

            btnMap1.setPosition(Gdx.graphics.getWidth() / 2f - btnMap1.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - 50);
            btnMap1.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    TitleScreen.selectCharacter = 1;
                    game.setScreen(new EscoMapScreen((Zombivive) game));
                }
            });
            stage.addActor(btnMap1);

            btnMap2 = new TextButton("Mago", Zombivive.gameSkin,"small");

            btnMap2.setPosition(Gdx.graphics.getWidth() / 2f - btnMap2.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - 150);
            btnMap2.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    TitleScreen.selectCharacter = 0;
                    game.setScreen(new EscoMapScreen((Zombivive)game));
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
            Gdx.gl.glClearColor(0, 0, 0, 1);
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


