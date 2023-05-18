package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.SCR_HEIGHT;
import static com.mygdx.game.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

public class ScreenGame2 implements Screen {

    MyGdxGame mgg;

    Texture texture, texture2;

    Texture[] imgMosq = new Texture[11]; // ссылка на текстуры (картинки)
    Texture imgBackGround; // фон
    Texture imgBtnMenu;

    // создание массива ссылок на объекты
    ArrayList<Enemy> enemiesArray;
    int countOfAliveEnemies;


    MosquitoButton btnRestart, btnExit;
    MosquitoButton btnMenu;
    MProgressBar hitPointsBar;
    MProgressBar weaponCoolDownBar;

    GameState gameSession;

    public ScreenGame2(MyGdxGame myGdxGame) {
        mgg = myGdxGame;

        for (int i = 0; i < imgMosq.length; i++) {
            imgMosq[i] = new Texture("mosq" + i + ".png");
        }
        imgBackGround = new Texture("backgrounds/bg_boloto.jpg");
        imgBtnMenu = new Texture("menu.png");

        btnRestart = new MosquitoButton(mgg.font, "Restart", 450, 200);
        btnExit = new MosquitoButton(mgg.font, "Exit", 750, 200);
        btnMenu = new MosquitoButton(SCR_WIDTH - 60, SCR_HEIGHT - 60, 50, 50);

        hitPointsBar = new MProgressBar(1000, 10, MemoryHelper.loadUserHitPoints(), 100, 100);
        weaponCoolDownBar = new MProgressBar(1000, 10, MemoryHelper.loadWeaponCoolDown() * 1000, 100, 70);
    }

    @Override
    public void show() {
        gameStart();
    }

    void gameStart() {
        enemiesArray = new ArrayList<Enemy>();
        countOfAliveEnemies = 0;
        gameSession = new GameState();
        hitPointsBar.setValue(MemoryHelper.loadUserHitPoints());
    }

    @Override
    public void render(float delta) {
        gameSession.updateTime();

        if (Gdx.input.justTouched()) {
            mgg.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            mgg.camera.unproject(mgg.touch);

            switch (gameSession.gameSate) {
                case (GameState.PLAY_GAME): {
                    Gdx.app.log("MyTag", "available: " + gameSession.isWeaponAvailable());
                    if (gameSession.isWeaponAvailable()) {
                        gameSession.updateShotTime();
                        for (int i = 0; i < enemiesArray.size(); i++) {
                            if (enemiesArray.get(i).isAlive
                                    && enemiesArray.get(i).hit(mgg.touch.x, mgg.touch.y)) {
                                gameSession.killedEnemy(enemiesArray.get(i).enemyType);
                                break;
                            }
                        }
                    }
                    break;
                }
                case (GameState.GAME_OVER): {
                    if (btnExit.hit(mgg.touch.x, mgg.touch.y)) mgg.setScreen(mgg.screenIntro);
                    if (btnRestart.hit(mgg.touch.x, mgg.touch.y)) gameStart();
                    break;
                }
            }

            if (btnMenu.hit(mgg.touch.x, mgg.touch.y))
                mgg.setScreen(mgg.screenIntro);
        }

        for (int idx = 0; idx < enemiesArray.size(); idx++) {
            enemiesArray.get(idx).makeStep();
            if (enemiesArray.get(idx).isAttacking()) {
                hitPointsBar.setValue(gameSession.userLeftHitPoints);
                gameSession.missedEnemy(enemiesArray.get(idx).enemyType.damage);
                enemiesArray.remove(idx);
                idx -= 1;
            }
        }

        if (gameSession.isTimeToSpawnEnemy())
            enemiesArray.add(new Enemy(mgg, EnemyType.EASY));

        mgg.camera.update();
        mgg.batch.setProjectionMatrix(mgg.camera.combined);
        mgg.batch.begin();
        mgg.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);

        if (gameSession.gameSate == GameState.PLAY_GAME) drawGameEnv();
        else if (gameSession.gameSate == GameState.GAME_OVER) drawAfterGameMenu();

        mgg.batch.end();
    }

    void drawGameEnv() {
        for (Enemy enemy : enemiesArray) {
            mgg.batch.draw(
                    imgMosq[0],
                    enemy.x,
                    enemy.y,
                    enemy.width,
                    enemy.height,
                    0, 0,
                    500, 500,
                    true, false
            );
        }
        mgg.font.draw(mgg.batch, "Kill points: " + gameSession.killsPoints, 10, SCR_HEIGHT - 10);
        mgg.font.draw(mgg.batch, "Time: " + gameSession.getSessionTime(), SCR_WIDTH - 500, SCR_HEIGHT - 10);
        mgg.font.draw(mgg.batch, "Hp: " + gameSession.userLeftHitPoints, 400, SCR_HEIGHT - 10);
        mgg.batch.draw(imgBtnMenu, btnMenu.x, btnMenu.y, btnMenu.width, btnMenu.height);
        hitPointsBar.drawBar(mgg.batch);
        weaponCoolDownBar.setValue(gameSession.getLeftWeaponCoolDown());
        weaponCoolDownBar.drawBar(mgg.batch);

    }

    void drawAfterGameMenu() {
        mgg.fontLarge.draw(mgg.batch, "Game Over", 0, 600, SCR_WIDTH, Align.center, true);

        mgg.font.draw(mgg.batch, "Kill points: " + gameSession.killsPoints, 10, SCR_HEIGHT - 10);
        mgg.font.draw(mgg.batch, "Time: " + gameSession.getSessionTime(), SCR_WIDTH - 500, SCR_HEIGHT - 10);

        mgg.font.draw(mgg.batch, btnRestart.text, btnRestart.x, btnRestart.y);
        mgg.font.draw(mgg.batch, btnExit.text, btnExit.x, btnExit.y);
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
