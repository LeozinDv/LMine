package me.devleo.mymine;

import com.sk89q.worldedit.bukkit.selections.Selection;
import me.devleo.mymine.Mines.Manager;
import me.devleo.mymine.Mines.MinaInstance;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class Comandos implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("mmine")) {
            if (!p.hasPermission("mine.admin")) {
                p.sendMessage("§cVocê não possui permissão para isso.");
                return true;
            }
            if (args.length == 0) {
                p.sendMessage("§f§lCOMANDOS MyMine");
                p.sendMessage("  §7/mmine criar [mina] §8- §fCria uma nova mina");
                p.sendMessage("  §7/mmine deletar [mina] §8- §fDeleta uma mina");
                p.sendMessage("  §7/mmine editar [mina] §8- §fEdita uma mina");
                return true;
            }
            if (args[0].equalsIgnoreCase("criar")) {
                if (args.length == 1) {
                    p.sendMessage("§f§lCOMANDOS MyMine");
                    p.sendMessage("  §7/mmine criar [mina] §8- §fCria uma nova mina");
                    p.sendMessage("  §7/mmine deletar [mina] §8- §fDeleta uma mina");
                    p.sendMessage("  §7/mmine editar [mina] §8- §fEdita uma mina");
                    return true;
                }
                String nome = args[1];
                if (new File(Main.plugin.getDataFolder() + File.separator + "minas", nome + ".mina.yml").exists()) {
                    p.sendMessage("§cJá existe uma mina com esse nome.");
                    return true;
                }
                Selection selecao = Main.plugin.getWorldEdit().getSelection(p);
                if (selecao != null) {
                    File mina = new File(Main.plugin.getDataFolder() + File.separator + "minas", nome + ".mina.yml");
                    YamlConfiguration config = new YamlConfiguration();
                    try {
                        config.set("Nome", nome);
                        config.set("Loc.Minimo", selecao.getMinimumPoint().getWorld().getName() + ";" + selecao.getMinimumPoint().getBlockX() + ";" + selecao.getMinimumPoint().getBlockY() + ";" + selecao.getMinimumPoint().getBlockZ());
                        config.set("Loc.Maximo", selecao.getMaximumPoint().getWorld().getName() + ";" + selecao.getMaximumPoint().getBlockX() + ";" + selecao.getMaximumPoint().getBlockY() + ";" + selecao.getMaximumPoint().getBlockZ());
                        config.set("Tempo", 5);
                        config.set("Tempo_Aviso", 1);
                        config.set("Silenciada", false);
                        config.set("Blocos", "--");
                        config.save(mina);
                        MinaInstance min = new MinaInstance(mina);
                        Manager.minas.put(mina.getName(), min);
                        p.sendMessage("§aMina " + nome + " criada com sucesso!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    p.sendMessage("§cSelecione a área da nova mina utilizando WorldEdit.");
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("deletar")) {
                if (args.length == 1) {
                    p.sendMessage("§f§lCOMANDOS MyMine");
                    p.sendMessage("  §7/mmine criar [mina] §8- §fCria uma nova mina");
                    p.sendMessage("  §7/mmine deletar [mina] §8- §fDeleta uma mina");
                    p.sendMessage("  §7/mmine editar [mina] §8- §fEdita uma mina");
                    return true;
                }
                String nome = args[1];
                if (!new File(Main.plugin.getDataFolder() + File.separator + "minas", nome + ".mina.yml").exists()) {
                    p.sendMessage("§cNão existe uma mina com esse nome.");
                    return true;
                }
                File mina = new File(Main.plugin.getDataFolder() + File.separator + "minas", nome + ".mina.yml");
                Manager.minas.remove(mina.getName());
                mina.delete();
                p.sendMessage("§aMina " + nome + " deletada com sucesso!");
            }
            if (args[0].equalsIgnoreCase("editar")) {
                if (args.length < 3) {
                    p.sendMessage("§f§lCOMANDOS MyMine");
                    p.sendMessage("  §7/mmine editar [mina] silenciar §8- §fMuda o status do mudo da mina");
                    p.sendMessage("  §7/mmine editar [mina] bloco <id:data;porcentagem>§8- §fSeta um bloco na mina");
                    p.sendMessage("  §7/mmine editar [mina] tempo <tempo>§8- §fSeta o tempo de reset da mina");
                    p.sendMessage("  §7/mmine editar [mina] tempow <tempo>§8- §fSeta o tempo de aviso da mina");
                    p.sendMessage("  §7/mmine editar [mina] reset §8- §fReseta os dados da mina");
                    return true;
                }
                String nome = args[1];
                if (!new File(Main.plugin.getDataFolder() + File.separator + "minas", nome + ".mina.yml").exists()) {
                    p.sendMessage("§cNão existe uma mina com esse nome.");
                    return true;
                }
                if (args[2].equalsIgnoreCase("silenciar")) {
                    File mina = new File(Main.plugin.getDataFolder() + File.separator + "minas", nome + ".mina.yml");
                    try {
                        YamlConfiguration config = new YamlConfiguration();
                        config.load(mina);
                        if (config.getBoolean("Silenciada")) {
                            config.set("Silenciada", false);
                            config.save(mina);
                            Manager.getMina(mina.getName()).setSilenciada(false);
                            p.sendMessage("§aAgora a mina " + nome + " não está mais silenciada.");
                        } else {
                            config.set("Silenciada", true);
                            config.save(mina);
                            Manager.getMina(mina.getName()).setSilenciada(true);
                            p.sendMessage("§aAgora a mina " + nome + " está silenciada.");
                        }
                    } catch (Exception e) {
                        p.sendMessage("§cNão foi possível alterar a config da mina " + nome + ".");
                        e.printStackTrace();
                    }
                }
                if (args[2].equalsIgnoreCase("bloco")) {
                    if (args.length != 4) {
                        p.sendMessage("§f§lCOMANDOS MyMine");
                        p.sendMessage("  §7/mmine editar [mina] silenciar §8- §fMuda o status do mudo da mina");
                        p.sendMessage("  §7/mmine editar [mina] bloco <id:data;porcentagem>§8- §fSeta um bloco na mina");
                        p.sendMessage("  §7/mmine editar [mina] tempo <tempo>§8- §fSeta o tempo de reset da mina");
                        p.sendMessage("  §7/mmine editar [mina] tempow <tempo>§8- §fSeta o tempo de aviso da mina");
                        p.sendMessage("  §7/mmine editar [mina] reset §8- §fReseta os dados da mina");
                        return true;
                    }
                    String bloco = args[3];
                    if (!bloco.contains(":")) {
                        p.sendMessage("§cFormato de bloco incorreto, utilize ID:Data");
                        return true;
                    }
                    File mina = new File(Main.plugin.getDataFolder() + File.separator + "minas", nome + ".mina.yml");
                    try {
                        YamlConfiguration config = new YamlConfiguration();
                        config.load(mina);
                        config.set("Blocos", bloco);
                        config.save(mina);
                        Manager.getMina(mina.getName()).setComposicao(bloco);
                        p.sendMessage("§aA mina " + nome + " agora é formada por " + new ItemStack(Material.getMaterial(Integer.parseInt(bloco.split(":")[0])), 1, Short.parseShort(bloco.split(":")[1])).getType() + ".");
                    } catch (Exception e) {
                        p.sendMessage("§cNão foi possível alterar a config da mina " + nome + ".");
                        e.printStackTrace();
                    }
                }
                if (args[2].equalsIgnoreCase("tempo")) {
                    if (args.length != 4) {
                        p.sendMessage("§f§lCOMANDOS MyMine");
                        p.sendMessage("  §7/mmine editar [mina] silenciar §8- §fMuda o status do mudo da mina");
                        p.sendMessage("  §7/mmine editar [mina] bloco <id:data;porcentagem>§8- §fSeta um bloco na mina");
                        p.sendMessage("  §7/mmine editar [mina] tempo <tempo>§8- §fSeta o tempo de reset da mina");
                        p.sendMessage("  §7/mmine editar [mina] tempow <tempo>§8- §fSeta o tempo de aviso da mina");
                        p.sendMessage("  §7/mmine editar [mina] reset §8- §fReseta os dados da mina");
                        return true;
                    }
                    try {
                        int numero = Integer.parseInt(args[3]);
                        if (numero < 0) {
                            p.sendMessage("§cO tempo deve ser um número válido.");
                            return true;
                        }
                        File mina = new File(Main.plugin.getDataFolder() + File.separator + "minas", nome + ".mina.yml");
                        YamlConfiguration config = new YamlConfiguration();
                        config.load(mina);
                        config.set("Tempo", numero);
                        config.save(mina);
                        Manager.getMina(mina.getName()).setTempo(numero);
                        p.sendMessage("§aTempo de reset da mina " + nome + " alterado para " + numero + ".");
                    } catch (Exception e) {
                        p.sendMessage("§cNão foi possível alterar a config da mina " + nome + ".");
                        e.printStackTrace();
                    }
                }
                if (args[2].equalsIgnoreCase("tempow")) {
                    if (args.length != 4) {
                        p.sendMessage("§f§lCOMANDOS MyMine");
                        p.sendMessage("  §7/mmine editar [mina] silenciar §8- §fMuda o status do mudo da mina");
                        p.sendMessage("  §7/mmine editar [mina] bloco <id:data;porcentagem>§8- §fSeta um bloco na mina");
                        p.sendMessage("  §7/mmine editar [mina] tempo <tempo>§8- §fSeta o tempo de reset da mina");
                        p.sendMessage("  §7/mmine editar [mina] tempow <tempo>§8- §fSeta o tempo de aviso da mina");
                        p.sendMessage("  §7/mmine editar [mina] reset §8- §fReseta os dados da mina");
                        return true;
                    }
                    try {
                        int numero = Integer.parseInt(args[3]);
                        if (numero < 0) {
                            p.sendMessage("§cO tempo deve ser um número válido.");
                            return true;
                        }
                        File mina = new File(Main.plugin.getDataFolder() + File.separator + "minas", nome + ".mina.yml");
                        YamlConfiguration config = new YamlConfiguration();
                        config.load(mina);
                        config.set("Tempo_Aviso", numero);
                        config.save(mina);
                        Manager.getMina(mina.getName()).setTempoAviso(numero);
                        p.sendMessage("§aTempo de aviso da mina " + nome + " alterado para " + numero + ".");
                    } catch (Exception e) {
                        p.sendMessage("§cNão foi possível alterar a config da mina " + nome + ".");
                        e.printStackTrace();
                    }
                }
                if (args[2].equalsIgnoreCase("reset")) {
                    File mina = new File(Main.plugin.getDataFolder() + File.separator + "minas", nome + ".mina.yml");
                    YamlConfiguration config = new YamlConfiguration();
                    try {
                        config.set("Tempo", 5);
                        config.set("Tempo_Aviso", 1);
                        config.set("Silenciada", false);
                        config.set("Blocos", "--");
                        config.save(mina);
                        p.sendMessage("§aConfiguração da mina " + nome + " resetada com sucesso!");
                    } catch (Exception e) {
                        p.sendMessage("§cNão foi possível alterar a config da mina " + nome + ".");
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
}
