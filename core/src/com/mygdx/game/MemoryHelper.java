package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class MemoryHelper {

    private static final Preferences prefs = Gdx.app.getPreferences("User characteristics");

    public static void saveWeaponDamage(int damage) {
        prefs.putString("weaponDamage", String.valueOf(damage));
        prefs.flush();
    }

    public static void saveUserHitPoints(int hitPoints) {
        prefs.putString("userHitPoints", String.valueOf(hitPoints));
        prefs.flush();
    }

    public static void saveWeaponCoolDown(double coolDown) {
        prefs.putString("weaponCoolDown", String.valueOf(coolDown));
        prefs.flush();
    }

    public static void saveUserMoney(int money) {
        prefs.putString("userMoney", String.valueOf(money));
        prefs.flush();
    }

    public static void saveUserMaxPoints(int points) {
        prefs.putString("maxPoints", String.valueOf(points));
        prefs.flush();
    }


    public static int loadWeaponDamage() {
        if (prefs.contains("weaponDamage"))
            return Integer.parseInt(prefs.getString("weaponDamage"));

        saveWeaponDamage(GameSettings.DEFAULT_WEAPON_DAMAGE);
        return GameSettings.DEFAULT_WEAPON_DAMAGE;
    }

    public static int loadUserHitPoints() {
        if (prefs.contains("userHitPoints"))
            return Integer.parseInt(prefs.getString("userHitPoints"));

        saveUserHitPoints(GameSettings.DEFAULT_USER_HP);
        return GameSettings.DEFAULT_USER_HP;
    }

    public static double loadWeaponCoolDown() {
        if (prefs.contains("weaponCoolDown"))
            return Double.parseDouble(prefs.getString("weaponCoolDown"));

        saveWeaponCoolDown(GameSettings.DEFAULT_WEAPON_COOLDOWN);
        return GameSettings.DEFAULT_WEAPON_COOLDOWN;
    }

    public static int loadUserMoney() {
        if (prefs.contains("userMoney"))
            return Integer.parseInt(prefs.getString("userMoney"));

        saveUserMoney(0);
        return 0;
    }

    public static int loadUserMaxPoints() {
        if (prefs.contains("maxPoints"))
            return Integer.parseInt(prefs.getString("maxPoints"));

        saveUserMoney(0);
        return 0;
    }
}
