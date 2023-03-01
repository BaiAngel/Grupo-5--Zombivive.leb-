package com.mygdx.game.screens;



import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Zombivive;

/**
 * Cire
 */
public class PuntuacioScreen implements Screen {

    private Stage stage;
    private Game game;

    public PuntuacioScreen(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());

        Table table = new Table();
        table.setFillParent(true);
        Preferences prefs = Gdx.app.getPreferences("preferencia");
        int score = prefs.getInteger("score", 0); // el segundo argumento es el valor predeterminado si no hay ningún valor almacenado
        Label labelNombre = new Label("Nombre", Zombivive.gameSkin);
        Label labelPuntuacion = new Label("Muertes", Zombivive.gameSkin);
        TextField fieldNombre = new TextField("", Zombivive.gameSkin);
        TextField fieldPuntuacion = new TextField(String.valueOf(score), Zombivive.gameSkin);
        TextButton sendPuntuacion = new TextButton("Enviar", Zombivive.gameSkin);

        table.add(labelNombre).padRight(20);
        table.add(fieldNombre);
        table.row();
        table.add(labelPuntuacion).padRight(20);
        table.add(fieldPuntuacion);
        table.row();
        table.add(sendPuntuacion).colspan(2).fill();
        table.debug();
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
        stage.act(delta);


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
