package club.flame.disqualified.menu.grant;

import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.manager.player.grants.GrantProcedureState;
import club.flame.disqualified.menu.grant.procedure.GrantConfirmMenu;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.utils.time.DateUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

public class GrantListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void handleGrantProcedure(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        String message = ChatColor.stripColor(event.getMessage());
        if (playerData == null) {
            return;
        }
        if (playerData.getGrantProcedure() == null || playerData.getGrantProcedure().getGrantProcedureState() == GrantProcedureState.START) {
            return;
        }
        if (playerData.getGrantProcedure().getGrantProcedureState() == GrantProcedureState.DURATION) {
            long duration;
            event.setCancelled(true);
            if (message.equalsIgnoreCase("perm") || message.equalsIgnoreCase("permanent")) {
                playerData.getGrantProcedure().setEnteredDuration(1L);
                playerData.getGrantProcedure().setPermanent(true);
                player.sendMessage(CC.translate("&aSuccess! &7You have been duration to &a" + playerData.getGrantProcedure().getNiceDuration()));
                playerData.getGrantProcedure().setGrantProcedureState(GrantProcedureState.REASON);
                player.sendMessage(CC.translate("&aPlease type a reason for grant."));
                player.playSound(player.getLocation(), Sound.NOTE_PLING, 2F, 2F);

                return;
            }

            duration = DateUtils.getDuration(message);
            if (!(duration > 0)) {
                player.sendMessage(CC.translate("&cThe duration isn't valid."));
                return;
            }

            playerData.getGrantProcedure().setEnteredDuration(duration);
            player.sendMessage(CC.translate("&aSuccess! &7You have been duration to &a" + playerData.getGrantProcedure().getNiceDuration()));
            playerData.getGrantProcedure().setGrantProcedureState(GrantProcedureState.REASON);
            player.sendMessage(CC.translate("&aPlease type a reason for grant."));
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 2F, 2F);
        } else if (playerData.getGrantProcedure().getGrantProcedureState() == GrantProcedureState.REASON) {
            event.setCancelled(true);
            playerData.getGrantProcedure().setEnteredReason(message);
            playerData.getGrantProcedure().setGrantProcedureState(GrantProcedureState.CONFIRMATION);
            new GrantConfirmMenu().openMenu(player);
        }
    }
}
