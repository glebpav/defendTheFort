package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenSettings implements Screen {
    MyGdxGame mgg;

    Texture imgBackGround; // фон
    MosquitoButton btnSound, btnMusic, btnClearRecords, btnBack;

    public ScreenSettings(MyGdxGame myGdxGame){
        mgg = myGdxGame;
        imgBackGround = new Texture("backgrounds/bg_intro.jpg");
        // создаём кнопки
        btnSound = new MosquitoButton(mgg.font, "Sound: ON", 500, 550);
        btnMusic = new MosquitoButton(mgg.font, "Music: ON", 500, 450);
        btnClearRecords = new MosquitoButton(mgg.font, "Delete save", 500, 350);
        btnBack = new MosquitoButton(mgg.font, "Back", 500, 150);
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
                btnClearRecords.text = "Save deleted";
                MemoryHelper.saveUserMoney(0);
                MemoryHelper.saveWeaponDamage(GameSettings.DEFAULT_WEAPON_DAMAGE);
                MemoryHelper.saveWeaponCoolDown(GameSettings.DEFAULT_WEAPON_COOLDOWN);
                MemoryHelper.saveUserHitPoints(GameSettings.DEFAULT_USER_HP);
                MemoryHelper.saveUserMaxPoints(0);
            }
            if(btnBack.hit(mgg.touch.x, mgg.touch.y)){
                mgg.setScreen(mgg.screenIntro);
            }
        }


        // отрисовка всего
        mgg.camera.update();
        mgg.batch.setProjectionMatrix(mgg.camera.combined);
        mgg.batch.begin();
        mgg.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
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
