package club.flame.disqualified.command.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.regex.Pattern;

public class RestrictLinks extends JavaPlugin implements Listener, CommandExecutor {

    private static final Pattern URL_PATTERN = Pattern.compile(
            "((http|https)://)?(www\\.)?[a-zA-Z0-9-]+\\.[a-zA-Z]{2,6}(/[a-zA-Z0-9#]+/?)*");

    private boolean linkRestrictionEnabled;
    private String restrictionMessage;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();

        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        getCommand("restrictlinks").setExecutor(this);

        getLogger().info("RestrictLinks plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("RestrictLinks plugin disabled!");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!linkRestrictionEnabled) {
            return;
        }

        String message = event.getMessage();

        if (URL_PATTERN.matcher(message).find()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', restrictionMessage));
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("restrictlinks")) {
            if (args.length < 1) {
                sender.sendMessage(ChatColor.RED + "Usage: /restrictlinks <on|off>");
                return true;
            }

            if (args[0].equalsIgnoreCase("on")) {
                linkRestrictionEnabled = true;
                sender.sendMessage(ChatColor.GREEN + "Link restriction enabled.");
            } else if (args[0].equalsIgnoreCase("off")) {
                linkRestrictionEnabled = false;
                sender.sendMessage(ChatColor.RED + "Link restriction disabled.");
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /restrictlinks <on|off>");
            }
            return true;
        }
        return false;
    }

    private void loadConfig() {
        linkRestrictionEnabled = getConfig().getBoolean("linkRestrictionEnabled", true);
        restrictionMessage = getConfig().getString("restrictionMessage", "&cLinks are not allowed in the chat.");
    }
}
