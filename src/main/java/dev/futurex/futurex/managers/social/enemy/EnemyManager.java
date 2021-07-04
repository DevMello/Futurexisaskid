package dev.futurex.futurex.managers.social.enemy;

import dev.futurex.futurex.module.modules.client.Social;
import dev.futurex.futurex.managers.ModuleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linustouchtips
 * @since 02/01/2021
 */

public class EnemyManager {
    public static List<Enemy> enemies;

    public EnemyManager() {
        enemies = new ArrayList<>();
    }

    public static List<Enemy> getEnemies() {
        return enemies;
    }

    public static void addEnemy(String name) {
        enemies.add(new Enemy(name));
    }

    public static void removeEnemy(String name) {
        enemies.remove(getEnemyByName(name));
    }

    public static boolean isEnemy(String name) {
        boolean isEnemy = false;

        for (Enemy enemy : getEnemies()) {
            if (enemy.getName().equalsIgnoreCase(name)) {
                isEnemy = true;
                break;
            }
        }

        return isEnemy;
    }

    public static Enemy getEnemyByName(String name) {
        return enemies.stream().filter(friend -> friend.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static boolean isFriendModuleEnabled() {
        return ModuleManager.getModuleByName("Social").isEnabled() && Social.enemies.getValue();
    }
}
