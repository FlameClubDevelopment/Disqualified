package club.flame.disqualified.command.punishment;

import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.menu.punishments.menus.PunishmentsMenu;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.lib.task.TaskUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

public class CheckPunishmentsCommand extends BaseCommand {

    @Command(name = "check", aliases = {"checkplayerpunishemnts", "punishments", "c", "history"}, permission = "core.punishments.check")
    @Override
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();

        TaskUtil.runAsync(() -> {
            if (args.length == 0) {
                player.sendMessage(CC.translate("&c/" + cmd.getLabel() + " <player>"));
                return;
            }
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            PlayerData data;
            if (offlinePlayer.isOnline()) {
                data = PlayerData.getPlayerData(offlinePlayer.getUniqueId());
            } else {
                player.sendMessage(CC.translate("&aLoading player data..."));
                data = PlayerData.loadData(offlinePlayer.getUniqueId());
                if (data == null) {
                    player.sendMessage(CC.translate("&cError! &7That player doesn't have data"));
                    return;
                }
                data.findAlts();
            }
            new PunishmentsMenu(data).openMenu(player);
        });
    }
}
