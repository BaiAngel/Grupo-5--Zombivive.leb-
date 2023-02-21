package com.mygdx.game.utils;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Methods {

    // Mètode que torna un float aleatori entre un mínim i un màxim
    public static float randomFloat(float min, float max) {
        Random r = new Random();
        return r.nextFloat() * (max - min) + min;

    }

    public static Drawable getColoredDrawable(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        pixmap.dispose();

        return drawable;
    }

    public static boolean getColision (Rectangle r1, Rectangle r2) {
        boolean contactTrue = false;
        if (r1.overlaps(r2)) {
            Gdx.app.log("app","contact");
            contactTrue = true;
        }
        else {
            Gdx.app.log("app","NO contact");
        }
        return contactTrue;
    }
}
