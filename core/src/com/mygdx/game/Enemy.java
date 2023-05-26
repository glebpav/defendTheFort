package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.SCR_WIDTH;
import static com.mygdx.game.MyGdxGame.SCR_HEIGHT;

import com.badlogic.gdx.math.MathUtils;

import com.badlogic.gdx.utils.TimeUtils;

public class Enemy {

    MyGdxGame mgg;
    EnemyType enemyType;
    int enemyHitPoints;
    float width, height;
    float x, y;
    float vx, vy;
    int faza;
    static int nFaz = 5*7;
    boolean isAlive;
    long defTime;

    public Enemy(MyGdxGame mgg, EnemyType enemyType) {
        this.mgg = mgg;
        this.enemyType = enemyType;

        isAlive = true;
        x = SCR_WIDTH;
        y = MathUtils.random(40, SCR_HEIGHT - 40);
        vx = enemyType.velocity;
        vy = 0;
        enemyHitPoints = enemyType.hitPoints;
        width = enemyType.sizeY;
        height = enemyType.sizeX;
        faza = 0;

    }

    void makeStep() {
        x += vx;
        y += vy;
    }

    boolean hit(float pointerX, float pointerY, int damage) {
        if (x < pointerX && pointerX < x + width && y < pointerY && pointerY < y + height) {
            enemyHitPoints -= damage;
            if (enemyHitPoints <= 0) {
                isAlive = false;
                vx = 0;
                vy = 0;
                defTime = TimeUtils.millis();
                return true;
            }
        }
        return false;
    }

    boolean isAttacking() {
        return x < 100;
    }

    void changePhase(){
        if(++faza == nFaz) faza = 0;
        //faza = ++faza % nFaz;
    }

}
