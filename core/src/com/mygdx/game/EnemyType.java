package com.mygdx.game;

public enum EnemyType {

    EASY(
            "EASY",
            GameSettings.EASY_ENEMY_HP,
            GameSettings.EASY_ENEMY_DAMAGE,
            -1.5f,
            50,
            50,
            GameSettings.EASY_ENEMY_MONEY_REWARD
    ),
    HARD(
            "HARD",
            GameSettings.HARD_ENEMY_HP,
            GameSettings.HARD_ENEMY_DAMAGE,
            -1,
            75,
            75,
            GameSettings.HARD_ENEMY_MONEY_REWARD
    );

    String name;
    int hitPoints;
    int damage;
    float velocity;
    int sizeX;
    int sizeY;
    int moneyReward;

    EnemyType(String name, int hitPoints, int damage, float velocity, int sizeX, int sizeY, int moneyReward) {
        this.name = name;
        this.hitPoints = hitPoints;
        this.damage = damage;
        this.velocity = velocity;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.moneyReward = moneyReward;
    }
}
