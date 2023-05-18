package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MProgressBar {

    Texture backFullBarTexture;
    Texture frontBarTexture;

    private int width;
    private final int height;
    private final int maxWidth;
    private final double maxValue;

    private final int positionX;
    private final int positionY;

    MProgressBar(int maxWidth, int height, double maxValue, int positionX, int positionY) {
        this.height = height;
        this.maxWidth = maxWidth;
        this.maxValue = maxValue;
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = maxWidth;

        initBars();
    }

    void setValue(double value) {
        if (value < 0) return;
        if (value > maxValue) width = maxWidth;
        else width = (int) (maxWidth * (value / maxValue));
    }

    void drawBar(SpriteBatch batch) {
        batch.draw(backFullBarTexture, positionX, positionY, maxWidth, height);
        if(width > 0) batch.draw(frontBarTexture, positionX, positionY, width, height);
    }

    void initBars() {
        int width = 1;
        int height = 1;
        Pixmap pixmap = createProceduralPixmap(width, height, 0, 1, 0);
        Pixmap pixmap2 = createProceduralPixmap(width, height, 1, 0, 0);

        backFullBarTexture = new Texture(pixmap2);
        frontBarTexture = new Texture(pixmap);
    }

    private Pixmap createProceduralPixmap(int width, int height, int r, int g, int b) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(r, g, b, 1);
        pixmap.fill();
        return pixmap;
    }

}
