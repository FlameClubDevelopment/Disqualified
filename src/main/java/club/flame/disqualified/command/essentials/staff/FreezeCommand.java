package club.flame.disqualified.command.essentials.staff;

import club.flame.disqualified.manager.event.freeze.PlayerFreezeEvent;
import club.flame.disqualified.manager.event.freeze.PlayerUnFreezeEvent;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

public class FreezeCommand extends BaseCommand implements Listener {

    @Command(name = "freeze", aliases = {"ss", "congelar"}, permission = "core.essentials.report")
    @Override
    public void onCommand(CommandArgs commandArgs) {
        Player player = commandArgs.getPlayer();
        String[] args = commandArgs.getArgs();
        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage /" + commandArgs.getLabel() + " <player>"));
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cError&7! &c" + args[0] + " isn't online."));
            return;
        }
        if (target == player) {
            player.sendMessage(CC.translate("&cError&7! &cYou can't freeze yourself."));
            return;
        }
        PlayerData targetData = PlayerData.getPlayerData(target.getUniqueId());
        if (targetData.isFreeze()) {
            new PlayerUnFreezeEvent(target, player).call();
        } else {
            new PlayerFreezeEvent(target, player).call();
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        // Listener implementation
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        if (playerData.isFreeze()) {
            String message = event.getMessage().toLowerCase();
            if (!message.startsWith("/msg") && !message.startsWith("/message") && !message.startsWith("/r")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        if (playerData.isFreeze()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDmg(EntityDamageEvent event) {
        Player player = (Player) event.getEntity();
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        if (playerData.isFreeze()) {
            event.setCancelled(true);
        }
    }
}
