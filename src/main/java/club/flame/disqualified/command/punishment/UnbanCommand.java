package club.flame.disqualified.command.punishment;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.database.redis.payload.Payload;
import club.flame.disqualified.manager.database.redis.payload.RedisMessage;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.manager.player.punishments.Punishment;
import club.flame.disqualified.manager.player.punishments.PunishmentType;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.utils.Utils;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.utils.punishment.PunishmentUtil;
import club.flame.disqualified.lib.task.TaskUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

public class UnbanCommand extends BaseCommand {

    @Command(name = "unban", inGameOnly = false, permission = "core.punishments.unban")
    @Override
    public void onCommand(CommandArgs cmd) {
        CommandSender commandSender = cmd.getSender();
        String[] args = cmd.getArgs();

        TaskUtil.runAsync(() -> {
            if (args.length == 0) {
                commandSender.sendMessage(CC.translate("&c/" + cmd.getLabel() + " <player> [reason] [-s]"));
                return;
            }
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            PlayerData data;
            if (offlinePlayer.isOnline()) {
                data = PlayerData.getPlayerData(offlinePlayer.getUniqueId());
            } else {
                commandSender.sendMessage(CC.translate("&aLoading player data..."));
                data = PlayerData.loadData(offlinePlayer.getUniqueId());
                if (data == null) {
                    commandSender.sendMessage(CC.translate("&cError! &7That player doesn't have data"));
                    return;
                }
            }
            Punishment punishment = data.getActivePunishment(PunishmentType.BAN);
            if (punishment == null) {
                commandSender.sendMessage(CC.translate("&cError! &7" + data.getName() + " isn't banned."));
                return;
            }
            String reason;
            boolean silent;
            if (args.length == 1) {
                reason = "No Reason Provided";
                silent = false;
            } else {
                reason = PunishmentUtil.reasonBuilder(args, 1);
                silent = reason.contains("-S") || reason.contains("-SILENT") || reason.contains("-s") || reason.contains("-silent");
                reason = PunishmentUtil.getReasonAndRemoveSilent(reason);
            }
            punishment.setPardonedAt(System.currentTimeMillis());
            punishment.setPardonedReason(reason);
            punishment.setPardoned(true);
            if (commandSender instanceof Player) {
                punishment.setPardonedBy(((Player) commandSender).getUniqueId());
            }
            data.saveData();

            if (Disqualified.getInstance().getRedisManager().isActive()) {
                String json = new RedisMessage(Payload.PUNISHMENTS_ADDED)
                        .setParam("PUNISHMENT", PunishmentUtil.serializePunishment(punishment))
                        .setParam("STAFF", Utils.getStaffName(commandSender))
                        .setParam("TARGET", data.getName())
                        .setParam("TARGET_IP", data.getIp())
                        .setParam("TARGET_UUID", data.getUuid().toString())
                        .setParam("SILENT", String.valueOf(silent))
                        .toJSON();
                Disqualified.getInstance().getRedisManager().write(json);
            } else {
                punishment.broadcast(Utils.getStaffName(commandSender), data.getName(), silent);
            }
            if (!data.isOnline()) {
                data.removeData();
            }
        });
    }
}

