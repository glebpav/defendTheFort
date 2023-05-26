package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.SCR_HEIGHT;
import static com.mygdx.game.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenIntro implements Screen {
    MyGdxGame mgg;

    Texture imgBackGround; // фон
    MosquitoButton btnGame, btnSettings, btnAbout, btnExit, btnShop;

    public ScreenIntro(MyGdxGame myGdxGame){
        mgg = myGdxGame;
        imgBackGround = new Texture("backgrounds/bg_intro.jpg");
        // создаём кнопки
        btnGame = new MosquitoButton(mgg.font, "PLAY", 570, 300);
        btnSettings = new MosquitoButton(mgg.font, "SETTINGS", 250, 430);
        btnShop = new MosquitoButton(mgg.font, "SHOP", 800, 430);
        btnAbout = new MosquitoButton(mgg.font, "ABOUT", 800, 130);
        btnExit = new MosquitoButton(mgg.font, "EXIT", 300, 130);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // касания экрана/клики мышью
        if(Gdx.input.justTouched()) {
            mgg.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            mgg.camera.unproject(mgg.touch);
            if(btnGame.hit(mgg.touch.x, mgg.touch.y)){
                mgg.setScreen(mgg.screenGame2);
            }
            if(btnSettings.hit(mgg.touch.x, mgg.touch.y)){
                mgg.setScreen(mgg.screenSettings);
            }
            if(btnShop.hit(mgg.touch.x, mgg.touch.y)){
                mgg.setScreen(mgg.screenShop);
            }
            if(btnAbout.hit(mgg.touch.x, mgg.touch.y)){
                mgg.setScreen(mgg.screenAbout);
            }
            if(btnExit.hit(mgg.touch.x, mgg.touch.y)){
                Gdx.app.exit();
            }
        }

        mgg.camera.update();
        mgg.batch.setProjectionMatrix(mgg.camera.combined);
        mgg.batch.begin();
        mgg.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnGame.font.draw(mgg.batch, btnGame.text, btnGame.x, btnGame.y);
        btnSettings.font.draw(mgg.batch, btnSettings.text, btnSettings.x, btnSettings.y);
        btnShop.font.draw(mgg.batch, btnShop.text, btnShop.x, btnShop.y);
        btnAbout.font.draw(mgg.batch, btnAbout.text, btnAbout.x, btnAbout.y);
        btnExit.font.draw(mgg.batch, btnExit.text, btnExit.x, btnExit.y);
        mgg.batch.end();
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
        imgBackGround.dispose();
    }
}
