package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.SCR_HEIGHT;
import static com.mygdx.game.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.Collections;

public class ScreenGame2 implements Screen {

    MyGdxGame mgg;

    Texture texture, texture2;
    Texture imgBackGround;
    Texture imgBtnMenu;
    Sprite killedEasyEnemySprite;
    Sprite killedHardEnemySprite;

    ArrayList<Sprite> easyEnemyImgArray;
    ArrayList<Sprite> hardEnemyImgArray;
    ArrayList<Enemy> enemiesArray;
    ArrayList<Color> enemiesColorArray;
    int countOfAliveEnemies;

    MyButton btnRestart, btnExit;
    MyButton btnMenu;
    MProgressBar hitPointsBar;
    MProgressBar weaponCoolDownBar;

    GameState gameSession;


    public ScreenGame2(MyGdxGame myGdxGame) {
        mgg = myGdxGame;
        easyEnemyImgArray = new ArrayList<>();
        hardEnemyImgArray = new ArrayList<>();
        texture = new Texture(Gdx.files.internal("enemyMoveTiles.png"));
        texture2 = new Texture(Gdx.files.internal("HardEnemyTS.png"));

        for (int i = 0; i < Enemy.nFaz / 7; i++) {
            Sprite sprite = new Sprite(texture, 70 * i + 3, 1, 65, 58);
            Sprite sprite1 = new Sprite(texture2, 1 + 76 * i, 1, 75, 55);
            sprite.flip(true, false);
            sprite1.flip(true, false);
            easyEnemyImgArray.add(sprite);
            hardEnemyImgArray.add(sprite1);
        }
        Collections.reverse(easyEnemyImgArray);
        Collections.reverse(hardEnemyImgArray);

        imgBackGround = new Texture("backgrounds/bg_boloto.jpg");
        imgBtnMenu = new Texture("menu.png");

        killedEasyEnemySprite = new Sprite(texture, 73, 1, 65, 58);
        killedHardEnemySprite = new Sprite(texture2, 229, 1, 75, 55);
        killedEasyEnemySprite.flip(false, true);
        killedHardEnemySprite.flip(false, true);

        btnRestart = new MyButton(mgg.font, "Restart", 450, 100);
        btnExit = new MyButton(mgg.font, "Exit", 750, 100);
        btnMenu = new MyButton(SCR_WIDTH - 60, SCR_HEIGHT - 60, 50, 50);

        hitPointsBar = new MProgressBar(1000, 10, MemoryHelper.loadUserHitPoints(), 30, 80, "hp");
        weaponCoolDownBar = new MProgressBar(1000, 10, MemoryHelper.loadWeaponCoolDown() * 1000, 30, 30, "cd");
    }

    @Override
    public void show() {
        gameStart();
    }

    void gameStart() {
        enemiesArray = new ArrayList<>();
        enemiesColorArray = new ArrayList<>();
        countOfAliveEnemies = 0;
        gameSession = new GameState();
        hitPointsBar.setMaxValue(MemoryHelper.loadUserHitPoints());
        weaponCoolDownBar.setMaxValue(MemoryHelper.loadWeaponCoolDown() * 1000);
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
                    if (gameSession.isWeaponAvailable()) {
                        gameSession.updateShotTime();
                        for (int i = 0; i < enemiesArray.size(); i++) {
                            if (enemiesArray.get(i).isAlive
                                    && enemiesArray.get(i).hit(mgg.touch.x, mgg.touch.y, gameSession.userWeaponDamage)) {
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
            if (!enemiesArray.get(idx).isAlive & gameSession.currentTime - enemiesArray.get(idx).defTime > 3000) {
                enemiesArray.remove(idx);
                enemiesColorArray.remove(idx);
                idx -= 1;
                continue;
            }
            if (!enemiesArray.get(idx).isAlive) {
                Color oldColor = mgg.batch.getColor();
                oldColor.a = (3000 - gameSession.currentTime + enemiesArray.get(idx).defTime) / 3000f;
                enemiesColorArray.set(idx, oldColor);
            }
            enemiesArray.get(idx).makeStep();
            if (enemiesArray.get(idx).isAttacking()) {
                gameSession.missedEnemy(enemiesArray.get(idx).enemyType.damage);
                hitPointsBar.setValue(gameSession.userLeftHitPoints);
                enemiesArray.remove(idx);
                enemiesColorArray.remove(idx);
                idx -= 1;
            }
        }

        if (gameSession.isTimeToSpawnEnemy()) {
            // Math.random()    0 - 1
            if (Math.random() > 0.01) enemiesArray.add(new Enemy(mgg, EnemyType.HARD));
            else enemiesArray.add(new Enemy(mgg, EnemyType.EASY));
            enemiesColorArray.add(mgg.batch.getColor());
        }

        mgg.camera.update();
        mgg.batch.setProjectionMatrix(mgg.camera.combined);
        mgg.batch.begin();
        mgg.batch.draw(imgBackGround, 0, 0, SCR_WIDTH, SCR_HEIGHT);

        if (gameSession.gameSate == GameState.PLAY_GAME) drawGameEnv();
        else if (gameSession.gameSate == GameState.GAME_OVER) drawAfterGameMenu();

        mgg.batch.end();
    }

    void drawGameEnv() {
        Color oldColor = mgg.batch.getColor();
        oldColor.a = 1;
        mgg.batch.setColor(oldColor);
        mgg.font.draw(mgg.batch, "Kill points: " + gameSession.killsPoints, 10, SCR_HEIGHT - 10);
        mgg.font.draw(mgg.batch, "Time: " + gameSession.getSessionTime(), SCR_WIDTH - 500, SCR_HEIGHT - 10);
        mgg.font.draw(mgg.batch, "Hp: " + gameSession.userLeftHitPoints, 400, SCR_HEIGHT - 10);
        mgg.batch.draw(imgBtnMenu, btnMenu.x, btnMenu.y, btnMenu.width, btnMenu.height);
        hitPointsBar.drawBar(mgg);
        weaponCoolDownBar.setValue(gameSession.getLeftWeaponCoolDown());
        weaponCoolDownBar.drawBar(mgg);

        int idx = 0;
        for (Enemy enemy : enemiesArray) {
            if (enemy.isAlive) {
                Sprite enemySprite;
                if (enemy.enemyType == EnemyType.EASY) enemySprite = easyEnemyImgArray.get(enemy.faza / 7);
                else enemySprite = hardEnemyImgArray.get(enemy.faza / 7);
                mgg.batch.draw(enemySprite, enemy.x, enemy.y, enemy.width, enemy.height);
                enemy.changePhase();
            }
            idx++;
        }
        idx = 0;
        for (Enemy enemy : enemiesArray) {
            if (!enemy.isAlive) {
                Sprite killedEnemySprite;
                if(enemy.enemyType == EnemyType.EASY) killedEnemySprite = killedEasyEnemySprite;
                else killedEnemySprite = killedHardEnemySprite;
                mgg.batch.setColor(enemiesColorArray.get(idx));
                mgg.batch.draw(killedEnemySprite, enemy.x, enemy.y, enemy.width, enemy.height);
            }
            idx++;
        }
    }

    void drawAfterGameMenu() {
        mgg.fontLarge.draw(mgg.batch, "Game Over", 0, 650, SCR_WIDTH, Align.center, true);

        mgg.font.draw(mgg.batch, getOutputStr("Kill points: ", String.valueOf(gameSession.killsPoints), 30), 0, 530, SCR_WIDTH, Align.center, true);
        mgg.font.draw(mgg.batch, getOutputStr("Best result: ", String.valueOf(MemoryHelper.loadUserMaxPoints()), 30), 0, 430, SCR_WIDTH, Align.center, true);
        mgg.font.draw(mgg.batch, getOutputStr("Earned money: ", String.valueOf(gameSession.earnedMoney), 30), 0, 330, SCR_WIDTH, Align.center, true);
        mgg.font.draw(mgg.batch, getOutputStr("Session time: ", String.valueOf(gameSession.getSessionTime()), 30), 0, 230, SCR_WIDTH, Align.center, true);

        mgg.font.draw(mgg.batch, btnRestart.text, btnRestart.x, btnRestart.y);
        mgg.font.draw(mgg.batch, btnExit.text, btnExit.x, btnExit.y);
    }

    String getOutputStr(String message, String value, int outStrLen) {
        StringBuilder messageBuilder = new StringBuilder(message);
        for (int i = messageBuilder.length(); i < outStrLen - value.length(); i++) {
            messageBuilder.append('_');
        }

        messageBuilder.append(' ');
        message = messageBuilder + value;
        return message;
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
