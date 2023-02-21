package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenSettings implements Screen {
    MyGdxGame mgg;

    Texture imgBackGround; // фон
    MosquitoButton btnMode, btnSound, btnMusic, btnClearRecords, btnBack;

    public ScreenSettings(MyGdxGame myGdxGame){
        mgg = myGdxGame;
        imgBackGround = new Texture("backgrounds/bg_settings.jpg");
        // создаём кнопки
        btnMode = new MosquitoButton(mgg.fontLarge, "Mode: Easy", 500, 550);
        btnSound = new MosquitoButton(mgg.fontLarge, "Sound: ON", 500, 450);
        btnMusic = new MosquitoButton(mgg.fontLarge, "Music: ON", 500, 350);
        btnClearRecords = new MosquitoButton(mgg.fontLarge, "Clear Records", 500, 250);
        btnBack = new MosquitoButton(mgg.fontLarge, "Back", 500, 150);
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
            if(btnMode.hit(mgg.touch.x, mgg.touch.y)){
                if(mgg.modeOfGame == MODE_EASY){
                    mgg.modeOfGame = MODE_NORMAL;
                    btnMode.text = "Mode: Normal";
                    mgg.numMosquitos = 50;
                    mgg.speedMosquitos = 7;
                    mgg.sizeMosquitos = 120;
                } else if(mgg.modeOfGame == MODE_NORMAL){
                    mgg.modeOfGame = MODE_HARD;
                    btnMode.text = "Mode: Hard";
                    mgg.numMosquitos = 250;
                    mgg.speedMosquitos = 10;
                    mgg.sizeMosquitos = 80;
                } else if(mgg.modeOfGame == MODE_HARD){
                    mgg.modeOfGame = MODE_EASY;
                    btnMode.text = "Mode: Easy";
                    mgg.numMosquitos = 10;
                    mgg.speedMosquitos = 5;
                    mgg.sizeMosquitos = 180;
                }
            }
            if(btnSound.hit(mgg.touch.x, mgg.touch.y)){
                mgg.soundOn = !mgg.soundOn;
                if(mgg.soundOn) btnSound.text = "Sound: ON";
                else btnSound.text = "Sound: OFF";
            }
            if(btnMusic.hit(mgg.touch.x, mgg.touch.y)){
                mgg.musicOn = !mgg.musicOn;
                if(mgg.musicOn) {
                    btnMusic.text = "Music: ON";
                    mgg.screenGame.sndMusic.play();
                }
                else {
                    btnMusic.text = "Music: OFF";
                    mgg.screenGame.sndMusic.stop();
                }
            }
            if(btnClearRecords.hit(mgg.touch.x, mgg.touch.y)){
                btnClearRecords.text = "Records Cleared";
                mgg.screenGame.clearTableOfRecords();
                mgg.screenGame.saveTableOfRecords();
            }
            if(btnBack.hit(mgg.touch.x, mgg.touch.y)){
                mgg.setScreen(mgg.screenIntro);
            }
        }

        // события игры
        // ------------

        // отрисовка всего
        mgg.camera.update();
        mgg.batch.setProjectionMatrix(mgg.camera.combined);
        mgg.batch.begin();
        mgg.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnMode.font.draw(mgg.batch, btnMode.text, btnMode.x, btnMode.y);
        btnSound.font.draw(mgg.batch, btnSound.text, btnSound.x, btnSound.y);
        btnMusic.font.draw(mgg.batch, btnMusic.text, btnMusic.x, btnMusic.y);
        btnClearRecords.font.draw(mgg.batch, btnClearRecords.text, btnClearRecords.x, btnClearRecords.y);
        btnBack.font.draw(mgg.batch, btnBack.text, btnBack.x, btnBack.y);
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
        btnClearRecords.text = "Clear Records";
    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
    }
}
