package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class MosquitoButton {
    float x, y;
    float width, height;
    String text;
    BitmapFont font;
    boolean textButton;

    public MosquitoButton(BitmapFont font, String text, float x, float y) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.font = font;
        GlyphLayout gl = new GlyphLayout(font, text);
        width = gl.width;
        height = gl.height;
        textButton = true;
    }

    public MosquitoButton(float x, float y, float width, float height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        textButton = false;
    }

    boolean hit(float tx, float ty){
        if(textButton)
            return x < tx && tx < x + width && y > ty && ty > y - height;
        else
            return x < tx && tx < x + width && y < ty && ty < y + height;
    }
}
