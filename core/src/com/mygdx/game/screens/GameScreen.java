package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.objects.Human;
import com.mygdx.game.utils.Settings;

public class GameScreen implements Screen {

        private Stage stage;
        private Human human;

        public GameScreen() {

                // Creem l'stage
                stage = new Stage();

                // Creem la persona
                human = new Human(Settings.HUMAN_STARTX, Settings.HUMAN_STARTY, Settings.HUMAN_WIDTH, Settings.HUMAN_HEIGHT);

                // Afegim els actors a l'stage
                stage.addActor(human);

        }

@Override
public void show() {

        }

@Override
public void render(float delta) {
        // Dibuixem i actualitzem tots els actors de l'stage
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

        }

        public Stage getStage() {
                return stage;
        }

        public Human getHuman() {
                return human;
        }
}
