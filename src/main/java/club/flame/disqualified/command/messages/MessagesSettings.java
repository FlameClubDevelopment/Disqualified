package club.flame.disqualified.command.messages;

import club.flame.disqualified.Disqualified;
import club.flame.disqualified.manager.player.PlayerData;
import club.flame.disqualified.menu.chat.MessagesSettingsMenu;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessagesSettings extends BaseCommand {
    @Command(name = "messagessettings", aliases = {"msgsettings", "msettings", "chatsettings"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        String[] args = cmd.getArgs();

        if (args.length == 0) {
            PlayerData playerData = PlayerData.getPlayerData(player.getUniqueId());
            String defaultChatColor = Disqualified.getInstance().getSettingsConfig().getConfiguration().getString("SETTINGS.CHAT.FORMAT.DEFAULT-COLOR");
            ChatColor CC;

            if (playerData.getChatColor() != null) {
                CC = ChatColor.valueOf(playerData.getChatColor());
            } else {
                CC = ChatColor.valueOf(defaultChatColor);
            }

            new MessagesSettingsMenu(CC).openMenu(player);
        }
    }
}
