package me.devleo.mymine;

import com.google.common.io.Resources;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import me.devleo.mymine.Mines.Manager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class Main extends JavaPlugin {

    public static Main plugin;

    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        try {
            File file = new File(getDataFolder() + File.separator, "config.yml");
            String allText = Resources.toString(file.toURI().toURL(), StandardCharsets.UTF_8);
            getConfig().loadFromString(allText);
            File file2 = new File(Main.plugin.getDataFolder(), "minas");
            if (!file2.exists())
                file2.mkdir();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Manager();
        getCommand("mmine").setExecutor(new Comandos());
        getLogger().info("Plugin ativado com sucesso!");
        getLogger().info("Versao: " + getDescription().getVersion());
        getLogger().info("Autor: " + getDescription().getAuthors());
    }

    public void onDisable() {
        getLogger().info("Plugin desativado com sucesso!");
    }

    public WorldEditPlugin getWorldEdit() {
        Plugin pl = Bukkit.getPluginManager().getPlugin("WorldEdit");
        if (pl instanceof WorldEditPlugin) return (WorldEditPlugin) pl;
        else return null;
    }
}