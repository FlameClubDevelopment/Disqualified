package club.flame.disqualified.command.chat;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.utils.Utils;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import club.flame.disqualified.lib.config.ConfigCursor;
import org.bukkit.entity.Player;

public class StaffChatCommand extends BaseCommand {
    @Command(name = "staffChat", permission = "core.staffChat", aliases = {"sc"}, inGameOnly = true)

    @Override
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor configCursor = new ConfigCursor(Disqualified.getInstance().getSettingsConfig(), "SETTINGS.STAFF-CHAT");
        PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
        if (args.length == 0) {
            playerData.setStaffChat(!playerData.isStaffChat());
            String sound = configCursor.getString("SOUND");
            String status = (playerData.isStaffChat() ? "§aEnabled" : "§cDisabled");
            player.sendMessage(CC.translate(configCursor.getString("TOGGLE").replace("<status>", status)));
            Utils.sendPlayerSound(player, sound);
        }
    }
}
