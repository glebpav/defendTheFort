package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.scrHeight;
import static com.mygdx.game.MyGdxGame.scrWidth;

import com.badlogic.gdx.math.MathUtils;

public class Mosquito {
    float x, y;
    float vx, vy;
    float width, height;
    int faza, nFaz = 10;
    boolean isAlive = true;

    public Mosquito(){
        width = height = MathUtils.random(100, 200);
        x = scrWidth / 2f - width / 2;
        y = scrHeight / 2f - height / 2;
        vx = MathUtils.random(-5f, 5);
        vy = MathUtils.random(-5f, 5);
        faza = MathUtils.random(0, nFaz-1);
    }

    void fly(){
        x += vx;
        y += vy;
        if(isAlive) {
            outOfBounds2();
            changePhase();
        }
    }

    void outOfBounds1(){
        if(x<0 || x>scrWidth-width) vx = -vx;
        if(y<0 || y>scrHeight-height) vy = -vy;
    }

    void outOfBounds2(){
        if(x<0-width) x = scrWidth;
        if(x>scrWidth) x = 0-width;
        if(y<0-height) y = scrHeight;
        if(y>scrHeight) y = 0-height;
    }

    void changePhase(){
        if(++faza == nFaz) faza = 0;
        //faza = ++faza % nFaz;
    }

    boolean isFlip(){
        //if(vx>0) return true;
        //else return false;
        return vx>0;
    }

    boolean hit(float tx, float ty){
        if(x < tx && tx < x+width && y < ty && ty < y+height){
            isAlive = false;
            faza = 10;
            vx = 0;
            vy = -8;
            return true;
        }
        return false;
    }
}
