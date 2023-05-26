package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.SCR_HEIGHT;
import static com.mygdx.game.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class ScreenShop implements Screen {

    MyGdxGame mgg;

    Texture imgBackGround; // фон
    MyButton btnReloadSpeedUpgrade;
    MyButton btnHealthUpgrade;
    MyButton btnDamageUpgrade;
    MyButton btnBack;

    String textAbout = "";

    ScreenShop(MyGdxGame myGdxGame) {
        mgg = myGdxGame;
        btnReloadSpeedUpgrade = new MyButton(mgg.font, "Improve reload speed", 380, 560);
        btnHealthUpgrade = new MyButton(mgg.font, "Improved walls", 380, 460);
        btnDamageUpgrade = new MyButton(mgg.font, "Improved damage", 380, 360);
        imgBackGround = new Texture("backgrounds/bg_shop.png");
        btnBack = new MyButton(mgg.font, "BACK", 500, 150);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            mgg.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            mgg.camera.unproject(mgg.touch);

            if (btnBack.hit(mgg.touch.x, mgg.touch.y)) {
                mgg.setScreen(mgg.screenIntro);
            }
            if (btnReloadSpeedUpgrade.hit(mgg.touch.x, mgg.touch.y)) {
                if (MemoryHelper.loadUserMoney() >= 1500 & MemoryHelper.loadWeaponCoolDown() == 1.5) {
                    MemoryHelper.saveWeaponCoolDown(MemoryHelper.loadWeaponCoolDown() - 0.7);
                    MemoryHelper.saveUserMoney(MemoryHelper.loadUserMoney() - 1000);
                }
            }
            if (btnHealthUpgrade.hit(mgg.touch.x, mgg.touch.y)) {
                if (MemoryHelper.loadUserMoney() >= MemoryHelper.loadUserHitPoints() / 2) {
                    int currentUserHitPoints = MemoryHelper.loadUserHitPoints();
                    MemoryHelper.saveUserHitPoints(currentUserHitPoints + GameSettings.HP_UPDATE);
                    MemoryHelper.saveUserMoney(MemoryHelper.loadUserMoney() - currentUserHitPoints / 2);
                }
            }
            if (btnDamageUpgrade.hit(mgg.touch.x, mgg.touch.y)) {
                if (MemoryHelper.loadUserMoney() >= 3000 & MemoryHelper.loadWeaponDamage() == 100) {
                    MemoryHelper.saveWeaponDamage(MemoryHelper.loadWeaponDamage() + 400);
                    MemoryHelper.saveUserMoney(MemoryHelper.loadUserMoney() - 3000);
                }
            }
        }


        mgg.camera.update();
        mgg.batch.setProjectionMatrix(mgg.camera.combined);
        mgg.batch.begin();
        mgg.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);
        btnReloadSpeedUpgrade.font.draw(mgg.batch, btnReloadSpeedUpgrade.text, btnReloadSpeedUpgrade.x, btnReloadSpeedUpgrade.y);
        btnHealthUpgrade.font.draw(mgg.batch, btnHealthUpgrade.text, btnHealthUpgrade.x, btnHealthUpgrade.y);
        btnDamageUpgrade.font.draw(mgg.batch, btnDamageUpgrade.text, btnDamageUpgrade.x, btnDamageUpgrade.y);
        btnBack.font.draw(mgg.batch, btnBack.text, btnBack.x, btnBack.y);
        mgg.font.draw(mgg.batch, "Your balance: " + MemoryHelper.loadUserMoney(), 770, 650);
        mgg.font.draw(mgg.batch, "Price: 1500", 70, 560);
        mgg.font.draw(mgg.batch, "Price: " + MemoryHelper.loadUserHitPoints() / 2, 70, 460);
        mgg.font.draw(mgg.batch, "Price: 3000", 70, 360);
        if (MemoryHelper.loadUserHitPoints() != 100) {
            mgg.font.draw(mgg.batch, "X " + (MemoryHelper.loadUserHitPoints() - 100) / 20, 740, 460);
        }
        if (MemoryHelper.loadWeaponCoolDown() != 1.5) {
            mgg.font.draw(mgg.batch, "(Max)", 880, 560);
        }
        if (MemoryHelper.loadWeaponDamage() != 100) {
            mgg.font.draw(mgg.batch, "(Max)", 780, 360);
        }
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

    }
}
