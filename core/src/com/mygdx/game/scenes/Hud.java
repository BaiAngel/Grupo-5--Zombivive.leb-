package com.mygdx.game.scenes;

import static com.mygdx.game.objects.Human.lvl;
import static java.lang.String.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.helpers.AssetManager;
import com.mygdx.game.objects.Human;
import com.mygdx.game.utils.Settings;

public class Hud implements Disposable{

    //Scene2D.ui Stage and its own Viewport for HUD
    public Stage stage;
    private Viewport viewport;

    //Mario score/time Tracking Variables
    private static Integer worldTimer;
    private static Integer lives;
    private boolean timeUp; // true when the world timer reaches 0
    private static boolean dead;// true when lives reaches 0
    private float timeCount;
    private static Integer score;

    //Scene2D widgets
    private Label countdownLabel;
    private static Label scoreLabel;
    private Label timeLabel;
    private static Label livesLabel;
    private Label worldLabel;
    private Label marioLabel;

    public Hud(SpriteBatch sb){
        //define our tracking variables
        worldTimer = 300;
        timeCount = 0;
        score = 0;
        lives=3;


        //setup the HUD viewport using a new camera seperate from our gamecam
        //define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(Settings.GAME_WIDTH, Settings.GAME_HEIGHT, new OrthographicCamera(Settings.GAME_WIDTH, Settings.GAME_HEIGHT));
        stage = new Stage(viewport, sb);

        //define a table used to organize our hud's labels
        Table table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        //define our labels using the String, and a Label style consisting of a font and color
        countdownLabel = new Label(format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel =new Label(format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        livesLabel = new Label(format("%01d", lvl), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel = new Label("LEVEL", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel = new Label("KILLS", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        //add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        //add a second row to our table
        table.row();
        table.add(scoreLabel).expandX();
        table.add(livesLabel).expandX();
        table.add(countdownLabel).expandX();

        //add our table to the stage
        stage.addActor(table);

    }

    public void act(float dt){
        timeCount += dt;
        if(timeCount >= 1){
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            countdownLabel.setText(format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    public static void addScore(int value){
        score += value;
        scoreLabel.setText(format("%06d", score));
        if (score >= 10 && lvl == 1) {
            lvl = 2;
            AssetManager.lvlsound.play();
        }
        else if (score >= 25 && lvl == 2) {
            lvl = 3;
            AssetManager.lvlsound.play();
        }
        else if (score >= 50 && lvl == 3) {
            lvl = 4;
            AssetManager.lvlsound.play();
        }
        else if (score >= 100 && lvl == 4) {
            lvl = 5;
            AssetManager.lvlsound.play();
        }
        livesLabel.setText(format("%01d", lvl));
        Preferences prefs = Gdx.app.getPreferences("preferencia");
        // reemplaza esto con tu propio puntaje
        prefs.putInteger("score", score);
        prefs.flush(); // es importante llamar a flush para que los cambios se guarden
    }


    @Override
    public void dispose() { stage.dispose(); }

    public static Integer getScore() {
        return score;
    }

    public boolean isTimeUp() { return timeUp; }

    public boolean isDead() { return dead; }

}

