package club.flame.disqualified.command.rank;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.manager.ranks.Rank;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.utils.Utils;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.utils.time.DateUtils;
import club.flame.disqualified.lib.task.TaskUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

public class SetRankCommand extends BaseCommand {

    @Command(name = "setrank", permission = "core.rank.setrank", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs cmd) {
        CommandSender player = cmd.getSender();
        String[] args = cmd.getArgs();

        TaskUtil.runAsync(() -> {
            if (args.length < 4) {
                player.sendMessage(CC.translate("&c/setrank <player> <rank> <duration> <reason>"));
                return;
            }
            if (!Rank.isRankExist(args[1])) {
                player.sendMessage(CC.translate("&cThat rank doesn't exists"));
                return;
            }

            Rank rankData = Rank.getRankByName(args[1]);
            String durationTime = "";
            long duration = -1L;
            if (args[2].equalsIgnoreCase("perm") || args[2].equalsIgnoreCase("permanent")) {
                durationTime = "permanent";
            } else {
                try {
                    duration = DateUtils.getDuration(args[2]);
                } catch (Exception e) {
                    player.sendMessage(CC.translate("&cThe duration isn't valid."));
                    return;
                }
            }

            String reason = Utils.buildMessage(args, 3);
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (target.isOnline()) {
                PlayerData targetData = PlayerData.getPlayerData(target.getUniqueId());
                if (targetData.hasRank(rankData)) {
                    player.sendMessage(CC.translate("&cError! &7That player already have " + rankData.getColor() + rankData.getName() + " &7rank."));
                    return;
                }
                Disqualified.getInstance().getRankManager().giveRank(player, targetData, duration, durationTime.equalsIgnoreCase("permanent"), reason, rankData, "Global");
            } else {
                player.sendMessage(CC.translate("&aLoading player data..."));
                if (!PlayerData.hasData(target.getName())) {
                    player.sendMessage(CC.translate("&cThat player doesn't have data"));
                    return;
                }

                PlayerData targetData = PlayerData.loadData(target.getUniqueId());
                if (targetData != null) {
                    if (targetData.hasRank(rankData)) {
                        player.sendMessage(CC.translate("&cError! &7That player already have " + rankData.getColor() + rankData.getName() + " &7rank."));
                        targetData.removeData();
                        return;
                    }
                    Disqualified.getInstance().getRankManager().giveRank(player, targetData, duration, durationTime.equalsIgnoreCase("permanent"), reason, rankData, "Global");
                    targetData.removeData();
                }
            }
        });
    }
}
