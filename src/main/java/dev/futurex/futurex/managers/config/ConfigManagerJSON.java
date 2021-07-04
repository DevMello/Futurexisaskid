package dev.futurex.futurex.managers.config;

import com.google.gson.*;
import dev.futurex.futurex.gui.hud.element.HUDElement;
import dev.futurex.futurex.managers.social.enemy.Enemy;
import dev.futurex.futurex.managers.social.enemy.EnemyManager;
import dev.futurex.futurex.managers.social.friend.Friend;
import dev.futurex.futurex.managers.social.friend.FriendManager;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.Setting;
import dev.futurex.futurex.setting.SubSetting;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.checkbox.SubCheckbox;
import dev.futurex.futurex.setting.color.ColorPicker;
import dev.futurex.futurex.setting.keybind.Keybind;
import dev.futurex.futurex.setting.keybind.SubKeybind;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.mode.SubMode;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.setting.slider.SubSlider;
import dev.futurex.futurex.managers.HUDElementManager;
import dev.futurex.futurex.managers.ModuleManager;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author linustouchtips & Hoosiers
 * @since 01/18/2020 n
 */

public class ConfigManagerJSON {

    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void createDirectory() throws IOException {
        if (!Files.exists(Paths.get("futurex/")))
            Files.createDirectories(Paths.get("futurex/"));

        if (!Files.exists(Paths.get("futurex/modules")))
            Files.createDirectories(Paths.get("futurex/modules"));

        if (!Files.exists(Paths.get("futurex/gui")))
            Files.createDirectories(Paths.get("futurex/gui"));

        if (!Files.exists(Paths.get("futurex/social")))
            Files.createDirectories(Paths.get("futurex/social"));
    }

    public static void registerFiles(String name, String path) throws IOException {
        if (!Files.exists(Paths.get("futurex/" + path + "/" + name + ".json")))
            Files.createFile(Paths.get("futurex/" + path + "/" + name + ".json"));

        else {
            File file = new File("futurex/" + path + "/" + name + ".json");
            file.delete();
            Files.createFile(Paths.get("futurex/" + path + "/" + name + ".json"));
        }
    }

    public static void saveConfig() {
        try {
            saveModules();
            saveHUD();
            saveFriends();
            saveEnemies();
        } catch (IOException ignored) {

        }
    }

    public static void loadConfig() {
        try {
            createDirectory();
            loadModules();
            loadHUD();
            loadFriends();
            loadEnemies();
        } catch (IOException ignored) {

        }
    }

    public static void saveModules() throws IOException {
        for (Module module : ModuleManager.getModules()) {
            registerFiles(module.getName(), "modules");
            OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("futurex/modules/" + module.getName() + ".json"), StandardCharsets.UTF_8);

            JsonObject moduleObject = new JsonObject();
            JsonObject settingObject = new JsonObject();
            JsonObject subSettingObject = new JsonObject();

            moduleObject.add("Name", new JsonPrimitive(module.getName()));
            moduleObject.add("Enabled", new JsonPrimitive(module.isEnabled()));
            moduleObject.add("Drawn", new JsonPrimitive(module.isDrawn()));
            moduleObject.add("Bind", new JsonPrimitive(module.getKeybind().getKeyCode()));

            for (Setting setting : module.getSettings()) {
                if (setting instanceof dev.futurex.futurex.setting.checkbox.Checkbox) {
                    settingObject.add(((dev.futurex.futurex.setting.checkbox.Checkbox) setting).getName(), new JsonPrimitive(((dev.futurex.futurex.setting.checkbox.Checkbox) setting).getValue()));

                    if (((dev.futurex.futurex.setting.checkbox.Checkbox) setting).hasSubSettings()) {
                        for (SubSetting subSetting : ((dev.futurex.futurex.setting.checkbox.Checkbox) setting).getSubSettings()) {
                            if (subSetting instanceof SubCheckbox)
                                subSettingObject.add(((SubCheckbox) subSetting).getName(), new JsonPrimitive(((SubCheckbox) subSetting).getValue()));

                            if (subSetting instanceof SubSlider)
                                subSettingObject.add(((SubSlider) subSetting).getName(), new JsonPrimitive(((SubSlider) subSetting).getValue()));

                            if (subSetting instanceof SubMode)
                                subSettingObject.add(((SubMode) subSetting).getName(), new JsonPrimitive(((SubMode) subSetting).getValue()));

                            if (subSetting instanceof SubKeybind)
                                subSettingObject.add(((SubKeybind) subSetting).getName(), new JsonPrimitive(((SubKeybind) subSetting).getKey()));

                            if (subSetting instanceof ColorPicker) {
                                JsonObject subColorObject = new JsonObject();

                                subColorObject.add("Red", new JsonPrimitive(((ColorPicker) subSetting).getRed()));
                                subColorObject.add("Green", new JsonPrimitive(((ColorPicker) subSetting).getGreen()));
                                subColorObject.add("Blue", new JsonPrimitive(((ColorPicker) subSetting).getBlue()));
                                subColorObject.add("Alpha", new JsonPrimitive(((ColorPicker) subSetting).getAlpha()));

                                subSettingObject.add(((ColorPicker) subSetting).getName(), subColorObject);
                            }
                        }
                    }
                }

                if (setting instanceof Slider) {
                    settingObject.add(((Slider) setting).getName(), new JsonPrimitive(((Slider) setting).getValue()));

                    if (((Slider) setting).hasSubSettings()) {
                        for (SubSetting subSetting : ((Slider) setting).getSubSettings()) {
                            if (subSetting instanceof SubCheckbox)
                                subSettingObject.add(((SubCheckbox) subSetting).getName(), new JsonPrimitive(((SubCheckbox) subSetting).getValue()));

                            if (subSetting instanceof SubSlider)
                                subSettingObject.add(((SubSlider) subSetting).getName(), new JsonPrimitive(((SubSlider) subSetting).getValue()));

                            if (subSetting instanceof SubMode)
                                subSettingObject.add(((SubMode) subSetting).getName(), new JsonPrimitive(((SubMode) subSetting).getValue()));

                            if (subSetting instanceof SubKeybind)
                                subSettingObject.add(((SubKeybind) subSetting).getName(), new JsonPrimitive(((SubKeybind) subSetting).getKey()));
                        }
                    }
                }

                if (setting instanceof Mode) {
                    settingObject.add(((Mode) setting).getName(), new JsonPrimitive(((Mode) setting).getValue()));

                    if (((Mode) setting).hasSubSettings()) {
                        for (SubSetting subSetting : ((Mode) setting).getSubSettings()) {
                            if (subSetting instanceof SubCheckbox)
                                subSettingObject.add(((SubCheckbox) subSetting).getName(), new JsonPrimitive(((SubCheckbox) subSetting).getValue()));

                            if (subSetting instanceof SubSlider)
                                subSettingObject.add(((SubSlider) subSetting).getName(), new JsonPrimitive(((SubSlider) subSetting).getValue()));

                            if (subSetting instanceof SubMode)
                                subSettingObject.add(((SubMode) subSetting).getName(), new JsonPrimitive(((SubMode) subSetting).getValue()));

                            if (subSetting instanceof SubKeybind)
                                subSettingObject.add(((SubKeybind) subSetting).getName(), new JsonPrimitive(((SubKeybind) subSetting).getKey()));
                        }
                    }
                }

                if (setting instanceof Keybind) {
                    settingObject.add(((Keybind) setting).getName(), new JsonPrimitive(((Keybind) setting).getKey()));

                    if (((Keybind) setting).hasSubSettings()) {
                        for (SubSetting subSetting : ((Keybind) setting).getSubSettings()) {
                            if (subSetting instanceof SubCheckbox)
                                subSettingObject.add(((SubCheckbox) subSetting).getName(), new JsonPrimitive(((SubCheckbox) subSetting).getValue()));

                            if (subSetting instanceof SubSlider)
                                subSettingObject.add(((SubSlider) subSetting).getName(), new JsonPrimitive(((SubSlider) subSetting).getValue()));

                            if (subSetting instanceof SubMode)
                                subSettingObject.add(((SubMode) subSetting).getName(), new JsonPrimitive(((SubMode) subSetting).getValue()));

                            if (subSetting instanceof SubKeybind)
                                subSettingObject.add(((SubKeybind) subSetting).getName(), new JsonPrimitive(((SubKeybind) subSetting).getKey()));
                        }
                    }
                }
            }

            settingObject.add("SubSettings", subSettingObject);
            moduleObject.add("Settings", settingObject);
            String jsonString = gson.toJson(new JsonParser().parse(moduleObject.toString()));
            fileOutputStreamWriter.write(jsonString);
            fileOutputStreamWriter.close();
        }
    }

    public static void loadModules() {
        for (Module module : ModuleManager.getModules()) {
            if (!Files.exists(Paths.get("futurex/modules/" + module.getName() + ".json")))
                continue;

            JsonObject moduleObject = null;

            try {
                InputStream inputStream = Files.newInputStream(Paths.get("futurex/modules/" + module.getName() + ".json"));
                moduleObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();
            } catch (Exception ignored) {

            }

            assert moduleObject != null;
            if (moduleObject.get("Name") == null || moduleObject.get("Enabled") == null || moduleObject.get("Drawn") == null || moduleObject.get("Bind") == null)
                continue;

            JsonObject settingObject = moduleObject.get("Settings").getAsJsonObject();
            JsonObject subSettingObject = settingObject.get("SubSettings").getAsJsonObject();

            for (Setting setting : module.getSettings()) {
                JsonElement settingValueObject = null;

                if (setting instanceof dev.futurex.futurex.setting.checkbox.Checkbox) {
                    settingValueObject = settingObject.get(((dev.futurex.futurex.setting.checkbox.Checkbox) setting).getName());

                    for (SubSetting subSetting : ((dev.futurex.futurex.setting.checkbox.Checkbox) setting).getSubSettings()) {
                        JsonElement subSettingValueObject = null;

                        JsonElement redValueObject = null;
                        JsonElement greenValueObject = null;
                        JsonElement blueValueObject = null;
                        JsonElement alphaValueObject = null;

                        if (subSetting instanceof SubCheckbox)
                            subSettingValueObject = subSettingObject.get(((SubCheckbox) subSetting).getName());

                        if (subSetting instanceof SubSlider)
                            subSettingValueObject = subSettingObject.get(((SubSlider) subSetting).getName());

                        if (subSetting instanceof SubMode)
                            subSettingValueObject = subSettingObject.get(((SubMode) subSetting).getName());

                        if (subSetting instanceof SubKeybind)
                            subSettingValueObject = subSettingObject.get(((SubKeybind) subSetting).getName());

                        if (subSetting instanceof ColorPicker) {
                            try {
                                if (subSettingObject.get(((ColorPicker) subSetting).getName()).getAsJsonObject() == null)
                                    return;

                                JsonObject subColorObject = subSettingObject.get(((ColorPicker) subSetting).getName()).getAsJsonObject();

                                if (subColorObject.get("Red") == null || subColorObject.get("Green") == null || subColorObject.get("Blue") == null || subColorObject.get("Alpha") == null)
                                    return;

                                redValueObject = subColorObject.get("Red");
                                greenValueObject = subColorObject.get("Green");
                                blueValueObject = subColorObject.get("Blue");
                                alphaValueObject = subColorObject.get("Alpha");
                            } catch (Exception ignored) {

                            }
                        }

                        if (subSettingValueObject != null) {
                            if (subSetting instanceof SubCheckbox)
                                ((SubCheckbox) subSetting).setChecked(subSettingValueObject.getAsBoolean());

                            if (subSetting instanceof SubSlider)
                                ((SubSlider) subSetting).setValue(subSettingValueObject.getAsDouble());

                            if (subSetting instanceof SubMode)
                                ((SubMode) subSetting).setMode(subSettingValueObject.getAsInt());

                            if (subSetting instanceof SubKeybind)
                                ((SubKeybind) subSetting).setKey(subSettingValueObject.getAsInt());
                        }

                        if (redValueObject != null && greenValueObject != null && blueValueObject != null && alphaValueObject != null)
                            ((ColorPicker) subSetting).setColor(new Color(redValueObject.getAsInt(), greenValueObject.getAsInt(), blueValueObject.getAsInt(), alphaValueObject.getAsInt()));
                    }
                }

                if (setting instanceof Slider) {
                    settingValueObject = settingObject.get(((Slider) setting).getName());

                    for (SubSetting subSetting : ((Slider) setting).getSubSettings()) {
                        JsonElement subSettingValueObject = null;

                        if (subSetting instanceof SubCheckbox)
                            subSettingValueObject = subSettingObject.get(((SubCheckbox) subSetting).getName());

                        if (subSetting instanceof SubSlider)
                            subSettingValueObject = subSettingObject.get(((SubSlider) subSetting).getName());

                        if (subSetting instanceof SubMode)
                            subSettingValueObject = subSettingObject.get(((SubMode) subSetting).getName());

                        if (subSetting instanceof SubKeybind)
                            subSettingValueObject = subSettingObject.get(((SubKeybind) subSetting).getName());

                        if (subSettingValueObject != null) {
                            if (subSetting instanceof SubCheckbox)
                                ((SubCheckbox) subSetting).setChecked(subSettingValueObject.getAsBoolean());

                            if (subSetting instanceof SubSlider)
                                ((SubSlider) subSetting).setValue(subSettingValueObject.getAsDouble());

                            if (subSetting instanceof SubMode)
                                ((SubMode) subSetting).setMode(subSettingValueObject.getAsInt());

                            if (subSetting instanceof SubKeybind)
                                ((SubKeybind) subSetting).setKey(subSettingValueObject.getAsInt());
                        }
                    }
                }

                if (setting instanceof Mode) {
                    settingValueObject = settingObject.get(((Mode) setting).getName());

                    for (SubSetting subSetting : ((Mode) setting).getSubSettings()) {
                        JsonElement subSettingValueObject = null;

                        if (subSetting instanceof SubCheckbox)
                            subSettingValueObject = subSettingObject.get(((SubCheckbox) subSetting).getName());

                        if (subSetting instanceof SubSlider)
                            subSettingValueObject = subSettingObject.get(((SubSlider) subSetting).getName());

                        if (subSetting instanceof SubMode)
                            subSettingValueObject = subSettingObject.get(((SubMode) subSetting).getName());

                        if (subSetting instanceof SubKeybind)
                            subSettingValueObject = subSettingObject.get(((SubKeybind) subSetting).getName());

                        if (subSettingValueObject != null) {
                            if (subSetting instanceof SubCheckbox)
                                ((SubCheckbox) subSetting).setChecked(subSettingValueObject.getAsBoolean());

                            if (subSetting instanceof SubSlider)
                                ((SubSlider) subSetting).setValue(subSettingValueObject.getAsDouble());

                            if (subSetting instanceof SubMode)
                                ((SubMode) subSetting).setMode(subSettingValueObject.getAsInt());

                            if (subSetting instanceof SubKeybind)
                                ((SubKeybind) subSetting).setKey(subSettingValueObject.getAsInt());
                        }
                    }
                }

                if (setting instanceof Keybind) {
                    settingValueObject = settingObject.get(((Keybind) setting).getName());

                    for (SubSetting subSetting : ((Keybind) setting).getSubSettings()) {
                        JsonElement subSettingValueObject = null;

                        if (subSetting instanceof SubCheckbox)
                            subSettingValueObject = subSettingObject.get(((SubCheckbox) subSetting).getName());

                        if (subSetting instanceof SubSlider)
                            subSettingValueObject = subSettingObject.get(((SubSlider) subSetting).getName());

                        if (subSetting instanceof SubMode)
                            subSettingValueObject = subSettingObject.get(((SubMode) subSetting).getName());

                        if (subSetting instanceof SubKeybind)
                            subSettingValueObject = subSettingObject.get(((SubKeybind) subSetting).getName());

                        if (subSettingValueObject != null) {
                            if (subSetting instanceof SubCheckbox)
                                ((SubCheckbox) subSetting).setChecked(subSettingValueObject.getAsBoolean());

                            if (subSetting instanceof SubSlider)
                                ((SubSlider) subSetting).setValue(subSettingValueObject.getAsDouble());

                            if (subSetting instanceof SubMode)
                                ((SubMode) subSetting).setMode(subSettingValueObject.getAsInt());

                            if (subSetting instanceof SubKeybind)
                                ((SubKeybind) subSetting).setKey(subSettingValueObject.getAsInt());
                        }
                    }
                }

                if (settingValueObject != null) {
                    if (setting instanceof dev.futurex.futurex.setting.checkbox.Checkbox)
                        ((Checkbox) setting).setChecked(settingValueObject.getAsBoolean());

                    if (setting instanceof Slider)
                        ((Slider) setting).setValue(settingValueObject.getAsDouble());

                    if (setting instanceof Mode)
                        ((Mode) setting).setMode(settingValueObject.getAsInt());

                    if (setting instanceof Keybind)
                        ((Keybind) setting).setKey(settingValueObject.getAsInt());
                }
            }

            module.setEnabled(moduleObject.get("Enabled").getAsBoolean());
            module.setDrawn(moduleObject.get("Drawn").getAsBoolean());
            module.getKeybind().setKeyCode(moduleObject.get("Bind").getAsInt());
        }
    }

    public static void saveHUD() throws IOException {
        registerFiles("HUD", "gui");

        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("futurex/gui/HUD.json"), StandardCharsets.UTF_8);
        JsonObject guiObject = new JsonObject();
        JsonObject hudObject = new JsonObject();

        for (HUDElement component : HUDElementManager.getComponents()) {
            JsonObject positionObject = new JsonObject();

            positionObject.add("x", new JsonPrimitive(component.x));
            positionObject.add("y", new JsonPrimitive(component.y));
            positionObject.add("enabled", new JsonPrimitive(component.isDrawn()));

            hudObject.add(component.getName(), positionObject);
        }

        guiObject.add("Components", hudObject);
        String jsonString = gson.toJson(new JsonParser().parse(guiObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    public static void loadHUD() throws IOException {
        if (!Files.exists(Paths.get("futurex/gui/HUD.json")))
            return;

        InputStream inputStream = Files.newInputStream(Paths.get("futurex/gui/HUD.json"));
        JsonObject guiObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (guiObject.get("Components") == null)
            return;

        JsonObject windowObject = guiObject.get("Components").getAsJsonObject();
        for (HUDElement component : HUDElementManager.getComponents()) {
            if (windowObject.get(component.getName()) == null)
                return;

            JsonObject categoryObject = windowObject.get(component.getName()).getAsJsonObject();

            JsonElement hudXObject = categoryObject.get("x");
            if (hudXObject != null && hudXObject.isJsonPrimitive())
                component.x = hudXObject.getAsInt();

            JsonElement hudYObject = categoryObject.get("y");
            if (hudYObject != null && hudYObject.isJsonPrimitive())
                component.y = hudYObject.getAsInt();

            JsonElement hudEnabledObject = categoryObject.get("enabled");
            if (hudEnabledObject != null && hudEnabledObject.isJsonPrimitive())
                if (hudEnabledObject.getAsBoolean())
                    component.toggleElement();
        }

        inputStream.close();
    }

    public static void saveFriends() throws IOException {
        registerFiles("Friends", "Social");

        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("futurex/social/Friends.json"), StandardCharsets.UTF_8);
        JsonObject mainObject = new JsonObject();
        JsonArray friendArray = new JsonArray();

        for (Friend friend : FriendManager.getFriends()) {
            friendArray.add(friend.getName());
        }

        mainObject.add("Friends", friendArray);
        String jsonString = gson.toJson(new JsonParser().parse(mainObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    public static void loadFriends() throws IOException {
        if (!Files.exists(Paths.get("futurex/social/Friends.json")))
            return;

        InputStream inputStream = Files.newInputStream(Paths.get("futurex/social/Friends.json"));
        JsonObject mainObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (mainObject.get("Friends") == null)
            return;

        JsonArray friendObject = mainObject.get("Friends").getAsJsonArray();

        friendObject.forEach(object -> FriendManager.addFriend(object.getAsString()));

        inputStream.close();
    }

    public static void saveEnemies() throws IOException {
        registerFiles("Enemies", "social");

        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("futurex/social/Enemies.json"), StandardCharsets.UTF_8);
        JsonObject mainObject = new JsonObject();
        JsonArray enemyArray = new JsonArray();

        for (Enemy enemy : EnemyManager.getEnemies()) {
            enemyArray.add(enemy.getName());
        }

        mainObject.add("Enemies", enemyArray);
        String jsonString = gson.toJson(new JsonParser().parse(mainObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    public static void loadEnemies() throws IOException {
        if (!Files.exists(Paths.get("futurex/social/Enemies.json")))
            return;

        InputStream inputStream = Files.newInputStream(Paths.get("futurex/social/Enemies.json"));
        JsonObject mainObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (mainObject.get("Enemies") == null)
            return;

        JsonArray enemyObject = mainObject.get("Enemies").getAsJsonArray();

        enemyObject.forEach(object -> EnemyManager.addEnemy(object.getAsString()));

        inputStream.close();
    }
}