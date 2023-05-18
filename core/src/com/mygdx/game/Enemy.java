package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.SCR_WIDTH;
import static com.mygdx.game.MyGdxGame.SCR_HEIGHT;

import com.badlogic.gdx.math.MathUtils;

public class Enemy {

    MyGdxGame mgg;
    EnemyType enemyType;
    float width, height;
    float x, y;
    float vx, vy;
    boolean isAlive;

    public Enemy(MyGdxGame mgg, EnemyType enemyType) {
        this.mgg = mgg;
        this.enemyType = enemyType;

        isAlive = true;
        x = SCR_WIDTH;
        y = MathUtils.random(40, SCR_HEIGHT - 40);
        vx = enemyType.velocity;
        vy = 0;
        width = enemyType.sizeY;
        height = enemyType.sizeX;

    }

    void makeStep() {
        x += vx;
        y += vy;
    }

    boolean hit(float pointerX, float pointerY) {
        if (x < pointerX && pointerX < x + width && y < pointerY && pointerY < y + height) {
            isAlive = false;
            vx = 0;
            vy = 0;
            return true;
        }
        return false;
    }

    boolean isAttacking() {
        return x < 100;
    }

}