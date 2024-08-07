package club.flame.disqualified.manager.player;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.event.freeze.PlayerJoinFreezeEvent;
import club.flame.disqualified.manager.event.freeze.PlayerLeaveFreezeEvent;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.task.TaskUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerDataLoad implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        if (!Disqualified.getInstance().isJoinable()) {
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            e.setKickMessage("§cPlease wait to server load.");
            return;
        }

        Player player = Bukkit.getPlayer(e.getUniqueId());
        if (player != null && player.isOnline()) {
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            e.setKickMessage(CC.translate("&cYou tried to login too quickly after disconnecting.\nTry again in a few seconds."));
            Disqualified.getInstance().getServer().getScheduler().runTask(Disqualified.getInstance(), () -> player.kickPlayer(CC.translate("&cDuplicate login :/.")));
            return;
        }

        PlayerData playerData = PlayerData.getPlayerData(e.getUniqueId());
        if (playerData == null) {
            playerData = PlayerData.createPlayerData(e.getUniqueId(), e.getName());
        }
        if (playerData.getBannablePunishment() != null) {
            e.setKickMessage(playerData.getBannablePunishment().toKickMessage(null));
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            return;
        }
        if (playerData.getIp() == null) {
            playerData.setIp(e.getAddress().getHostAddress());
        }
        if (!playerData.getIpAddresses().contains(e.getAddress().getHostAddress())) {
            playerData.getIpAddresses().add(e.getAddress().getHostName());
        }
        if (!playerData.getIp().equalsIgnoreCase(e.getAddress().getHostAddress())) {
            playerData.setIp(e.getAddress().getHostAddress());
        }
        playerData.findAlts();
        for (PlayerData altsData : playerData.getAlts()) {
            if (altsData != null) {
                if (altsData.getBannablePunishment() != null) {
                    e.setKickMessage(altsData.getBannablePunishment().toKickMessage(altsData.getName()));
                    e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                }
            }
        }
        playerData.saveData();
    }


    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerLoginEvent(PlayerLoginEvent e) {
        PlayerData playerData = PlayerData.getPlayerData(e.getPlayer().getUniqueId());
        if (playerData == null) {
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage("§cAn error has ocurred while loading your profile. Please reconnect.");
            return;
        }
        if (!playerData.isDataLoaded()) {
            playerData.saveData();
            e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            e.setKickMessage("§cAn error has ocurred while loading your profile. Please reconnect.");
            return;
        }
        TaskUtil.runAsync(() -> playerData.loadPermissions(e.getPlayer()));
    }


    private void handledSaveDate(Player player) {
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        if (playerData != null) {
            if (playerData.isFreeze()) {
                new PlayerLeaveFreezeEvent(player).call();
            }
            playerData.setLastServerTime(System.currentTimeMillis());
            playerData.removeData();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        PlayerData playerData = PlayerData.getPlayerData(event.getPlayer().getUniqueId());

        TaskUtil.runLaterAsync(() -> {
            if (playerData != null && event.getPlayer() != null) {
                playerData.loadPermissions(event.getPlayer()); //This is because of bungee permissions!
            }
        }, 20L * 2);
        if (playerData.isFreeze()) {
            new PlayerJoinFreezeEvent(playerData.getPlayer()).call();
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e) {
        handledSaveDate(e.getPlayer());
    }

    @EventHandler
    public void onPlayerKickEvent(PlayerKickEvent e) {
        handledSaveDate(e.getPlayer());
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (event.getMessage().equalsIgnoreCase("/gmc") || event.getMessage().equalsIgnoreCase("/gm1")) {
            event.setCancelled(true);
            player.performCommand("gm c");
        } else if (event.getMessage().equalsIgnoreCase("/gms") || event.getMessage().equalsIgnoreCase("/gm0")) {
            event.setCancelled(true);
            player.performCommand("gm s");
        } else if (event.getMessage().equalsIgnoreCase("/gma") || event.getMessage().equalsIgnoreCase("/gm2")) {
            event.setCancelled(true);
            player.performCommand("gm a");
        }
    }
}
