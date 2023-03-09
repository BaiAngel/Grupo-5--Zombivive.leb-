package com.mygdx.game.screens;



import static com.mygdx.game.helpers.AssetManager.music;
import static com.mygdx.game.helpers.AssetManager.musica;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Zombivive;
import com.mygdx.game.helpers.AssetManager;

import java.io.IOException;

public class OptionScreen implements Screen {

    private Stage stage;
    private Game game;
    private float volume = 1.0f;

    public OptionScreen(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());

        Label title = new Label("Volumen", Zombivive.gameSkin,"big-black");
        title.setAlignment(Align.center);
        title.setY(Gdx.graphics.getHeight()*2/3);
        title.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);

        TextButton backButton = new TextButton("Atras",Zombivive.gameSkin);
        backButton.setWidth(Gdx.graphics.getWidth()/2);
        backButton.setPosition(Gdx.graphics.getWidth()/2-backButton.getWidth()/2,Gdx.graphics.getHeight()/4-backButton.getHeight()/2);
        backButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                try {
                    game.setScreen(new TitleScreen((Zombivive) game));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(backButton);







        // Obtener una instancia de Music o Sound
        //final Music music = Gdx.audio.newMusic(Gdx.files.internal("sounds/kingdom.mp3"));

// Crear un control deslizante para el volumen
        Skin skin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
        final Slider volumeSlider = new Slider(0, 1, 0.1f, false, skin);
        volumeSlider.setValue(AssetManager.getVolume());


        volumeSlider.setValue(music.getVolume()); // Establecer el valor inicial del control deslizante al volumen actual
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float volume = volumeSlider.getValue();
                AssetManager.setVolume(volume);



            }

        });
        Table table = new Table();
        table.setFillParent(true); // La tabla ocupa todo el espacio disponible en la pantalla
        table.center(); // Centramos la tabla en la pantalla
        // Agrega la barra deslizante a la tabla
        table.add(volumeSlider).center().padBottom(50f);
        stage.addActor(table);
// Agregar el control deslizante a la interfaz de usuario


    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
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
        stage.dispose();
    }
}
