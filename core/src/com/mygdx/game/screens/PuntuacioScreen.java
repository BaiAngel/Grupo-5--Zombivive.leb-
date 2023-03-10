package com.mygdx.game.screens;



import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Zombivive;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Json;

import java.io.IOException;

public class PuntuacioScreen implements Screen {
    private static final String SERVER_URL = "http://localhost:3000/score";

    private Stage stage;
    private Game game;
    private TextField fieldNombre;
    public PuntuacioScreen(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());

        Table table = new Table();
        table.setFillParent(true);
        Preferences prefs = Gdx.app.getPreferences("preferencia");
        final int score = prefs.getInteger("score", 0); // el segundo argumento es el valor predeterminado si no hay ningún valor almacenado
        Label labelNombre = new Label("Nombre", Zombivive.gameSkin);
        Label labelPuntuacion = new Label("Muertes", Zombivive.gameSkin);
        fieldNombre = new TextField("", Zombivive.gameSkin);
        TextField fieldPuntuacion = new TextField(String.valueOf(score), Zombivive.gameSkin);
        TextButton sendPuntuacion = new TextButton("Enviar", Zombivive.gameSkin,"small");
        TextButton menu = new TextButton("Tornar al menu", Zombivive.gameSkin, "small");
        menu.setPosition(Gdx.graphics.getWidth() / 2f - menu.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - 150);
        menu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                try {
                    game.setScreen(new TitleScreen((Zombivive) game));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        stage.addActor(menu);

        sendPuntuacion.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                System.out.println(fieldNombre.getText());
                System.out.println(score);
                sendScore(fieldNombre.getText(), score);
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        table.add(labelNombre).padRight(20);
        table.add(fieldNombre);
        table.row();
        table.add(labelPuntuacion).padRight(20);
        table.add(fieldPuntuacion);
        table.row();
        table.add(sendPuntuacion).colspan(2).fill();

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    private void sendScore(String username, int score) {
        // Crea el objeto Score con los datos del usuario
        Score scoreObject = new Score(username, score);

        // Convierte el objeto Score a JSON
        Json json = new Json();
        String scoreJson = json.toJson(scoreObject);

        // Envía los datos a través de una solicitud HTTP POST
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();
        Net.HttpRequest request = requestBuilder.newRequest().method(Net.HttpMethods.POST).url(SERVER_URL).build();
        request.setHeader("Content-Type", "application/json");
        request.setContent(scoreJson);
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                // El servidor ha respondido
                int statusCode = httpResponse.getStatus().getStatusCode();
                String response = httpResponse.getResultAsString();
                Gdx.app.log("MyGame", "Response: " + response + " (status code " + statusCode + ")");
            }

            @Override
            public void failed(Throwable t) {
                // La solicitud ha fallado
                Gdx.app.error("MyGame", "Request failed", t);
            }

            @Override
            public void cancelled() {
                // La solicitud ha sido cancelada
                Gdx.app.log("MyGame", "Request cancelled");
            }
        });
    }

    private static class Score {
        public String username;
        public int score;

        public Score(String username, int score) {
            this.username = username;
            this.score = score;
        }
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
