package club.flame.disqualified.command.rank.grant;

import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.menu.grant.grants.GrantsMenu;
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

public class GrantsCommand extends BaseCommand {
    @Command(name = "grants", permission = "core.rank.grants")
    @Override
    public void onCommand(CommandArgs cmd) {
        String[] args = cmd.getArgs();
        Player player = cmd.getPlayer();

        TaskUtil.runAsync(() -> {
            if (args.length == 0) {
                player.sendMessage(CC.translate("&c/grants <player>"));
                return;
            }
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (target.isOnline()) {
                PlayerData targetData = PlayerData.getPlayerData(target.getName());
                (new GrantsMenu(targetData)).openMenu(player);
            } else {
                player.sendMessage(CC.translate("&aLoading player data..."));
                PlayerData targetData = PlayerData.loadData(target.getUniqueId());
                if (targetData == null) {
                    player.sendMessage(CC.translate("&cThat player doesn't have data"));
                    return;
                }
                (new GrantsMenu(targetData)).openMenu(player);
            }
        });
    }
}
