package net.minezero.disableresources;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class DisableResources extends JavaPlugin implements @NotNull Listener {
    FileConfiguration config;
    List<String> worlds = new ArrayList<>();

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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equals("dresource")) {
            if (!sender.isOp()) {
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage("/dresource add : 現在いるワールドの鉱石を破壊したとき石にします");
                sender.sendMessage("/dresource remove : 現在いるワールドの鉱石を石にする設定を削除します");
                sender.sendMessage("/dresource list : 登録されているワールド一覧を出します");
                return true;
            }
            Player p = (Player)sender;
            if (args[0].equals("add")) {
                worlds.add(p.getLocation().getWorld().getName());
                sender.sendMessage("登録しました");
                return true;
            }
            if (args[0].equals("remove")) {
                if (!worlds.contains(p.getWorld().getName())) {
                    sender.sendMessage("そのワールドは登録されていません");
                    return true;
                }
                worlds.remove(p.getWorld().getName());
                sender.sendMessage("削除しました");
                return true;
            }
            if (args[0].equals("list")) {
                for (String s : worlds) {
                    sender.sendMessage(s);
                }
                return true;
            }
        }
        return true;
    }
}
