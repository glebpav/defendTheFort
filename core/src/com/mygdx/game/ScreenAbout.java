package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.SCR_HEIGHT;
import static com.mygdx.game.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenAbout implements Screen {
    MyGdxGame mgg;

    Texture imgBackGround; // фон
    MosquitoButton btnBack;

    String textAbout =  "Игра Crazy Mosquitos создана\n" +
                        "в рамках проекта Mobile Game\n" +
                        "Development на языке Java\n" +
                        "с использованием LibGDX.\n" +
                        "Цель игры: как можно быстрее\n" +
                        "сбить всех комаров.";

    public ScreenAbout(MyGdxGame myGdxGame){
        mgg = myGdxGame;
        imgBackGround = new Texture("backgrounds/bg_about.jpg");
        // создаём кнопки

        btnBack = new MosquitoButton(mgg.fontLarge, "BACK", 500, 150);
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
        mgg.font.draw(mgg.batch, textAbout, 400, 650);
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

    }

    @Override
    public void dispose() {
        imgBackGround.dispose();
    }
}
