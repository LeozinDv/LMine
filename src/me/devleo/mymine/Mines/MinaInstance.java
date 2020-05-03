package me.devleo.mymine.Mines;

import com.google.common.io.Resources;
import me.devleo.mymine.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class MinaInstance {

    private String nome;
    private World mundo;
    private int minX;
    private int minY;
    private int minZ;
    private int maxX;
    private int maxY;
    private int maxZ;
    private int tempo;
    private int tempo_temp;
    private int tempo_aviso;
    private boolean silenciada;
    String composicao;

    public MinaInstance(File arquivo) {
        YamlConfiguration file = new YamlConfiguration();
        try {
            String allText = Resources.toString(arquivo.toURI().toURL(), StandardCharsets.UTF_8);
            file.loadFromString(allText);
            file.load(arquivo);

            nome = file.getString("Nome");
            mundo = Bukkit.getWorld(file.getString("Loc.Minimo").split(";")[0]);
            minX = Integer.parseInt(file.getString("Loc.Minimo").split(";")[1]);
            minY = Integer.parseInt(file.getString("Loc.Minimo").split(";")[2]);
            minZ = Integer.parseInt(file.getString("Loc.Minimo").split(";")[3]);
            maxX = Integer.parseInt(file.getString("Loc.Maximo").split(";")[1]);
            maxY = Integer.parseInt(file.getString("Loc.Maximo").split(";")[2]);
            maxZ = Integer.parseInt(file.getString("Loc.Maximo").split(";")[3]);
            tempo = file.getInt("Tempo");
            tempo_temp = tempo;
            tempo_aviso = file.getInt("Tempo_Aviso");
            silenciada = file.getBoolean("Silenciada");
            if (!file.getString("Blocos").equals("--"))
                composicao = file.getString("Blocos");
            else
                Main.plugin.getLogger().warning("A mina " + nome + " nao possui um bloco setado.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
        this.tempo_temp = tempo;
    }

    public void setTempoAviso(int tempo) {
        this.tempo_aviso = tempo;
    }

    public void setSilenciada(boolean silenciada) {
        this.silenciada = silenciada;
    }

    public void setComposicao(String composicao){
        this.composicao = composicao;
    }

    public void temporizador() {
        if (tempo_temp > 0) {
            tempo_temp--;
        }
        if (tempo_temp == tempo_aviso) {
            if (!silenciada) {
                Bukkit.broadcastMessage(Main.plugin.getConfig().getString("Mensagens.Aviso_Resetar").replace("&", "§").replace("@mina", nome).replace("@tempo", "" + tempo_aviso));
            }
        }
        if (tempo_temp == 0) {
            if (!silenciada) {
                Bukkit.broadcastMessage(Main.plugin.getConfig().getString("Mensagens.Aviso_Resetou").replace("&", "§").replace("@mina", nome));
            }
            resetar();
            tempo_temp = tempo;
        }
    }

    public void resetar() {
        for (int x = this.minX; x <= this.maxX; x++) {
            for (int y = this.minY; y <= this.maxY; y++) {
                for (int z = this.minZ; z <= this.maxZ; z++) {
                    mundo.getBlockAt(x, y, z).setTypeIdAndData(Integer.parseInt(composicao.split(":")[0]), (byte) Integer.parseInt(composicao.split(":")[1]), false);
                }
            }
        }
    }
}