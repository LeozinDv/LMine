package me.devleo.mymine.Mines;

import me.devleo.mymine.Main;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.HashMap;

public class Manager {

    public static HashMap<String, MinaInstance> minas = new HashMap<>();

    public Manager() {
        Main.plugin.getLogger().info("Carregando minas...");
        File[] arquivos = (new File(Main.plugin.getDataFolder(), "minas")).listFiles();
        assert arquivos != null;
        for (File arquivo : arquivos) {
            if (arquivo.getName().contains(".mina.yml")) {
                MinaInstance mina = new MinaInstance(arquivo);
                minas.put(arquivo.getName(), mina);
            }
        }
        Main.plugin.getLogger().info(minas.size() + " minas carregadas com sucesso!");
        start();
    }

    public static MinaInstance getMina(String id) {
        return minas.get(id);
    }

    void start() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.plugin, () -> minas.values().forEach(MinaInstance::temporizador), 60 * 20L, 60 * 20L);
    }
}
