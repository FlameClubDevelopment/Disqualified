package club.flame.disqualified.manager.database.redis.payload;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.manager.player.grants.Grant;
import club.flame.disqualified.manager.player.punishments.Punishment;
import club.flame.disqualified.manager.ranks.Rank;
import club.flame.disqualified.manager.staff.StaffLang;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.config.ConfigCursor;
import club.flame.disqualified.utils.punishment.PunishmentUtil;
import club.flame.disqualified.lib.task.TaskUtil;
import com.google.gson.Gson;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.JedisPubSub;

import java.util.Objects;
import java.util.UUID;

public class RedisListener extends JedisPubSub {
    @Override
    public void onMessage(String channel, String message) {
        try {
            new BukkitRunnable() {
                @Override
                public void run() {
                    RedisMessage redisMessage = new Gson().fromJson(message, RedisMessage.class);
                    switch (redisMessage.getPayload()) {
                        case SERVER_MANAGER: {
                            String server = redisMessage.getParam("SERVER");
                            String status = redisMessage.getParam("STATUS");
                            if (status.equalsIgnoreCase("online")) {
                                status = "&aonline";
                            } else {
                                status = "&coffline";
                            }
                            String format = CC.translate(Disqualified.getInstance().getMessagesConfig().getConfiguration().getString("NETWORK.SERVER-MANAGER.FORMAT")
                                    .replace("<prefix>", CC.translate(Disqualified.getInstance().getMessagesConfig().getConfiguration().getString("NETWORK.SERVER-MANAGER.PREFIX")))
                                    .replace("<server>", server)
                                    .replace("<status>", status));
                            StaffLang.sendRedisServerMsg(format);
                            break;
                        }
                        case STAFF_JOIN: {
                            String player = redisMessage.getParam("STAFF");
                            String server = redisMessage.getParam("SERVER");
                            StaffLang.StaffJoinMessage(player, server);
                            break;
                        }
                        case STAFF_LEAVE: {
                            String player = redisMessage.getParam("STAFF");
                            String server = redisMessage.getParam("SERVER");
                            StaffLang.StaffLeaveMessage(player, server);
                            break;
                        }
                        case STAFF_SWITCH: {
                            String player = redisMessage.getParam("STAFF");
                            String last_server = redisMessage.getParam("LAST_SERVER");
                            String actual_server = redisMessage.getParam("ACTUAL_SERVER");
                            StaffLang.StaffSwitchMessage(player, last_server, actual_server);
                            break;
                        }
                        case STAFF_CHAT: {
                            ConfigCursor configCursor = new ConfigCursor(Disqualified.getInstance().getSettingsConfig(), "SETTINGS.STAFF-CHAT");
                            String server = redisMessage.getParam("SERVER");
                            String player = redisMessage.getParam("PLAYER");
                            String msg = redisMessage.getParam("TEXT");
                            String format = CC.translate(configCursor.getString("FORMAT")
                                    .replace("<server>", server)
                                    .replace("<player>", player)
                                    .replace("<text>", msg));
                            StaffLang.sendStaffChat(format);
                            break;
                        }
                        case ADMIN_CHAT: {
                            ConfigCursor configCursor = new ConfigCursor(Disqualified.getInstance().getSettingsConfig(), "SETTINGS.ADMIN-CHAT");
                            String server = redisMessage.getParam("SERVER");
                            String player = redisMessage.getParam("PLAYER");
                            String msg = redisMessage.getParam("TEXT");
                            String format = CC.translate(configCursor.getString("FORMAT")
                                    .replace("<server>", server)
                                    .replace("<player>", player)
                                    .replace("<text>", msg));
                            StaffLang.sendAdminChat(format);
                            break;
                        }
                        case REPORT: {
                            String sender = redisMessage.getParam("SENDER");
                            String target = redisMessage.getParam("TARGET");
                            String server = redisMessage.getParam("SERVER");
                            String reason = redisMessage.getParam("REASON");

                            StaffLang.sendReport(sender, target, server, reason);
                            break;
                        }
                        case RANK_UPDATE_PERMS: {
                            Rank rank = Rank.getRankByName(redisMessage.getParam("RANK"));
                            Bukkit.getOnlinePlayers().forEach(player -> {
                                PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
                                if (playerData != null) {
                                    if (rank != null) {
                                        if (playerData.hasRank(rank)) {
                                            playerData.refreshPlayer(playerData.getPlayer());
                                        }
                                    }
                                }
                            });
                            break;
                        }
                        case RANK_DELETE: {
                            Rank rank = Rank.getRankByName(redisMessage.getParam("RANK"));
                            if (rank != null) {
                                Bukkit.getOnlinePlayers().forEach(player -> {
                                    PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
                                    if (playerData != null) {
                                        if (playerData.hasRank(rank)) {
                                            playerData.deleteRank(playerData.getPlayer(), rank);
                                        }
                                    }
                                });
                                Document document = Disqualified.getInstance().getMongoManager().getRanksData().find(Filters.eq("NAME", rank.getName())).first();
                                if (document != null) {
                                    Disqualified.getInstance().getMongoManager().getRanksData().deleteOne(document);
                                }
                                Rank.ranks.remove(rank);
                            }
                            break;
                        }
                        case PLAYER_PERMISSION_UPDATE: {
                            Player player = Bukkit.getPlayer(redisMessage.getParam("NAME"));
                            String permission = redisMessage.getParam("PERMISSION");
                            if (player != null) {
                                PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
                                if (playerData != null) {
                                    playerData.getPermissions().add(permission);
                                    playerData.loadPermissions(player);
                                }
                            }
                        }
                        break;
                        case GRANT_ADD: {
                            Player player = Bukkit.getPlayer(redisMessage.getParam("NAME"));
                            if (player != null) {
                                player.sendMessage(CC.translate(redisMessage.getParam("MESSAGE")));
                            }
                        }
                        break;
                        case GRANT_UPDATE: {
                            Player player = Bukkit.getPlayer(redisMessage.getParam("NAME"));
                            String grantMessage = redisMessage.getParam("GRANT");
                            String[] grantsSplit = grantMessage.split(";");
                            if (player != null) {
                                PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
                                if (playerData != null) {
                                    Grant grant = new Grant(
                                            grantsSplit[0],
                                            Long.parseLong(grantsSplit[1]),
                                            Long.parseLong(grantsSplit[2]),
                                            Long.parseLong(grantsSplit[3]),
                                            grantsSplit[4],
                                            grantsSplit[5],
                                            grantsSplit[6],
                                            Boolean.parseBoolean(grantsSplit[7]),
                                            Boolean.parseBoolean(grantsSplit[8]),
                                            grantsSplit[9]);
                                    if (!playerData.getGrants().contains(grant)) {
                                        playerData.getGrants().add(grant);
                                    }
                                    TaskUtil.runAsync(() -> playerData.loadPermissions(player));
                                }
                            }
                        }
                        break;
                        case PUNISHMENTS_ADDED:
                            Punishment punishment = PunishmentUtil.getPunishmentToString(redisMessage.getParam("PUNISHMENT"));
                            String staffName = redisMessage.getParam("STAFF");
                            String targetName = redisMessage.getParam("TARGET");
                            UUID uuid = UUID.fromString(redisMessage.getParam("TARGET_UUID"));
                            String targetIP = redisMessage.getParam("TARGET_IP");
                            boolean silent = Boolean.parseBoolean(redisMessage.getParam("SILENT"));
                            punishment.broadcast(staffName, targetName, silent);
                            if (punishment.getType().isBannable()) {
                                for (Player player : Disqualified.getInstance().getServer().getOnlinePlayers()) {
                                    if (player.getAddress().getAddress().getHostAddress().equalsIgnoreCase(targetIP)) {
                                        TaskUtil.run(() -> player.kickPlayer(punishment.toKickMessage(CC.strip(targetName).equalsIgnoreCase(player.getName()) ? null : targetName)));
                                    }
                                }
                            }

                            Player player = Disqualified.getInstance().getServer().getPlayer(uuid);
                            if (player != null) {
                                PlayerData profile = PlayerData.getPlayerData(player.getUniqueId());
                                profile.getPunishments().removeIf(other -> Objects.equals(other, punishment));
                                profile.getPunishments().add(punishment);
                            }
                            break;
                        default:
                            Disqualified.getInstance().getLogger().info("[Redis] The message was received, but there was no response");
                            break;
                    }
                }
            }.runTask(Disqualified.getInstance());
        } catch (Exception e) {
            Disqualified.getInstance().getLogger().info("[RedisListener] Exception during server shutdown");
            Disqualified.getInstance().getLogger().info("[RedisListener] Related to -> Trying to send Redis packet when the server is offline.");
        }
    }
}
