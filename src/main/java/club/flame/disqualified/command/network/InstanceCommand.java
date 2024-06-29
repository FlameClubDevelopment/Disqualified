package club.flame.disqualified.command.network;

import club.flame.disqualified.utils.lang.Lang;
import club.flame.disqualified.lib.chat.CC;
import club.flame.disqualified.lib.commands.BaseCommand;
import club.flame.disqualified.lib.commands.Command;
import club.flame.disqualified.lib.commands.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Re-Work Code by HCFAlerts
 * Project: Disqualified
 * Credits: FCD
 */

public class InstanceCommand extends BaseCommand {
    @Command(name = "instance", aliases = {"serverinstance", "checkinstance", "currentserver"}, permission = "core.network.instance", usage = "Usage: /instance <serverName>", inGameOnly = true)

    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate("&4&lDisqualified &4✖ &7- &fServer Instance"));
        player.sendMessage(CC.CHAT_BAR);
        player.sendMessage(CC.translate("&8 ▸ &7Server&f: &4" + Lang.SERVER_NAME));
        player.sendMessage(CC.translate("&8 ▸ &7Players&f: &4" + Bukkit.getServer().getOnlinePlayers().size() + "/" + Bukkit.getServer().getMaxPlayers()));
        player.sendMessage(CC.CHAT_BAR);
    }
}
