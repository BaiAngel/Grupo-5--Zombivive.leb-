package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.helpers.AssetManager;
import com.mygdx.game.screens.GameScreen;
import com.mygdx.game.screens.PuntuacioScreen;
import com.mygdx.game.screens.SplashScreen;
import com.mygdx.game.screens.TitleScreen;

public class Zombivive extends Game {
	static public Skin gameSkin;

	@Override
	public void create() {
		gameSkin = new Skin(Gdx.files.internal("skin/glassy-ui.json"));
		// A l'iniciar el joc carreguem els recursos
		AssetManager.load();
		// I definim la pantalla principal com a la pantalla
		setScreen(new SplashScreen(this));
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		Gdx.app.log("LifeCycle", "resize(" + Integer.toString(width) + ", " + Integer.toString(height) + ")");
	}

	@Override
	public void pause() {
		super.pause();
		Gdx.app.log("LifeCycle", "pause()");
	}

	@Override
	public void resume() {
		super.resume();
		Gdx.app.log("LifeCycle", "resume()");
	}

	@Override
	public void render() {
		super.render();
//Gdx.app.log("LifeCycle", "render()");
	}

	@Override
	public void dispose() {
		super.dispose();
		AssetManager.dispose();
		Gdx.app.log("LifeCycle", "dispose()");

	}
}