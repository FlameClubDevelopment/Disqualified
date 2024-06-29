package club.flame.disqualified.command.rank.grant;

import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.menu.grant.procedure.GrantMenu;
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

public class GrantCommand extends BaseCommand {

    @Command(name = "grant", permission = "core.rank.grant")

    @Override
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();

        TaskUtil.runAsync(() -> {
            if (args.length == 0) {
                player.sendMessage(CC.translate("&c/" + cmd.getLabel() + "<player>"));
                return;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (target.isOnline()) {
                PlayerData targetData = PlayerData.getPlayerData(Bukkit.getPlayer(args[0]).getUniqueId());
                (new GrantMenu(targetData)).openMenu(player);
            } else {
                player.sendMessage(CC.translate("&aLoading player data..."));
                PlayerData targetData = PlayerData.loadData(target.getUniqueId());
                if (targetData == null) {
                    player.sendMessage(CC.translate("&cError! &7That player doesn't have data."));
                    return;
                }
                (new GrantMenu(targetData)).openMenu(player);
            }
        });
    }
}
