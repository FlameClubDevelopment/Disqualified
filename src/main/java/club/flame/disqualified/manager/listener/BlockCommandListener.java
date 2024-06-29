package club.flame.disqualified.manager.listener;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.lib.chat.CC;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class BlockCommandListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e) {
        List<String> blockedCommand = Disqualified.getInstance().getSettingsConfig().getConfiguration().getStringList("SETTINGS.COMMANDS-BLOCKED.LIST");

        blockedCommand.forEach(cmd -> {
            String command = e.getMessage().replace("/", "").replaceAll("(?i)bukkit:", "");
            String cmdXD = cmd.replace("/", "");
            if (command.equalsIgnoreCase(cmdXD)) {
                if (e.getPlayer().hasPermission("core.blocked.bypass")) return;
                if (e.getPlayer().isOp()) return;
                e.setCancelled(true);
                e.getPlayer().sendMessage(CC.translate(Disqualified.getInstance().getSettingsConfig().getConfiguration().getString("SETTINGS.COMMANDS-BLOCKED.MSG")));
            }
        });
    }
}
