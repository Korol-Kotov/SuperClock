package me.korolkotov.superclock.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class ChatUtil {

    public static String format(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void sendMessage(CommandSender sender, String text) {
        sender.sendMessage(text);
    }

    public static void sendConfigMessage(CommandSender sender, String path) {
        sendMessage(sender, ConfigManager.instance.get("messages").getString(path));
    }

    public static void sendConfigMessage(CommandSender sender, String path, Map<String, String> args) {
        String result = ConfigManager.instance.get("messages").getString(path);
        for (String key : args.keySet()) {
            result = result.replace(key, args.get(key));
        }
        sendMessage(sender, result);
    }
}
