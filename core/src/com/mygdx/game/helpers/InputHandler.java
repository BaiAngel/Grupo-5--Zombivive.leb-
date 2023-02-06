package com.mygdx.game.helpers;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.objects.Human;
import com.mygdx.game.screens.GameScreen;

public class InputHandler implements InputProcessor {

    // Objectes necessaris
    private Human human;
    private GameScreen screen;

    // Enter per a la gestió del moviment d'arrossegament
    int previousY = 0;
    int previousX = 0;

    public InputHandler(GameScreen screen) {

        // Obtenim tots els elements necessaris
        this.screen = screen;
        human = screen.getHuman();

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        human.goStraight();
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        if (character == 'w') {
            human.goUp();
        }
        else if (character == 'd') {
            human.goLeft();
        }
        else if (character == 'a') {
            human.goRight();
        }
        else if (character == 's') {
            human.goDown();
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        previousY = screenY;
        previousX = screenX;
        Gdx.app.log("Mouse", "Mousex: " + screenX + " Mousey: "+ screenY);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // Quan deixem anar el dit acabem un moviment
        // i posem la nau a l'estat normal
        human.goStraight();
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Posem un llindar per evitar gestionar events quan el dit està quiet
        if (Math.abs(previousY - screenY) > 2)
            // Si la Y és major que la que tenim
            // guardada és que va cap avall
            if (previousY < screenY) {
                human.goDown();
            } else {
            // En cas contrari cap amunt
                human.goUp();
            }
        // Guardem la posició de la Y
        previousY = screenY;

        // Posem un llindar per evitar gestionar events quan el dit està quiet
        if (Math.abs(previousX - screenX) > 2)
            // Si la Y és major que la que tenim
            // guardada és que va cap avall
            if (previousX < screenX) {
                human.goLeft();
            } else {
                // En cas contrari cap amunt
                human.goRight();
            }
        // Guardem la posició de la Y
        previousX = screenX;

        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

}