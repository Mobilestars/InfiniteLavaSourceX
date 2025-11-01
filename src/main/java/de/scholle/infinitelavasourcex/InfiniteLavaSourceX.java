package de.scholle.infinitelavasourcex;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InfiniteLavaSourceX extends JavaPlugin implements TabExecutor {

    private FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        applyGamerule();
        getCommand("infinitelavasource").setExecutor(this);
        getCommand("infinitelavasource").setTabCompleter(this);
        getLogger().info("InfiniteLavaSourceX enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("InfiniteLavaSourceX disabled.");
    }

    private void applyGamerule() {
        boolean enabled = config.getBoolean("enabled", true);
        for (World world : Bukkit.getWorlds()) {
            world.setGameRuleValue("lavaSourceConversion", String.valueOf(enabled));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("infinitelavasourcex.command")) {
            sender.sendMessage("§cYou don't have permission.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§eUsage: /" + label + " <enable|disable|reload>");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "enable":
            case "e":
                config.set("enabled", true);
                saveConfig();
                applyGamerule();
                sender.sendMessage("§aInfiniteLavaSourceX enabled.");
                break;

            case "disable":
            case "d":
                config.set("enabled", false);
                saveConfig();
                applyGamerule();
                sender.sendMessage("§cInfiniteLavaSourceX disabled.");
                break;

            case "reload":
                reloadConfig();
                config = getConfig();
                applyGamerule();
                sender.sendMessage("§eInfiniteLavaSourceX reloaded.");
                break;

            default:
                sender.sendMessage("§eUsage: /" + label + " <enable|disable|reload>");
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("infinitelavasourcex.command")) return new ArrayList<>();
        if (args.length == 1) {
            List<String> completions = Arrays.asList("enable", "disable", "reload", "e", "d");
            String input = args[0].toLowerCase();
            List<String> result = new ArrayList<>();
            for (String c : completions) {
                if (c.startsWith(input)) result.add(c);
            }
            return result;
        }
        return new ArrayList<>();
    }
}
