package dev.futurex.futurex.managers.config;

import dev.futurex.futurex.gui.click.Frame;
import dev.futurex.futurex.gui.hud.element.HUDElement;
import dev.futurex.futurex.managers.social.friend.Friend;
import dev.futurex.futurex.managers.social.friend.FriendManager;
import dev.futurex.futurex.module.Module;
import dev.futurex.futurex.setting.Setting;
import dev.futurex.futurex.setting.SubSetting;
import dev.futurex.futurex.setting.checkbox.Checkbox;
import dev.futurex.futurex.setting.checkbox.SubCheckbox;
import dev.futurex.futurex.setting.keybind.Keybind;
import dev.futurex.futurex.setting.keybind.SubKeybind;
import dev.futurex.futurex.setting.mode.Mode;
import dev.futurex.futurex.setting.mode.SubMode;
import dev.futurex.futurex.setting.slider.Slider;
import dev.futurex.futurex.setting.slider.SubSlider;
import dev.futurex.futurex.managers.HUDElementManager;
import dev.futurex.futurex.managers.ModuleManager;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

/**
 * @author bon & linustouchtips
 * @since 12/05/2020
 */

public class ConfigManager {

    public static File config;

    public static void createFile() {
        config = new File("futurex");

        if (!config.exists())
            config.mkdirs();
    }

    public static void saveConfig() {
        saveEnabledModules();
        saveDrawnModules();
        saveSettings();
        saveSubSettings();
        saveFriends();
        saveGUI();
        saveHUDPositions();
        saveHUDElements();
        saveHUDSettings();
        saveBinds();
        saveDrawn();
    }

    public static void loadConfig() {
        createFile();
        loadEnabledModules();
        loadDrawnModules();
        loadSettings();
        loadSubSettings();
        loadFriends();
        loadGUI();
        loadHUDPositions();
        loadHUDElements();
        loadHUDSettings();
        loadBinds();
        loadDrawn();
    }

    public static void saveEnabledModules() {
        try {
            File modules = new File(config.getAbsolutePath(), "EnabledModules.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(modules));
            for (Module m : ModuleManager.getModules()) {
                bw.write(m.getName() + ":");
                if (m.isEnabled()) {
                    bw.write("true");
                } else {
                    bw.write("false");
                }

                bw.write("\r\n");
            } bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadEnabledModules() {
        try {
            File modules = new File(config.getAbsolutePath(), "EnabledModules.txt");
            BufferedReader br = new BufferedReader(new FileReader(modules));
            List<String> linezz = Files.readAllLines(modules.toPath());
            int lineIndex = 0;
            for (String line : linezz) {
                String[] regex = line.split(":");
                Module m = ModuleManager.getModules().get(lineIndex);
                if (regex[0].startsWith(m.getName())) {
                    if (Boolean.parseBoolean(regex[1])) {
                        m.enable();
                    }

                } lineIndex++;
            } br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveDrawnModules() {
        try {
            File modules = new File(config.getAbsolutePath(), "Drawn.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(modules));
            for (Module m : ModuleManager.getModules()) {
                bw.write(m.getName() + ":");
                if (m.isDrawn()) {
                    bw.write("true");
                } else {
                    bw.write("false");
                }

                bw.write("\r\n");
            } bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadDrawnModules() {
        try {
            File modules = new File(config.getAbsolutePath(), "Drawn.txt");
            BufferedReader br = new BufferedReader(new FileReader(modules));
            List<String> linezz = Files.readAllLines(modules.toPath());
            int lineIndex = 0;
            for (String line : linezz) {
                String[] regex = line.split(":");
                Module m = ModuleManager.getModules().get(lineIndex);
                if (regex[0].startsWith(m.getName())) {
                    m.setDrawn(Boolean.parseBoolean(regex[1]));
                } lineIndex++;
            } br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveSettings() {
        try {
            File settings = new File(config.getAbsolutePath(), "Settings.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(settings));
            for (Module m : ModuleManager.getModules()) {
                bw.write(m.getName() + ":");
                for (Setting s : m.getSettings()) {
                    if (s instanceof Checkbox) {
                        bw.write(((Checkbox) s).getValue() + ":");
                    }

                    if (s instanceof Slider) {
                        bw.write(((Slider) s).getValue() + ":");
                    }

                    if (s instanceof Mode) {
                        bw.write(((Mode) s).getValue() + ":");
                    }

                    if (s instanceof Keybind) {
                        bw.write(((Keybind) s).getKey());
                    }
                }

                bw.write("\r\n");
            } bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadSettings() {
        try {
            File settings = new File(config.getAbsolutePath(), "Settings.txt");
            BufferedReader br = new BufferedReader(new FileReader(settings));
            List<String> linezz = Files.readAllLines(settings.toPath());
            int lineIndex = 0;
            for (String line : linezz) {
                String[] regex = line.split(":");
                Module m = ModuleManager.getModules().get(lineIndex);
                if (regex[0].startsWith(m.getName())) {
                    int regexCount = 0;
                    for (Setting s : m.getSettings()) {
                        if (s instanceof Checkbox) {
                            ((Checkbox) s).setChecked(Boolean.parseBoolean(regex[regexCount + 1]));
                            regexCount++;
                        }

                        if (s instanceof Slider) {
                            ((Slider) s).setValue(Double.parseDouble(regex[regexCount + 1]));
                            regexCount++;
                        }

                        if (s instanceof Mode) {
                            ((Mode) s).setMode(Integer.parseInt(regex[regexCount + 1]));
                            regexCount++;
                        }

                        if (s instanceof Keybind) {
                            ((Keybind) s).setKey(Integer.parseInt(regex[regexCount + 1]));
                            regexCount++;
                        }
                    }
                } lineIndex++;
            } br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveSubSettings() {
        try {
            File settings = new File(config.getAbsolutePath(), "SubSettings.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(settings));
            for (Module m : ModuleManager.getModules()) {
                bw.write(m.getName() + ":");
                for (Setting s : m.getSettings()) {
                    if (s instanceof Checkbox) {
                        if (((Checkbox) s).hasSubSettings()) {
                            for (SubSetting ss : ((Checkbox) s).getSubSettings()) {
                                if (ss instanceof SubCheckbox)
                                    bw.write(((SubCheckbox) ss).getValue() + ":");

                                if (ss instanceof SubSlider)
                                    bw.write(((SubSlider) ss).getValue() + ":");

                                if (ss instanceof SubMode)
                                    bw.write(((SubMode) ss).getValue() + ":");

                                if (ss instanceof SubKeybind)
                                    bw.write(((SubKeybind) ss).getKey() + ":");
                            }
                        }
                    }

                    if (s instanceof Slider) {
                        if (((Slider) s).hasSubSettings()) {
                            for (SubSetting ss : ((Slider) s).getSubSettings()) {
                                if (ss instanceof SubCheckbox)
                                    bw.write(((SubCheckbox) ss).getValue() + ":");

                                if (ss instanceof SubSlider)
                                    bw.write(((SubSlider) ss).getValue() + ":");

                                if (ss instanceof SubMode)
                                    bw.write(((SubMode) ss).getValue() + ":");

                                if (ss instanceof SubKeybind)
                                    bw.write(((SubKeybind) ss).getKey() + ":");
                            }
                        }
                    }

                    if (s instanceof Mode) {
                        if (((Mode) s).hasSubSettings()) {
                            for (SubSetting ss : ((Mode) s).getSubSettings()) {
                                if (ss instanceof SubCheckbox)
                                    bw.write(((SubCheckbox) ss).getValue() + ":");

                                if (ss instanceof SubSlider)
                                    bw.write(((SubSlider) ss).getValue() + ":");

                                if (ss instanceof SubMode)
                                    bw.write(((SubMode) ss).getValue() + ":");

                                if (ss instanceof SubKeybind)
                                    bw.write(((SubKeybind) ss).getKey() + ":");
                            }
                        }
                    }

                    if (s instanceof Keybind) {
                        if (((Keybind) s).hasSubSettings()) {
                            for (SubSetting ss : ((Keybind) s).getSubSettings()) {
                                if (ss instanceof SubCheckbox)
                                    bw.write(((SubCheckbox) ss).getValue() + ":");

                                if (ss instanceof SubSlider)
                                    bw.write(((SubSlider) ss).getValue() + ":");

                                if (ss instanceof SubMode)
                                    bw.write(((SubMode) ss).getValue() + ":");

                                if (ss instanceof SubKeybind)
                                    bw.write(((SubKeybind) ss).getKey() + ":");
                            }
                        }
                    }
                }

                bw.write("\r\n");
            } bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadSubSettings() {
        try {
            File settings = new File(config.getAbsolutePath(), "SubSettings.txt");
            BufferedReader br = new BufferedReader(new FileReader(settings));
            List<String> linezz = Files.readAllLines(settings.toPath());
            int lineIndex = 0;
            for (String line : linezz) {
                String[] regex = line.split(":");
                Module m = ModuleManager.getModules().get(lineIndex);
                if (regex[0].startsWith(m.getName())) {
                    int regexCount = 0;
                    for (Setting s : m.getSettings()) {
                        if (m.hasSettings()) {
                            if (s instanceof Checkbox) {
                                if (((Checkbox) s).hasSubSettings()) {
                                    for (SubSetting ss : ((Checkbox) s).getSubSettings()) {
                                        if (ss instanceof SubCheckbox)
                                            ((SubCheckbox) ss).setChecked(Boolean.parseBoolean(regex[regexCount + 1]));

                                        if (ss instanceof SubSlider)
                                            ((SubSlider) ss).setValue(Double.parseDouble(regex[regexCount + 1]));

                                        if (ss instanceof SubMode)
                                            ((SubMode) ss).setMode(Integer.parseInt(regex[regexCount + 1]));

                                        if (ss instanceof SubKeybind)
                                            ((SubKeybind) ss).setKey(Integer.parseInt(regex[regexCount + 1]));

                                        regexCount++;
                                    }
                                }
                            }

                            if (s instanceof Slider) {
                                if (((Slider) s).hasSubSettings()) {
                                    for (SubSetting ss : ((Slider) s).getSubSettings()) {
                                        if (ss instanceof SubCheckbox)
                                            ((SubCheckbox) ss).setChecked(Boolean.parseBoolean(regex[regexCount + 1]));

                                        if (ss instanceof SubSlider)
                                            ((SubSlider) ss).setValue(Double.parseDouble(regex[regexCount + 1]));

                                        if (ss instanceof SubMode)
                                            ((SubMode) ss).setMode(Integer.parseInt(regex[regexCount + 1]));

                                        if (ss instanceof SubKeybind)
                                            ((SubKeybind) ss).setKey(Integer.parseInt(regex[regexCount + 1]));

                                        regexCount++;
                                    }
                                }
                            }

                            if (s instanceof Mode) {
                                for (SubSetting ss : ((Mode) s).getSubSettings()) {
                                    if (((Mode) s).hasSubSettings()) {
                                        if (ss instanceof SubCheckbox)
                                            ((SubCheckbox) ss).setChecked(Boolean.parseBoolean(regex[regexCount + 1]));

                                        if (ss instanceof SubSlider)
                                            ((SubSlider) ss).setValue(Double.parseDouble(regex[regexCount + 1]));

                                        if (ss instanceof SubMode)
                                            ((SubMode) ss).setMode(Integer.parseInt(regex[regexCount + 1]));

                                        if (ss instanceof SubKeybind)
                                            ((SubKeybind) ss).setKey(Integer.parseInt(regex[regexCount + 1]));

                                        regexCount++;
                                    }
                                }
                            }

                            if (s instanceof Mode) {
                                for (SubSetting ss : ((Mode) s).getSubSettings()) {
                                    if (((Mode) s).hasSubSettings()) {
                                        if (ss instanceof SubCheckbox)
                                            ((SubCheckbox) ss).setChecked(Boolean.parseBoolean(regex[regexCount + 1]));

                                        if (ss instanceof SubSlider)
                                            ((SubSlider) ss).setValue(Double.parseDouble(regex[regexCount + 1]));

                                        if (ss instanceof SubMode)
                                            ((SubMode) ss).setMode(Integer.parseInt(regex[regexCount + 1]));

                                        if (ss instanceof SubKeybind)
                                            ((SubKeybind) ss).setKey(Integer.parseInt(regex[regexCount + 1]));

                                        regexCount++;
                                    }
                                }
                            }

                            if (s instanceof Keybind) {
                                for (SubSetting ss : ((Keybind) s).getSubSettings()) {
                                    if (((Keybind) s).hasSubSettings()) {
                                        if (ss instanceof SubCheckbox)
                                            ((SubCheckbox) ss).setChecked(Boolean.parseBoolean(regex[regexCount + 1]));

                                        if (ss instanceof SubSlider)
                                            ((SubSlider) ss).setValue(Double.parseDouble(regex[regexCount + 1]));

                                        if (ss instanceof SubMode)
                                            ((SubMode) ss).setMode(Integer.parseInt(regex[regexCount + 1]));

                                        if (ss instanceof SubKeybind)
                                            ((SubKeybind) ss).setKey(Integer.parseInt(regex[regexCount + 1]));

                                        regexCount++;
                                    }
                                }
                            }
                        }
                    }
                } lineIndex++;
            } br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveFriends() {
        try {
            File friends = new File(config.getAbsolutePath(), "Friends.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(friends));
            for (Friend friend : FriendManager.getFriends()) {
                bw.write(friend.getName());
                bw.write("\r\n");
            }

            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadFriends() {
        try {
            File friends = new File(config.getAbsolutePath(), "Friends.txt");
            BufferedReader br = new BufferedReader(new FileReader(friends));
            List<String> linezz = Files.readAllLines(friends.toPath());
            for (String line : linezz) {
                FriendManager.addFriend(line);
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveGUI() {
        try {
            File guiPos = new File(config.getAbsolutePath(), "ClickGUI.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(guiPos));
            for (Frame w : Frame.frames) {
                bw.write("x:" + w.x + ":" + "y:" + w.y);
                bw.write("\r\n");
            }

            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadGUI() {
        try {
            File guiPos = new File(config.getAbsolutePath(), "ClickGUI.txt");
            BufferedReader br = new BufferedReader(new FileReader(guiPos));
            List<String> linezz = Files.readAllLines(guiPos.toPath());
            int lineIndex = 0;
            for (String line : linezz) {
                String[] regex = line.split(":");
                Frame w = Frame.frames.get(lineIndex);
                w.x = Integer.parseInt(regex[1]);
                w.y = Integer.parseInt(regex[3]);
                lineIndex++;
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveHUDPositions() {
        try {
            File guiPos = new File(config.getAbsolutePath(), "HUDPositions.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(guiPos));
            for (HUDElement component : HUDElementManager.getComponents()) {
                bw.write( component.x + ":" + component.y);
                bw.write("\r\n");
            }

            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadHUDPositions() {
        try {
            File guiPos = new File(config.getAbsolutePath(), "HUDPositions.txt");
            BufferedReader br = new BufferedReader(new FileReader(guiPos));
            List<String> linezz = Files.readAllLines(guiPos.toPath());
            int lineIndex = 0;
            for (String line : linezz) {
                String[] regex = line.split(":");
                HUDElement component = HUDElementManager.getComponents().get(lineIndex);

                component.x = (Integer.parseInt(regex[0]));
                component.y = (Integer.parseInt(regex[1]));

                lineIndex++;
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveHUDElements() {
        try {
            File modules = new File(config.getAbsolutePath(), "HUDElements.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(modules));
            for (HUDElement component : HUDElementManager.getComponents()) {
                bw.write(component.getName() + ":");
                if (component.isDrawn()) {
                    bw.write("true");
                } else {
                    bw.write("false");
                }

                bw.write("\r\n");
            } bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadHUDElements() {
        try {
            File modules = new File(config.getAbsolutePath(), "HUDElements.txt");
            BufferedReader br = new BufferedReader(new FileReader(modules));
            List<String> linezz = Files.readAllLines(modules.toPath());
            int lineIndex = 0;
            for (String line : linezz) {
                String[] regex = line.split(":");
                HUDElement component = HUDElementManager.getComponents().get(lineIndex);
                if (regex[0].startsWith(component.getName())) {
                    component.toggleElement();

                } lineIndex++;
            } br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveHUDSettings() {
        try {
            File settings = new File(config.getAbsolutePath(), "HUDSettings.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(settings));
            for (HUDElement component : HUDElementManager.getComponents()) {
                bw.write(component.getName() + ":");
                for (Setting s : component.settingList) {
                    if (s instanceof Checkbox) {
                        bw.write(((Checkbox) s).getValue() + ":");
                    }

                    if (s instanceof Slider) {
                        bw.write(((Slider) s).getValue() + ":");
                    }

                    if (s instanceof Mode) {
                        bw.write(((Mode) s).getValue() + ":");
                    }
                }

                bw.write("\r\n");
            } bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadHUDSettings() {
        try {
            File settings = new File(config.getAbsolutePath(), "HUDSettings.txt");
            BufferedReader br = new BufferedReader(new FileReader(settings));
            List<String> linezz = Files.readAllLines(settings.toPath());
            int lineIndex = 0;
            for (String line : linezz) {
                String[] regex = line.split(":");
                HUDElement component = HUDElementManager.getComponents().get(lineIndex);
                if (regex[0].startsWith(component.getName())) {
                    int regexCount = 0;
                    for (Setting s : component.settingList) {
                        if (s instanceof Checkbox) {
                            ((Checkbox) s).setChecked(Boolean.parseBoolean(regex[regexCount + 1]));
                            regexCount++;
                        }

                        if (s instanceof Slider) {
                            ((Slider) s).setValue(Double.parseDouble(regex[regexCount + 1]));
                            regexCount++;
                        }

                        if (s instanceof Mode) {
                            ((Mode) s).setMode(Integer.parseInt(regex[regexCount + 1]));
                            regexCount++;
                        }
                    }
                } lineIndex++;
            } br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveBinds() {
        try {
            File binds = new File(config.getAbsolutePath(), "Keybinds.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(binds));
            for (Module m : ModuleManager.getModules()) {
                bw.write(m.getName() + ":" + m.getKeybind().getKeyCode());
                bw.write("\r\n");
            }

            bw.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void loadBinds() {
        try {
            File binds = new File(config.getAbsolutePath(), "Keybinds.txt");
            BufferedReader br = new BufferedReader(new FileReader(binds));
            List<String> linezz = Files.readAllLines(binds.toPath());
            for (String line : linezz) {
                String[] regex = line.split(":");
                Module m = ModuleManager.getModuleByName(regex[0]);
                m.getKeybind().setKeyCode(Integer.parseInt(regex[1]));
            }

            br.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void saveDrawn() {
        try {
            File binds = new File(config.getAbsolutePath(), "Drawn.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(binds));
            for (Module m : ModuleManager.getModules()) {
                bw.write(m.getName() + ":" + m.isDrawn());
                bw.write("\r\n");
            }

            bw.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void loadDrawn() {
        try {
            File binds = new File(config.getAbsolutePath(), "Drawn.txt");
            BufferedReader br = new BufferedReader(new FileReader(binds));
            List<String> linezz = Files.readAllLines(binds.toPath());
            for (String line : linezz) {
                String[] regex = line.split(":");
                Module m = ModuleManager.getModuleByName(regex[0]);
                m.setDrawn(Boolean.parseBoolean(regex[1]));
            }

            br.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}