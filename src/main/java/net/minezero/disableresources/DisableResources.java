package net.minezero.disableresources;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class DisableResources extends JavaPlugin implements Listener {
    FileConfiguration config;
    List<String> worlds = new ArrayList<>();
    List<Material> ores = new ArrayList<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this,this);
        saveDefaultConfig();
        config = getConfig();
        try {
            worlds = config.getStringList("Worlds");
        } catch (NullPointerException e) {
            worlds = new ArrayList<>();
        }
        ores.add(Material.COAL_ORE);
        ores.add(Material.COPPER_ORE);
        ores.add(Material.LAPIS_ORE);
        ores.add(Material.IRON_ORE);
        ores.add(Material.GOLD_ORE);
        ores.add(Material.REDSTONE_ORE);
        ores.add(Material.DIAMOND_ORE);
        ores.add(Material.EMERALD_ORE);
        ores.add(Material.NETHER_GOLD_ORE);
        ores.add(Material.NETHER_QUARTZ_ORE);
        ores.add(Material.ANCIENT_DEBRIS);
        ores.add(Material.DEEPSLATE_COAL_ORE);
        ores.add(Material.DEEPSLATE_COPPER_ORE);
        ores.add(Material.DEEPSLATE_IRON_ORE);
        ores.add(Material.DEEPSLATE_GOLD_ORE);
        ores.add(Material.DEEPSLATE_DIAMOND_ORE);
        ores.add(Material.DEEPSLATE_EMERALD_ORE);
        ores.add(Material.DEEPSLATE_LAPIS_ORE);
        ores.add(Material.DEEPSLATE_REDSTONE_ORE);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equals("dresource")) {
            if (!(sender instanceof Player p)) {
                sender.sendMessage("This command can only be executed by Player.");
                return false;
            }

            if (!p.isOp()) {
                return true;
            }
            if (args.length == 0) {
                p.sendMessage("/dresource add : 現在いるワールドの鉱石を破壊したとき石にします");
                p.sendMessage("/dresource remove [worldName] : 現在いるワールドの鉱石を石にする設定を削除します");
                p.sendMessage("/dresource list : 登録されているワールド一覧を出します");
                return true;
            }

            if (args[0].equals("add")) {

                String currentWorld = p.getWorld().getName();

                if (worlds.contains(currentWorld)) {
                    p.sendMessage("既に登録されたワールドです。");
                    return false;
                }

                worlds.add(p.getLocation().getWorld().getName());
                saveConfig();
                p.sendMessage("登録しました");
                return true;
            }
            if (args[0].equals("remove")) {
                if (args.length != 2) {
                    p.sendMessage("/dresource remove [worldName] : 現在いるワールドの鉱石を石にする設定を削除します");
                    return false;
                }

                String worldName = args[1];
                if (!worlds.contains(worldName)) {
                    p.sendMessage("そのワールドは登録されていません");
                    return true;
                }
                worlds.remove(worldName);
                saveConfig();
                p.sendMessage("削除しました");
                return true;
            }
            if (args[0].equals("list")) {
                for (String s : worlds) {
                    p.sendMessage(s);
                }
                return true;
            }
        }
        return true;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (!worlds.contains(e.getBlock().getWorld().getName())) {
            return;
        }
        if (!ores.contains(e.getBlock().getType())) {
            return;
        }
        e.setDropItems(false);
        e.getPlayer().sendMessage("このワールドで破壊された鉱石は石に変換されます");
        e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.STONE));
    }
}
