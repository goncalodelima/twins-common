package com.twins.common.util;

import com.twins.common.model.user.LanguageType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Configuration extends YamlConfiguration {

    private final File file;
    private final JavaPlugin plugin;
    private final String name;
    private final String directory;

    public Configuration(JavaPlugin plugin, String directory, String name) {
        this.directory = directory;
        file = new File((this.plugin = plugin).getDataFolder() + File.separator + this.directory, this.name = name);
    }

    public void reloadConfig() {
        try {
            load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDefaultConfig() {
        plugin.saveResource(this.directory + File.separator + name, false);
        reloadConfig();
    }

    public String getString(LanguageType type, String path) {
        return getString(type.name() + "." + path, "§cContact an administrator. (need §f" + type + File.separator + directory + File.separator + name + ": " + path + "§c)");
    }

    public int getInt(LanguageType type, String path) {
        return getInt(type.name() + "." + path);
    }

    public List<String> getStringList(LanguageType type, String path) {
        return getStringList(type.name() + "." + path);
    }

    public List<Integer> getIntegerList(LanguageType type, String path) {
        return getIntegerList(type.name() + "." + path);
    }

}
