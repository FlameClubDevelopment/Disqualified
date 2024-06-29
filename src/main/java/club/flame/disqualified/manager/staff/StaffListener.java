package club.flame.disqualified.manager.staff;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.database.redis.payload.Payload;
import club.flame.disqualified.manager.database.redis.payload.RedisMessage;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.utils.lang.Lang;
import club.flame.disqualified.lib.task.TaskUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class StaffListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoinStaffEvent(PlayerJoinEvent e) {
        TaskUtil.runAsync(() -> {
            PlayerData playerData = PlayerData.getPlayerData(e.getPlayer().getUniqueId());
            if (playerData == null) return;
            if (!playerData.getPlayer().hasPermission("core.staff")) return;
            if (Disqualified.getInstance().getRedisManager().isActive()) {
                String json;
                String lastServer = playerData.getLastServer() == null ? Lang.SERVER_NAME : playerData.getLastServer();
                if (lastServer.equals(Lang.SERVER_NAME)) {
                    json = new RedisMessage(Payload.STAFF_JOIN)
                            .setParam("STAFF", e.getPlayer().getDisplayName())
                            .setParam("SERVER", Lang.SERVER_NAME).toJSON();
                } else {
                    long time = System.currentTimeMillis() - playerData.getLastServerTime();
                    if (time < (5 * 1000)) { // 5 segundos p cambiar de server
                        json = new RedisMessage(Payload.STAFF_SWITCH)
                                .setParam("STAFF", e.getPlayer().getDisplayName())
                                .setParam("LAST_SERVER", playerData.getLastServer())
                                .setParam("ACTUAL_SERVER", Lang.SERVER_NAME).toJSON();
                    } else {
                        json = new RedisMessage(Payload.STAFF_JOIN)
                                .setParam("STAFF", e.getPlayer().getDisplayName())
                                .setParam("SERVER", Lang.SERVER_NAME).toJSON();
                    }
                }
                Disqualified.getInstance().getRedisManager().write(json);
            } else {
                StaffLang.StaffJoinMessage(e.getPlayer().getDisplayName(), Lang.SERVER_NAME);
            }
        });
    }

    @EventHandler
    public void onLeaveStaffEvent(PlayerQuitEvent e) {
        PlayerData playerData = PlayerData.getPlayerData(e.getPlayer().getUniqueId());
        if (playerData == null) return;
        if (!playerData.getPlayer().hasPermission("core.staff")) return;
        TaskUtil.runAsync(() -> {
            if (Disqualified.getInstance().getRedisManager().isActive()) {
                String json = new RedisMessage(Payload.STAFF_LEAVE)
                        .setParam("STAFF", playerData.getHighestRank().getPrefix() + e.getPlayer().getName())
                        .setParam("SERVER", Lang.SERVER_NAME).toJSON();
                Disqualified.getInstance().getRedisManager().write(json);
            } else {
                StaffLang.StaffLeaveMessage(e.getPlayer().getDisplayName(), Lang.SERVER_NAME);
            }
        });
    }
}
