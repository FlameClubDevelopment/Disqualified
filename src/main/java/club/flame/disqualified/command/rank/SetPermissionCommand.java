package club.flame.disqualified.command.rank;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.database.redis.payload.Payload;
import club.flame.disqualified.manager.database.redis.payload.RedisMessage;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.utils.lang.Lang;
import club.flame.disqualified.lib.task.TaskUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

public class SetPermissionCommand extends BaseCommand {

    @Command(name = "setpermission", permission = "core.rank.setperm", aliases = {"setperm"}, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs cmd) {
        CommandSender player = cmd.getSender();
        String[] args = cmd.getArgs();
        TaskUtil.runAsync(() -> {
            if (commandGetterWithThreeArgs(player, args, cmd)) {
                return;
            }

            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if (offlinePlayer.isOnline()) {
                PlayerData playerData = PlayerData.getPlayerData(offlinePlayer.getUniqueId());
                setPermission(player, playerData, args[1], args[2].equalsIgnoreCase("true"));
                playerData.loadPermissions(playerData.getPlayer());
            } else {
                player.sendMessage(CC.translate("&aLoading player data..."));
                PlayerData targetData = PlayerData.loadData(offlinePlayer.getUniqueId());
                setPermission(player, targetData, args[1], args[2].equalsIgnoreCase("true"));
            }
        });
    }

    private boolean commandGetterWithThreeArgs(CommandSender player, String[] args, CommandArgs commandArgs) {
        if (args.length == 0) {
            player.sendMessage(CC.translate("&c/" + commandArgs.getLabel() + " <player> <permission> <true/false>"));
            return true;
        }
        if (args.length < 1) {
            player.sendMessage(CC.translate("&cSpecific a player."));
            return true;
        }
        if (args.length < 2) {
            player.sendMessage(CC.translate("&cSpecific a permission."));
            return true;
        }
        if (args.length < 3) {
            player.sendMessage(CC.translate("&ctrue or false"));
            return true;
        }

        return false;
    }

    private void setPermission(CommandSender sender, PlayerData playerData, String permission, boolean set) {
        if (set) {
            if (playerData.hasPermission(permission)) {
                sender.sendMessage(CC.translate(Lang.PREFIX + "&cError! &7That player already has " + permission + " permission."));
                return;
            }
            playerData.getPermissions().add(permission);
            sender.sendMessage(CC.translate(Lang.PREFIX + "&aSuccess! &7Added " + permission + " to " + playerData.getName()));
        } else {
            if (!playerData.hasPermission(permission)) {
                sender.sendMessage(CC.translate(Lang.PREFIX + "&cError! &7That player don't have " + permission + " permission."));
                return;
            }
            playerData.getPermissions().remove(permission);
            sender.sendMessage(CC.translate(Lang.PREFIX + "&aSuccess! &7Removed " + permission + " to " + playerData.getName()));
        }

        Player target = Bukkit.getPlayer(playerData.getName());
        if (target == null) {
            String json = new RedisMessage(Payload.PLAYER_PERMISSION_UPDATE).setParam("NAME", playerData.getName()).setParam("PERMISSION", permission).toJSON();
            if (Disqualified.getInstance().getRedisManager().isActive()) {
                Disqualified.getInstance().getRedisManager().write(json);
            }
        }
        if (playerData.isOnline()) {
            playerData.saveData();
        } else {
            PlayerData.deleteOfflineProfile(playerData);
        }
    }
}
