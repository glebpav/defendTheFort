package com.mygdx.game;

import com.badlogic.gdx.utils.TimeUtils;

public class GameState {

    public static final int PLAY_GAME = 0, ENTER_NAME = 1, GAME_OVER = 2;

    int gameSate;
    int earnedMoney;
    int killsPoints;
    int userLeftHitPoints;
    int userMaxHitPoints;
    int userWeaponDamage;
    double userWeaponCoolDown;

    long startTime;
    long currentTime;
    long lastShotTime;
    long lastEnemySpawnTime;

    public GameState() {
        userWeaponDamage = MemoryHelper.loadWeaponDamage();
        userMaxHitPoints = MemoryHelper.loadUserHitPoints();
        userWeaponCoolDown = MemoryHelper.loadWeaponCoolDown();
        userLeftHitPoints = userMaxHitPoints;
        gameSate = PLAY_GAME;
        earnedMoney = 0;
        killsPoints = 0;
        lastShotTime = TimeUtils.millis();
        lastEnemySpawnTime = TimeUtils.millis();
        startTime = TimeUtils.millis();
    }

    public void updateTime() {
        if (gameSate == PLAY_GAME) currentTime = TimeUtils.millis();
    }

    public void missedEnemy(int enemyDamage) {
        userLeftHitPoints -= enemyDamage;
        if (userLeftHitPoints <= 0) {
            userMaxHitPoints = 0;
            gameSate = GAME_OVER;
            saveStatistic();
        }
    }

    public void killedEnemy(EnemyType enemyType) {
        killsPoints += enemyType.hitPoints;
        earnedMoney += enemyType.moneyReward;
    }

    public boolean isWeaponAvailable() {
        return (currentTime - lastShotTime) > userWeaponCoolDown * 1000;
    }

    public boolean isTimeToSpawnEnemy() {
        long sessionTime = currentTime - startTime;
        if (currentTime - lastEnemySpawnTime > (100 / (Math.sqrt(sessionTime) + 10)) * 1000) {
            lastEnemySpawnTime = currentTime;
            return true;
        }
        return false;
    }

    public double getLeftWeaponCoolDown() {
        return userWeaponCoolDown * 1000 - (currentTime - lastShotTime);
    }

    public String getSessionTime() {
        long sessionTime = currentTime - startTime;
        String min = "" + sessionTime / 1000 / 60 / 10 + sessionTime / 1000 / 60 % 10;
        String sec = "" + sessionTime / 1000 % 60 / 10 + sessionTime / 1000 % 60 % 10;
        return min + ":" + sec;
    }

    public void updateShotTime() {
        lastShotTime = TimeUtils.millis();
    }

    public void saveStatistic() {
        int userMoney = earnedMoney + MemoryHelper.loadUserMoney();
        MemoryHelper.saveUserMoney(userMoney);
        int maxPoints = MemoryHelper.loadUserMaxPoints();
        if (maxPoints < killsPoints) MemoryHelper.saveUserMaxPoints(killsPoints);
    }

}
